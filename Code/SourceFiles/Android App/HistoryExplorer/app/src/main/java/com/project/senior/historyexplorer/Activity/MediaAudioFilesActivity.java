package com.project.senior.historyexplorer.Activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.project.senior.historyexplorer.Controllers.AlertDialogManager;
import com.project.senior.historyexplorer.Controllers.GPSTracker;
import com.project.senior.historyexplorer.Places.GooglePlaces;
import com.project.senior.historyexplorer.Places.Place;
import com.project.senior.historyexplorer.Places.PlaceList;
import com.project.senior.historyexplorer.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MediaAudioFilesActivity extends Fragment {

    private static String url = "http://ha-dev.cis.fiu.edu/WebApp/getAllPlaces.php";
    // Google Places
    private GooglePlaces googlePlaces;
    // Places List
    private PlaceList nearPlaces;
    // Progress dialog
    private ProgressDialog pDialog;
    // Alert Dialog Manager
    private AlertDialogManager alert = new AlertDialogManager();
    // GPS Location
    private GPSTracker gps;
    // ListItems data
    private ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();

    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place
    public static String KEY_NAME = "name"; // name of the place

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.media_audio_files, container, false);
        new MediaAudioFileListActivity();
        return rootView;
    }


    private class MediaAudioFileListActivity extends ListActivity {

        ListView lv;
        Context context;
        MediaPlayer player, stopper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            lv = (ListView) findViewById(R.id.list);

            // calling background Async task to load Google Places
            // After getting places from Google all the data is shown in listview
            new LoadPlaces();
            //new ProgressTask(MediaAudioFileListActivity.this).execute();
        }

        /**
         * Background Async Task to Load Google places
         */
        class LoadPlaces extends AsyncTask<String, String, String> {

            /**
             * Before starting background thread Show Progress Dialog
             */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(MediaAudioFileListActivity.this);
                pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }

            /**
             * getting Places JSON
             */
            protected String doInBackground(String... args) {
                // creating Places class object
                googlePlaces = new GooglePlaces();

                try {
                 /*   // Separate your place types by PIPE symbol "|"
                    // If you want all types places make it as null
                    // Check list of types supported by google
                    //
                    String types = "museum|restaurant|embassy|establishment|art_gallery"; // Listing places only cafes, restaurants

                    // Radius in meters - increase this value if you don't find any places
                    double radius = 1000; // 1000 meters

                    // get nearest places
                    nearPlaces = googlePlaces.search(gps.getLatitude(),
                            gps.getLongitude(), radius, types);
*/

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
             * *
             */
            protected void onPostExecute(String file_url) {
                // dismiss the dialog after getting all products
                pDialog.dismiss();
                // updating UI from Background Thread
                runOnUiThread(new Runnable() {
                    public void run() {
                        /**
                         * Updating parsed Places into LISTVIEW
                         * */
                        // Get json response status
                        String status = nearPlaces.status;

                        // Check for all possible status
                        if (status.equals("OK")) {
                            // Successfully got places details
                            if (nearPlaces.results != null) {
                                // loop through each place
                                for (Place p : nearPlaces.results) {
                                    HashMap<String, String> map = new HashMap<String, String>();

                                    // Place reference won't display in listview - it will be hidden
                                    // Place reference is used to get "place full details"
                                    map.put(KEY_REFERENCE, p.reference);

                                    // Place name
                                    map.put(KEY_NAME, p.name);


                                    // adding HashMap to ArrayList
                                    placesListItems.add(map);
                                }
                                // list adapter
                                ListAdapter adapter = new SimpleAdapter(MediaAudioFileListActivity.this, placesListItems,
                                        R.layout.media_audio_files,
                                        new String[]{KEY_REFERENCE, KEY_NAME}, new int[]{
                                        R.id.reference, R.id.name});

                                // Adding data into listview
                                lv.setAdapter(adapter);
                            }
                        } else if (status.equals("ZERO_RESULTS")) {
                            // Zero results found
                            alert.showAlertDialog(MediaAudioFileListActivity.this, "Near Places",
                                    "Sorry no places found. Try to change the types of places",
                                    false);
                        } else if (status.equals("UNKNOWN_ERROR")) {
                            alert.showAlertDialog(MediaAudioFileListActivity.this, "Places Error",
                                    "Sorry unknown error occured.",
                                    false);
                        } else if (status.equals("OVER_QUERY_LIMIT")) {
                            alert.showAlertDialog(MediaAudioFileListActivity.this, "Places Error",
                                    "Sorry query limit to google places is reached",
                                    false);
                        } else if (status.equals("REQUEST_DENIED")) {
                            alert.showAlertDialog(MediaAudioFileListActivity.this, "Places Error",
                                    "Sorry error occured. Request is denied",
                                    false);
                        } else if (status.equals("INVALID_REQUEST")) {
                            alert.showAlertDialog(MediaAudioFileListActivity.this, "Places Error",
                                    "Sorry error occured. Invalid Request",
                                    false);
                        } else {
                            alert.showAlertDialog(MediaAudioFileListActivity.this, "Places Error",
                                    "Sorry error occured.",
                                    false);
                        }
                    }
                });

            }

        /*private class ProgressTask extends AsyncTask<String, Void, Boolean> {
            private ProgressDialog dialog;
            private ListActivity activity;
            private List<Message> messages;

            public ProgressTask(ListActivity activity) {
                this.activity = activity;
                context = activity;
                dialog = new ProgressDialog(context);
            }

            private Context context;

            @Override
            protected void onPostExecute(final Boolean success) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                ListAdapter adapter = new SimpleAdapter(context, jsonlist,
                        R.layout.media_audio_files,
                        new String[]{KEY_AUDIO},
                        new int[] {R.id.list});
                setListAdapter(adapter);
                //select single ListView item
                lv = getListView();
            }

            protected void onPreExecute() {
                this.dialog.setMessage("Progress start");
                this.dialog.show();
            }

            @Override
            protected Boolean doInBackground(String... params) {

                JSONParser jParser = new JSONParser();

                //get JSON data from URL
                JSONArray json = jParser.getJSONFromUrl(url);

                for (int i = 0; i < json.length(); i++) {
                    try {
                        JSONObject aud = json.getJSONObject(i);
                        String audType = aud.getString("Audio 1");

                        HashMap<String, String> map = new HashMap<>();

                        map.put("Audio 1", audType);
                        jsonlist.add(map);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }*/
        }
    }
}