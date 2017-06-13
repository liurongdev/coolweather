package android.coolweather.com.coolweather;

import android.content.SharedPreferences;
import android.coolweather.com.coolweather.gson.Forecast;
import android.coolweather.com.coolweather.gson.Weather;
import android.coolweather.com.coolweather.util.HttpUtil;
import android.coolweather.com.coolweather.util.Utility;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degressText;
    private TextView weatherInfoText;
    private LinearLayout forcastLayout;

    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    private ImageView bingPicImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        this.weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        this.titleCity = (TextView) findViewById(R.id.title_city);
        this.titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        this.degressText = (TextView) findViewById(R.id.degree_text);
        this.weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        this.forcastLayout = (LinearLayout) findViewById(R.id.forecast_layout);

        this.aqiText = (TextView) findViewById(R.id.aqi_text);
        this.pm25Text = (TextView) findViewById(R.id.pm25_text);
        this.sportText = (TextView) findViewById(R.id.sport_text);
        this.carWashText = (TextView) findViewById(R.id.car_wash_text);
        this.comfortText = (TextView) findViewById(R.id.comfort_text);
        this.bingPicImg=(ImageView)findViewById(R.id.bing_pc_img);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather", null);
        Log.d("data","@weatherString="+weatherString);
        if (weatherString != null) {
            Log.d("data","weatherString!=null is right");
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            String weatherId = getIntent().getStringExtra("weather_id");
            Log.d("data","weather_id form intent="+weatherId);
            weatherLayout.setVisibility(View.VISIBLE);
            requestWeather(weatherId);
        }
        String bingPic=preferences.getString("bing_pic",null);
        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            loadBingPic();
        }
    }
    public void loadBingPic(){
        String picUrl="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(picUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });

    }
        public void showWeatherInfo(Weather weather){
            Log.d("data","###showWeatherInfo");
            String  cityName=weather.basic.cityName;
            String updateTime="未知时间";
            Log.d("data","updateTime beginning="+updateTime);
            updateTime=weather.basic.update.updateTime.split(" ")[1];
            Log.d("data","*****updateTime="+updateTime);
            String degree=weather.now.temperature+"℃";
            String weatherInfo=weather.now.more.info;


            Log.d("data","***degree="+degree);
            Log.d("data","****weatherInfo="+weatherInfo);
            titleCity.setText(cityName);
            titleUpdateTime.setText(updateTime);
            degressText.setText(degree);
            weatherInfoText.setText(weatherInfo);
            forcastLayout.removeAllViews();
            for(Forecast forecast:weather.forecastList){
                View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forcastLayout,false);
                TextView dataText=(TextView)view.findViewById(R.id.date_text);
                TextView infoText=(TextView)view.findViewById(R.id.info_text);
                TextView maxText=(TextView)view.findViewById(R.id.max_text);
                TextView minText=(TextView)view.findViewById(R.id.min_text);
                dataText.setText(forecast.date);
                infoText.setText(forecast.more.info);
                maxText.setText(forecast.temperature.max);
                minText.setText(forecast.temperature.min);
                forcastLayout.addView(view);
            }
            if(weather.aqi!=null){
                aqiText.setText(weather.aqi.city.aqi);
                pm25Text.setText(weather.aqi.city.pm25);
            }
            String comfort="舒适度："+weather.suggestion.comfort.info;
            String carWash="洗车指数："+weather.suggestion.carWash.info;
            String sport="运动建议："+weather.suggestion.sport.info;
            comfortText.setText(comfort);
            sportText.setText(sport);
            carWashText.setText(carWash);
            weatherLayout.setVisibility(View.VISIBLE);

        }
        public void requestWeather(final String weatherId){
            String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=245b5c784c494f268c35aa12e0dfc55e";
            Log.d("data","weatherUrl+"+weatherUrl);
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败！", Toast.LENGTH_SHORT).show();
                    }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText=response.body().string();
                    Log.d("data","reponseText="+responseText);
                    final Weather weather=Utility.handleWeatherResponse(responseText);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(weather!=null && "ok".equals(weather.status)){
                                Log.d("data","response status==ok");
                                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                                editor.putString("weather",responseText);
                                editor.apply();
                                showWeatherInfo(weather);
                            }else{
                                Toast.makeText(WeatherActivity.this, "获取天气信息失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
}
