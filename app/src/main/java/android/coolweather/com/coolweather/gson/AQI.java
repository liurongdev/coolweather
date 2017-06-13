package android.coolweather.com.coolweather.gson;

/**
 * Created by liurong on 2017/6/12.
 */

public class AQI {
    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
