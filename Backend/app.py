from fastapi import FastAPI, HTTPException, Query
import yfinance as yf
import helper
import predict
import ast
import pandas as pd
app = FastAPI()


# ---------- UTIL ----------

def format_market_cap(value):
    try:
        if value >= 1e12:
            return f"{value/1e12:.1f}T"
        elif value >= 1e9:
            return f"{value/1e9:.1f}B"
        elif value >= 1e6:
            return f"{value/1e6:.1f}M"
        return str(value)
    except:
        return "N/A"


def parse_financial_analysis(output):
    namespace = {}
    for line in output.strip().splitlines():
        if '=' in line:
            key, value = line.split('=', 1)
            key = key.strip()
            value = value.strip()
            try:
                value = ast.literal_eval(value)
            except:
                pass
            namespace[key] = value
    return namespace


def get_safe(info, key, default=0):
    return info.get(key, default) if info else default


# ---------- SEARCH ----------

@app.get("/search")
def search_stock(query: str = Query(...)):
    try:
        df = helper.get_stock_suggestions(None, query)

        if df.empty:
            return []

        return [
            {
                "symbol": row["Ticker"],
                "name": row["Company Name"],
                "exchange": row["Exchange"]
            }
            for _, row in df.iterrows()
        ]

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


# ---------- STOCK DASHBOARD ----------

@app.get("/stock")
def get_stock(symbol: str = Query(...)):
    try:
        ticker = yf.Ticker(symbol)
        info = helper.get_info(ticker)

        if not info or "currentPrice" not in info:
            raise HTTPException(status_code=404, detail="Invalid symbol")

        return {
            "symbol": symbol,
            "companyName": info.get("longName", symbol),

            # Basic
            "currency": get_safe(info, "currency", "N/A"),
            "currentPrice": get_safe(info, "currentPrice"),
            "priceChange": get_safe(info, "regularMarketChangePercent"),
            "fiftyTwoWeekHigh": get_safe(info, "fiftyTwoWeekHigh"),
            "fiftyTwoWeekLow": get_safe(info, "fiftyTwoWeekLow"),
            "marketCap": format_market_cap(get_safe(info, "marketCap")),
            "sector": info.get("sector", "N/A"),
            "volume": get_safe(info, "volume"),

            # Financials
            "priceToBook": get_safe(info, "priceToBook"),
            "trailingPE": get_safe(info, "trailingPE"),
            "forwardPE": get_safe(info, "forwardPE"),
            "priceToSales": get_safe(info, "priceToSalesTrailing12Months"),

            # Description
            "businessSummary": info.get("longBusinessSummary", ""),

            # Analyst
            "recommendationMean": get_safe(info, "recommendationMean"),
            "recommendationKey": info.get("recommendationKey", "hold"),

            # Targets
            "targetHighPrice": get_safe(info, "targetHighPrice"),
            "targetLowPrice": get_safe(info, "targetLowPrice"),
            "targetMeanPrice": get_safe(info, "targetMeanPrice"),
            "targetMedianPrice": get_safe(info, "targetMedianPrice"),

            # Risk
            "auditRisk": get_safe(info, "auditRisk"),
            "boardRisk": get_safe(info, "boardRisk"),
            "shareHolderRightsRisk": get_safe(info, "shareHolderRightsRisk"),
            "overallRisk": get_safe(info, "overallRisk")
        }

    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


# ---------- AI PREDICT (BUTTON) ----------

@app.get("/predict")
def predict_stock(symbol: str = Query(...)):
    try:
        ticker = yf.Ticker(symbol)
        info = helper.get_info(ticker)

        if not info or "currentPrice" not in info:
            raise HTTPException(status_code=404, detail="Invalid symbol")

        result = predict.predict_result(
            name=info.get("longName", symbol),
            info=info,
            net_income=helper.net_income(ticker),
            news = helper.stock_news(ticker),
            recommendations=helper.get_recommendations(ticker),
            holders=helper.holders(ticker),
            dividends=helper.dividends(ticker),
            analyst_targets=helper.analyst_targets(ticker),
            temperature=0.8,
            top_p=0.1
        )

        parsed = parse_financial_analysis(result)

        return {
            "prediction": parsed.get("Rating", ""),
            "optimalPrice": parsed.get("Price", 0.0),
            "goodThings": parsed.get("Good_Things", []),
            "anomalies": parsed.get("Anomalies", [])
        }

    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

def get_nifty50_symbols():
    url = "https://archives.nseindia.com/content/indices/ind_nifty50list.csv"
    df = pd.read_csv(url)
    return [symbol + ".NS" for symbol in df["Symbol"].tolist()]

@app.get("/screener/nifty50")
def screener_nifty50(category: str = Query(...)):
    try:
        tickers = get_nifty50_symbols()
        df = helper.run_screener(category.lower(), tickers)

        if df.empty:
            return []

        return df.to_dict(orient="records")

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
    
@app.get("/news")
def get_news(limit: int = Query(10)):
    try:
        news = helper.get_global_news(limit)

        if not news:
            return []

        return news

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))