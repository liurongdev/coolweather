package android.coolweather.com.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by liurong on 2017/6/11.
 */

public class City extends DataSupport{
    private int id;
    private String cityName;
    private int cityCode;
    private int proviceId;

    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getCityName(){
        return this.cityName;
    }
    public void setCityName(String cityName){
        this.cityName=cityName;
    }
    public int getCityCode(){
        return this.cityCode;
    }
    public void setCityCode(int cityCode){
        this.cityCode=cityCode;
    }
    public int getProviceId(){
        return this.proviceId;
    }
    public void setProviceId(int proviceId){
        this.proviceId=proviceId;
    }

}



