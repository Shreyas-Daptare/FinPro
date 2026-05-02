package com.finpro.platform.furnitureecommerceappui;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/stock")
    Call<StockResponse> getStockData(@Query("symbol") String symbol);

    @GET("/search")
    Call<List<SearchResponse>> searchStocks(@Query("query") String query);

    @GET("/predict")
    Call<PredictResponse> predictStock(@Query("symbol") String symbol);

    @GET("/screener/nifty50")
    Call<List<ScreenerResponse>> getScreenerData(@Query("category") String category);

    @GET("/news")
    Call<List<NewsResponse>> getNews(@Query("limit") int limit);
}
