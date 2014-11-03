package com.project.senior.historyexplorer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.senior.historyexplorer.MediaAudioFilesActivity;
import com.project.senior.historyexplorer.MediaDocuFilesActivity;
import com.project.senior.historyexplorer.MediaPhotoFilesActivity;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Media's Audio fragment activity
                return new MediaAudioFilesActivity();
            case 1:
                // Media'a Document fragment activity
                //return new MediaDocuFilesActivity();
            case 2:
                // Media's Photo fragment activity
                //return new MediaPhotoFilesActivity();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}