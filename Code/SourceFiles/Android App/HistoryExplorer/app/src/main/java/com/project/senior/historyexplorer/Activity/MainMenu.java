package com.project.senior.historyexplorer.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.project.senior.historyexplorer.R;


public class MainMenu extends FragmentActivity {
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //makes icon transparent
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#003300")));

        /*Transition from Main Menu to Map Screen*/
        findViewById(R.id.go_to_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainMenu.this, MapsActivity.class);
                MainMenu.this.startActivity(intent);
            }
        });

        findViewById(R.id.now_trending).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainMenu.this, TrendingActivity.class);
                MainMenu.this.startActivity(intent);
            }
        });

        findViewById(R.id.saved_routes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainMenu.this, SavedRoutes.class);
                MainMenu.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void goToSite (View view) {
        goToUrl ( "http://ha-dev.cis.fiu.edu/WebApp/#/layout/feedback");
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}
