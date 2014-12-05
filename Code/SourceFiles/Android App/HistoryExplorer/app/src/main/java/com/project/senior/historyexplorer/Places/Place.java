package com.project.senior.historyexplorer.Places;

import com.google.api.client.util.Key;

import java.io.Serializable;

/** Implement this class from "Serializable"
 * So that you can pass this class Object to another using Intents
 * Otherwise you can't pass to another actitivy
 * */
public class Place implements Serializable {

    public String reference;

    @Key
    public String name;

    @Key
    public String address;

    @Key
    public String email;

    @Key
    public String phone_number;

    @Key
    public String hours;

    @Key
    public String url;

    @Key
    public String description;

    @Key
    public String audioPath;

    @Key
    public String imagePath;

    @Key
    public String docuPath;

    @Key
    public String coordinates;

    @Key
    public Geometry geometry;


    @Override
    public String toString() {
        return name + " - " + reference + " - " + description;
    }

    public static class Geometry implements Serializable
    {
        @Key
        public Location location;
    }

    public static class Location implements Serializable
    {
        @Key
        public double lat;

        @Key
        public double lng;
    }

}