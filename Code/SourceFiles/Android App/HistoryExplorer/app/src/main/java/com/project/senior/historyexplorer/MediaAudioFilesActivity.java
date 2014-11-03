package com.project.senior.historyexplorer;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.project.senior.historyexplorer.Parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MediaAudioFilesActivity extends Fragment{

    private static String url = "http://ha-dev.cis.fiu.edu/WebApp/Files/data.json";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.media_audio_files, container, false);
        new MediaAudioFileListActivity();
        return rootView;

    }


    private class MediaAudioFileListActivity extends ListActivity
    {

        private Context context;

        //JSON Keys
        private static final String KEY_ITEM = "item"; // parent node
        private static final String KEY_NAME = "name";
        private static final String KEY_LAT = "latitude";
        private static final String KEY_LONGIT = "longitude";
        private static final String KEY_DESC = "description";
        //private static final String KEY_PHOTO = "photo";
        private static final String KEY_AUDIO = "audio";
        //private static final String KEY_DOCU ="document";


        ArrayList<HashMap<String, String>> jsonlist = new ArrayList<>();
        ListView lv;

        @Override
        protected void onCreate (Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            findViewById(R.id.Audio1Button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        MediaPlayer player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource("http://ha-dev.cis.fiu.edu/WebApp/Files/HofR4.m4a");
                        player.prepare();
                        player.start();
                    }
                    catch(Exception e)
                    {
                        //Toast.makeText("Could not play audio file. Try again.");
                        e.printStackTrace();
                    }
                }
            });
            new ProgressTask(MediaAudioFileListActivity.this).execute();
        }

        private class ProgressTask extends AsyncTask<String, Void, Boolean> {
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
                        new int[] {R.id.listViewAudio});
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
        }
    }
}