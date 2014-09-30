package edu.fiu.seniorproject.historyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class MainScreenClass extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.mainscreenlayout);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mainscreenlayout);

    }
    //button code information
    //http://developer.android.com/guide/topics/ui/controls/button.html

    //Go to Map

    //Now Trending

    //SavedRoutes

    //Feedback
}
