package com.finpro.platform.furnitureecommerceappui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    LinearLayout homeLinearLayout;
    LinearLayout knowledgeTab, linksTab;
    LinearLayout knowledgeContent, linksContent;
    TextView knowledgeText, linksText;
    View knowledgeIndicator, linksIndicator;
    
    CardView githubCard, screenerCard, juiceCard, yahooCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        homeLinearLayout = findViewById(R.id.home_linear_layout);
        knowledgeTab = findViewById(R.id.knowledge_tab);
        linksTab = findViewById(R.id.links_tab);
        knowledgeContent = findViewById(R.id.knowledge_content);
        linksContent = findViewById(R.id.links_content);
        knowledgeText = findViewById(R.id.knowledge_text);
        linksText = findViewById(R.id.links_text);
        knowledgeIndicator = findViewById(R.id.knowledge_indicator);
        linksIndicator = findViewById(R.id.links_indicator);
        
        githubCard = findViewById(R.id.github_card);
        screenerCard = findViewById(R.id.screener_card);
        juiceCard = findViewById(R.id.juice_card);
        yahooCard = findViewById(R.id.yahoo_card);
    }

    private void setupClickListeners() {
        homeLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(Main3Activity.this, Main2Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        knowledgeTab.setOnClickListener(v -> switchTab(true));
        linksTab.setOnClickListener(v -> switchTab(false));

        githubCard.setOnClickListener(v -> openUrl("https://github.com/Shreyas-Daptare"));
        screenerCard.setOnClickListener(v -> openUrl("https://www.screener.in"));
        juiceCard.setOnClickListener(v -> openUrl("https://www.financialjuice.com/home"));
        yahooCard.setOnClickListener(v -> openUrl("https://finance.yahoo.com/"));
    }

    private void switchTab(boolean showKnowledge) {
        if (showKnowledge) {
            knowledgeContent.setVisibility(View.VISIBLE);
            linksContent.setVisibility(View.GONE);
            knowledgeText.setTextColor(getResources().getColor(android.R.color.black));
            linksText.setTextColor(getResources().getColor(android.R.color.darker_gray));
            knowledgeIndicator.setVisibility(View.VISIBLE);
            linksIndicator.setVisibility(View.INVISIBLE);
        } else {
            knowledgeContent.setVisibility(View.GONE);
            linksContent.setVisibility(View.VISIBLE);
            linksText.setTextColor(getResources().getColor(android.R.color.black));
            knowledgeText.setTextColor(getResources().getColor(android.R.color.darker_gray));
            linksIndicator.setVisibility(View.VISIBLE);
            knowledgeIndicator.setVisibility(View.INVISIBLE);
        }
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
