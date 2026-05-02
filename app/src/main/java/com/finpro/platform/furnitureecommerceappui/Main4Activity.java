package com.finpro.platform.furnitureecommerceappui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main4Activity extends AppCompatActivity {

    private ListView screenerListView;
    private ProgressBar progressBar;
    private TextView noResultsText;
    private TextView screenerTitle;
    private ImageView backButton;
    private ArrayAdapter<String> adapter;
    private List<ScreenerResponse> screenerData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        screenerListView = findViewById(R.id.screenerListView);
        progressBar = findViewById(R.id.screenerProgressBar);
        noResultsText = findViewById(R.id.noResultsText);
        screenerTitle = findViewById(R.id.screenerTitle);
        backButton = findViewById(R.id.backButton);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        screenerListView.setAdapter(adapter);

        String category = getIntent().getStringExtra("category");
        if (category != null) {
            screenerTitle.setText(category.substring(0, 1).toUpperCase() + category.substring(1) + " Screener");
            fetchScreenerData(category);
        }

        backButton.setOnClickListener(v -> finish());

        screenerListView.setOnItemClickListener((parent, view, position, id) -> {
            String ticker = screenerData.get(position).getTicker();
            Intent intent = new Intent(Main4Activity.this, Main5Activity.class);
            intent.putExtra("stock_symbol", ticker);
            startActivity(intent);
        });
    }

    private void fetchScreenerData(String category) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getScreenerData(category).enqueue(new Callback<List<ScreenerResponse>>() {
            @Override
            public void onResponse(Call<List<ScreenerResponse>> call, Response<List<ScreenerResponse>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    screenerData = response.body();
                    if (screenerData.isEmpty()) {
                        noResultsText.setVisibility(View.VISIBLE);
                    } else {
                        noResultsText.setVisibility(View.GONE);
                        List<String> displayList = new ArrayList<>();
                        for (ScreenerResponse item : screenerData) {
                            String info = item.getTicker() + " - " + item.getSignal();
                            if (item.getChangePercent() != null) info += " (" + item.getChangePercent() + "%)";
                            displayList.add(info);
                        }
                        adapter.clear();
                        adapter.addAll(displayList);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(Main4Activity.this, "Failed to load screener data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ScreenerResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Main4Activity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
