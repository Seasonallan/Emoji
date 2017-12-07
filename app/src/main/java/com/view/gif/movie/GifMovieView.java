package com.view.gif.movie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.view.scale.IScaleView;
import com.view.scale.ScaleView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-09-26 18:37
 */
public class GifMovieView extends View implements IScaleView {
    private static final int DEFAULT_MOVIE_DURATION = 1000;
    private Movie mMovie;
    private long mMovieStart;
    private int mCurrentAnimationTime = 0;
    private float mLeft;
    private float mTop;
    private float mScale = 1;
    private int mMeasuredMovieWidth;
    private int mMeasuredMovieHeight;
    private boolean mVisible = true;
    private volatile boolean mPaused = false;


    int resourceId;
    public String file;
    public String url;
    public GifMovieView(Context context) {
        super(context);
        init();
    }

    public GifMovieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        isFullScreen = true;
        init();
    }

    public GifMovieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        isFullScreen = true;
        init();
    }

    private boolean isFullScreen = false;
    public GifMovieView(Context context, boolean full) {
        super(context);
        isFullScreen = full;
        init();
    }

    Paint paint;
    private void init(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        }
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    /**
     * @param giftResId
     */
    public void setMovieResource(int giftResId) {
        resourceId = giftResId;
        byte[] bytes = getGiftBytes(getResources().openRawResource(giftResId));
        mMovie = Movie.decodeByteArray(bytes, 0, bytes.length);
       // LogUtil.log("duration= " + mMovie.duration());
        requestLayout();
    }

    /**
     * @param path
     */
    public boolean setMovieResource(String path) {
        file = path;
        try {
            byte[] bytes = getGiftBytes(new FileInputStream(path));
            mMovie = Movie.decodeByteArray(bytes, 0, bytes.length);
            requestLayout();
            return mMovie.duration() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return Movie
     */
    public Movie getMovie() {
        return mMovie;
    }

    /**
     * @param time
     */
    public void setMovieTime(int time) {
        mCurrentAnimationTime = time;
        invalidate();
    }

    /**
     * @param paused
     */
    public void setPaused(boolean paused) {
        this.mPaused = paused;
        if (!paused) {
            mMovieStart = android.os.SystemClock.uptimeMillis()
                    - mCurrentAnimationTime;
        }
        invalidate();
    }

    /**
     * @return
     */
    public boolean isPaused() {
        return this.mPaused;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMovie != null) {
            int movieWidth = mMovie.width();
            int movieHeight = mMovie.height();

            int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
            if (!isFullScreen){
                maximumWidth = movieWidth;
            }
            float scaleW = (float) movieWidth / (float) maximumWidth;
            mScale = 1f / scaleW;
            mMeasuredMovieWidth = maximumWidth;
            mMeasuredMovieHeight = (int) (movieHeight * mScale);
            setMeasuredDimension(mMeasuredMovieWidth, mMeasuredMovieHeight);
        } else {
            setMeasuredDimension(getSuggestedMinimumWidth(),
                    getSuggestedMinimumHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mLeft = (getWidth() - mMeasuredMovieWidth) / 2f;
        mTop = (getHeight() - mMeasuredMovieHeight) / 2f;
        mVisible = getVisibility() == View.VISIBLE;
        if (getParent() instanceof ScaleView){
            int width = r - l;
            int height = b - t;
            if (width > 0 && height > 0){
                ((ScaleView) getParent()).rebindOpView();
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawCanvas(canvas);
    }

    @Override
    public void drawCanvas(Canvas canvas) {
        if (mMovie != null) {
            if (!mPaused) {
                updateAnimationTime();
                drawMovieFrame(canvas);
                if (autoPlay){
                    invalidate();
                }
            } else {
                drawMovieFrame(canvas);
            }
        }
        isSeeking = false;
    }

    @Override
    public boolean isSeeking(){
        return isSeeking;
    }
    boolean isSeeking = false;

    public boolean autoPlay = false;

    @Override
    public void onRelease(){
        autoPlay = false;
        if (mMovie != null){
            mMovie = null;
        }
    }

    /**
     */
    private void invalidateView() {
        if (mVisible) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postInvalidateOnAnimation();
            } else {
                invalidate();
            }
        }
    }


    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        mVisible = screenState == SCREEN_STATE_ON;
        invalidateView();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        mVisible = visibility == View.VISIBLE;
        invalidateView();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == View.VISIBLE;
        invalidateView();
    }

    /**
     * @return byte[]
     */
    private byte[] getGiftBytes(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len;
        try {
            while ((len = is.read(b, 0, 1024)) != -1) {
                baos.write(b, 0, len);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return baos.toByteArray();
    }


    @Override
    public int getViewWidth() {
        if (isFullScreen || mMovie == null){
            return 0;
        }
        return mMovie.width();
    }

    @Override
    public int getViewHeight() {
        if (isFullScreen  || mMovie == null){
            return 0;
        }
        return mMovie.height();
    }

    private int duration = -1;
    @Override
    public int getDuration() {
        if (duration <= 0){
            if (mMovie == null){
                duration = 0;
            }
            duration = mMovie.duration();
            if(duration <= 0){
                duration = DEFAULT_MOVIE_DURATION;
            }
        }
        return duration;
    }


    /**
     */
    private void updateAnimationTime() {
        long now = System.currentTimeMillis();
        if (mMovieStart == 0) {
            mMovieStart = now;
        }
        mCurrentAnimationTime = (int) ((now - mMovieStart) % getDuration());
    }
    /**
     */
    private void drawMovieFrame(Canvas canvas) {
        canvas.save();
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG| Paint.FILTER_BITMAP_FLAG));
        if (recordTime >= 0){
            mMovie.setTime(recordTime);
            mMovie.draw(canvas, mLeft / mScale, mTop / mScale, paint);
        }else{
            mMovie.setTime(mCurrentAnimationTime);
            //canvas.scale(mScale, mScale);
            mMovie.draw(canvas, mLeft / mScale, mTop / mScale, paint);
        }
        canvas.restore();
    }


    boolean isRecordRelyView = false;
    @Override
    public void startRecord() {
        isRecordRelyView = true;
    }

    private int recordTime = -1;
    @Override
    public void recordFrame(int time) {
        if (getDuration() <= 0){
            recordTime = 0;
            return;
        }
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
        isRecordRelyView = false;
        recordTime = -1;
    }

    @Override
    public int getDelay() {
        if (delay < 0){
            if (!TextUtils.isEmpty(file)){
                try {
                    delay = DelayDecoder.getDelay(new FileInputStream(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else{
                delay = DelayDecoder.getDelay(getResources().openRawResource(resourceId));
            }
        }
        return delay;
    }

    int delay = -1;

    public GifMovieView copy() {
        GifMovieView gifView = new GifMovieView(getContext());
        if (!TextUtils.isEmpty(file)){
            gifView.setMovieResource(file);
        }else{
            gifView.setMovieResource(resourceId);
        }
        gifView.url = url;
        gifView.file = file;
        gifView.isFullScreen = isFullScreen;
        return gifView;
    }
}