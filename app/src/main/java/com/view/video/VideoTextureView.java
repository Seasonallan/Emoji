package com.view.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import com.view.scale.IScaleView;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Administrator on 2017/10/10.
 */

public class VideoTextureView extends TextureView implements IScaleView {


    public VideoTextureView(Context context) {
        super(context);
        init();
    }

    public VideoTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private Surface mOESSurface;
    private VideoGLRenderer renderer;
    private void init() {

        setSurfaceTextureListener(new SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                if (true){
                    mOESSurface = new Surface(surfaceTexture);
                    playVideo();
                    return;
                }
                try {
                    renderer = new VideoGLRenderer(VideoTextureView.this);
                    mOESSurface = new Surface(renderer.getVideoOrignalShowOESTexture());
                } catch (Exception e) {
                    mOESSurface = new Surface(surfaceTexture);
                }
                playVideo();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                onRelease();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }



    String filePath;
    float duration;
    int delay;
    private MediaPlayer mMediaPlayer;
    private boolean autoDestroyed = false;
    private void playVideo(){
        if (TextUtils.isEmpty(filePath)){
            return;
        }
        try {
            if (mMediaPlayer != null){
                try {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            delay = 120;
            mMediaPlayer= new MediaPlayer();
            mMediaPlayer.setSurface(mOESSurface);
            isReady = false;
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            mMediaPlayer.setDataSource(fis.getFD());
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp){
                    isReady = true;
                    mMediaPlayer.setLooping(true);
                    duration = mMediaPlayer.getDuration();
                    mMediaPlayer.start();
                    setSpeed(speed);
                    if (listener != null){
                        listener.onPrepared(mp);
                    }
                }
            });
            mMediaPlayer.prepare();
        } catch (Exception e) {
            if (!autoDestroyed){
                File file = new File(filePath);
                file.deleteOnExit();
            }
            autoDestroyed = false;
            e.printStackTrace();
        }
    }

    private MediaPlayer.OnPreparedListener listener;
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener){
        this.listener = listener;
    }

    /**
     * 以文件形式设置gif图片
     *
     * @param strFileName
     */
    public void setMovieResource(String strFileName) {
        filePath = strFileName;
        if (mOESSurface != null){
            playVideo();
        }
    }

    boolean isReady = false;
    float speed = 1.0f;
    public void setSpeed(float speed){
        try {
            this.speed = speed;
            if (isReady){
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    //            ToastUtil.show("视频变速的预览效果在Android6.0以下暂不支持");
                } else {
                    duration = duration/speed;
                    delay = (int) (120/speed);
                    mMediaPlayer.setPlaybackParams(new PlaybackParams().setSpeed(speed));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void destroy() {
    }


    public Bitmap getBitmap(){
        return super.getBitmap();
    }

    @Override
    public int getViewWidth() {
        return 0;
    }


    @Override
    public int getDelay() {
        return delay;
    }

    @Override
    public int getViewHeight() {
        return 0;
    }

    @Override
    public int getDuration() {
        return (int) duration;
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    public void startRecord() {
        mMediaPlayer.seekTo(0);
    }

    @Override
    public void recordFrame(int time) {

    }

    @Override
    public boolean isSeeking(){
        return isSeeking;
    }

    @Override
    public void drawCanvas(Canvas canvas) {

    }

    boolean isSeeking = false;
    public void recordFrameCallback(int time) {
        if (mMediaPlayer == null){
            return;
        }
        isSeeking = true;
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                mp.pause();
                isSeeking = false;
            }
        });
        if (time > getDuration() || time > getDuration() - delay){//n - 1 ~ n 时强制绘制最后一帧
            mMediaPlayer.seekTo((int) (getDuration() * speed));
            // mMediaPlayer.pause();
        }else{
            mMediaPlayer.seekTo((int) (time * speed));
            //  mMediaPlayer.pause();
        }
    }

    public void recordFrameCallback(int time, MediaPlayer.OnSeekCompleteListener listener) {
        if (mMediaPlayer == null){
            return;
        }
        mMediaPlayer.setOnSeekCompleteListener(listener);
        if (time > getDuration()){
            mMediaPlayer.seekTo((int) (getDuration() * speed));
           // mMediaPlayer.pause();
        }else{
            mMediaPlayer.seekTo((int) (time * speed));
          //  mMediaPlayer.pause();
        }
    }

    @Override
    public void stopRecord() {
        if (mMediaPlayer == null){
            return;
        }
        mMediaPlayer.setOnSeekCompleteListener(null);
        mMediaPlayer.start();
    }

    @Override
    public void onRelease() {
        try {
            autoDestroyed = true;
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
