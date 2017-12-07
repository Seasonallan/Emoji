package com.view.animation;

/**
 * Created by Administrator on 2017/11/11.
 */

public class FlushProvider extends AnimationProvider {

    @Override
    public boolean isRepeat() {
        return true;
    }

    @Override
    public int getDuration() {
        return 500;
    }

    @Override
    public int setTime(int time, boolean record) {
        time = time % getDuration();
        if (time < getDuration()/2){
            return super.setTime(time, record);
        }
        return 0;
    }
}
