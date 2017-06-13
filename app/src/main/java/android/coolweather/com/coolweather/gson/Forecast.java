package android.coolweather.com.coolweather.gson;

import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import javax.xml.transform.Templates;

/**
 * Created by liurong on 2017/6/12.
 */

public class Forecast {
    public String date;

    @SerializedName("cond")
    public More more;

    @SerializedName("tmp")
    public Temperature temperature;

    public class More{
        @SerializedName("txt_d")
        public String info;
    }
    public class Temperature{
        public String max;
        public String min;
    }

}
