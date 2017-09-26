package com.season.emoji.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.season.emoji.util.CameraUtil;



/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-09-26 22:49
 */
public class PushMediaCardView extends RelativeLayout implements SurfaceTextureListener{

  TextureView textureView;
  int showWidth = 640;
  int showHeight = 480;
  private Bitmap cacheBitmap;
  public boolean isStartVideo = true, isStartAudio = true, isAudioEnable, isVideoEnable;
  public int currentCameraID;
  boolean isRelease;
  private TextureView decodeTextureView;


  public PushMediaCardView(Context context) {
    super(context);
    initView();
  }

  public PushMediaCardView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }

  private void initView() {
    textureView = new TextureView(getContext());
    LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
    params.addRule(CENTER_IN_PARENT);
    addView(textureView, params);
    setBackgroundColor(Color.BLACK);
    //test decodeView
    init();
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    if (changed) {
      resize(r - l, b - t);
    }
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

//    if (avcEncoder == null) {
//      cacheBitmap = Bitmap.createBitmap(showWidth, showHeight, Config.RGB_565);
//      avcEncoder = new SurfaceAVCEncoder(showWidth, showHeight);
//      avcEncoder.setCallback(this);
//    }

  }

  private void resize(int w, int h) {
    int finalWidth = showWidth; int finalHeight = showWidth;
    if (showWidth < w){
      finalWidth = w;
      finalHeight = h * w/showWidth;
    }
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

  @Override
  public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//    if (avcEncoder != null) {
//      textureView.getBitmap(cacheBitmap);
//      avcEncoder.drawBitmap(cacheBitmap);
//    }
  }


  public void release() {
    isRelease = true;
  }

  public void startSendVideo() {
    isStartVideo = true;
  }

  public void stopSendVideo() {
    isStartVideo = false;
  }

  public void startSendAudio() {
    isStartAudio = true;

  }

    public void enableAudio() {
        isAudioEnable = true;
    }
    public void enableVideo() {
        isVideoEnable = true;
    }

  public void stopSendAudio() {
    isStartAudio = false;
  }

  public void switchCamera() {
    if (currentCameraID < 0) {
      return;
    }
    CameraUtil.getInstance().doStopCamera();
    CameraUtil.getInstance().doOpenCamera((currentCameraID = CameraInfo
            .CAMERA_FACING_BACK == currentCameraID ? CameraInfo.CAMERA_FACING_FRONT
            : CameraInfo.CAMERA_FACING_BACK));
    CameraUtil.getInstance().doStartPreview(textureView.getSurfaceTexture(), 30, showWidth,
            showHeight);
  }

}
