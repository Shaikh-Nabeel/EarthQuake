package com.nabeel130.earthquake;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Helper {
    public static int limit = 25;
    public static boolean isOrderByMagnitude= false;
    public static boolean isOrderByTime = false;
//    private static final String urlForAsia =


    public static String getFormattedDate(String timeInMilliSec){

        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");

        long milliSeconds= Long.parseLong(timeInMilliSec);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getFormattedTime(String timeInMilliSec){
        @SuppressLint("SimpleDateFormat") DateFormat timeFormat = new SimpleDateFormat("h:mm a");
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = Long.parseLong(timeInMilliSec);
        calendar.setTimeInMillis(timeInMillis);
        return timeFormat.format(calendar.getTime());
    }

    //extracting city name
    public static String getCityCountryName(String name){
        if(name == null)
            return null;
        return name.substring(!name.contains("of")?0:name.indexOf("of")+3);
    }

    //url of api to fetch json data
    public static URL getURL() throws MalformedURLException {
        String [] dates = getDates();
        String date1 = dates[0];
        String date2 = dates[1];
        String URL_JSON = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime="+date2+"&endtime="+date1+"&minmagnitude=4.5&limit="+limit;
        if(isOrderByMagnitude)URL_JSON += "&orderby=magnitude";
        return new URL(URL_JSON);
    }

    public static String[] getDates(){
        String[] dates = new String[2];
        long currTimeInMillis = System.currentTimeMillis();
        dates[0] = currentDateFromTime(currTimeInMillis);
        dates[1] = currentDateFromTime(currTimeInMillis -20*24*60*60*1000);
        return dates;
    }

    public static URL getCustomURL() throws MalformedURLException {
        String [] dates = getDates();
        String date1 = dates[0];
        String date2 = dates[1];
        String URL_JSON = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime="+date2+"&endtime="+date1+"&minmagnitude=4.5&limit="+limit+"&minlatitude=8.4&maxlatitude=37.6&minlongitude=68&maxlongitude=97";
        return new URL(URL_JSON);
    }

    private static String currentDateFromTime(long date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    public static boolean isEmpty(JSONObject jsonObject){
        return jsonObject == null;
    }

    public static boolean isEmpty(JSONArray jsonArray){
        return jsonArray == null;
    }
}
