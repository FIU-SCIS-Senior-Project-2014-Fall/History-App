package edu.fiu.seniorproject.historyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;


public class MainScreenClass extends Activity {
    Button goToMapButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.mainscreenlayout);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mainscreenlayout);
        addListenerOnButton();
    }

    private void addListenerOnButton() {
        final Context context = this;

        //Go to Map Button
        goToMapButton = (Button) findViewById(R.id.GoToMapButton);

        goToMapButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, SearchMapClass.class);
                startActivity(intent);

            }

        });
    }
    //button code information
    //http://developer.android.com/guide/topics/ui/controls/button.html

    //Go to Map

    //Now Trending

    //SavedRoutes

    //Feedback
}
