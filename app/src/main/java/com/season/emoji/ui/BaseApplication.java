package com.season.emoji.ui;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/12/7.
 * http://api.biaoqing.com/material/hotword?type=1
 http://api.biaoqing.com/category?pageSize=24&pageNum=1&keyword=HOT&type=1
 http://api.biaoqing.com/material?pageNum=1&categoryId=8391
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    private static Context sContext;

    public static Context getInstance() {
        return sContext;
    }
}
