package com.project.senior.historyexplorer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MediaDocuFilesActivity extends Fragment {

    /* //XML Node Keys
     private static final String KEY_ITEM = "item"; // parent node
     private static final String KEY_NAME = "name";
     private static final String KEY_LAT = "latitude";
     private static final String KEY_LONGIT = "longitude";
     private static final String KEY_DESC = "description";
     private static final String KEY_PHOTO = "photo";
     private static final String KEY_AUDIO = "audio";
     private static final String KEY_DOCU ="document";
 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.media_docu_files, container, false);

        return rootView;
    }
}