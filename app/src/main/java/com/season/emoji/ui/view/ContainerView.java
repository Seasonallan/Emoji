package com.season.emoji.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.march.gifmaker.GifMaker;
import com.season.emoji.ui.view.gif.frame.GifFrameView;
import com.season.emoji.ui.view.scale.IScaleView;
import com.season.emoji.ui.view.scale.ScaleView;
import com.season.emoji.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-09-27 14:44
 */
public class ContainerView extends RelativeLayout {

    private boolean isStart = false;
    public boolean start(GifMaker.OnGifMakerListener listener) {
        relyView = null;
        calMaxDuration(this);
        if (relyView == null){
            return false;
        }
        String absolutePath = new File(Environment.getExternalStorageDirectory() + "/3/"
                , System.currentTimeMillis() + ".gif").getAbsolutePath();
        mGifMaker = new GifMaker(relyView.getDuration(), 120,  Executors.newCachedThreadPool())
                .setOutputPath(absolutePath);
        mGifMaker.setGifMakerListener(listener);
        isStart = true;
        return true;
    }

    private IScaleView relyView;
    private void calMaxDuration(ViewGroup parent){
        for (int i = 0; i< parent.getChildCount(); i++){
            View view = parent.getChildAt(i);
            if (view instanceof ViewGroup){
                calMaxDuration((ViewGroup) view);
            }else{
                if (view instanceof IScaleView){
                    ((IScaleView) view).startRecord();
                    if (relyView == null){
                        relyView = (IScaleView) view;
                    }
                    int duration = ((IScaleView) view).getDuration();
                    if (relyView.getDuration() < duration){
                        relyView = (IScaleView) view;
                    }
                }
            }
        }
    }

    public void stop(){
        isStart = false;
    }

    public interface  IType{
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
        init();
    }

    public ContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    boolean running = true;
    @Override
    public void dispatchWindowVisibilityChanged(int visibility) {
        if (visibility == GONE || visibility == INVISIBLE) {
            running = false;
            LogUtil.log("dispatchWindowVisibilityChanged gone");
        } else if (visibility == VISIBLE) {
            LogUtil.log("dispatchWindowVisibilityChanged VISIBLE");
            running = true;
        }
        super.dispatchWindowVisibilityChanged(visibility);
    }


    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (running){
                refreshView(ContainerView.this);
            }
            if (isStart){
                if (relyView.recordOrNot()){
                    setDrawingCacheEnabled(true);
                    Bitmap tBitmap = getDrawingCache();
                    tBitmap = getBitmap(tBitmap, 270, 270);
                    setDrawingCacheEnabled(false);
                    mGifMaker.addBitmap(tBitmap);
                }
            }else{
            }
           // mHandler.sendEmptyMessageDelayed(0, 120 - mTimeCount.getTimeCost());
            mHandler.sendEmptyMessageDelayed(0, 50);
        }
    };

    private void refreshView(ViewGroup parent){
        for (int i = 0; i< parent.getChildCount(); i++){
            View view = parent.getChildAt(i);
            if (view instanceof ViewGroup){
                refreshView((ViewGroup) view);
            }else{
                view.invalidate();
            }
        }
    }

    GifMaker mGifMaker;
    private void init(){
        mHandler.sendEmptyMessageDelayed(0, 10);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (child instanceof ScaleView){
            if (getWidth() <= 0){
                ((ScaleView)child).randomPosition(0, 1);
            }else{
                ((ScaleView)child).randomPosition(getWidth(), getHeight());
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



    public boolean hasCameraView(ViewGroup parent){
        for (int i = 0; i< parent.getChildCount(); i++){
            View view = parent.getChildAt(i);
            if (view instanceof ViewGroup){
                boolean has = hasCameraView((ViewGroup) view);
                if (has){
                    return true;
                }
            }else{
                if (view instanceof GifFrameView){
                    return true;
                }
            }
        }
        return false;
    }

    public void reset(){
        removeAllViews();
        position = -1;
        list.clear();
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

    private int getPreScaleViewPosition(int position){
        ScaleView scaleView = list.get(position).scaleView;
        for (int i = position - 1; i >= 0; i --){
            if (list.get(i).scaleView == scaleView){
                return i;
            }
        }
        return position;
    }

    public void pre(){
        switch (list.get(position).type){
            case IType.OP:
                int pos = getPreScaleViewPosition(position);
                ScaleView scaleView = list.get(pos).scaleView;
                float[] data  = list.get(pos).matrix;
                scaleView.resetMatrix(data);
                break;
            case IType.ADD:
                removeView(list.get(position).scaleView);
                break;
            case IType.REMOVE:
                int index = list.get(position).scaleView.index;
                if (index == 0){
                    addView(list.get(position).scaleView, 0);
                }else{
                    addView(list.get(position).scaleView);
                }
                break;
        }
        position --;
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
                int index = list.get(position).scaleView.index;
                if (index == 0){
                    addView(list.get(position).scaleView, 0);
                }else{
                    addView(list.get(position).scaleView);
                }
                break;
            case IType.REMOVE:
                removeView(list.get(position).scaleView);
                break;
        }
    }

    /**
     */
    public static Bitmap getBitmap(Bitmap bitmap, int width, int height)
    {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = (float) width / w;
        float scale2 = (float) height / h;
        scale = scale < scale2 ? scale : scale2;
        matrix.postScale(scale, scale);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return bmp;
    }
}
