package edu.fiu.seniorproject.historyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.net.Uri;


public class MainScreenClass extends Activity {
   /* Button goToMapButton;
    Button nowTrendingButton;
    Button mySavedRoutesButton;
    Button feedbackButton;*/

    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.mainscreenlayout);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mainscreenlayout);
        //addListenerOnButton();
        //Go to Map Button
        //goToMapButton = (Button) findViewById(R.id.GoToMapButton);
        //goToMapButton.setOnClickListener((OnClickListener) this);

        //Now Trending Button
        //nowTrendingButton = (Button) findViewById(R.id.NowTrendingButton);
        //nowTrendingButton.setOnClickListener((OnClickListener) this);

        //Saved Routes Button
        //mySavedRoutesButton = (Button) findViewById(R.id.SavedRoutesButton);
        //mySavedRoutesButton.setOnClickListener((OnClickListener) this);

        //Saved Routes Button
        //feedbackButton = (Button) findViewById(R.id.FeedbackButton);
        //feedbackButton.setOnClickListener((OnClickListener) this);

    }
    public void onClick(View v){
        switch(v.getId())
        {
            case R.id.GoToMapButton:
                intent = new Intent(MainScreenClass.this, SearchMapClass.class);
                startActivity(intent);
                break;
            case R.id.NowTrendingButton:
                intent = new Intent(MainScreenClass.this, NowTrendingClass.class);
                startActivity(intent);
                break;
            /*case R.id.mySavedRoutesButton:
                intent = new Intent(MainScreenClass.this, SavedRoutesClass.class);
                startActivity(intent);
                break;*/
            case R.id.FeedbackButton:
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://ha-dev.cis.fiu.edu/"));
                startActivity(intent);
                break;
        }
    }

    //button code information
    //http://developer.android.com/guide/topics/ui/controls/button.html


    //SavedRoutes

    //Feedback
}
