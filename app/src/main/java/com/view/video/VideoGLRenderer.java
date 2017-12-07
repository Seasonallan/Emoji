package com.view.video;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.TextureView;

import com.view.video.filter.FilterEngine;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/**
 * Created by lb6905 on 2017/6/28.
 */

public class VideoGLRenderer implements SurfaceTexture.OnFrameAvailableListener {
    private Context mContext;
    private TextureView mTextureView;
    private int mOESTextureId = -1;
    private float[] transformMatrix = new float[16];

    private EGL10 mEgl = null;
    private EGLDisplay mEGLDisplay = EGL10.EGL_NO_DISPLAY;
    private EGLContext mEGLContext = EGL10.EGL_NO_CONTEXT;
    private EGLConfig[] mEGLConfig = new EGLConfig[1];
    private EGLSurface mEglSurface;

    private SurfaceTexture mOESSurfaceTexture;
    private Handler mHandler;


    VideoGLRenderer(TextureView textureView) throws Exception {
        mContext = textureView.getContext();
        mTextureView = textureView;
        mOESTextureId = Utils.createOESTextureObject();

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 101){
                    initEGL();
                }else{
                    try {
                        drawFrame();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mHandler.sendEmptyMessage(101);
    }

    /**
     * 获取视频输出源的接收SurfaceTexture（未处理）
     *
     * @return
     */
    public SurfaceTexture getVideoOrignalShowOESTexture() {
        mOESSurfaceTexture = new SurfaceTexture(mOESTextureId);
        mOESSurfaceTexture.setOnFrameAvailableListener(this);
        return mOESSurfaceTexture;
    }

    private void initEGL(){
        mEgl = (EGL10) EGLContext.getEGL();

        //获取显示设备
        mEGLDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (mEGLDisplay == EGL10.EGL_NO_DISPLAY) {
            throw new RuntimeException("eglGetDisplay failed! " + mEgl.eglGetError());
        }

        //version中存放EGL版本号
        int[] version = new int[2];

        //初始化EGL
        if (!mEgl.eglInitialize(mEGLDisplay, version)) {
            throw new RuntimeException("eglInitialize failed! " + mEgl.eglGetError());
        }

        //构造需要的配置列表
        int[] attributes = {
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_ALPHA_SIZE, 8,
                EGL10.EGL_BUFFER_SIZE, 32,
                EGL10.EGL_RENDERABLE_TYPE, 4,
                EGL10.EGL_SURFACE_TYPE, EGL10.EGL_WINDOW_BIT,
                EGL10.EGL_NONE
        };
        int[] configsNum = new int[1];

        //EGL选择配置
        if (!mEgl.eglChooseConfig(mEGLDisplay, attributes, mEGLConfig, 1, configsNum)) {
            throw new RuntimeException("eglChooseConfig failed! " + mEgl.eglGetError());
        }
        SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
        if (surfaceTexture == null)
            throw new RuntimeException("surfaceTexture failed! ");

        //创建EGL显示窗口
        mEglSurface = mEgl.eglCreateWindowSurface(mEGLDisplay, mEGLConfig[0], surfaceTexture, null);
        //创建上下文
        int[] contextAttribs = {
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL10.EGL_NONE
        };
        mEGLContext = mEgl.eglCreateContext(mEGLDisplay, mEGLConfig[0], EGL10.EGL_NO_CONTEXT, contextAttribs);

        if (mEGLDisplay == EGL10.EGL_NO_DISPLAY || mEGLContext == EGL10.EGL_NO_CONTEXT) {
            throw new RuntimeException("eglCreateContext fail failed! " + mEgl.eglGetError());
        }

        if (!mEgl.eglMakeCurrent(mEGLDisplay, mEglSurface, mEglSurface, mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent failed! " + mEgl.eglGetError());
        }

        mFilterEngine = FilterEngine.getEngine(0, mOESTextureId);

    }

    private FilterEngine mFilterEngine;

    private void drawFrame() {
        if (mOESSurfaceTexture != null) {
            mOESSurfaceTexture.updateTexImage();
            mOESSurfaceTexture.getTransformMatrix(transformMatrix);
        }
        mEgl.eglMakeCurrent(mEGLDisplay, mEglSurface, mEglSurface, mEGLContext);
        GLES20.glViewport(0, 0, mTextureView.getWidth(), mTextureView.getHeight());
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClearColor(1f, 1f, 0f, 0f);
        mFilterEngine.drawTexture(transformMatrix);
        mEgl.eglSwapBuffers(mEGLDisplay, mEglSurface);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mHandler.sendEmptyMessage(0);
    }

}