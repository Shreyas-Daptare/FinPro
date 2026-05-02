package com.finpro.platform.furnitureecommerceappui;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PredictResponse {
    @SerializedName("prediction") private String prediction;
    @SerializedName("optimalPrice") private double optimalPrice;
    @SerializedName("goodThings") private List<String> goodThings;
    @SerializedName("anomalies") private List<String> anomalies;

    public String getPrediction() { return prediction; }
    public double getOptimalPrice() { return optimalPrice; }
    public List<String> getGoodThings() { return goodThings; }
    public List<String> getAnomalies() { return anomalies; }
}
