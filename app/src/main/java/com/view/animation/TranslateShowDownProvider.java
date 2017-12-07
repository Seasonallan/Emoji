package com.view.animation;

import android.graphics.Canvas;
import android.view.animation.OvershootInterpolator;

/**
 *
 * 总时长为 50 * 5 * 4 = 1000毫秒
 * Created by Administrator on 2017/11/11.
 */

public class TranslateShowDownProvider extends AnimationProvider {

    float dy = 0;
    @Override
    public int getDuration() {
        return totalTime;
    }

    @Override
    public void preCanvas(Canvas canvas, int centerX, int centerY) {
        canvas.save();
        canvas.translate(0, dy);
    }

    @Override
    public void proCanvas(Canvas canvas) {
        canvas.restore();
    }

    @Override
    public int setTime(int time, boolean record) {
        if (time > getDuration()){
            dy = 0;
            return super.setTime(time, record);
        }
        if (time < getDelay()){
            dy = 0;
        }else{
            int showTime = 1000;
            if (time < showTime){
                float percentTime = new OvershootInterpolator(2f).getInterpolation(time * 1.0f/showTime);
                dy = - textHeight * 1.25f * (1 - percentTime);
            }else{
                dy = 0;
            }
        }
        return super.setTime(time, record);
    }

}
