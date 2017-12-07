package com.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/10/27.
 */

public class DiyCoverView extends FrameLayout {
    public DiyCoverView(@NonNull Context context) {
        super(context);
    }

    public DiyCoverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DiyCoverView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private OnDownListener listener;
    public void setOnActionDownListener(OnDownListener listener){
        this.listener = listener;
    }

    public interface OnDownListener{
        void onActionDown(float x, float y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            float x = event.getX();
            float y = event.getY();
            if (listener != null){
                try {
                    listener.onActionDown(x, y);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
