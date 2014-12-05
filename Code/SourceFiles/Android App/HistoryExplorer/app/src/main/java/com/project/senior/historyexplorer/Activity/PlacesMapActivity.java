package com.project.senior.historyexplorer.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.senior.historyexplorer.Controllers.AddItemizedOverlay;
import com.project.senior.historyexplorer.Places.GooglePlaces;
import com.project.senior.historyexplorer.Places.Place;
import com.project.senior.historyexplorer.Places.PlaceList;
import com.project.senior.historyexplorer.R;
import com.project.senior.historyexplorer.maps.GeoPoint;
import com.project.senior.historyexplorer.maps.Overlay;
import com.project.senior.historyexplorer.maps.MapController;
import com.project.senior.historyexplorer.maps.OverlayItem;
import com.project.senior.historyexplorer.maps.MapView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class PlacesMapActivity extends MapsActivity {
     //MapView mapView;
    GoogleMap mapView;

    // Progress dialog
    ProgressDialog pDialog;

    // Google Places
    GooglePlaces googlePlaces;

    // Places List
    List<String> nearPlaces;

    private MarkerOptions markerOptions;
    private LatLng latLng;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapView = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        new LoadPlaces().execute();


    }

    /**
     * Background Async Task to Load Google places
     * */
    class LoadPlaces extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PlacesMapActivity.this);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Places JSON
         * */
        protected String doInBackground(String... args) {
            // creating Places class object
            googlePlaces = new GooglePlaces();

            try {
                nearPlaces = googlePlaces.searchForMap();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * and show the data in UI
         * Always use runOnUiThread(new Runnable()) to update UI from background
         * thread, otherwise you will get error
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed Places into LISTVIEW
                     * */

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


                        Marker marker = historyMap.addMarker(markerOptions);


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
            });

        }

    }

}
