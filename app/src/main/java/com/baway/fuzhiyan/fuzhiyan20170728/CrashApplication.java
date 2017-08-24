package com.baway.fuzhiyan.fuzhiyan20170728;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2017/7/28.
 * time: 2017-7-28 14:44:39
 * author:付智焱
 * 类用途：自定义全局异常的application类。用来初始化
 */

public class CrashApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
    }
}
