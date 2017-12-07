package com.view.util;

import android.content.Context;
import android.content.res.Configuration;

import com.season.emoji.ui.BaseApplication;


/**
 * Created by Administrator on 2017/11/22.
 */

public class ToolPaint {

    private static ToolPaint defaultInstance;
    public static ToolPaint getDefault() {
        if (defaultInstance == null) {
            synchronized (ToolPaint.class) {
                if (defaultInstance == null) {
                    defaultInstance = new ToolPaint();
                }
            }
        }
        return defaultInstance;
    }

    ToolPaint(){

    }

    int paintSize = -1;
    public int getPaintSize(){
        if (paintSize <= 0){
            //参照line，这个数值一行最大显示13个字
            paintSize = ScreenUtils.getScreenWidth()*225/1000;
            if (isPad(BaseApplication.getInstance())){
                //PAD字体太大会出现描边错位的问题
                paintSize = paintSize/2;
            }
        }
        return paintSize;
    }

    public int paintWidth = -1;
    public int getPaintWidth(){
        if (paintWidth <= 0){
            paintWidth = ScreenUtils.getPercentWidthSize(32);
            if (isPad(BaseApplication.getInstance())){
                paintWidth = paintWidth/2;
            }
        }
        return paintWidth;
    }

    public int strokeWidth = -1;
    public int getStrokeWidth(){
        if (strokeWidth <= 0){
            strokeWidth = ScreenUtils.getPercentWidthSize(18);
        }
        return strokeWidth;
    }


    private float maxTextLength = -1;
    public float getMaxTextLength(){
        if (maxTextLength <= 0){
            maxTextLength = ScreenUtils.getScreenWidth() * 2.75f;
        }
        return maxTextLength;
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public float getScale(int count){
        int showWidth = getPaintSize() * count;
        int screenWidth = ScreenUtils.getScreenWidth();
        if (showWidth < screenWidth){
            return 0.6f;
        }else{
            return screenWidth * 1.0f/showWidth;
        }

    }

}
