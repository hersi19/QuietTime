package com.example.quiettime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    private HashMap<String, String> parseJsonObject(JSONObject object){

        //Init hashmap
        HashMap<String, String> dataList = new HashMap<>();

        //Get name from object
        try {
            String name= object.getString("name");

            //Get latitude from object
            String latitude=object.getJSONObject("geometry").getJSONObject("location").getString("lat");

            //Get longitude from object
            String longitude=object.getJSONObject("geometry").getJSONObject("location").getString("lng");

            //Put all values in the hashmap
            dataList.put("lat",latitude);
            dataList.put("lng",longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    private List<HashMap<String, String>> parseJsonArray(JSONArray jsonArray){

        //Init hashmap list
        List<HashMap<String,String>> dataList=new ArrayList<>();

        for (int i=0; i<jsonArray.length(); i++){

            //init hash map
            try {
                HashMap<String, String> data= parseJsonObject((JSONObject) jsonArray.get(i));

                //Add data in hashmap list
                dataList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return dataList;
    }



}
