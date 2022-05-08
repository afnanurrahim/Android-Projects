package com.example.json_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView weatherTextView,temperatureTextView;
    AutoCompleteTextView cityName;
    Button searchButton;
    ArrayList<String> citiesName;    // for autocomplete
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(BuildConfig.DEBUG)
            StrictMode.enableDefaults();
        weatherTextView=findViewById(R.id.weatherTextView);
        temperatureTextView=findViewById(R.id.temperatureTextView);
        cityName=findViewById(R.id.cityName);
        searchButton=findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        citiesName=new ArrayList<>();
        autocomplete();
        Log.i("CITY",citiesName.get(0));
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,citiesName);
        cityName.setAdapter(adapter);
    }
    public int[] getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> address;
        int[] LatLng = new int[2];

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            LatLng[0]=(int)location.getLatitude();
            LatLng[1]=(int)location.getLongitude();
            Log.i("ERROR","IN GET LOCATION");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return LatLng;
    }

    @Override
    public void onClick(View view) {
        int[] loc=getLocationFromAddress(cityName.getText().toString());
        if (loc[0]==0 && loc[1]==0) {
            weatherTextView.setText("Enter a valid city name");
        }else{
            String latitude = Integer.toString(loc[0]);
            String longitude = Integer.toString(loc[1]);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=4d730c1e114cd61aa1c7e1d3aa33a6d9", latitude, longitude));
        }
    }

    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String webPage="";
            try {
                URL url =new URL(urls[0]);
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                while ((line= bufferedReader.readLine())!=null){
                    webPage+=line;
                }
                Log.i("Error",webPage);
                return webPage;
            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {        // The value that is returned in the above method is passed in this
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo=jsonObject.getString("weather");
                Log.i("JSON ARRAY",weatherInfo);
                String display="Weather";
                JSONArray jsonArray=new JSONArray(weatherInfo);
                for (int i=0;i<jsonArray.length();i++){           //there can be multiple objects in like: drizzle,cold,etc for that reason we have a for loop
                    JSONObject jsonPart=jsonArray.getJSONObject(i);
                    display+="\n\t\t"+jsonPart.getString("main");
                    display+="\n\t\t"+jsonPart.getString("description");
                    weatherTextView.setText(display);
                    Log.i("main",jsonPart.getString("main"));
                    Log.i("description",jsonPart.getString("description"));
               }
                display="Temperature";
                String tempInfo="["+jsonObject.getString("main")+"]";
                Log.i("JSON ARRAY @",tempInfo);
                jsonArray=new JSONArray(tempInfo);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonPart=jsonArray.getJSONObject(i);
                    Log.i("main",jsonPart.getString("temp"));
                    display+="\n\t\tCurrent: "+jsonPart.getString("temp");
                    display+="\n\t\tMin: "+jsonPart.getString("temp_min");
                    display+="\n\t\tMax: "+jsonPart.getString("temp_max");
                    display+="\n\nPressure: "+jsonPart.getString("pressure");
                    display+="\nHumidity: "+jsonPart.getString("humidity");
                    temperatureTextView.setText(display);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void autocomplete(){
        InputStream in =getResources().openRawResource(R.raw.worldcities);
        BufferedReader buff=new BufferedReader(
                new InputStreamReader(in, StandardCharsets.UTF_8));
        String line="";
        try {
            buff.readLine();    // This is done to avoid the 1st line of csv file
            while ((line=buff.readLine())!=null){
                String[] city=line.split(",");
                city[0]=city[0].substring(1,city[0].length()-1);    // to avoid " " in city name
                citiesName.add(city[0]);        // 1st column is for city name
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}