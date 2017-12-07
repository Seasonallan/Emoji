package com.view.animation;

import android.graphics.Canvas;

/**
 *
 * 总时长为 50 * 5 * 4 = 1000毫秒
 * Created by Administrator on 2017/11/11.
 */

public class HorseLampProvider extends AnimationProvider {

    float dx = 0;
    @Override
    public int getDuration() {
        return totalTime;
    }

    @Override
    public void preCanvas(Canvas canvas, int centerX, int centerY) {
        canvas.save();
        canvas.translate(dx, 0);
    }

    @Override
    public void proCanvas(Canvas canvas) {
        canvas.restore();
    }


    @Override
    public int setTime(int time, boolean record) {
        if (record){
            time = time % getDuration();
        }
        float perX = (textWidth * 1.2f) * 2.0f/getDuration();
        dx = (textWidth * 1.2f) - perX * time;
        return super.setTime(time, record);
    }

    @Override
    public int getDelay() {
        return delayDefault;
    }

}
