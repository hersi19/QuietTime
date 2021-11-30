package com.example.quiettime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.AsyncTaskLoader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

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

    final String TAG = "PlaceFinder";
    Spinner spType;
    Button btFind;
    SupportMapFragment mSupportMapFragment;
    GoogleMap map;
    FusedLocationProviderClient mFusedLocationProviderClient;
    double currentLat=0, currentLong=0;
    BottomNavigationView bottom_nav;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_finder);
        Log.d(TAG,"OnCreate called");

        toolbar = findViewById(R.id.map_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quiet Places");

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
                    PlaceFinder.this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        btFind.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v ){

                int i=spType.getSelectedItemPosition();

                //Init url
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                        "?location=" + currentLat + "," + currentLong +
                        "&radius=20000" + "&type=" + placeTypeList[i] +
                        "&sensor=true" + "&key=" + getResources().getString(R.string.google_map_key);

                Log.d(TAG,"place URL set");
                //Execute place task method to download json data
                new PlaceTask().execute(url);


            }
        });



        //handle bottom navigation bar
        bottom_nav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottom_nav.setSelectedItemId(R.id.navigation_maps);

        bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigation_task:
                        startActivity(new Intent(getApplicationContext(),TasksActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_maps:
                        startActivity(new Intent(getApplicationContext(),PlaceFinder.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });


    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>(){
            @Override
            public void onSuccess(Location location){

                if (location != null) {
                    currentLat = location.getLatitude();
                    currentLong = location.getLongitude();

                    mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                            map = googleMap;

                            //Zoom into current location
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLat, currentLong), 12
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
                Log.d(TAG,"call URL"+strings[0]);
               data = downloadUrl(strings[0]);
                Log.d(TAG,"return from downloadUrl");
                Log.d(TAG,data);

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
        Log.d(TAG,"downloadUrl");
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
        protected List<HashMap<String,String>> doInBackground(String... strings){

            //Create json parser class
            JsonParser jsonParser= new JsonParser();

            //Init hash map list
            List<HashMap<String, String>> mapList=null;
            //Init json object
            JSONObject object=null;
            try {
                object=new JSONObject(strings[0]);
                //Parse json object
                mapList=jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG,"mapList>>"+mapList);

            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps){

            //Clear map
            map.clear();


            for(int i=0;i<hashMaps.size(); i++){
                //Init hashmap
                HashMap<String, String> hashMapList=hashMaps.get(i);
                //Get latitude
                double lat=Double.parseDouble(hashMapList.get("lat"));
                //Get longitude
                double lng=Double.parseDouble(hashMapList.get("lng"));
                //Get name
                String name=hashMapList.get("name");
                //Concat lat and lng
                LatLng latLng=new LatLng(lat,lng);
                //Init marker options
                MarkerOptions options= new MarkerOptions();
                //Set position
                options.position(latLng);
                //Set title
                options.title(name);
                //Add marker on map
                map.addMarker(options);

            }
        }

    }

}