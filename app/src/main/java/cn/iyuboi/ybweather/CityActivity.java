package cn.iyuboi.ybweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.thinkland.juheapi.common.JsonCallBack;
import com.thinkland.juheapi.data.weather.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by yubo on 2016/7/26.
 */
public class CityActivity extends Activity {

    private ListView lv_city;
    private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actcvity_city);
        initViews();
        getCities();
    }

    private void initViews(){
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lv_city=(ListView)findViewById(R.id.lv_city);
    }

    private void getCities(){
        WeatherData data=WeatherData.getInstance();
        data.getCities(new JsonCallBack() {
            @Override
            public void jsonLoaded(JSONObject json) {
                Log.w("yubo",json+"");

                try{
                    int code= json.getInt("resultcode");
                    if(code==200){
                        list=new ArrayList<String>();
                        Set<String> citySet=new HashSet<String>();
                        JSONArray resultArray=json.getJSONArray("result");
                        for(int i=0;i<resultArray.length();i++){
                            String city=resultArray.getJSONObject(i).getString("city");
                            citySet.add(city);
                        }
                        list.addAll(citySet);
                        CityListAdapter adapter=new CityListAdapter(CityActivity.this,list);
                        lv_city.setAdapter(adapter);
                        lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent();
                                intent.putExtra("city", list.get(i));
                                setResult(1, intent);
                                finish();
                            }
                        });

                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
