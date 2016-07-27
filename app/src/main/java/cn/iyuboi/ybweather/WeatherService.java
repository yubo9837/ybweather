package cn.iyuboi.ybweather;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.thinkland.juheapi.common.JsonCallBack;
import com.thinkland.juheapi.data.air.AirData;
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
import java.util.concurrent.CountDownLatch;

import android.os.Handler;
import android.widget.Toast;


/**
 * Created by yubo on 2016/7/26.
 */
public class WeatherService extends Service{

    private String city;
    private final String tag="WeatherService";
    private  static final int REPEAT_MSG=0x01;
    private final int CALLBACK_OK = 0x02;
    private final int CALLBACK_ERROR = 0x04;
    private boolean isRunning=false;
    private int count=0;
    private List<HoursWeatherBeam> list;
    private WeatherServiceBinder binder=new WeatherServiceBinder();
    private PMBeam pmBeam;
    private WeatherBeam weatherBeam;

    private OnParseCallBack callBack;

    public interface OnParseCallBack{
        public void onParseComplete(List<HoursWeatherBeam> list,PMBeam pmBeam,WeatherBeam weatherBeam);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        city="武汉";
        mHandler.sendEmptyMessage(REPEAT_MSG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(tag, "onDestroy");
    }


    public void setCallBack(OnParseCallBack callBack){
        this.callBack=callBack;
    }

    public void removeCallBack(){
        callBack=null;
    }


    Handler mHandler=new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case REPEAT_MSG:

                    getCityWeather();
                    sendEmptyMessageDelayed(REPEAT_MSG, 30 * 60 * 1000);
                    break;
                case CALLBACK_OK:
                    if (callBack != null) {
                        callBack.onParseComplete(list, pmBeam, weatherBeam);
                    }
                    isRunning = false;
                    break;

                default:
                    break;
            }
        }

    };

    public void getCityWeather(String city){
        this.city=city;
        getCityWeather();
    }

    //weather
    public void getCityWeather() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        final CountDownLatch countDownLatch = new CountDownLatch(3);
        WeatherData data = WeatherData.getInstance();
        data.getByCitys(city, 2, new JsonCallBack() {
            @Override
            public void jsonLoaded(JSONObject jsonObject) {
                weatherBeam = parseWeather(jsonObject);

                countDownLatch.countDown();
//                callBack.onParseComplete(list,pmBeam,weatherBeam);
                    Log.w("yubo", "" + jsonObject.toString());

            }
        });

        data.getForecast3h(city, new JsonCallBack() {
            @Override
            public void jsonLoaded(JSONObject jsonObject) {
                Log.w("yubo", "" + jsonObject.toString());
                list = parseForecast3h(jsonObject);
                countDownLatch.countDown();

//                setHourViews(list);
//                callBack.onParseComplete(list,pmBeam,weatherBeam);

            }
        });

        AirData airData = AirData.getInstance();
        airData.cityAir(city, new JsonCallBack() {
            @Override
            public void jsonLoaded(JSONObject jsonObject) {
                countDownLatch.countDown();
                pmBeam=parsePM(jsonObject);
//                if(pmBeam!=null) {
//                    setPMView(pmBeam);
//                }
//                callBack.onParseComplete(list,pmBeam,weatherBeam);
                Log.w("yubo", "" + jsonObject.toString());
            }
        });

        new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    countDownLatch.await();
                    mHandler.sendEmptyMessage(CALLBACK_OK);
                } catch (InterruptedException ex) {
                    mHandler.sendEmptyMessage(CALLBACK_ERROR);
                    return;
                }
            }

        }.start();

    }


    //解析PM值
    private PMBeam parsePM(JSONObject json){
        PMBeam beam=null;
        try {
            int code = json.getInt("resultcode");
            int error_code = json.getInt("error_code");
            if(error_code==0&&code==200){
                beam =new PMBeam();
                JSONObject pmJson = json.getJSONArray("result").getJSONObject(0).getJSONObject("citynow");
                beam.setAqi(pmJson.getString("AQI"));
                beam.setQuality(pmJson.getString("quality"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return beam;
    }


    //解析未来三天天气
    public WeatherBeam parseWeather(JSONObject json){

        WeatherBeam beam=null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");

        try{
            int code =json.getInt("resultcode");
//            int error_code=json.getInt("error_code");
            if(code==200){
                JSONObject resultJson=json.getJSONObject("result");
                beam=new WeatherBeam();
                //today
                JSONObject todayJson=resultJson.getJSONObject("today");
                beam.setCity(todayJson.getString("city"));
                beam.setUv_index(todayJson.getString("uv_index"));
                beam.setFelt_temp(todayJson.getString("exercise_index"));
                beam.setTemp(todayJson.getString("temperature"));
                beam.setWeather_str(todayJson.getString("weather"));
                beam.setWeather_id(todayJson.getJSONObject("weather_id").getString("fa"));
                beam.setDressing_index(todayJson.getString("dressing_advice"));

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

    //解析未来三小时天气
    private List<HoursWeatherBeam> parseForecast3h(JSONObject json){
        List<HoursWeatherBeam> list =null;
        SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddhhmmss");
        Date date=new Date(System.currentTimeMillis());

        try {
            int code = json.getInt("resultcode");
            // int error_code = json.getInt("error_code");
            if (code == 200) {
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


    public class WeatherServiceBinder extends Binder{
        public WeatherService getService(){
            return WeatherService.this;
        }
    }
}
