package com.season.emoji.ui.view;

import com.season.emoji.util.LogUtil;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-09-28 17:45
 */
public class TimeCount {

    long firstTime;

    public void reset(){
        firstTime = System.currentTimeMillis();
    }

    public void reset(boolean log){
        if (log){
            LogUtil.log("costTime = "+ ( System.currentTimeMillis() - firstTime) );
        }
        firstTime = System.currentTimeMillis();
    }

    public long getTimeCost(){
        return System.currentTimeMillis() - firstTime;
    }

    public void addCost(int cost){
        firstTime -= cost;
    }

}
