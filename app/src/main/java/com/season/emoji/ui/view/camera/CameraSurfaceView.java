package com.season.emoji.ui.view.camera;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.season.emoji.R;
import com.season.emoji.ui.view.TimeCount;
import com.season.emoji.util.LogUtil;
import com.view.gif.frame.GifFrame;

import java.util.ArrayList;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-09-27 20:53
 */
public class CameraSurfaceView extends RelativeLayout implements TextureView.SurfaceTextureListener  {


    TextureView textureView;
    int showWidth = 240;
    int showHeight = 240;
    public boolean isStart = false;
    public int currentCameraID;
    TimeCount timeCount;

    public CameraSurfaceView(Context context) {
        super(context);
        initView();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        timeCount = new TimeCount();

        textureView = new TextureView(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(CENTER_IN_PARENT);
        addView(textureView, params);
        setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        init();
    }

    public void init() {
        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
        textureView.setSurfaceTextureListener(this);
        if (surfaceTexture != null) {
            onSurfaceTextureAvailable(surfaceTexture, getWidth(), getHeight());
        }
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        currentCameraID = Camera.getNumberOfCameras() - 1;
        if (currentCameraID < 0) {
            return;
        }
        CameraUtil.getInstance().doOpenCamera(currentCameraID);
        CameraUtil.getInstance().doStartPreview(surface, 30, showWidth, showHeight);
        showWidth = CameraUtil.getInstance().getPreViewWidth();
        showHeight = CameraUtil.getInstance().getPreViewHeight();
        this.post(new Runnable() {
            @Override
            public void run() {
                resize(getWidth(), getHeight());
            }
        });
    }


    private void resize(int w, int h) {
        int finalWidth = 0; int finalHeight = 0;
        if (showWidth < w){
            finalHeight = w;//w * showHeight/showWidth;
            finalWidth = w;
        }
        LogUtil.log(finalWidth +"    ,     "+ finalHeight);
        LogUtil.log(showWidth +"    ,     "+ showHeight);
        LogUtil.log(w +"    ,     "+ h);

        Rect rect = new Rect(0,0, finalWidth, finalHeight);
        ViewGroup.LayoutParams layoutParams = textureView.getLayoutParams();
        layoutParams.width = rect.right - rect.left;
        layoutParams.height = rect.bottom - rect.top;
        textureView.setLayoutParams(layoutParams);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        CameraUtil.getInstance().doStopCamera();
        return false;
    }

    ArrayList<GifFrame> mFrameList;
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        if (isStart){
            if (timeCount.getTimeCost() >= 120){
                timeCount.reset();
                mFrameList.add(new GifFrame(textureView.getBitmap(270, 270), 120));
                if (mFrameList.size() >= 3000/120){
                    mOnGifMakerListener.onMakeGifSucceed(mFrameList);
                }
            }
        }
    }

    public void switchCamera() {
        if (currentCameraID < 0) {
            return;
        }
        CameraUtil.getInstance().doStopCamera();
        CameraUtil.getInstance().doOpenCamera((currentCameraID = Camera.CameraInfo
                .CAMERA_FACING_BACK == currentCameraID ? Camera.CameraInfo.CAMERA_FACING_FRONT
                : Camera.CameraInfo.CAMERA_FACING_BACK));
        CameraUtil.getInstance().doStartPreview(textureView.getSurfaceTexture(), 30, showWidth,
                showHeight);
    }

    private OnGifMakerListener mOnGifMakerListener;

    public interface OnGifMakerListener {
        void onMakeGifSucceed(ArrayList<GifFrame> frameList);
    }

    public void start(OnGifMakerListener listener) {
        mOnGifMakerListener = listener;
        mFrameList = new ArrayList<>();
        isStart = true;
    }

    public void stop(boolean force) {
        isStart = false;
        if (force){
            mOnGifMakerListener.onMakeGifSucceed(mFrameList);
        }
    }
}
