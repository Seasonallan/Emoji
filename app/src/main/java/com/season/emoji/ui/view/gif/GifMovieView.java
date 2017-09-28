package com.season.emoji.ui.view.gif;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.view.View;

import com.season.emoji.ui.view.scale.IScaleView;
import com.season.emoji.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-09-26 18:37
 */
public class GifMovieView extends View implements IScaleView {
    /** gift动态效果总时长，在未设置时长时默认为1秒 */
    private static final int DEFAULT_MOVIE_DURATION = 1000;
    /** Movie实例，用来显示gift图片 */
    private Movie mMovie;
    /** 显示gift图片的动态效果的开始时间 */
    private long mMovieStart;
    /** 动态图当前显示第几帧 */
    private int mCurrentAnimationTime = 0;
    /** 图片离屏幕左边的距离 */
    private float mLeft;
    /** 图片离屏幕上边的距离 */
    private float mTop;
    /** 图片的缩放比例 */
    private float mScale = 1;
    /** 图片在屏幕上显示的宽度 */
    private int mMeasuredMovieWidth;
    /** 图片在屏幕上显示的高度 */
    private int mMeasuredMovieHeight;
    /** 是否显示动画,为true表示显示，false表示不显示 */
    private boolean mVisible = true;
    /** 动画效果是否被暂停 */
    private volatile boolean mPaused = false;

    public GifMovieView(Context context) {
        super(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        }
    }

    private boolean isFullScreen = false;
    public GifMovieView(Context context, boolean full) {
        super(context);
        isFullScreen = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        }
    }


    /**
     * 设置gif图资源
     * @param giftResId
     */
    public void setMovieResource(int giftResId) {
        byte[] bytes = getGiftBytes(getResources().openRawResource(giftResId));
        mMovie = Movie.decodeByteArray(bytes, 0, bytes.length);
        LogUtil.log("duration= "+ mMovie.duration());
        requestLayout();
    }

    /**
     * 设置gif图资源
     * @param path
     */
    public void setMovieResource(String path) {
        try {
            byte[] bytes = getGiftBytes(new FileInputStream(path));
            mMovie = Movie.decodeByteArray(bytes, 0, bytes.length);
            LogUtil.log("duration= "+ mMovie.duration());
            requestLayout();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 手动设置 Movie对象
     * @param movie Movie
     */
    public void setMovie(Movie movie) {
        this.mMovie = movie;
        requestLayout();
    }

    /**
     * 得到Movie对象
     * @return Movie
     */
    public Movie getMovie() {
        return mMovie;
    }

    /**
     * 设置要显示第几帧动画
     * @param time
     */
    public void setMovieTime(int time) {
        mCurrentAnimationTime = time;
        invalidate();
    }

    /**
     * 设置暂停
     *
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
     * 判断gif图是否停止了
     *
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mMovie != null) {
            if (!mPaused) {
                updateAnimationTime();
                drawMovieFrame(canvas);
                invalidateView();
            } else {
                drawMovieFrame(canvas);
            }
        }
    }

    /**
     * 重绘
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

    /**
     * 更新当前显示进度
     */
    private void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();
        // 如果第一帧，记录起始时间
        if (mMovieStart == 0) {
            mMovieStart = now;
        }
        // 取出动画的时长
        int dur = mMovie.duration();
        if (dur == 0) {
            dur = DEFAULT_MOVIE_DURATION;
        }
        // 算出需要显示第几帧
        mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
    }

    /**
     * 绘制图片
     * @param canvas 画布
     */
    private void drawMovieFrame(Canvas canvas) {
      //  LogUtil.d(mCurrentAnimationTime);
        // 设置要显示的帧，绘制即可
        mMovie.setTime(mCurrentAnimationTime);
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(mScale, mScale);
        mMovie.draw(canvas, mLeft / mScale, mTop / mScale);
        canvas.restore();

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
     * 将gif图片转换成byte[]
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
    public int getShowWidth(int width) {
        if (isFullScreen){
            return width;
        }
        return mMovie.width();
    }

    @Override
    public int getShowHeight(int height) {
        if (isFullScreen){
            return height;
        }
        return mMovie.height();
    }
}