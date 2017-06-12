package android.coolweather.com.coolweather.util;

import android.coolweather.com.coolweather.db.City;
import android.coolweather.com.coolweather.db.County;
import android.coolweather.com.coolweather.db.Provice;
import android.text.TextUtils;
import android.util.Log;

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
                Log.d("data","@@@@@@@");
                JSONArray allProvices=new JSONArray(response);
                Log.d("data","JSONArray.length="+allProvices.length());
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
                Log.d("data","counties Length="+allCounties.length());
                for(int i=0;i<allCounties.length();i++){
                    JSONObject countyObject=allCounties.getJSONObject(i);
                    County county=new County();
                    Log.d("data","*********@@@@");
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
}



