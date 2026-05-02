import yfinance as yf
import pandas as pd
from datetime import datetime

def fetch_data(tickers, period="5d", interval="5m"):
    data = yf.download(tickers, period=period, interval=interval, group_by='ticker', auto_adjust=True)
    return data

def get_stock_suggestions(st, query):
    if not query:
        return pd.DataFrame() # Return empty DataFrame if no query

    try:
        # Use yfinance.Search to find matching quotes (tickers)
        search_results = yf.Search(query, max_results=10)
        
        # Extract the list of quotes
        quotes = search_results.quotes
        
        # Process the results into a list of dictionaries
        data = []
        for q in quotes:
            # We only care about EQUITY results that have a name
            if q.get('quoteType') == 'EQUITY' and q.get('shortname'):
                data.append({
                    "Ticker": q.get('symbol', 'N/A'),
                    "Company Name": q.get('shortname', 'N/A'),
                    "Exchange": q.get('exchange', 'N/A')
                })
        
        return pd.DataFrame(data)
    
    except Exception as e:
        st.error(f"Could not retrieve search results: {e}")
        return pd.DataFrame()

def convert_to_tradingview_format(yahoo_symbol):
    """
    Converts a Yahoo Finance ticker (e.g., 'TCS.NS') to the TradingView format (e.g., 'NSE:TCS').
    """
    # 1. Map Yahoo Suffixes to TradingView Exchange Prefixes
    # Note: US stocks usually don't have a suffix in yfinance (e.g., AAPL), 
    # but their exchange is typically implied (NASDAQ or NYSE).
    EXCHANGE_MAP = {
        'NS': 'NSE',    # National Stock Exchange of India
        'BO': 'BSE',    # Bombay Stock Exchange
        'T': 'TSE',     # Tokyo Stock Exchange
        'L': 'LSE',     # London Stock Exchange
        # Add more as needed
    }

    if '.' in yahoo_symbol:
        # International Ticker (e.g., TCS.NS)
        ticker_part, suffix = yahoo_symbol.split('.', 1)
        exchange = EXCHANGE_MAP.get(suffix.upper(), suffix.upper()) 
        # Use the mapped exchange code, or the suffix if not found
        
        return f"{exchange}:{ticker_part}"
    
    else:
        # US Ticker (AAPL, MSFT, etc.)
        # TradingView requires the exchange, but yfinance doesn't always provide it clearly in the search result.
        # We'll default to NASDAQ for simplicity for unsuffixed U.S. symbols.
        # For a truly robust app, you would use ticker.info.get('exchange') if possible, 
        # but that requires another API call.
        
        # In this context, we'll assume US stocks searched via yfinance are on NASDAQ/NYSE
        if yahoo_symbol.startswith(('A','G','T','F','M','I','N')): # Simple guess for major exchanges
             return f"NASDAQ:{yahoo_symbol}"
        else: # Fallback to a generic exchange if needed
             return f"NYSE:{yahoo_symbol}"

# Info
def get_info(ticker):
    info = ticker.info
    return info

def net_income(ticker):
    annual_income_statement = ticker.income_stmt
    net_income_annual = annual_income_statement.loc['Net Income']
    
    quarterly_income_statement = ticker.quarterly_income_stmt
    net_income_quarterly = quarterly_income_statement.loc['Net Income']

    return net_income_annual, net_income_quarterly
 
def get_recommendations(ticker):
    recommendations_summary = ticker.recommendations_summary
    return recommendations_summary

def holders(ticker):
    major_holders = ticker.major_holders
    return major_holders

def dividends(ticker):
    dividends_history = ticker.dividends
    return dividends_history

def splits(ticker):
    splits_history = ticker.splits
    return splits

def analyst_targets(ticker):
    targets = ticker.analyst_price_targets
    return targets

def stock_news(ticker):
    try:
        news_items = ticker.news

        results = []
        for item in news_items:
            results.append({
                "title": item.get("title"),
                "publisher": item.get("publisher"),
                "link": item.get("link"),
                "published": item.get("providerPublishTime")
            })

        return results

    except:
        return []
# Screeners
def momentum_screener(tickers):
    data = fetch_data(tickers, period="2d", interval="5m")
    results = []

    for ticker in tickers:
        try:
            df = data[ticker].dropna()
            if len(df) < 10:
                continue

            change = (df['Close'].iloc[-1] - df['Close'].iloc[0]) / df['Close'].iloc[0] * 100

            if change > 2:  # threshold
                results.append({
                    "Ticker": ticker,
                    "Change %": round(change, 2),
                    "Signal": "Momentum 📈"
                })
        except:
            continue

    return pd.DataFrame(results)

def high_volume_screener(tickers):
    data = fetch_data(tickers, period="2d", interval="5m")
    results = []

    for ticker in tickers:
        try:
            df = data[ticker].dropna()

            avg_vol = df['Volume'][:-1].mean()
            latest_vol = df['Volume'].iloc[-1]

            if latest_vol > 2 * avg_vol:
                results.append({
                    "Ticker": ticker,
                    "Volume": int(latest_vol),
                    "Signal": "High Volume ⚡"
                })
        except:
            continue

    return pd.DataFrame(results)

def breakout_screener(tickers):
    data = fetch_data(tickers, period="5d", interval="5m")
    results = []

    for ticker in tickers:
        try:
            df = data[ticker].dropna()

            recent_high = df['High'][:-1].max()
            latest_close = df['Close'].iloc[-1]

            if latest_close > recent_high:
                results.append({
                    "Ticker": ticker,
                    "Price": latest_close,
                    "Signal": "Breakout 🚀"
                })
        except:
            continue

    return pd.DataFrame(results)

def reversal_screener(tickers):
    data = fetch_data(tickers, period="5d", interval="5m")
    results = []

    for ticker in tickers:
        try:
            df = data[ticker].dropna()

            # Simple reversal proxy: strong drop then bounce
            drop = (df['Close'].iloc[-5] - df['Close'].iloc[-2]) / df['Close'].iloc[-5]
            bounce = df['Close'].iloc[-1] > df['Close'].iloc[-2]

            if drop > 0.02 and bounce:
                results.append({
                    "Ticker": ticker,
                    "Signal": "Reversal 🔄"
                })
        except:
            continue

    return pd.DataFrame(results)

def run_screener(category, tickers):
    if category == "momentum":
        return momentum_screener(tickers)
    
    elif category == "volume":
        return high_volume_screener(tickers)
    
    elif category == "breakout":
        return breakout_screener(tickers)
    
    elif category == "reversal":
        return reversal_screener(tickers)
    
    else:
        return pd.DataFrame()
    
def get_global_news(limit=10, query="market"):
    try:
        search = yf.Search(query, news_count=limit)
        news_items = search.news or []

        results = []
        for item in news_items[:limit]:
            ts = item.get("providerPublishTime")
            published = datetime.fromtimestamp(ts) if ts else None

            results.append({
                "title": item.get("title"),
                "publisher": item.get("publisher"),
                "link": item.get("link"),
                "published": published,
            })
        #print(results)
        return results

    except Exception:
        # Optionally log here
        return []
    
