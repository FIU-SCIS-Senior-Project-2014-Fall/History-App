package edu.fiu.seniorproject.historyapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class SearchMapClass extends Activity{
    private GoogleMap googleMap;
    //Connect to searchmaplayout

    //connect to google map api
    //http://glennsayers.com/android-mapping/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchmaplayout);

        //Start up Google Maps
        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
/*
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
}*/



