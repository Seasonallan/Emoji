package com.view.animation;

import android.graphics.Canvas;

/**
 * Created by Administrator on 2017/11/11.
 */

public class TextCutProvider extends AnimationProvider {
    /**
     * 每个字有不同的动画
     * @return
     */
    @Override
    public boolean isWordSplited(){
        return true;
    }

    @Override
    public int getAlpha() {
        return alpha;
    }

    int wordDelay = 50;
    int count = 4;
    int alpha = 0;
    float scale = 1;
    @Override
    public int getDuration() {
        return getPerTime() * (totalSize + 1)+ stayTime;
    }

    private int getPerTime(){
        return getDelay() * count + wordDelay;
    }

    @Override
    public void init() {
        wordDelay = (totalTime - stayTime)/ (totalSize + 1) - getDelay() * count;
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
        if (display == 0){
            scale = 1;
            alpha = 255;
            return super.setTime(time, record);
        }
        if (display == 1){
            return 0;
        }
        display -= 2;
        if (display == position){
            scale = 1;
            time = time % perTime;
            if (time > getDelay() * count){
                alpha = 255;
            }else{
                if (time < (perTime - wordDelay)/2){//缩小
                    float percent = time * 1.0f/ ((perTime - wordDelay)/2);
                    alpha = (int) (percent * 130);
                }else{
                    float percent = (time - (perTime - wordDelay)/2) * 1.0f/ ((perTime - wordDelay)/2);
                    alpha = 130 + (int) (percent * 125);
                }
            }
        }else{
            if (display > position){
                scale = 1;
                alpha = 255;
            }else{
                scale = 0;
                alpha = 0;
            }
        }
        return super.setTime(time, record);
    }

}
