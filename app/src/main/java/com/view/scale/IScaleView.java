package com.view.scale;

import android.graphics.Canvas;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-09-27 19:32
 */
public interface IScaleView {
    int getViewWidth();
    int getViewHeight();
    int getDuration();
    int getDelay();

    void startRecord();
    void recordFrame(int time);
    void stopRecord();

    void onRelease();

    boolean isSeeking();

    void drawCanvas(Canvas canvas);
}
