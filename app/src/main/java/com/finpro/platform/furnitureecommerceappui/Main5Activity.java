package com.finpro.platform.furnitureecommerceappui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main5Activity extends AppCompatActivity {

    private static final String TAG = "Main5Activity";
    
    // UI Elements
    TextView stockNameTextView;
    TextView priceValue, sectorValue, high52Value, low52Value, marketCapValue, volumeValue;
    TextView pbRatioValue, peTrailingValue, peForwardValue, psRatioValue, businessSummaryValue;
    TextView recommendationMeanValue, recommendationKeyValue;
    TextView targetHigh, targetLow, targetMean, targetMedian;
    TextView auditRisk, boardRisk, shRisk, overallRisk;
    
    // Summary Dropdown
    LinearLayout summaryHeader;
    CardView summaryCard;
    ImageView summaryArrow;
    boolean isSummaryExpanded = false;

    // AI Elements
    Button predictButton;
    LinearLayout aiContentLayout;
    TextView goodThingsList, anomaliesList, aiRatingValue, optimalPriceValue;
    ProgressBar progressBar;
    
    ImageButton backButton;
    String currentSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        initViews();

        currentSymbol = getIntent().getStringExtra("stock_symbol");
        if (currentSymbol != null) {
            stockNameTextView.setText(currentSymbol);
            fetchStockData(currentSymbol);
        }

        predictButton.setOnClickListener(v -> fetchPrediction());
        backButton.setOnClickListener(v -> finish());
        summaryHeader.setOnClickListener(v -> toggleSummary());
    }

    private void initViews() {
        stockNameTextView = findViewById(R.id.stockNameTextView);
        priceValue = findViewById(R.id.priceValue);
        sectorValue = findViewById(R.id.sectorValue);
        high52Value = findViewById(R.id.high52Value);
        low52Value = findViewById(R.id.low52Value);
        marketCapValue = findViewById(R.id.marketCapValue);
        volumeValue = findViewById(R.id.volumeValue);
        pbRatioValue = findViewById(R.id.pbRatioValue);
        peTrailingValue = findViewById(R.id.peTrailingValue);
        peForwardValue = findViewById(R.id.peForwardValue);
        psRatioValue = findViewById(R.id.psRatioValue);
        businessSummaryValue = findViewById(R.id.businessSummaryValue);
        recommendationMeanValue = findViewById(R.id.recommendationMeanValue);
        recommendationKeyValue = findViewById(R.id.recommendationKeyValue);
        targetHigh = findViewById(R.id.targetHigh);
        targetLow = findViewById(R.id.targetLow);
        targetMean = findViewById(R.id.targetMean);
        targetMedian = findViewById(R.id.targetMedian);
        auditRisk = findViewById(R.id.auditRisk);
        boardRisk = findViewById(R.id.boardRisk);
        shRisk = findViewById(R.id.shRisk);
        overallRisk = findViewById(R.id.overallRisk);
        
        summaryHeader = findViewById(R.id.summaryHeader);
        summaryCard = findViewById(R.id.summaryCard);
        summaryArrow = findViewById(R.id.summaryArrow);

        predictButton = findViewById(R.id.predictButton);
        aiContentLayout = findViewById(R.id.aiContentLayout);
        goodThingsList = findViewById(R.id.goodThingsList);
        anomaliesList = findViewById(R.id.anomaliesList);
        aiRatingValue = findViewById(R.id.aiRatingValue);
        optimalPriceValue = findViewById(R.id.optimalPriceValue);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.backButton);
    }

    private void toggleSummary() {
        isSummaryExpanded = !isSummaryExpanded;
        if (isSummaryExpanded) {
            summaryCard.setVisibility(View.VISIBLE);
            summaryArrow.setRotation(270); 
        } else {
            summaryCard.setVisibility(View.GONE);
            summaryArrow.setRotation(90);
        }
    }

    private void fetchStockData(String symbol) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getStockData(symbol).enqueue(new Callback<StockResponse>() {
            @Override
            public void onResponse(Call<StockResponse> call, Response<StockResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateStockUI(response.body());
                } else {
                    Toast.makeText(Main5Activity.this, "Failed to load metrics", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StockResponse> call, Throwable t) {
                Toast.makeText(Main5Activity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStockUI(StockResponse data) {
        if (data.getCompanyName() != null && !data.getCompanyName().isEmpty()) {
            stockNameTextView.setText(data.getCompanyName());
        }

        String curr = data.getCurrency();
        if (curr == null) curr = "";
        
        priceValue.setText(String.format(Locale.getDefault(), "%s %.2f", curr, data.getCurrentPrice()));
        sectorValue.setText(data.getSector());
        high52Value.setText(String.format(Locale.getDefault(), "%s %.2f", curr, data.getFiftyTwoWeekHigh()));
        low52Value.setText(String.format(Locale.getDefault(), "%s %.2f", curr, data.getFiftyTwoWeekLow()));
        marketCapValue.setText(data.getMarketCap());
        volumeValue.setText(data.getVolume());
        
        pbRatioValue.setText(String.format(Locale.getDefault(), "%.2f", data.getPriceToBook()));
        peTrailingValue.setText(String.format(Locale.getDefault(), "%.2f", data.getTrailingPE()));
        peForwardValue.setText(String.format(Locale.getDefault(), "%.2f", data.getForwardPE()));
        psRatioValue.setText(String.format(Locale.getDefault(), "%.2f", data.getPriceToSales()));
        
        if (data.getBusinessSummary() != null && !data.getBusinessSummary().isEmpty()) {
            businessSummaryValue.setText(data.getBusinessSummary());
        } else {
            businessSummaryValue.setText("No summary information available for this stock.");
        }
        
        recommendationMeanValue.setText(String.format(Locale.getDefault(), "%.2f", data.getRecommendationMean()));
        recommendationKeyValue.setText(data.getRecommendationKey() != null ? data.getRecommendationKey().toUpperCase() : "-");
        
        targetHigh.setText("High: " + String.format(Locale.getDefault(), "%.2f", data.getTargetHighPrice()));
        targetLow.setText("Low: " + String.format(Locale.getDefault(), "%.2f", data.getTargetLowPrice()));
        targetMean.setText("Mean: " + String.format(Locale.getDefault(), "%.2f", data.getTargetMeanPrice()));
        targetMedian.setText("Median: " + String.format(Locale.getDefault(), "%.2f", data.getTargetMedianPrice()));
        
        auditRisk.setText("Audit: " + String.format(Locale.getDefault(), "%.1f", data.getAuditRisk()));
        boardRisk.setText("Board: " + String.format(Locale.getDefault(), "%.1f", data.getBoardRisk()));
        shRisk.setText("Rights: " + String.format(Locale.getDefault(), "%.1f", data.getShareHolderRightsRisk()));
        overallRisk.setText("Overall: " + String.format(Locale.getDefault(), "%.1f", data.getOverallRisk()));
    }

    private void fetchPrediction() {
        progressBar.setVisibility(View.VISIBLE);
        predictButton.setEnabled(false);
        
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.predictStock(currentSymbol).enqueue(new Callback<PredictResponse>() {
            @Override
            public void onResponse(Call<PredictResponse> call, Response<PredictResponse> response) {
                progressBar.setVisibility(View.GONE);
                predictButton.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    String currentCurr = "";
                    try {
                        String[] parts = priceValue.getText().toString().split(" ");
                        if (parts.length > 0) {
                            currentCurr = parts[0];
                        }
                    } catch (Exception e) {}
                    updatePredictionUI(response.body(), currentCurr);
                } else {
                    Log.e(TAG, "Prediction failed with code: " + response.code());
                    Toast.makeText(Main5Activity.this, "Prediction failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PredictResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                predictButton.setEnabled(true);
                Toast.makeText(Main5Activity.this, "Connection Timeout", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePredictionUI(PredictResponse data, String currency) {
        aiContentLayout.setVisibility(View.VISIBLE);
        
        StringBuilder good = new StringBuilder();
        if (data.getGoodThings() != null) {
            for (int i = 0; i < data.getGoodThings().size(); i++) {
                good.append(i + 1).append(". ").append(data.getGoodThings().get(i)).append("\n\n");
            }
        }
        goodThingsList.setText(good.length() > 0 ? good.toString().trim() : "No positive indicators found.");

        StringBuilder anomalies = new StringBuilder();
        if (data.getAnomalies() != null) {
            for (int i = 0; i < data.getAnomalies().size(); i++) {
                anomalies.append(i + 1).append(". ").append(data.getAnomalies().get(i)).append("\n\n");
            }
        }
        anomaliesList.setText(anomalies.length() > 0 ? anomalies.toString().trim() : "No anomalies detected.");
        
        aiRatingValue.setText(data.getPrediction());
        optimalPriceValue.setText(String.format(Locale.getDefault(), "%s %.1f", currency, data.getOptimalPrice()));
    }
}
