package cn.iyuboi.ybweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.thinkland.juheapi.common.JsonCallBack;
import com.thinkland.juheapi.data.weather.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yubo on 2016/7/26.
 */
public class CityActivity extends Activity implements SearchView.OnQueryTextListener{


    private SearchView searchView;
    private ListView lv_city;
    private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actcvity_city);

        initViews();
        getCities();
        lv_city.setTextFilterEnabled(true);
//        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);

    }


    @Override
    public boolean onQueryTextChange(String newText) {
        //Toast.makeText(CityActivity.this, "textChange--->" + newText,Toast.LENGTH_SHORT).show();
        //if(lv_city!=null){
//            Log.w("yubo11",list+"");
        if (TextUtils.isEmpty(newText)) {
            // 清除ListView的过滤
            lv_city.setFilterText("武汉");
//            lv_city.clearTextFilter();
        } else {
            Log.w("yubo11",newText+"");
            // 使用用户输入的内容对ListView的列表项进行过滤
            lv_city.setFilterText(newText);
        }
    //    }
//
        Log.w("yubo11",newText+"");
//        lv_city.setFilterText(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // 实际应用中应该在该方法内执行实际查询
        // 此处仅使用Toast显示用户输入的查询内容
        //Toast.makeText(this, "您的选择是:" + query, Toast.LENGTH_SHORT).show();
        return false;
    }

    private void initViews(){
        searchView=(SearchView)findViewById(R.id.searchView);
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
//                Log.w("yubo",json+"");

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
