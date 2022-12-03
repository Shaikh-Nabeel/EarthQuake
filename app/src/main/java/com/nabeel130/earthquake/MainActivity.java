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

import com.nabeel130.earthquake.models.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<Properties> data = new ArrayList<>();
    private ArrayList<Integer> listOfColors;
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
        listOfColors.add(R.color.red);
        listOfColors.add(R.color.orange);
        listOfColors.add(R.color.dark_green);
        listOfColors.add(R.color.dull_green);

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
                data.clear();
            }
            else
                return;

            for(int i=0; i<jsonArray1.length(); i++){

                JSONObject jsonObject1 = jsonArray1.optJSONObject(i);
                if(Helper.isEmpty(jsonObject1)) break;
                JSONObject feature = jsonObject1.optJSONObject("properties");
                if(Helper.isEmpty(feature)) break;

                Properties properties = new Properties(
                        feature.optString("code"),
                        feature.optString("type"),
                        feature.optString("title"),
                        feature.optString("magType"),
                        feature.optInt("tsunami"),
                        feature.optDouble("mag"),
                        feature.optInt("gap"),
                        feature.optString("place"),
                        feature.optString("url"),
                        feature.optLong("time"),
                        feature.optString("detail"),
                        feature.optString("status")
                );
                data.add(properties);
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
            progressDialog.dismiss();
            if(result == null)
                return;
            updateUI(result);
        }

        @SuppressLint("ResourceType")
        @Override
        protected void onPreExecute() {
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.custom_dialog);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.show();
        }
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
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
            TextView place = myView.findViewById(R.id.txtView1);
            TextView magnitude = myView.findViewById(R.id.txtMagnitude);
            TextView date_tv = myView.findViewById(R.id.date_tv);
            TextView time_tv = myView.findViewById(R.id.time_tv);
            TextView viewMore_tv = myView.findViewById(R.id.viewMore_tv);
            TextView status_tv = myView.findViewById(R.id.status_tv);
            viewMore_tv.setOnClickListener(v -> goToURL(position));
            Properties properties = data.get(position);
            place.setText(Objects.equals(properties.getPlace(), "null") ?"Unknown place":properties.getPlace());
            magnitude.setText(String.valueOf(properties.getMag()).substring(0,3));

            String date = Helper.getFormattedDate(String.valueOf(properties.getTime()));
            String time = Helper.getFormattedTime(String.valueOf(properties.getTime()));
            date_tv.setText(date);
            time_tv.setText(time);
            status_tv.setText(properties.getStatus());

            GradientDrawable gdOfTextView = (GradientDrawable) magnitude.getBackground();
            if(properties.getMag() <= 3.5){
                gdOfTextView.setColor(ContextCompat.getColor(getBaseContext(), listOfColors.get(2)));
            }else if(properties.getMag() <= 4.0){
                gdOfTextView.setColor(ContextCompat.getColor(getBaseContext(), listOfColors.get(3)));
            } else if(properties.getMag() <= 5.0){
                gdOfTextView.setColor(ContextCompat.getColor(getBaseContext(), listOfColors.get(1)));
            }else {
                gdOfTextView.setColor(ContextCompat.getColor(getBaseContext(), listOfColors.get(0)));
            }

            return myView;
        }
    }

    private void goToURL(int position){
        if(data.get(position).getUrl() == null || data.get(position).getUrl().equals(""))
            return;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(data.get(position).getUrl()));
        startActivity(intent);
    }
}