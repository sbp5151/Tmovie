package tmovie.jld.com.tmovie;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import tmovie.jld.com.tmovie.util.Constants;

/**
 * 项目名称：Tmovie
 * 晶凌达科技有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
 *
 * @creator boping
 * @create-time 2016/8/31 13:42
 */
public class MyApplication extends Application {

    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static Context getAppContext() {
        return sInstance;
    }



}
