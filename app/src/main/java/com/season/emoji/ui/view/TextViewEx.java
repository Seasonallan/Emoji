package com.season.emoji.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.season.emoji.ui.view.scale.IScaleView;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-09-28 15:50
 */
public class TextViewEx extends TextView implements IScaleView{
    public TextViewEx(Context context) {
        super(context);
    }

    public TextViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getShowWidth(int width) {
        try {
            return (int) getPaint().measureText(getText().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return 124;
    }

    @Override
    public int getShowHeight(int height) {
        return 48;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public void startRecord() {
    }
}
