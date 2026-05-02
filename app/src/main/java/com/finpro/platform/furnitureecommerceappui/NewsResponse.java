package com.finpro.platform.furnitureecommerceappui;

import com.google.gson.annotations.SerializedName;

public class NewsResponse {
    @SerializedName("title") private String title;
    @SerializedName("publisher") private String publisher;
    @SerializedName("link") private String link;
    @SerializedName("published") private String published;

    public String getTitle() { return title; }
    public String getPublisher() { return publisher; }
    public String getLink() { return link; }
    public String getPublished() { return published; }
}
