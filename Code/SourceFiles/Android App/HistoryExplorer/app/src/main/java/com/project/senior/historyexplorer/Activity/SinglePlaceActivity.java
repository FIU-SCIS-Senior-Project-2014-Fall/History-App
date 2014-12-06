package com.project.senior.historyexplorer.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.senior.historyexplorer.Controllers.AlertDialogManager;
import com.project.senior.historyexplorer.Controllers.ConnectionManager;
import com.project.senior.historyexplorer.Places.GooglePlaces;
import com.project.senior.historyexplorer.Places.Place;
import com.project.senior.historyexplorer.R;

import java.util.List;

public class SinglePlaceActivity extends Activity {

    // Google Places
    GooglePlaces googlePlaces;

    // Place Details
    Button mediaButton;

    // Progress dialog
    ProgressDialog pDialog;
    String name;

    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_single_place);

        Intent i = getIntent();

        // Place reference id

        //Here you retrieve the information from the previous activity
        // such as Name, Address, Phone, etc
        String reference = i.getStringExtra(KEY_REFERENCE);

        // Calling a Async Background thread
        new LoadSinglePlaceDetails().execute(reference);

        mediaButton = (Button)findViewById(R.id.mediaButton);
        /** Button click event for shown on map */
        mediaButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(),
                        MediaActivity.class);
                i.putExtra("name", name);

                // staring activity
                startActivity(i);
            }
        });
    }


    /**
     * Background Async Task to Load Google places
     * */
    class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {

        List<String> placeListDetails;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SinglePlaceActivity.this);
            pDialog.setMessage("Loading profile ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Profile JSON
         * */
        protected String doInBackground(String... args) {
            String reference = args[0];

            // creating Places class object
            googlePlaces = new GooglePlaces();

            // Check if used is connected to Internet
            try {
                placeListDetails = googlePlaces.searchPlaceDetails(reference);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
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
                    final Place place = new Place();

                    /*Place Information:
                    * Fill in Place information*/
                    name = place.name = placeListDetails.get(0);

                    place.address = placeListDetails.get(1);
                    place.email = placeListDetails.get(2);
                    place.phone_number = placeListDetails.get(3);
                    place.hours = placeListDetails.get(4);
                    place.url = placeListDetails.get(5);
                    place.description = placeListDetails.get(6);
                    place.coordinates = placeListDetails.get(7);

                    TextView lbl_name = (TextView) findViewById(R.id.name);
                    TextView lbl_address = (TextView) findViewById(R.id.address);
                    TextView lbl_email = (TextView) findViewById(R.id.email);
                    TextView lbl_phone = (TextView) findViewById(R.id.phoneNumber);
                    TextView lbl_hours = (TextView) findViewById(R.id.hours);
                    TextView lbl_website = (TextView) findViewById(R.id.website);
                    TextView lbl_description = (TextView) findViewById(R.id.description);

                    // Check for null data from google
                    // Sometimes place details might missing
                    /*name = name == null ? "Not present" : name; // if name is null display as "Not present"
                    address = address == null ? "Not present" : address;
                    phone = phone == null ? "Not present" : phone;
                    latitude = latitude == null ? "Not present" : latitude;
                    longitude = longitude == null ? "Not present" : longitude;*/

                    lbl_name.setText(place.name);
                    lbl_address.setText(place.address);
                    lbl_email.setText(place.email);
                    lbl_phone.setText(place.phone_number);
                    lbl_hours.setText(place.hours);
                    lbl_website.setText(place.url);
                    lbl_description.setText(place.description);

                                    }
            });

        }

    }

}
