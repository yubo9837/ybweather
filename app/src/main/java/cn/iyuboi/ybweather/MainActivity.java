package cn.iyuboi.ybweather;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.iyuboi.ybweather.WeatherService.WeatherServiceBinder;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private WeatherService mService;
    private Context mContext;
    private TextView tv_city,// 城市
            tv_release,// 发布时间
            tv_exercise,
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

    private RelativeLayout rl_city;

    private void initService(){
        Intent intent=new Intent(mContext,WeatherService.class);
        startService(intent);
        bindService(intent,conn,Context.BIND_AUTO_CREATE);
    }

    ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = ((WeatherServiceBinder)iBinder).getService();
            mService.setCallBack(new WeatherService.OnParseCallBack() {
                @Override
                public void onParseComplete(List<HoursWeatherBeam> list, PMBeam pmBeam, WeatherBeam weatherBeam) {
                    if(list!=null){
                        setHourViews(list);
                    }

                    if(pmBeam!=null){
                        setPMView(pmBeam);
                    }

                    if(weatherBeam!=null){
                        setWeatherViews(weatherBeam);
                    }
                }
            });
            mService.getCityWeather();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService.removeCallBack();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;



        init();
        initService();

    }

    //得到数据主函数


    //显示PM值
    private void setPMView(PMBeam beam){
        tv_aqi.setText(beam.getAqi());
        tv_quality.setText(beam.getQuality());
    }

    //显示未来三天天气值
    private void setWeatherViews(WeatherBeam beam){
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



        tv_exercise.setText(beam.getFelt_temp());
        tv_humidity.setText(beam.getHumidity());
        tv_dressing_index.setText(beam.getDressing_index());
        tv_uv_index.setText(beam.getUv_index());
        tv_wind.setText(beam.getWind());

        Calendar c = Calendar.getInstance();
        int time = c.get(Calendar.HOUR_OF_DAY);
        String prefixStr = null;
        if (time >= 6 && time < 18) {
            prefixStr = "d";
        } else {
            prefixStr = "n";
        }
        iv_now_weather.setImageResource(getResources().getIdentifier(prefixStr + beam.getWeather_id(), "drawable", "cn.iyuboi.ybweather"));

        tv_humidity.setText(beam.getHumidity());
        tv_dressing_index.setText(beam.getDressing_index());
        tv_uv_index.setText(beam.getUv_index());
        tv_wind.setText(beam.getWind());

    }

    //显示未来三小时预报值
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

    private void init(){
        final SwipeRefreshLayout swipeView=(SwipeRefreshLayout)findViewById(R.id.swipeLayout);
        swipeView.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mService.getCityWeather();
                        swipeView.setRefreshing(false);
                    }
                },5000);
            }
        });

        rl_city = (RelativeLayout) findViewById(R.id.rl_city);
        rl_city.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivityForResult(new Intent(mContext, CityActivity.class), 1);

            }
        });
//        mService.getCityWeather();

        tv_exercise=(TextView)findViewById(R.id.tv_felt_temp);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==1&&resultCode==1){
            String city=data.getStringExtra("city");
            mService.getCityWeather(city);
        }
    }

    @Override
    protected void onDestroy() {

        unbindService(conn);
        super.onDestroy();
    }
}
