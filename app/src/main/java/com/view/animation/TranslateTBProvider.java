package com.view.animation;

import android.graphics.Canvas;

/**
 *
 * 总时长为 50 * 5 * 4 = 1000毫秒
 * Created by Administrator on 2017/11/11.
 */

public class TranslateTBProvider extends AnimationProvider {

    @Override
    public boolean isRepeat() {
        return true;
    }

    float dy = 0;
    float count = 5f;
    @Override
    public int getDuration() {
        return (int) (getDelay() * count * 2);
    }

    @Override
    public void init(){
        count = 5f;
        int perSize = totalTime/getDuration();
        if (totalTime % getDuration() != 0)
            perSize ++;
        float realDuration = totalTime * 1.0f/perSize;
        count = realDuration/(getDelay() * 2);
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
        int duration = getDuration();
        if (record){
            time = time % duration;
        }
        if (time < duration/2){
            float percent = time * 1.0f/ (duration/2);
            dy = transMax - percent * transMax * 2;
        }else{
            float percent = (time - duration/2) * 1.0f/ (duration/2);
            dy = - transMax + percent * transMax * 2;
        }
        return super.setTime(time, record);
    }

}
