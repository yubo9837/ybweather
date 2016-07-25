package cn.iyuboi.ybweather;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.thinkland.juheapi.common.JsonCallBack;
import com.thinkland.juheapi.data.weather.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextView tv_city,// 城市
            tv_release,// 发布时间
            tv_now_weather,// 天气
            tv_today_temp,// 温度
            tv_now_temp,// 当前温度
            tv_aqi,// 空气质量指数
            tv_quality,// 空气质量
            tv_next_three,// 3小时
            tv_next_six,// 6小时
            tv_next_nine,// 9小时
            tv_next_twelve,// 12小时
            tv_next_fifteen,// 15小时
            tv_next_three_temp,// 3小时温度
            tv_next_six_temp,// 6小时温度
            tv_next_nine_temp,// 9小时温度
            tv_next_twelve_temp,// 12小时温度
            tv_next_fifteen_temp,// 15小时温度
            tv_today_temp_a,// 今天温度a
            tv_today_temp_b,// 今天温度b
            tv_tomorrow,// 明天
            tv_tomorrow_temp_a,// 明天温度a
            tv_tomorrow_temp_b,// 明天温度b
            tv_thirdday,// 第三天
            tv_thirdday_temp_a,// 第三天温度a
            tv_thirdday_temp_b,// 第三天温度b
            tv_fourthday,// 第四天
            tv_fourthday_temp_a,// 第四天温度a
            tv_fourthday_temp_b,// 第四天温度b
            tv_humidity,// 湿度
            tv_wind, tv_uv_index,// 紫外线指数
            tv_dressing_index;// 穿衣指数

    private ImageView iv_now_weather,// 现在
            iv_next_three,// 3小时
            iv_next_six,// 6小时
            iv_next_nine,// 9小时
            iv_next_twelve,// 12小时
            iv_next_fifteen,// 15小时
            iv_today_weather,// 今天
            iv_tomorrow_weather,// 明天
            iv_thirdday_weather,// 第三天
            iv_fourthday_weather;// 第四天

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_release = (TextView) findViewById(R.id.tv_release);
        tv_now_weather = (TextView) findViewById(R.id.tv_now_weather);
        tv_today_temp = (TextView) findViewById(R.id.tv_today_temp);
        tv_now_temp = (TextView) findViewById(R.id.tv_now_temp);
        tv_aqi = (TextView) findViewById(R.id.tv_aqi);
        tv_quality = (TextView) findViewById(R.id.tv_quality);
        tv_next_three = (TextView) findViewById(R.id.tv_next_three);
        tv_next_six = (TextView) findViewById(R.id.tv_next_six);
        tv_next_nine = (TextView) findViewById(R.id.tv_next_nine);
        tv_next_twelve = (TextView) findViewById(R.id.tv_next_twelve);
        tv_next_fifteen = (TextView) findViewById(R.id.tv_next_fifteen);
        tv_next_three_temp = (TextView) findViewById(R.id.tv_next_three1);
        tv_next_six_temp = (TextView) findViewById(R.id.tv_next_six1);
        tv_next_nine_temp = (TextView) findViewById(R.id.tv_next_nine1);
        tv_next_twelve_temp = (TextView) findViewById(R.id.tv_next_twelve1);
        tv_next_fifteen_temp = (TextView) findViewById(R.id.tv_next_fifteen1);
        tv_today_temp_a = (TextView) findViewById(R.id.tv_today_temp_a);
        tv_today_temp_b = (TextView) findViewById(R.id.tv_today_temp_b);
        tv_tomorrow = (TextView) findViewById(R.id.tv_tomorrow);
        tv_tomorrow_temp_a = (TextView) findViewById(R.id.tv_tomorrow_temp_a);
        tv_tomorrow_temp_b = (TextView) findViewById(R.id.tv_tomorrow_temp_b);
        tv_thirdday = (TextView) findViewById(R.id.tv_third);
        tv_thirdday_temp_a = (TextView) findViewById(R.id.tv_third_temp_a);
        tv_thirdday_temp_b = (TextView) findViewById(R.id.tv_third_temp_b);
        tv_fourthday = (TextView) findViewById(R.id.tv_fourth);
        tv_fourthday_temp_a = (TextView) findViewById(R.id.tv_fourth_temp_a);
        tv_fourthday_temp_b = (TextView) findViewById(R.id.tv_fourth_temp_b);
        tv_humidity = (TextView) findViewById(R.id.tv_humidity);
        tv_wind = (TextView) findViewById(R.id.tv_wind);
        tv_uv_index = (TextView) findViewById(R.id.tv_uv_index);
        tv_dressing_index = (TextView) findViewById(R.id.tv_dressing_index);

        iv_now_weather = (ImageView) findViewById(R.id.iv_now_weather);
        iv_next_three = (ImageView) findViewById(R.id.iv_next_three);
        iv_next_six = (ImageView) findViewById(R.id.iv_next_six);
        iv_next_nine = (ImageView) findViewById(R.id.iv_next_nine);
        iv_next_twelve = (ImageView) findViewById(R.id.iv_next_twelve);
        iv_next_fifteen = (ImageView) findViewById(R.id.iv_next_fifteen);
        iv_today_weather = (ImageView) findViewById(R.id.iv_today_weather);
        iv_tomorrow_weather = (ImageView) findViewById(R.id.iv_tomorrow_weather);
        iv_thirdday_weather = (ImageView) findViewById(R.id.iv_third_weather);
        iv_fourthday_weather = (ImageView) findViewById(R.id.iv_fourth_weather);




        getCityWeather();



