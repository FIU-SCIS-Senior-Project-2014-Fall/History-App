package com.project.senior.historyexplorer;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class TrendingActivity extends Activity{

    private ImageView image;
    private String[] topRoutes;
    private String[] topSites;
    private String[] popularSites;
    private String[] popularRoutes;
    private String[] newestSites;

    private Spinner topSiteSpinner;
    private Spinner topRouteSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_trending);

        //ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        //initialize sections in Now Trending

        //fill out sections with current data

        //allow action clicking to select the site or route data within the Map Screen
    }

    public ActionBar getSupportActionBar()
    {
        return null;
    }
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private String[] fillArray(String[] section)
    {
        //call the webservice
        //pull information from the database to fill String arrays with values
        return null;
    }

}
