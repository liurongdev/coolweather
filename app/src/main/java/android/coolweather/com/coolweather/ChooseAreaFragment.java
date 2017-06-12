package android.coolweather.com.coolweather;

import android.app.ProgressDialog;
import android.coolweather.com.coolweather.db.City;
import android.coolweather.com.coolweather.db.County;
import android.coolweather.com.coolweather.db.Provice;
import android.coolweather.com.coolweather.util.HttpUtil;
import android.coolweather.com.coolweather.util.Utility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by liurong on 2017/6/12.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVICE=0;
    public static final int LEVLE_CITY=1;
    public static final int LEVEL_COUNTY=2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;

    private ArrayAdapter<String>adapter;
    private List<String>dataList=new ArrayList<>();

    private List<Provice>proviceList;
    private List<City>cityList;
    private List<County>countyList;

    private Provice selectedProvice;
    private City selectedCity;
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.choose_area,container,false);
        titleText=(TextView)view.findViewById(R.id.title_text);
        backButton=(Button)view.findViewById(R.id.back_button);
        listView=(ListView)view.findViewById(R.id.list_view);
        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);
        Log.d("data","datalist="+dataList);
        listView.setAdapter(adapter);
        Log.d("data","1111");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVICE){
                    selectedProvice=proviceList.get(position);
                    queryCities();
                }else if(currentLevel==LEVLE_CITY){
                    selectedCity=cityList.get(position);
                    Log.d("data","queryCounty**cityid="+selectedCity.getId());
                    queryCounties();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel==LEVEL_COUNTY){
                    queryCities();
                }else if(currentLevel==LEVLE_CITY){
                    queryProvices();
                }
            }
        });
        queryProvices();
    }
    public void queryProvices(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        Log.d("data","22222");
        proviceList= DataSupport.findAll(Provice.class);
        Log.d("data","33333");
        if(proviceList.size()>0){
            dataList.clear();
            Log.d("data","44444");
            for(Provice provice:proviceList){
                dataList.add(provice.getProviceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_PROVICE;
        }else{
            String address="http://guolin.tech/api/china";
            Log.d("data","55555");
            queryFromServer(address,"province");
        }
    }
    public void queryCities(){
        titleText.setText(selectedProvice.getProviceName());
        backButton.setVisibility(View.VISIBLE);
        cityList=DataSupport.where("proviceid=?",String.valueOf(selectedProvice.getId())).find(City.class);
        if(cityList.size()>0){
            dataList.clear();
            for(City city:cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVLE_CITY;
        }else{
            int proviceCode=selectedProvice.getProviceCode();
            String address="http://guolin.tech/api/china/"+proviceCode;
            queryFromServer(address,"city");
        }
    }
    public void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList=DataSupport.where("cityid=?",String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size()>0){
            dataList.clear();
            for(County county:countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }else{
            int proviceCode=selectedProvice.getProviceCode();
            int cityCode=selectedCity.getCityCode();
            String address="http://guolin.tech/api/china/"+proviceCode+"/"+cityCode;
            queryFromServer(address,"county");
        }
    }
    public void queryFromServer(String address, final String type){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败..", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    Log.d("data","6666666");
                    String responseText=response.body().string();
                    Log.d("data","responseText="+responseText);
                    boolean result=false;
                    if("province".equals(type)){
                        Log.d("data","777777");
                        result= Utility.handleProviceResponse(responseText);
                        Log.d("data","result="+result);
                    }else if("city".equals(type)){
                        Log.d("data","888888");
                        result=Utility.handleCityResponse(responseText,selectedProvice.getId());
                    }else if("county".equals(type)){
                        Log.d("data","9999999");
                        result=Utility.handleCountyResponse(responseText,selectedCity.getId());
                    }
                    if(result){
                        Log.d("data","!!!!!!!!!!");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("data","++++++++");
                                closeProgressDialog();
                                if("province".equals(type)){
                                    Log.d("data","=========");
                                    queryProvices();
                                    Log.d("data","##########");
                                }else if("city".equals(type)){
                                    Log.d("data","$$$$$$$$$$");
                                    queryCities();
                                }else if("county".equals(type)){
                                    Log.d("data","%%%%%%%%");
                                    queryCounties();
                                }
                            }
                        });
                    }
            }
        });
    }
    public void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
    public void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
}

