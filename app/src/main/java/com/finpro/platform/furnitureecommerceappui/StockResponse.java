package com.finpro.platform.furnitureecommerceappui;

import com.google.gson.annotations.SerializedName;

public class StockResponse {
    @SerializedName("symbol") private String symbol;
    @SerializedName("companyName") private String companyName;
    @SerializedName("currentPrice") private double currentPrice;
    @SerializedName("priceChange") private double priceChange;
    @SerializedName("fiftyTwoWeekHigh") private double fiftyTwoWeekHigh;
    @SerializedName("fiftyTwoWeekLow") private double fiftyTwoWeekLow;
    @SerializedName("marketCap") private String marketCap;
    @SerializedName("sector") private String sector;
    @SerializedName("volume") private String volume;
    @SerializedName("currency") private String currency;

    // Financials
    @SerializedName("priceToBook") private double priceToBook;
    @SerializedName("trailingPE") private double trailingPE;
    @SerializedName("forwardPE") private double forwardPE;
    @SerializedName("priceToSales") private double priceToSales;
    
    // Support both custom key and default yfinance key
    @SerializedName(value = "businessSummary", alternate = {"longBusinessSummary"}) 
    private String businessSummary;

    // Analyst Ratings
    @SerializedName("recommendationMean") private double recommendationMean;
    @SerializedName("recommendationKey") private String recommendationKey;
    
    // Analyst Targets
    @SerializedName("targetHighPrice") private double targetHighPrice;
    @SerializedName("targetLowPrice") private double targetLowPrice;
    @SerializedName("targetMeanPrice") private double targetMeanPrice;
    @SerializedName("targetMedianPrice") private double targetMedianPrice;

    // Risk Scores
    @SerializedName("auditRisk") private double auditRisk;
    @SerializedName("boardRisk") private double boardRisk;
    @SerializedName("shareHolderRightsRisk") private double shareHolderRightsRisk;
    @SerializedName("overallRisk") private double overallRisk;

    // Getters
    public String getSymbol() { return symbol; }
    public String getCompanyName() { return companyName; }
    public double getCurrentPrice() { return currentPrice; }
    public double getPriceChange() { return priceChange; }
    public double getFiftyTwoWeekHigh() { return fiftyTwoWeekHigh; }
    public double getFiftyTwoWeekLow() { return fiftyTwoWeekLow; }
    public String getMarketCap() { return marketCap; }
    public String getSector() { return sector; }
    public String getVolume() { return volume; }
    public String getCurrency() { return currency; }
    public double getPriceToBook() { return priceToBook; }
    public double getTrailingPE() { return trailingPE; }
    public double getForwardPE() { return forwardPE; }
    public double getPriceToSales() { return priceToSales; }
    public String getBusinessSummary() { return businessSummary; }
    public double getRecommendationMean() { return recommendationMean; }
    public String getRecommendationKey() { return recommendationKey; }
    public double getTargetHighPrice() { return targetHighPrice; }
    public double getTargetLowPrice() { return targetLowPrice; }
    public double getTargetMeanPrice() { return targetMeanPrice; }
    public double getTargetMedianPrice() { return targetMedianPrice; }
    public double getAuditRisk() { return auditRisk; }
    public double getBoardRisk() { return boardRisk; }
    public double getShareHolderRightsRisk() { return shareHolderRightsRisk; }
    public double getOverallRisk() { return overallRisk; }
}
