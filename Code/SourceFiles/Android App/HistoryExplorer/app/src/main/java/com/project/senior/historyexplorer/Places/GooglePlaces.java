package com.project.senior.historyexplorer.Places;

import android.util.Log;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.project.senior.historyexplorer.Parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@SuppressWarnings("deprecation")
public class GooglePlaces {

    /** Global instance of the HTTP transport. */
    //private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    // Google API Key
    //private static final String API_KEY = "AIzaSyD4OFUFpZUAfphENx613OD5Ti0AVFR27bg";

    // Google Places search url's
/*    private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final String PLACES_TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";*/

    //Pull from Web Service -- History Explorer
    private static final String PLACES_SEARCH_URL = "http://ha-dev.cis.fiu.edu/WebApp/rest/getAllPlaces.php";

    //List for searchPlaceNames()
    List<String> allPlaceName;

    //List of searchPlaceDetails
    //List<String> placesDetails;

    //List of the information to display on the Map page
    List<String> placesMapInfo;

    JSONArray placeDetails;

    JSONParser parser;

    JSONObject placeName;
    /*
    private double _latitude;
    private double _longitude;
    private double _radius;
*/

    /**
     * Searches a specific site in the database and returns a list of their details
     * @return list of places
     * */
    public List<String> searchPlaceName()
            throws Exception {

        parser = new JSONParser();
        placeDetails = parser.getJSONFromUrl(PLACES_SEARCH_URL);
        allPlaceName = new ArrayList<>();

        for (int i = 0; i < placeDetails.length(); i++) {
            JSONObject placeName = placeDetails.getJSONObject(i);
            String name = placeName.getString("Name");
            allPlaceName.add(name);
        }

        return allPlaceName;
        //Place place -- This needs to be separate
        /*String[] places = new String[request.length()];
        List<Place> plist;

        for(int i = 0; i < request.length(); i++) {
            places[i] = request.getString(i);
            //plist.add(i, request.get(i).toString());
        }

        PlaceList list = new PlaceList();
        list.results = places;*/

        //list.results. = (List) places;
            /*HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
            HttpRequest request = httpRequestFactory
                    .buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));

            //request.getUrl().put("key", API_KEY);
            *//*request.getUrl().put("location", _latitude + "," + _longitude);
            request.getUrl().put("radius", _radius); // in meters*//*
            //request.getUrl().put("sensor", "false");
            if(types != null)
                request.getUrl().put("types", types);*/

        //PlaceList list = request.execute().parseAs(PlaceList.class);
        // Check log cat for places response status
        //Log.d("Places Status", "" + list.status);
        //return placesDetails;

        /*} catch (HttpResponseException e) {
            Log.e("Error:", e.getMessage());
            return null;
        }*/

    }

    /**
     * Searching of all sites in the database
     * @return list of places
     * # Make sure it does not show Null... just a message "Not Available At This Time." #
        1. Name:
        2. Address:
        3. Email:
        4. Phone:
        5. Hours:
        6. Website:
        7. Description:
     * */
    public List<String> searchPlaceDetails(String siteName)
            throws Exception {
        parser = new JSONParser();
        placeDetails = parser.getJSONFromUrl(PLACES_SEARCH_URL);
        allPlaceName = new ArrayList<>();

        for(int i = 0; i < placeDetails.length(); i++)
        {
            JSONObject placeName = placeDetails.getJSONObject(i);
            String name = placeName.getString("Name");
            if(name.equals(siteName)) {
                String address = placeName.getString("Address");
                String email = placeName.getString("Email");
                String phone = placeName.getString("Phone");
                String hours = placeName.getString("Hours");
                String website = placeName.getString("Website");
                String description = placeName.getString("Description");
                String coordinates = placeName.getString("Coordinates");

                //return a list of the places details.
                allPlaceName.add(name);
                allPlaceName.add(address);
                allPlaceName.add(email);
                allPlaceName.add(phone);
                allPlaceName.add(hours);
                allPlaceName.add(website);
                allPlaceName.add(description);
                allPlaceName.add(coordinates);
            }
        }
       /* for (int i = 0; i < placeDetails.length(); i++) {
            JSONObject placeName = placeDetails.getJSONObject(i);
            String name = placeName.getString("Name");
            allPlaceName.add(name);
        }*/

        return allPlaceName;
    }

    /**
     * Searching of all sites in the database to display on the map
     * @return list of places
     * */
    public List<String> searchForMap()
            throws Exception {

        parser = new JSONParser();
        placeDetails = parser.getJSONFromUrl(PLACES_SEARCH_URL);
        placesMapInfo = new ArrayList<>();

        for (int i = 0; i < placeDetails.length(); i++) {
            JSONObject placeName = placeDetails.getJSONObject(i);
            String name = placeName.getString("Name");
            String coord = placeName.getString("Coordinates");
            placesMapInfo.add(name);
            placesMapInfo.add(coord);
        }

        return placesMapInfo;
    }
    /**
     * Searching single place full details
     * @param reference - reference id of place
     *                 - which you will get in search api request
     * */
 /*   public PlaceDetails getPlaceDetails(String reference) throws Exception {
        try {

            HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
            HttpRequest request = httpRequestFactory
                    .buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
            //request.getUrl().put("key", API_KEY);

            //parse json and return a list of the places details

            request.getUrl().put("reference", reference);
            request.getUrl().put("sensor", "false");

            PlaceDetails place = request.execute().parseAs(PlaceDetails.class);

            return place;

        } catch (HttpResponseException e) {
            Log.e("Error in Perform Details", e.getMessage());
            throw e;
        }
    }
*/
    /**
     * Creating http request Factory
     * */
    public static HttpRequestFactory createRequestFactory(
            final HttpTransport transport) {
        return transport.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                GoogleHeaders headers = new GoogleHeaders();
                headers.setApplicationName("AndroidHive-Places-Test");
                request.setHeaders(headers);
                JsonHttpParser parser = new JsonHttpParser(new JacksonFactory());
                request.addParser(parser);
            }
        });
    }

}