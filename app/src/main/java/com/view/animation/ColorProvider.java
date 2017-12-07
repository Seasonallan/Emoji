package com.view.animation;

/**
 * Created by Administrator on 2017/11/11.
 */

public class ColorProvider extends AnimationProvider {

    @Override
    public boolean isRepeat() {
        return true;
    }

    @Override
    public int getDelay() {
        return 100;
    }

    @Override
    public int getDuration() {
        return getDelay() * 2;
    }

    int position = 0;
    int color1 = 0xFFff8f34;
    int color2 = 0xffffb80f;
    @Override
    public int getColor() {
        if (isRecord){
            if (position == 0){
                position = 1;
                return color1;
            }else{
                position = 0;
                return color2;
            }
        }
        if (time < getDuration()/2){
            return color1;
        }
        return color2;
    }

    int time = 0;
    boolean isRecord = false;
    @Override
    public int setTime(int timeIn, boolean record) {
        isRecord = record;
        time = timeIn % getDuration();
        return super.setTime(timeIn, record);
    }


}
