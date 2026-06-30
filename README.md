# FinPro - AI Stock Insights

A Stock Market Screener and Analysis mobile application that helps traders and investors discover trading opportunities, track real-time stock data, and get AI-powered predictions — all from a clean, intuitive Android interface.

---

## 📖 Overview

Retail traders are often overwhelmed by the sheer volume of stock market data available today, with little personalization to help them act on it. **FinPro** solves this by combining real-time financial data, automated technical screening, market news, and AI-driven predictions into a single, easy-to-use mobile experience.

Users can search for stocks, browse predefined screener categories (Momentum, High Volume, Breakouts, Reversals, and more), view detailed financial dashboards, read the latest market news, and get an AI-generated rating with strengths, anomalies, and an optimal price estimate for any stock.

---

## ✨ Features

- 🔍 **Stock Search** — Find stocks instantly by company name or ticker symbol
- 📊 **Stock Screener** — Filter stocks using predefined categories: Momentum, High Volume, Breakout, Reversal, and Liquidity-based signals
- 💹 **Real-Time Data** — Live prices, volume, market cap, P/E ratios, and other key financial metrics powered by `yfinance`
- 📰 **Market News** — Stay updated with the latest stock and market-related news
- 🤖 **AI-Based Predictions** — Get a Buy/Sell/Hold rating, optimal price estimation, and a breakdown of key strengths and anomalies for any stock
- 📱 **Clean Mobile UI** — A responsive, intuitive interface built for quick decision-making

---

## 🖼️ Screenshots

<!-- Add your screenshots here -->

| Home / Discover Stocks | Stock Detail Dashboard |
|:---:|:---:|
| _add screenshot_ | _add screenshot_ |

| AI Overview & Prediction | Momentum Screener |
|:---:|:---:|
| _add screenshot_ | _add screenshot_ |

---

## 🏗️ Architecture

FinPro follows a **client-server architecture** with three primary layers:

1. **Presentation Layer (Android App)** — Built with Java and XML layouts in Android Studio. Uses Retrofit for API communication and Gson for JSON parsing.
2. **Application Layer (FastAPI Backend)** — Handles business logic, request processing, and exposes REST endpoints.
3. **Data Layer (External APIs)** — Fetches stock and news data from `yfinance` and news APIs, processed using Pandas.

```
Android App (Java + Retrofit)
        │  REST API (JSON)
        ▼
FastAPI Backend (Python)
   ├── Screener Engine
   ├── Prediction Module
   └── Data Processing (Pandas)
        │  Data Fetching
        ▼
External APIs (yfinance, News API)
```

### API Endpoints

| Endpoint | Description |
|---|---|
| `/search` | Search for stocks by name or ticker |
| `/stock` | Fetch detailed stock information |
| `/screener` | Filter stocks by screening category |
| `/predict` | Generate AI-based stock prediction |
| `/news` | Fetch latest market-related news |

---

## 🛠️ Tech Stack

**Frontend (Android)**
- Java
- Android Studio / XML Layouts
- Retrofit — API communication
- Gson — JSON parsing

**Backend**
- Python
- FastAPI — REST API framework
- Uvicorn — ASGI server
- Pandas — data processing

**External Data Sources**
- [`yfinance`](https://pypi.org/project/yfinance/) — real-time and historical stock data
- News API / Yahoo Finance News — market news

---

## 📋 Prerequisites

- Android Studio (latest stable version)
- Android device/emulator running **Android 8.0 (API Level 26)** or higher
- Python 3.8+
- pip

---

## 🚀 Getting Started

### Backend Setup

```bash
# Clone the repository
git clone https://github.com/Shreyas-Daptare/FinPro.git
cd FinPro/backend

# Create and activate a virtual environment (recommended)
python -m venv venv
source venv/bin/activate   # On Windows: venv\Scripts\activate

# Install dependencies
pip install -r requirements.txt

# Run the FastAPI server
uvicorn main:app --reload
```

The backend will be available at `http://127.0.0.1:8000`.

### Android App Setup

1. Open the `android/` folder in **Android Studio**.
2. Update the base URL in the Retrofit configuration to point to your backend server (e.g. `http://<your-local-ip>:8000/`).
3. Build and run the app on an emulator or physical device (Android 8.0+).

---

## 📱 Usage

1. Launch the app to land on the **Discover Stocks** screen.
2. Use the search bar to look up a specific stock by name or ticker.
3. Tap a **Screener** card (Momentum, High Volume, Breakout, Reversal) to view filtered stock lists.
4. Tap a stock to open its detailed dashboard with price, financials, analyst ratings, and risk scores.
5. Tap **Predict** to generate an AI overview with a rating, optimal buy price, key strengths, and anomalies.
6. Browse the **Market News** section on the home screen for the latest updates.

---

## 🗺️ Roadmap

- [ ] Integration of advanced technical indicators (RSI, MACD, Bollinger Bands)
- [ ] Expansion to NIFTY 500 and global markets
- [ ] Real-time data streaming via WebSockets
- [ ] Improved AI/ML prediction models
- [ ] Push notifications and trading alerts
- [ ] Cloud sync for user preferences and watchlists
- [ ] Enhanced charts and data visualizations

---

## ⚠️ Limitations

- Relies on external APIs (`yfinance`), which may occasionally return delayed or incomplete data
- Currently limited to a smaller dataset (e.g., NIFTY 50)
- No advanced technical indicators yet
- Requires a continuous internet connection

---

## 👤 Author

**Shreyas Daptare**
Department of Artificial Intelligence & Data Science
K. K. Wagh Institute of Engineering Education and Research, Nashik

**Guided by:** Prof. Priti G. Jadhav

---

## 📄 License

This project is part of an academic mini-project submission. Please check with the author before reuse or distribution.
