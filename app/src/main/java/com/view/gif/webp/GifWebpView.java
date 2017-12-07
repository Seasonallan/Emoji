package com.view.gif.webp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.view.scale.IScaleView;
import com.view.scale.ScaleView;


public class GifWebpView extends View
        implements IScaleView {
    public String url;
    public boolean isTuya = false;

    public GifWebpView copy(){
        GifWebpView layerImageView = new GifWebpView(getContext());
        layerImageView.url = url;
        layerImageView.isTuya = isTuya;
        layerImageView.setImageFile(filePath);
        return layerImageView;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getParent() instanceof ScaleView){
            int width = right - left;
            int height = bottom - top;
            if (width > 0 && height > 0){
                ((ScaleView) getParent()).rebindOpView();
            }
        }
    }

    public GifWebpView(Context context)
    {
        super(context);
        init();
    }

    public GifWebpView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        init();
    }


    public boolean autoPlay = false;
    private void init()
    {
        bitmapCacheStart = System.currentTimeMillis();
    }

    public Bitmap getBitmap()
    {
        return null;
    }

    private static final int DEFAULT_MOVIE_DURATION = 1000;
    public String filePath;
    private long bitmapCacheStart;
    private int mCurrentAnimationTime = 0;
    private BitmapCache bitmapCache;

    public int getWebpWidth(){
        if(bitmapCache != null){
            return bitmapCache.getWidth();
        }
        return 0;
    }
    public int getWebpHeight(){
        if(bitmapCache != null){
            return bitmapCache.getHeight();
        }
        return 0;
    }

    public void setImageFile(String filePath) {
        this.filePath = filePath;
        try {
            bitmapCache = BitmapCacheUtil.getDefault().decodeFile(filePath);
            duration = bitmapCache.getDuration();
            if (duration <= 0){
                duration = bitmapCache.getFrameCount() * 100;
            }
            delay = duration/bitmapCache.getFrameCount();
            if (duration % bitmapCache.getFrameCount() != 0){
                //delay ++;
            }
            if (bitmapCache.getFrameCount() <= 1){
                duration = 0;
                delay = 0;
            }
            requestLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0, height = 0;
        if (bitmapCache != null){
            width = bitmapCache.getWidth();
            height = bitmapCache.getHeight();
        }
        setMeasuredDimension(width, height);
    }


    /**
     */
    private void drawMovieFrame(Canvas canvas) {


//        if (index == recordIndex){
//            recordCurrentData = false;
//        }else{
//            recordIndex = index;
//            recordCurrentData = true;
//            bitmap = Bitmap.createBitmap(bitmapCache.getWidth(), bitmapCache.getHeight(), Bitmap.Config.ARGB_8888);
//            bitmap.eraseColor(Color.TRANSPARENT);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
//                bitmap.setHasAlpha(true);
//            }
//            decodeAllFrames(bitmapCache);
//            WebPFrame webPFrame = bitmapCache.getFrame(index);
//            webPFrame.renderFrame(getWidth(), getHeight(), bitmap);
//        }
//        paint.setAntiAlias(true);
//        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
//
//        canvas.drawBitmap(bitmap, 0, 0, paint);

    }


    @Override
    public int getViewWidth() {
        if (bitmapCache == null){
            return 0;
        }
        return bitmapCache.getWidth();
    }

    @Override
    public int getViewHeight() {
        if (bitmapCache == null){
            return 0;
        }
        return bitmapCache.getHeight();
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawCanvas(canvas);
    }

    @Override
    public void drawCanvas(Canvas canvas) {
        if (bitmapCache != null) {
            int index = 0;
            if (duration <= 0){
                index = 0;
            }else{
                if (delay != 0){
                    mCurrentAnimationTime = (int) ((System.currentTimeMillis() - bitmapCacheStart) % duration);
                    if (recordTime >= 0){
                        mCurrentAnimationTime = recordTime;
                    }
                    index = mCurrentAnimationTime/delay;
                }
            }

            if (index >= bitmapCache.size()){
                index = bitmapCache.size() - 1;
            }
            Bitmap bitmap = bitmapCache.get(index);
            if (bitmap != null && !bitmap.isRecycled()){
                //Logger.d("onDraw>> "+ index +"   recordTime="+recordTime);
                canvas.drawBitmap(bitmap, 0, 0, null);
            }
            if (autoPlay){
                invalidate();
            }
        }
        isSeeking = false;
    }

    @Override
    public boolean isSeeking(){
        return isSeeking;
    }
    boolean isSeeking = false;

    boolean isRecordRelyView = false;
    @Override
    public void startRecord() {
        isRecordRelyView = true;
    }

    private int recordTime = -1;
    @Override
    public void recordFrame(int time) {
        if (time > getDuration()){
            if (isRecordRelyView){
                recordTime = getDuration();
            }else{
                recordTime = time % getDuration();
            }
        }else{
            recordTime = time;
        }
        isSeeking = true;
    }

    @Override
    public void stopRecord() {
        recordTime = -1;
        isRecordRelyView = false;
    }

    @Override
    public int getDelay() {
        return delay;
    }

    int delay = 120;
    int duration = 1000;
    int recordIndex = -1 ;

    @Override
    public void onRelease() {
        autoPlay = false;
        bitmapCache.release();
        BitmapCacheUtil.getDefault().release(filePath);
    }

}
