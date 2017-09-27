package com.season.emoji.ui.view;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.season.emoji.util.LogUtil;

import java.io.LineNumberInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-09-27 14:44
 */
public class ContainerView extends RelativeLayout {

    interface  IType{
        int ADD = 1;
        int REMOVE = 2;
        int OP = 3;
    }
    class Operate{
        public int type;
        public ScaleView scaleView;
        public float[] matrix;
        public Operate(int op, ScaleView scaleView, Matrix ma){
            matrix = new float[9];
            ma.getValues(matrix);
            type = op;
            this.scaleView = scaleView;
        }
    }

    public ContainerView(Context context) {
        super(context);
    }

    public ContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (child instanceof ScaleView){
            if (getWidth() <= 0){
                ((ScaleView)child).randomPosition(0, 1);
            }else{
                ((ScaleView)child).randomPosition(new Random().nextInt(getWidth()), new Random().nextInt(getHeight()));
            }
        }
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        if (view instanceof ScaleView){
           // addEvent(IType.REMOVE, (ScaleView) view, ((ScaleView) view).mCurrentMatrix);
        }
    }

    int position = -1;
    List<Operate> list = new ArrayList<>();

    public void addEvent(int type, ScaleView scaleView, Matrix matrix){
        if (position < list.size() - 1){
            for (int i = list.size() - 1 ; i>position ; i--){
                list.remove(i);
            }
        }
        list.add(new Operate(type, scaleView, matrix));
        position = list.size() - 1;
    }

    public boolean canPre(){
        return position >= 0;
    }
    public boolean canPro(){
        return position < list.size()-1;
    }

    public void pre(){
        switch (list.get(position).type){
            case IType.OP:
                position --;
                ScaleView scaleView = list.get(position).scaleView;
                float[] data  = list.get(position).matrix;
                scaleView.resetMatrix(data);
                break;
            case IType.ADD:
                removeView(list.get(position).scaleView);
                position --;
                break;
            case IType.REMOVE:
                addView(list.get(position).scaleView);
                position --;
                break;
        }
    }

    public void pro(){
        position ++;
        switch (list.get(position).type){
            case IType.OP:
                ScaleView scaleView = list.get(position).scaleView;
                float[] data  = list.get(position).matrix;
                scaleView.resetMatrix(data);
                break;
            case IType.ADD:
                addView(list.get(position).scaleView);
                break;
            case IType.REMOVE:
                removeView(list.get(position).scaleView);
                break;
        }
    }

}
