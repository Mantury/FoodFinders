package com.example.christoph.ur.mi.de.foodfinders.starting_screen;

import com.example.christoph.ur.mi.de.foodfinders.log.Log;
import com.example.christoph.ur.mi.de.foodfinders.restaurants.restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by juli on 09.09.15.
 */
public class JSONtoObjectConverter {

    private String JSONResponse;

    private static final String NAME = "name";
    private static final String LAT = "lat";
    private static final String LNG = "lng";
    private static final String OPEN = "open_now";
    private static final String ADDRESS = "vicinity";
    private static final String ID = "place_id";
    private static final String RESULTS="results";
    private static final String GEOMETRY="geometry";
    private static final String LOCATION="location";
    private static final String OPENINGHOURS="opening_hours";
    private static final String WEEKDAYTEXT="weekday_text";
    private static final String OPENNOW="open_now";
    private static final String RESULT="result";
    private static final String FORMATTEDADDRESS="formatted_address";
    private static final String FORMATTEDPHONENUMBER="formatted_phone_number";
    private static final String REVIEWS="reviews";
    private static final String TEXT="text";
    private static final String USERRATINGSTOTAL="user_ratings_total";
    private static final String PHOTOS="photos";
    private static final String PHOTOREFERENCE="photo_reference";
    private ArrayList<restaurant> list;
    private ArrayList<String> commentlist;
    public int open;

    public JSONtoObjectConverter(String JSONResponse) {
        Log.d("Set up converter");
        this.JSONResponse = JSONResponse;
    }

    public ArrayList<restaurant> convertJSONTorestaurant() {
        try {
            JSONObject jsonOb = new JSONObject(JSONResponse);
            Log.d(String.valueOf(jsonOb));
            JSONArray jsonArray = jsonOb.getJSONArray(RESULTS);
            list = new ArrayList<restaurant>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject locationobject = jsonObject.getJSONObject(GEOMETRY);
                JSONObject latlngobject = locationobject.getJSONObject(LOCATION);
                double lng = latlngobject.getDouble(LNG);
                double lat = latlngobject.getDouble(LAT);
                String name = jsonObject.getString(NAME);
                JSONObject openobject = jsonObject.optJSONObject(OPENINGHOURS);
                if (openobject != null) {
                    if (openobject.getBoolean(OPENNOW)) {
                        open = 1;
                    } else {
                        open = 2;
                    }

                } else {
                    open = 0;
                }
                String id = jsonObject.getString(ID);
                String address = jsonObject.getString(ADDRESS);
                restaurant res = new restaurant(name, lat, lng, id, open, address);
                list.add(res);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    //Creates a Detailed Restaurant by redownloading the data.
    public restaurant convertToDetailedRestaurant() {
        restaurant detailedRest = null;
        try {
            JSONObject jsonOb = new JSONObject(JSONResponse);
            JSONObject jsonrestaurant = jsonOb.getJSONObject(RESULT);
            String address = "nicht in google vorhanden";
            address = jsonrestaurant.optString(FORMATTEDADDRESS);
            String number = jsonrestaurant.getString(FORMATTEDPHONENUMBER);
            String name = jsonrestaurant.getString(NAME);
            String id = jsonrestaurant.getString(ID);

            JSONObject openinghours = jsonrestaurant.optJSONObject(OPENINGHOURS);
            int open;
            String openweekday="notfound";

            if (openinghours != null) {
                openweekday = openinghours.getString(WEEKDAYTEXT);
                if (openinghours.getBoolean(OPENNOW)) {
                    open = 1;
                } else {
                    open = 2;
                }
            } else {
                open = 0;
            }

            JSONArray comments = jsonrestaurant.optJSONArray(REVIEWS);
            commentlist = new ArrayList<String>();
            if(comments!=null) {
              //  commentlist = new ArrayList<String>();
                for (int i = 0; i < comments.length(); i++) {
                    JSONObject jsonObject = comments.getJSONObject(i);
                    String comment = jsonObject.getString(TEXT);
                    commentlist.add(comment);
                }
            }else{
                commentlist.add("Sorry no comments!");
            }
            String rating = "no ratings";
            rating = jsonrestaurant.optString(USERRATINGSTOTAL);
            String image_ref = "no Image!!";
            JSONArray jsonImage = jsonrestaurant.optJSONArray(PHOTOS);
            if (jsonImage != null) {
                JSONObject image1 = jsonImage.getJSONObject(0);
                image_ref = image1.getString(PHOTOREFERENCE);
            }
            detailedRest = new restaurant(name,0,0 ,id, open, address);
            detailedRest.setDetails(image_ref, number, rating, openweekday, commentlist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return detailedRest;
    }
}
