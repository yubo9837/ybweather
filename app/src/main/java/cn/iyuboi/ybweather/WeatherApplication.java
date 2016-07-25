package cn.iyuboi.ybweather;

/**
 * Created by yubo on 2016/7/24.
 */
import com.thinkland.juheapi.common.CommonFun;

import android.app.Application;

public class WeatherApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        CommonFun.initialize(getApplicationContext());
    }
}