//        final SwipeRefreshLayout swipeView=(SwipeRefreshLayout)findViewById(R.id.swipeLayout);
//        swipeView.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
//        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
//
//            @Override
//            public void onRefresh() {
//                swipeView.setRefreshing(true);
//            }
//        });
    }
    public void getCityWeather(){
        WeatherData data=WeatherData.getInstance();
        data.getByCitys("武汉",2,new JsonCallBack(){
            @Override
            public void jsonLoaded(JSONObject arg0){
                WeatherBeam beam=parseWeather(arg0);

                if(beam!=null){
                    setViews(beam);
                }
            }
        });

        data.getForecast3h("武汉", new JsonCallBack() {
            @Override
            public void jsonLoaded(JSONObject jsonObject) {
                Log.w("yubo",""+jsonObject.toString());
                List<HoursWeatherBeam> list= parseForecast3h(jsonObject);

//                if(list!=null&&list.size()>=5){
                    setHourViews(list);
//                }
            }
        });
    }



    public WeatherBeam parseWeather(JSONObject json){

        WeatherBeam beam=null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");

        try{
            int code =json.getInt("resultcode");
            int error_code=json.getInt("error_code");
            if(error_code==0&&code==200){
                JSONObject resultJson=json.getJSONObject("result");
                beam=new WeatherBeam();
                //today
                JSONObject todayJson=resultJson.getJSONObject("today");
                beam.setCity(todayJson.getString("city"));
                beam.setUv_index(todayJson.getString("uv_index"));
                beam.setTemp(todayJson.getString("temperature"));
                beam.setWeather_str(todayJson.getString("weather"));
                beam.setWeather_id(todayJson.getJSONObject("weather_id").getString("fa"));
                beam.setDressing_index(todayJson.getString("dressing_index"));

                //sk
                JSONObject skJson=resultJson.getJSONObject("sk");
                beam.setWind(skJson.getString("wind_direction")+skJson.getString("wind_strength"));
                beam.setNow_temp(skJson.getString("temp"));
                beam.setRelease(skJson.getString("time"));
                beam.setHumidity(skJson.getString("humidity"));


                Date date=new Date(System.currentTimeMillis());
                //future
                List<FutureWeatherBeam> futureList=new ArrayList<FutureWeatherBeam>();
                JSONArray futureArray=resultJson.getJSONArray("future");
                for(int i=0;i<futureArray.length();i++){
                    JSONObject futureJson=futureArray.getJSONObject(i);
                    FutureWeatherBeam futureBeam=new FutureWeatherBeam();
                    Date datef=sdf.parse(futureJson.getString("date"));
                    if(!datef.after(date)){
                        continue;
                    }

                    futureBeam.setTemp(futureJson.getString("temperature"));
                    futureBeam.setWeek(futureJson.getString("week"));
                    futureBeam.setWeather_id(futureJson.getJSONObject("weather_id").getString("fa"));
                    //futureBeam.setDate();
                    futureList.add(futureBeam);
                    if(futureList.size()==3)
                        break;
                }
                beam.setFutureList(futureList);
            }else{

            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        return beam;
    }

    private List<HoursWeatherBeam> parseForecast3h(JSONObject json){
        List<HoursWeatherBeam> list =null;
        SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddhhmmss");
        Date date=new Date(System.currentTimeMillis());

        try {
            int code = json.getInt("resultcode");
            int error_code = json.getInt("error_code");
            if (error_code == 0 && code == 200) {
                list=new ArrayList<HoursWeatherBeam>();
                JSONArray resultArray=json.getJSONArray("result");
                for(int i=0;i<resultArray.length();i++){
                    JSONObject hourJson=resultArray.getJSONObject(i);
                    Date hDate= sdf.parse(hourJson.getString("sfdate"));
                    if(!hDate.after(date)){
                        continue;
                    }
                    HoursWeatherBeam beam=new HoursWeatherBeam();
                    beam.setWeather_id(hourJson.getString("weatherid"));
                    beam.setTemp(hourJson.getString("temp1"));
                    Calendar c=Calendar.getInstance();
                    c.setTime(hDate);
                    beam.setTime(c.get(Calendar.HOUR_OF_DAY)+"");
                    list.add(beam);
                    if(list.size()==5)
                        break;
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }catch(ParseException e){
            e.printStackTrace();
        }
        return list;
    }

    private void setViews(WeatherBeam beam){
        tv_city.setText(beam.getCity());
        tv_release.setText(beam.getRelease());
        tv_now_weather.setText(beam.getWeather_str());
        String[] tempArr = beam.getTemp().split("~");
        String temp_str_a = tempArr[1].substring(0, tempArr[1].indexOf("℃"));
        String temp_str_b = tempArr[0].substring(0, tempArr[0].indexOf("℃"));
        // 温度 8℃~16℃" ↑ ↓ °
        tv_today_temp.setText("↑ " + temp_str_a + "°   ↓" + temp_str_b + "°");
        tv_now_temp.setText(beam.getNow_temp() + " °");
        iv_today_weather.setImageResource(getResources().getIdentifier("d" + beam.getWeather_id(), "drawable", "cn.iyuboi.ybweather"));

        tv_today_temp_a.setText(temp_str_a + "°");
        tv_today_temp_b.setText(temp_str_b + "°");

        List<FutureWeatherBeam> futureList = beam.getFutureList();
        if (futureList != null && futureList.size() == 3) {
            setFutureData(tv_tomorrow, iv_tomorrow_weather, tv_tomorrow_temp_a, tv_tomorrow_temp_b, futureList.get(0));
            setFutureData(tv_thirdday, iv_thirdday_weather, tv_thirdday_temp_a, tv_thirdday_temp_b, futureList.get(1));
            setFutureData(tv_fourthday, iv_fourthday_weather, tv_fourthday_temp_a, tv_fourthday_temp_b, futureList.get(2));
        }
        tv_humidity.setText(beam.getHumidity());
        tv_dressing_index.setText(beam.getDressing_index());
        tv_uv_index.setText(beam.getUv_index());
        tv_wind.setText(beam.getWind());
//        Calendar c = Calendar.getInstance();
//        int time = c.get(Calendar.HOUR_OF_DAY);
//        String prefixStr = null;
//        if (time >= 6 && time < 18) {
//            prefixStr = "d";
//        } else {
//            prefixStr = "n";
//        }
//        iv_now_weather.setImageResource(getResources().getIdentifier(prefixStr + beam.getWeather_id(), "drawable", "com.juhe.weather"));

        tv_humidity.setText(beam.getHumidity());
        tv_dressing_index.setText(beam.getDressing_index());
        tv_uv_index.setText(beam.getUv_index());
        tv_wind.setText(beam.getWind());

    }

    private void setHourViews(List<HoursWeatherBeam> list){
        setHourData(tv_next_three,iv_next_three,tv_next_three_temp,list.get(0));
        setHourData(tv_next_six,iv_next_six,tv_next_six_temp,list.get(1));
        setHourData(tv_next_nine,iv_next_nine,tv_next_nine_temp,list.get(2));
        setHourData(tv_next_twelve,iv_next_twelve,tv_next_twelve_temp,list.get(3));
        setHourData(tv_next_fifteen,iv_next_fifteen,tv_next_fifteen_temp,list.get(4));
    }

    private void setHourData(TextView tv_hour,ImageView iv_weather,TextView tv_temp,HoursWeatherBeam beam){

        String prefixStr=null;
        int time =Integer.valueOf(beam.getTime());
        if(time>=6&&time<18){
            prefixStr="d";
        }else{
            prefixStr="n";
        }

        tv_hour.setText(beam.getTime() + "时");
        iv_weather.setImageResource(getResources().getIdentifier(prefixStr + beam.getWeather_id(), "drawable", "cn.iyuboi.ybweather"));
        tv_temp.setText(beam.getTemp() + "°");
    }

    private void setFutureData(TextView tv_week,ImageView iv_weather,TextView tv_temp_a,TextView tv_temp_b,FutureWeatherBeam beam){
        tv_week.setText(beam.getWeek());
        iv_weather.setImageResource(getResources().getIdentifier(
                "d"+beam.getWeather_id(),"drawable","cn.iyuboi.ybweather"));
        String[] tempArr = beam.getTemp().split("~");
        String temp_str_a = tempArr[1].substring(0, tempArr[1].indexOf("℃"));
        String temp_str_b = tempArr[0].substring(0, tempArr[0].indexOf("℃"));
        tv_temp_a.setText(temp_str_a + "°");
        tv_temp_b.setText(temp_str_b + "°");//todo
    }

}
