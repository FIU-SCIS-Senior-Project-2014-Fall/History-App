package com.project.senior.historyexplorer.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.senior.historyexplorer.Places.GooglePlaces;
import com.project.senior.historyexplorer.R;

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

    //Places Lat and Lng
    String[] placesLatLng;

    private MarkerOptions markerOptions;
    private LatLng latLng;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_map_activity);

        mapView = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.placemap)).getMap();
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

                    if(nearPlaces==null || nearPlaces.size()==0){
                        Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
                    }

                    // Clears all the existing markers on the map
                    mapView.clear();

                    // Adding Markers on Google Map for each Place in the Database
                    for(int i=0;i<nearPlaces.size();i+=2){
                        String name = nearPlaces.get(0);
                        String coordinates = nearPlaces.get(1);

                        //Strings enter as "####","####".
                        // //This method makes it into a list of two: {Lat,Lng)
                        //If coordinates is empty,
                        if(coordinates != null) {

                            String[] input = coordinates.split(",");
                            double lat = Double.parseDouble(input[0]);
                            double lng = Double.parseDouble(input[1]);

                            // Creating an instance of GeoPoint, to display in Google Map
                            latLng = new LatLng(lat, lng);


                            // Manages the markers details, such as where to place the marker,
                            // design of the marker, and the information within the popup
                            markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title(name);
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.historical_museum));
                            markerOptions.flat(true);

                            //adds markers to the map
                            mapView.addMarker(markerOptions);


                            /*//Moves to the new location
                            mapView.setMyLocationEnabled(true);
                            mapView.animateCamera(CameraUpdateFactory.zoomTo(15));*/
                        }
                        //Moves to the new location
                        mapView.setMyLocationEnabled(true);
                        mapView.animateCamera(CameraUpdateFactory.zoomTo(15));

                    }
                }
            });

        }

    }

}
