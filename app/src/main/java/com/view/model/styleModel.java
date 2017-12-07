package com.view.model;

import java.io.Serializable;

/**
 * Created by lizhongxin on 2017/5/9.
 * 参考value的textbook.plist（ios对应的mode）进行mode的设置。
 */

public class styleModel implements Serializable {
    private String strokeColor="";
    private double strokeSize;

    private String textColor="";
    private double textColorSize;

    private String startColor="";
    private String endColor="";

    private double miaobianAlpha;
    private double textAlpha;

    public double getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(double strokeSize) {
        this.strokeSize = strokeSize;
    }

    public double getTextSize() {
        return textColorSize;
    }

    public void setTextColorSize(double textSize) {
        this.textColorSize = textSize;
    }


    public double getMiaobianAlpha()
    {
        return miaobianAlpha;
    }

    public void setMiaobianAlpha(double miaobianAlpha)
    {
        this.miaobianAlpha = miaobianAlpha;
    }

    public double getTextAlpha()
    {
        return textAlpha;
    }

    public void setTextAlpha(double textAlpha)
    {
        this.textAlpha = textAlpha;
    }
    public String getStrokeColor() {

        return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }



    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }



    public String getStartColor() {
        return startColor;
    }

    public void setStartColor(String startColor) {
        this.startColor = startColor;
    }

    public String getEndColor() {
        return endColor;
    }

    public void setEndColor(String endColor) {
        this.endColor = endColor;
    }
}
