package com.nabeel130.earthquake;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class Params {

    public String makeHTTPRequest(URL url) throws IOException {

        if(url == null)
            return "";

        String jsonResponse = "";
        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection =(HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            //if response is 200 then connection is successful
            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        }catch(Exception e){
            e.printStackTrace();
            return "ERROR";
        }finally {
            if(urlConnection != null)
                urlConnection.disconnect();
            if(inputStream != null)
                inputStream.close();
        }
        Log.d("jsoncode", jsonResponse.substring(0,30));
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder response = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line != null){
                response.append(line);
                line = bufferedReader.readLine();
            }
        }
        return response.toString();
    }

}

