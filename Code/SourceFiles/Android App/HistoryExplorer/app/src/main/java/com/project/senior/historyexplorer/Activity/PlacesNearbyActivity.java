package com.project.senior.historyexplorer.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.project.senior.historyexplorer.Controllers.AlertDialogManager;
import com.project.senior.historyexplorer.Controllers.ConnectionManager;
import com.project.senior.historyexplorer.Controllers.GPSTracker;
import com.project.senior.historyexplorer.Places.GooglePlaces;
import com.project.senior.historyexplorer.Places.Place;
import com.project.senior.historyexplorer.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PlacesNearbyActivity extends Activity {

    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionManager cd;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Google Places
    GooglePlaces googlePlaces;

    // Places List
    List<String> nearPlaces;

    // GPS Location
    GPSTracker gps;

    // Button
    Button btnShowOnMap;
    Button btnExplMap;

    // Progress dialog
    ProgressDialog pDialog;

    // Places Listview
    ListView lv;

    // ListItems data
    ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();


    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place
    public static String KEY_NAME = "name"; // name of the place

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);

        cd = new ConnectionManager(getApplicationContext());

        // Check if Internet present
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            // Internet Connection is not present
            alert.showAlertDialog(PlacesNearbyActivity.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        // creating GPS Class object
        gps = new GPSTracker(this);

        // check if GPS location can get
        if (gps.canGetLocation()) {
            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
        } else {
            // Can't get user's current location
            alert.showAlertDialog(PlacesNearbyActivity.this, "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    false);
            // stop executing code by return
            return;
        }

        // Getting listview
        lv = (ListView) findViewById(R.id.list);

        // button show on map
        btnShowOnMap = (Button) findViewById(R.id.btn_show_map);

        // calling background Async task to load Google Places
        // After getting places from Google all the data is shown in listview
        new LoadPlaces().execute();

        /** Button click event for shown on map */
        btnShowOnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(),
                        PlacesMapActivity.class);
                // staring activity
                startActivity(i);
            }
        });

        // button show on map
        btnExplMap = (Button) findViewById(R.id.btn_expl_map);

        /** Button click event for exploring Map */
        btnExplMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(),
                        MapsActivity.class);
                 startActivity(i);
            }
        });
        /**
         * ListItem click event
         * On selecting a list item SinglePlaceActivity is launched
         * */
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        SinglePlaceActivity.class);

                // Sending place reference id to single place activity
                // place reference id used to get "Place full details"

                // Attach details needed
                in.putExtra(KEY_REFERENCE, reference);

                startActivity(in);
            }
        });
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
            pDialog = new ProgressDialog(PlacesNearbyActivity.this);
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
 /*            ** This can be used if you want to search by a specific radius **
                // Separate your place types by PIPE symbol "|"
                // If you want all types places make it as null
                // Check list of types supported by google
                //
                String types = "cafe|restaurant|establishment|art_gallery|hindu_temple|library|church|museum|stadium|courthouse|mosque|synagogue"; // Listing places only cafes, restaurants

                // Radius in meters - increase this value if you don't find any places
                double radius = 1000; // 1000 meters

                // get nearest places
                nearPlaces = googlePlaces.search(gps.getLatitude(),
                        gps.getLongitude(), radius, types);*/
                nearPlaces = googlePlaces.searchPlaceName();

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
                    //add an if statement
                    //if nearPlaces is not empty do the following
                    //else report an issue
                    for (int p =0 ; p < nearPlaces.size(); p++){
                    //for (Place p : nearPlaces) {
                        HashMap<String, String> map = new HashMap<String, String>();

                        // Place reference won't display in listview - it will be hidden
                        // Place reference is used to get "place full details"

                           map.put(KEY_REFERENCE, nearPlaces.get(p));

                            map.put(KEY_NAME, nearPlaces.get(p));

                        // adding HashMap to ArrayList
                        placesListItems.add(map);
                    }
                    // list adapter
                    ListAdapter adapter = new SimpleAdapter(PlacesNearbyActivity.this,
                            placesListItems,
                            R.layout.list_items,
                            new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {
                            R.id.reference, R.id.name });

                    // Adding data into list view
                    lv.setAdapter(adapter);
               }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mediamain, menu);
        return true;
    }



}