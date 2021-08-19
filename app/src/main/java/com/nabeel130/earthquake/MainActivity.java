package com.nabeel130.earthquake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private String[] itemsMagnitude;
    private String[] itemsPlace;
    private String[] itemsURL;
    private ArrayList<Integer> listOfColors;
    private int size;
    private CustomAdapter customAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.customToolB);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setSubtitle(R.string.world);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.listView1);
        listOfColors = new ArrayList<>();
        listOfColors.add(R.color.blue);
        listOfColors.add(R.color.dark_green);
        listOfColors.add(R.color.light_blue);
        listOfColors.add(R.color.dark_blue);
        listOfColors.add(R.color.dull_green);
        size = listOfColors.size();

        try {
            Helper.getGlobalURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem){
        int id = menuItem.getItemId();
        boolean isClickedOnSort = false;
        if(id == R.id.sort){
            isClickedOnSort = true;
            Helper.isOrderByMagnitude = !Helper.isOrderByMagnitude;
            if(Helper.selectedURL.toString().contains("minlatitude"))
                id = R.id.indianRegionMenu;
            else
                id = R.id.globalMenu;

            if(Helper.isOrderByMagnitude)
            Toast.makeText(getApplicationContext(),"Sort By Magnitude", Toast.LENGTH_LONG).show();
        }

        if(id == R.id.indianRegionMenu){
            try {
                if(isClickedOnSort || !Helper.selectedURL.toString().contains("minlatitude")){
                    Helper.getIndianRegionURL();
                    Log.d("jsonCode", Helper.selectedURL.toString());
                    EarthquakeAsyncTask task = new EarthquakeAsyncTask();
                    task.execute();
                    toolbar.setSubtitle(R.string.indianRegion);
                    customAdapter.notifyDataSetChanged();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else if(id == R.id.globalMenu){
            try {
                if(isClickedOnSort || Helper.selectedURL.toString().contains("minlatitude")){
                    Helper.getGlobalURL();
                    Log.d("jsonCode", Helper.selectedURL.toString());
                    EarthquakeAsyncTask task = new EarthquakeAsyncTask();
                    task.execute();
                    toolbar.setSubtitle(R.string.world);
                    customAdapter.notifyDataSetChanged();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }


    public void updateUI(String jsonData){
        try {

            if(TextUtils.isEmpty(jsonData) || jsonData.contentEquals("ERROR"))
                return;

            //extracting the data from json
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray1 = jsonObject.optJSONArray("features");

            if(!Helper.isEmpty(jsonArray1)) {
                itemsPlace = new String[jsonArray1.length()];
                itemsMagnitude = new String[jsonArray1.length()];
                itemsURL = new String[jsonArray1.length()];
            }
            else
                return;

            for(int i=0; i<jsonArray1.length(); i++){

                JSONObject jsonObject1 = jsonArray1.optJSONObject(i);
                if(Helper.isEmpty(jsonObject1)) break;
                JSONObject jsonObject2 = jsonObject1.optJSONObject("properties");
                if(Helper.isEmpty(jsonObject2)) break;

                String magnitude = jsonObject2.optString("mag");
                String timeInMillis = jsonObject2.optString("time");
                String url = jsonObject2.optString("url");

                String date = Helper.getFormattedDate(timeInMillis);
                String time = Helper.getFormattedTime(timeInMillis);
                String place = Helper.getCityCountryName(jsonObject2.optString("place"));
                if(place.equals("null"))
                    place = "Unknown, click to know more";
                String data = "Place: "+place+"\nDate: "+date+"  "+"Time: "+time;

                itemsPlace[i] = data;
                itemsMagnitude[i] = magnitude;
                itemsURL[i] = url;
            }

            customAdapter = new CustomAdapter();
            listView.setAdapter(customAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class EarthquakeAsyncTask extends AsyncTask<URL, Void, String> {

        Dialog progressDialog = new Dialog(MainActivity.this);

        @Override
        protected String doInBackground(URL... urls) {
            try {
                Params params = new Params();
                Log.d("jsonCode","Background code executed");
                return params.makeHTTPRequest(Helper.selectedURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == null)
                return;
            progressDialog.dismiss();
            updateUI(result);
        }

        @SuppressLint("ResourceType")
        @Override
        protected void onPreExecute() {
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.custom_dialog);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.show();
        }
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return itemsPlace.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint({"ViewHolder", "InflateParams"}) View myView = getLayoutInflater().inflate(R.layout.list_item,null);
            TextView textView1 = myView.findViewById(R.id.txtView1);
            textView1.setText(itemsPlace[position]);
            TextView textView2 = myView.findViewById(R.id.txtMagnitude);
            textView2.setText(itemsMagnitude[position]);
            textView1.setOnClickListener(v -> goToURL(position));

            GradientDrawable gdOfTextView = (GradientDrawable) textView2.getBackground();
            gdOfTextView.setColor(ContextCompat.getColor(getBaseContext(), listOfColors.get((position+1)%size)));

            RelativeLayout relativeLayout = myView.findViewById(R.id.cardView1);
            GradientDrawable gradientDrawable =(GradientDrawable) relativeLayout.getBackground();
            gradientDrawable.setColor(ContextCompat.getColor(getBaseContext(), listOfColors.get(position%size)));
            return myView;
        }
    }

    private void goToURL(int position){
        if(itemsURL[position].equals("") || itemsURL[position] == null)
            return;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(itemsURL[position]));
        startActivity(intent);
    }
}