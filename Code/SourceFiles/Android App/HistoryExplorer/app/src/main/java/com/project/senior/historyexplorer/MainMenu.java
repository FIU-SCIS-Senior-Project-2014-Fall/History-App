package com.project.senior.historyexplorer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;


public class MainMenu extends FragmentActivity {
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

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
        //makes icon transparent
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
 }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void goToSite (View view) {
        goToUrl ( "http://ha-dev.cis.fiu.edu/");
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}
