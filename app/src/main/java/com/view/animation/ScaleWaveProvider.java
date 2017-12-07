package com.view.animation;

import android.graphics.Canvas;

/**
 * Created by Administrator on 2017/11/14.
 */

public class ScaleWaveProvider extends AnimationProvider {
    /**
     * 每个字有不同的动画
     * @return
     */
    @Override
    public boolean isWordSplited(){
        return true;
    }

    int wordDelay = 50;
    int count = 4;
    final float scaleMax = 0.4f;
    float scale = 1;
    @Override
    public int getDuration() {
        return getPerTime() * totalSize + stayTime;
    }

    private int getPerTime(){
        return getDelay() * count + wordDelay;
    }


    @Override
    public void init() {
        wordDelay = (totalTime - stayTime)/totalSize - getDelay() * count;
    }

    @Override
    public void preCanvas(Canvas canvas, int centerX, int centerY) {
        canvas.save();
        canvas.scale(scale, scale, centerX, centerY);
    }

    @Override
    public void proCanvas(Canvas canvas) {
        canvas.restore();
    }

    @Override
    public int setTime(int time, boolean record) {
        int perTime = getPerTime();
        int display = time/perTime;
        if (display == position){
            time = time % perTime;
            if (time > getDelay() * count){
                scale = 1;
            }else{
                if (time < (perTime - wordDelay)/2){//缩小
                    float percent = time * 1.0f/ ((perTime - wordDelay)/2);
                    scale = 1 + percent * scaleMax;
                }else{
                    float percent = (time - (perTime - wordDelay)/2) * 1.0f/ ((perTime - wordDelay)/2);
                    scale = 1 + scaleMax - percent * scaleMax;
                }
            }
        }else{
            scale = 1;
        }
        return super.setTime(time, record);
    }

}
