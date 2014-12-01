package com.project.senior.historyexplorer.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.project.senior.historyexplorer.Controllers.AddItemizedOverlay;
import com.project.senior.historyexplorer.Places.Place;
import com.project.senior.historyexplorer.Places.PlaceList;
import com.project.senior.historyexplorer.R;
import com.project.senior.historyexplorer.maps.GeoPoint;
import com.project.senior.historyexplorer.maps.Overlay;
import com.project.senior.historyexplorer.maps.MapController;
import com.project.senior.historyexplorer.maps.OverlayItem;
import com.project.senior.historyexplorer.maps.MapView;

import java.util.List;


public class PlacesMapActivity extends MapsActivity {
    // Nearest places
    PlaceList nearPlaces;

    // Map view
    //MapView mapView;
    GoogleMap mapView;

    // Map overlay items
    List<Overlay> mapOverlays;

    AddItemizedOverlay itemizedOverlay;

    GeoPoint geoPoint;
    // Map controllers
    MapController mc;

    double latitude;
    double longitude;
    OverlayItem overlayitem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Getting intent data
        Intent i = getIntent();

        // Users current geo location
        String user_latitude = i.getStringExtra("user_latitude");
        String user_longitude = i.getStringExtra("user_longitude");

        // Nearplaces list
        nearPlaces = (PlaceList) i.getSerializableExtra("near_places");

        mapView = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        // Geopoint to place on map
        geoPoint = new GeoPoint((int) (Double.parseDouble(user_latitude) * 1E6),
                (int) (Double.parseDouble(user_longitude) * 1E6));

        // Drawable marker icon
        Drawable drawable_user = this.getResources()
                .getDrawable(R.drawable.mark_blue);

        itemizedOverlay = new AddItemizedOverlay(drawable_user, this);

        // Map overlay item
        overlayitem = new OverlayItem(geoPoint, "Your Location!");

        /*itemizedOverlay.addOverlay(overlayitem);

        mapOverlays.add(itemizedOverlay);
        itemizedOverlay.populateNow();*/

        // Drawable marker icon
        Drawable drawable = this.getResources()
                .getDrawable(R.drawable.historical_museum);

        itemizedOverlay = new AddItemizedOverlay(drawable, this);

               // These values are used to get map boundary area
        // The area where you can see all the markers on screen
        int minLat = Integer.MAX_VALUE;
        int minLong = Integer.MAX_VALUE;
        int maxLat = Integer.MIN_VALUE;
        int maxLong = Integer.MIN_VALUE;

        // check for null in case it is null
        if (nearPlaces.results != null) {
            // loop through all the places
            for (Place place : nearPlaces.results) {
                latitude = place.geometry.location.lat; // latitude
                longitude = place.geometry.location.lng; // longitude

                // Geopoint to place on map
                geoPoint = new GeoPoint((int) (latitude * 1E6),
                        (int) (longitude * 1E6));

                // Map overlay item
                overlayitem = new OverlayItem(geoPoint, place.name);

                itemizedOverlay.addOverlay( (OverlayItem) overlayitem);


                // calculating map boundary area
                minLat  = (int) Math.min( geoPoint.getLatitudeE6(), minLat );
                minLong = (int) Math.min( geoPoint.getLongitudeE6(), minLong);
                maxLat  = (int) Math.max( geoPoint.getLatitudeE6(), maxLat );
                maxLong = (int) Math.max( geoPoint.getLongitudeE6(), maxLong );
            }
            /*mapOverlays.add(itemizedOverlay);*/

            // showing all overlay items
            itemizedOverlay.populateNow();
        }

        // Adjusting the zoom level so that you can see all the markers on map
        //Moves to the new location and shows center of the map

        mapView.animateCamera(CameraUpdateFactory.newLatLng(new LatLng((maxLat + minLat)/2, (maxLong + minLong)/2 )));
        mapView.animateCamera(CameraUpdateFactory.zoomTo(Math.abs( minLat - maxLat )));
        //mapView.getController().zoomToSpan(Math.abs( minLat - maxLat ), Math.abs( minLong - maxLong ));

        // Showing the center of the map
        //mc.animateTo(new GeoPoint((maxLat + minLat)/2, (maxLong + minLong)/2 ));
        //mapView.postInvalidate();

    }

    protected boolean isRouteDisplayed() {
        return false;
    }

}
