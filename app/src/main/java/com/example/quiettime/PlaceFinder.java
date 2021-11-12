package com.example.quiettime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.AsyncTaskLoader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.List;

public class PlaceFinder extends AppCompatActivity {

    Spinner spType;
    Button btFind;
    SupportMapFragment mSupportMapFragment;
    GoogleMap map;
    FusedLocationProviderClient mFusedLocationProviderClient;
    double currentLat=0, currentLong=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_finder);

        spType = findViewById(R.id.sp_type);
        btFind = findViewById(R.id.bt_find);
        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        //Type of quiet places
        String[] placeTypeList = {"library", "cafe"};

        //Place names
        String[] placeNameList = {"Library", "Cafe"};

        //Set adapter on spinner
        spType.setAdapter(new ArrayAdapter<>(
                PlaceFinder.this, android.R.layout.simple_spinner_dropdown_item, placeNameList));


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Check permission
        if (ActivityCompat.checkSelfPermission(PlaceFinder.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //If permission is granted, get current loaction
            getCurrentLocation();

        }else{

            //Request permission
            ActivityCompat.requestPermissions(
                    PlaceFinder.this, new String[] Manifest.permission.ACCESS_FINE_LOCATION, 44);
        }

        btFind.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v ){

                int i=spType.getSelectedItemPosition();

                //Init url
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                        "?location=" + currentLat + "," + currentLong +
                        "&radius=5000" + "&type=" + placeTypeList[i] +
                        "&sensor=true" + "&key=" + getResources().getString(R.string.google_map_key);

                //Execute place task method to download json data
                new PlaceTask().execute(url);
            }
        });


    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {

        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>(){
            @Override
            public void onSuccess(Location location){

                if(location !=null){

                    currentLat=location.getLatitude();
                    currentLong=location.getLongitude();

                    //Sync map
                    mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                            map=googleMap;

                            //Zoom into current location
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLat,currentLong), 10
                            ));

                        }
                    });
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //If permission is granted, get current loaction
                getCurrentLocation();

            }
        }
    }

    private class PlaceTask extends AsyncTask<String , Integer, String> {
        @Override
        protected String doInBackground(String... strings){

            String data=null;
            try {
                //Init data
               data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s){
            //super.onPostExecute(s);
            //Execute parser task
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        //Init url
        URL url=new URL(string);

        //Init connection
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();

        //Connect
        connection.connect();

        //Init input stream
        InputStream stream= connection.getInputStream();
        //Init buffer reader
        BufferedReader reader= new BufferedReader(new InputStreamReader(stream));

        //Init string builder
        StringBuilder builder=new StringBuilder();

        //Init string variable
        String line="";

        while((line= reader.readLine()) !=null){

            //Append line
            builder.append(line);

        }

        //Get append data
        String data=builder.toString();
        //Close reader
        reader.close();

        return data;
    }


    private class ParserTask extends AsyncTask<String,Integer,List<HashMap<String,String>>>{

        @Override
        List<HashMap<String,String>> doInBackground(String... strings){

            //Create json parser class
            JsonParser
            return null;
        }

    }

}