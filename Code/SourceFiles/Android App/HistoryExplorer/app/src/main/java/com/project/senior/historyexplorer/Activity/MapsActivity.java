package com.project.senior.historyexplorer.Activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.senior.historyexplorer.Controllers.AlertDialogManager;
import com.project.senior.historyexplorer.Controllers.ConnectionManager;
import com.project.senior.historyexplorer.Controllers.GPSTracker;
import com.project.senior.historyexplorer.Places.GooglePlaces;
import com.project.senior.historyexplorer.Places.PlaceList;
import com.project.senior.historyexplorer.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

//import com.google.android.GeoPoint;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationChangeListener {

    private GoogleMap historyMap;
    private MarkerOptions markerOptions;
    private LatLng latLng;
    private EditText mapSearchBox;

    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class cd
    ConnectionManager connMgr;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // GPS Location
    GPSTracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        connMgr = new ConnectionManager(getApplicationContext());

        // Check if Internet present
        isInternetPresent = connMgr.isConnectingToInternet();
        if (!isInternetPresent) {
            // Internet Connection is not present
            alert.showAlertDialog(MapsActivity.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        // creating GPS Class object
        gps = new GPSTracker(this);

        // check if GPS location can get
        if (gps.canGetLocation()) {
            setUpMapIfNeeded();

            historyMap.setMyLocationEnabled(true);
            historyMap.setOnMyLocationChangeListener(this);
            historyMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
        } else {
            // Can't get user's current location
            alert.showAlertDialog(MapsActivity.this, "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    false);
            // stop executing code by returned Fp
            return;
        }

        /*For Media page while google maps is down*/
        findViewById(R.id.mediaButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, MediaActivity.class);
                MapsActivity.this.startActivity(intent);
            }
        });

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
        }
    }

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
        private HashMap<String,Integer> mMarkers = new HashMap();
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

                // Manages the markers details, such as where to place the marker,
                // design of the marker, and the information within the popup
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.historical_museum));
                markerOptions.flat(true);
                markerOptions.snippet("Details");

/*                historyMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.infor_window_layout, null);

                        TextView note  = (TextView) v.findViewById(R.id.note);
                        note.setText(marker.getTitle());

                        return v;
                    }
                });

                markerOptions.infoWindowAnchor(0,0);*/
                Marker marker = historyMap.addMarker(markerOptions);
                mMarkers.put(marker.getId(), Integer.parseInt(latLng.toString()));


                //After selecting the marker

                //make the details button available
                /*findViewById(R.id.detailsButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MapsActivity.this, SinglePlaceActivity.class);
                        MapsActivity.this.startActivity(intent);
                    }
                });*/


                //Parse JSON
                //Display nearby places with a different marker

                //Moves to the new location
                historyMap.setMyLocationEnabled(false);
                historyMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                historyMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        int id = mMarkers.get(marker.getId());
                    }
                });
            }
        }
    }
}
