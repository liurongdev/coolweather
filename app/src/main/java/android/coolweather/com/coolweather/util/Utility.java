package android.coolweather.com.coolweather.util;

import android.coolweather.com.coolweather.db.City;
import android.coolweather.com.coolweather.db.County;
import android.coolweather.com.coolweather.db.Provice;
import android.coolweather.com.coolweather.gson.Weather;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liurong on 2017/6/12.
 */

public class Utility {
    public static  boolean handleProviceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvices=new JSONArray(response);
                for(int i=0;i<allProvices.length();i++){
                    JSONObject proviceObject=allProvices.getJSONObject(i);
                    Provice provice=new Provice();
                    provice.setProviceName(proviceObject.getString("name"));
                    provice.setProviceCode(proviceObject.getInt("id"));
                    Log.d("data","provice name="+proviceObject.getString("name"));
                    Log.d("data","provice id="+proviceObject.getInt("id"));
                    provice.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
        return false;
    }
    public static  boolean handleCityResponse(String response,int proviceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCites=new JSONArray(response);
                for(int i=0;i<allCites.length();i++){
                    JSONObject cityObject=allCites.getJSONObject(i);
                    City city=new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProviceId(proviceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties=new JSONArray(response);
                for(int i=0;i<allCounties.length();i++){
                    JSONObject countyObject=allCounties.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    Log.d("data","county name="+countyObject.getString("name"));
                    Log.d("data","county weatherID"+countyObject.getString("weather_id"));
                    Log.d("data","city id="+cityId);
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}



