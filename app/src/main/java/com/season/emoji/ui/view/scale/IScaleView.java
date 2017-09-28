package com.season.emoji.ui.view.scale;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-09-27 19:32
 */
public interface IScaleView {
    int getShowWidth(int width);
    int getShowHeight(int height);
    int getDuration();
    void startRecord();

    boolean recordOrNot();
}
