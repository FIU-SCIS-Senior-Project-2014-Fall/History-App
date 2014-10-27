package com.project.senior.historyexplorer;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationChangeListener {

    private GoogleMap historyMap;
    private MarkerOptions markerOptions;
    private LatLng latLng;
    private EditText mapSearchBox;
    //private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setUpMapIfNeeded();

        historyMap.setMyLocationEnabled(true);
        historyMap.setOnMyLocationChangeListener(this);
        historyMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        /*For Media page while google maps is down
        findViewById(R.id.mediaButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, MediaActivity.class);
                MapsActivity.this.startActivity(intent);
            }
        });*/

        /*
            Search function.
            Retrieves location from EditText and calls GeoCoderTask() to locate.
        */
        findViewById(R.id.btn_find).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting reference to EditText to get the user input location
                EditText etLocation = (EditText) findViewById(R.id.et_location);

                // Getting user input location
                String location = etLocation.getText().toString();

                if(location!=null && !location.equals("")){
                    new GeoCoderTask().execute(location);
                }
            }
        });


        /*
            Search bar functionality.
            Handles virtual keyboard and information in the search bar
        */
        mapSearchBox = (EditText) findViewById(R.id.et_location);
        mapSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_GO ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mapSearchBox.getWindowToken(), 0);

                    new SearchClicked(mapSearchBox.getText().toString()).execute();
                    mapSearchBox.setText("", TextView.BufferType.EDITABLE);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (historyMap == null) {

            historyMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            /*if (historyMap != null) {
                setUpMap();
            }*/
        }
    }
/*
    private void setUpMap() {
        //historyMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }*/


    @Override
    public void onMyLocationChange(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude,longitude);
        historyMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private class SearchClicked extends AsyncTask<Void, Void, Boolean> {
        private String toSearch;
        private Address address;

        public SearchClicked(String toSearch) {
            this.toSearch = toSearch;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.US);
                List<Address> results = geocoder.getFromLocationName(toSearch, 1);

                if (results.size() == 0) {
                    return false;
                }

                address = results.get(0);

                // Now do something with this GeoPoint:
                //GeoPoint p = new GeoPoint((int) (address.getLatitude() * 1E6), (int) (address.getLongitude() * 1E6))

            } catch (Exception e) {
                Log.e("", "Something went wrong: ", e);
                return false;
            }
            return true;
        }
    }

    // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeoCoderTask extends AsyncTask<String, Void, List<Address>>
    {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            historyMap.clear();

            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){

                Address address = addresses.get(i);

                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.historical_museum));

                historyMap.addMarker(markerOptions);

                // Locate the first location
                if(i==0)
                    historyMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }
}
