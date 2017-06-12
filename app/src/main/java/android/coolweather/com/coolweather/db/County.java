package android.coolweather.com.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by liurong on 2017/6/11.
 */

public class County extends DataSupport {
    private int id;
    private String countyName;
    private String  weatherId;
    private int cityId;
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getCountyName(){
        return this.countyName;
    }
    public void setCountyName(String countyName){
        this.countyName=countyName;
    }
    public String getWeatherId(){
        return this.weatherId;
    }
    public void setWeatherId(String weatherId){
        this.weatherId=weatherId;
    }
    public int getCityId(){
        return this.cityId;
    }
    public void setCityId(int cityId){
        this.cityId=cityId;
    }
}
