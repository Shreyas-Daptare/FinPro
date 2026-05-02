package com.finpro.platform.furnitureecommerceappui;

import com.google.gson.annotations.SerializedName;

public class ScreenerResponse {
    @SerializedName("Ticker") private String ticker;
    @SerializedName("Change %") private Double changePercent;
    @SerializedName("Volume") private Long volume;
    @SerializedName("Price") private Double price;
    @SerializedName("Signal") private String signal;

    public String getTicker() { return ticker; }
    public Double getChangePercent() { return changePercent; }
    public Long getVolume() { return volume; }
    public Double getPrice() { return price; }
    public String getSignal() { return signal; }
}
