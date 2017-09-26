package com.season.emoji.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraUtil {

  private Camera mCamera;
  private Camera.Parameters mParams;
  private boolean isPreviewing = false;
  private float mPreviwRate = -1f;
  int preViewWidth;
  int preViewHeight;
  private static CameraUtil mCameraInterface;

  private CameraUtil() {
  }

  public static synchronized CameraUtil getInstance() {
    if (mCameraInterface == null) {
      mCameraInterface = new CameraUtil();
    }
    return mCameraInterface;
  }

  public int getPreViewWidth() {
    return preViewWidth;
  }

  public int getPreViewHeight() {
    return preViewHeight;
  }

  /**
   * 打开Camera
   */
  public void doOpenCamera(int id) {
    mCamera = Camera.open(id);
  }

  /**
   */
  public void doStartPreview(SurfaceHolder holder, float previewRate,int width,int height) {
    if (isPreviewing) {
      mCamera.stopPreview();
      return;
    }
    if (mCamera != null) {
      try {
        mCamera.setPreviewDisplay(holder);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      initCamera(previewRate,width,height);
    }


  }

  /**
   */
  public void doStartPreview(SurfaceTexture surface, float previewRate, int width, int height) {
    if (isPreviewing) {
      mCamera.stopPreview();
      return;
    }
    if (mCamera != null) {
      try {
        mCamera.setPreviewTexture(surface);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      initCamera(previewRate, width, height);
    }
  }

  /**
   */
  public void doStopCamera() {
    if (null != mCamera) {
      mCamera.setPreviewCallback(null);
      mCamera.stopPreview();
      isPreviewing = false;
      mPreviwRate = -1f;
      mCamera.release();
      mCamera = null;
    }
  }

  /**
   */
  public void doTakePicture() {
    if (isPreviewing && (mCamera != null)) {
      mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
    }
  }


  private void initCamera(float previewRate, int width, int height) {
    if (mCamera != null) {

      mParams = mCamera.getParameters();
      mParams.setPictureFormat(PixelFormat.JPEG);

      Size finalSize=null;
      List<Size> supportedPreviewSizes = mParams.getSupportedPreviewSizes();
      Collections.sort(supportedPreviewSizes, new Comparator<Size>() {
        @Override
        public int compare(Size o1, Size o2) {
          return o1.width - o2.width;
        }
      });
      for (Size size : supportedPreviewSizes) {
        if (size.width >= width && size.height >= height) {
          finalSize = size;
          break;
        }
      }
      if (finalSize==null) {
        finalSize = supportedPreviewSizes.get(supportedPreviewSizes.size()-1);
      }
      this.preViewWidth = finalSize.width;
      this.preViewHeight= finalSize.height;
      mParams.setPreviewSize(preViewWidth, preViewHeight);
//			mParams.setPreviewSize(VideoCallActivity.width, VideoCallActivity.height);
      mCamera.setDisplayOrientation(90);

//			CamParaUtil.getInstance().printSupportFocusMode(mParams);
      List<String> focusModes = mParams.getSupportedFocusModes();
      if (focusModes.contains("continuous-video")) {
        mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
      }
//      mParams.setRotation(180);
      mCamera.setParameters(mParams);
      mCamera.startPreview();

      isPreviewing = true;
      mPreviwRate = previewRate;

      mParams = mCamera.getParameters();
    }
  }


  public float getPreviwRate() {
    return mPreviwRate;
  }

  ShutterCallback mShutterCallback = new ShutterCallback()
  {
    public void onShutter() {
      // TODO Auto-generated method stub
    }
  };
  PictureCallback mRawCallback = new PictureCallback()
      // 拍摄的未压缩原数据的回调,可以为null
  {

    public void onPictureTaken(byte[] data, Camera camera) {
      // TODO Auto-generated method stub

    }
  };
  PictureCallback mJpegPictureCallback = new PictureCallback()
  {
    public void onPictureTaken(byte[] data, Camera camera) {
      // TODO Auto-generated method stub
      Bitmap b = null;
      if (null != data) {
        b = BitmapFactory.decodeByteArray(data, 0, data.length);
        mCamera.stopPreview();
        isPreviewing = false;
      }
      if (null != b) {
//				Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
//				FileUtil.saveBitmap(rotaBitmap);
      }
      mCamera.startPreview();
      isPreviewing = true;
    }
  };


}
