package com.finpro.platform.furnitureecommerceappui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "Main2Activity";
    LinearLayout personLinearLayout, newsContainer;
    FloatingActionButton favorite;
    CardView momentumCard, volumeCard, breakoutCard, reversalCard;
    SearchView searchView;
    ListView suggestionListView;
    ArrayAdapter<String> adapter;
    List<SearchResponse> currentSuggestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        
        personLinearLayout = findViewById(R.id.person_linear_layout);
        favorite = findViewById(R.id.favorite);
        newsContainer = findViewById(R.id.newsContainer);
        
        momentumCard = findViewById(R.id.momentum_card);
        volumeCard = findViewById(R.id.volume_card);
        breakoutCard = findViewById(R.id.breakout_card);
        reversalCard = findViewById(R.id.reversal_card);
        
        searchView = findViewById(R.id.searchView);
        suggestionListView = findViewById(R.id.suggestionListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        suggestionListView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!currentSuggestions.isEmpty()) {
                    navigateToDetail(currentSuggestions.get(0).getSymbol());
                } else {
                    navigateToDetail(query.toUpperCase());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 2) {
                    performSearch(newText);
                } else {
                    suggestionListView.setVisibility(View.GONE);
                }
                return false;
            }
        });

        suggestionListView.setOnItemClickListener((parent, view, position, id) -> {
            SearchResponse selected = currentSuggestions.get(position);
            navigateToDetail(selected.getSymbol());
        });

        momentumCard.setOnClickListener(v -> navigateToScreener("momentum"));
        volumeCard.setOnClickListener(v -> navigateToScreener("volume"));
        breakoutCard.setOnClickListener(v -> navigateToScreener("breakout"));
        reversalCard.setOnClickListener(v -> navigateToScreener("reversal"));

        personLinearLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
            startActivity(intent);
        });
        
        favorite.setOnClickListener(view -> {
            //Intent intent = new Intent(getApplicationContext(), Main4Activity.class);
            //startActivity(intent);
        });

        fetchNews();
    }

    private void performSearch(String query) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.searchStocks(query).enqueue(new Callback<List<SearchResponse>>() {
            @Override
            public void onResponse(Call<List<SearchResponse>> call, Response<List<SearchResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentSuggestions = response.body();
                    List<String> displayList = new ArrayList<>();
                    for (SearchResponse s : currentSuggestions) {
                        displayList.add(s.getSymbol() + " - " + s.getName());
                    }
                    adapter.clear();
                    adapter.addAll(displayList);
                    adapter.notifyDataSetChanged();
                    suggestionListView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<SearchResponse>> call, Throwable t) {
                Log.e(TAG, "Search API call failed", t);
            }
        });
    }

    private void fetchNews() {
        Log.d(TAG, "Fetching news...");
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getNews(10).enqueue(new Callback<List<NewsResponse>>() {
            @Override
            public void onResponse(Call<List<NewsResponse>> call, Response<List<NewsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "News successfully loaded: " + response.body().size() + " items");
                    displayNews(response.body());
                } else {
                    Log.e(TAG, "News API server error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<NewsResponse>> call, Throwable t) {
                Log.e(TAG, "News API network error", t);
            }
        });
    }

    private void displayNews(List<NewsResponse> newsList) {
        newsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        if (newsList == null || newsList.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("No news available at the moment.");
            emptyText.setPadding(32, 32, 32, 32);
            emptyText.setGravity(android.view.Gravity.CENTER);
            newsContainer.addView(emptyText);
            return;
        }

        for (NewsResponse news : newsList) {
            // Skip items with missing mandatory data
            if (news.getTitle() == null || news.getPublisher() == null) continue;

            View newsView = inflater.inflate(R.layout.news_item, newsContainer, false);
            
            TextView title = newsView.findViewById(R.id.newsTitle);
            TextView publisher = newsView.findViewById(R.id.newsPublisher);

            title.setText(news.getTitle());
            publisher.setText(news.getPublisher());

            if (news.getLink() != null && !news.getLink().isEmpty()) {
                newsView.setOnClickListener(v -> {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getLink()));
                        startActivity(browserIntent);
                    } catch (Exception e) {
                        Toast.makeText(this, "Could not open link", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            newsContainer.addView(newsView);
        }
    }

    private void navigateToScreener(String category) {
        Intent intent = new Intent(this, Main4Activity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    private void navigateToDetail(String stockSymbol) {
        Intent intent = new Intent(Main2Activity.this, Main5Activity.class);
        intent.putExtra("stock_symbol", stockSymbol);
        startActivity(intent);
    }
}
