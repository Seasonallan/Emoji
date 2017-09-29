package com.season.emoji.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.season.emoji.R;
import com.season.emoji.ui.view.camera.CameraProgressBar;
import com.season.emoji.ui.view.camera.CameraSurfaceView;
import com.season.emoji.ui.view.gif.frame.GifFrame;
import com.season.emoji.util.LogUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CameraActivity extends AppCompatActivity {

    private CameraProgressBar mProgressbar;
    private CameraSurfaceView mCameraSurfaceView;

    private int recLen = 300;
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);

        mCameraSurfaceView = (CameraSurfaceView) findViewById(R.id.camera);

        mProgressbar = (CameraProgressBar) findViewById(R.id.progress);
        mProgressbar.setMaxProgress(recLen);
        /**
         * 拍照，拍摄按钮监听
         */
        mProgressbar.setOnProgressTouchListener(new CameraProgressBar.OnProgressTouchListener() {
            @Override
            public void onClick(CameraProgressBar progressBar) {
                LogUtil.log("图片模式未开发");
            }

            @Override
            public void onLongClick(CameraProgressBar progressBar) {
                LogUtil.log("onLongClick");
                recLen = 300;
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {      // UI thread
                            @Override
                            public void run() {
                                mProgressbar.setProgress(mProgressbar.getProgress() + 1);
                                recLen--;
                                if(recLen < 0){
                                    timer.cancel();
                                }
                            }
                        });
                    }
                }, 10, 10);
                mCameraSurfaceView.start(new CameraSurfaceView.OnGifMakerListener() {
                    @Override
                    public void onMakeGifSucceed(ArrayList<GifFrame> frameList) {

                        mCameraSurfaceView.stop(false);
                        Intent intent = new Intent();
                        MainActivity.sFrameList = frameList;
                        intent.putExtra("list", "LIST"); //将值回传回去
                        setResult(2, intent);
                        finish();
                    }
                });
            }

            @Override
            public void onZoom(boolean zoom) {
                LogUtil.log("onZoom");
            }

            @Override
            public void onLongClickUp(CameraProgressBar progressBar) {
                LogUtil.log("onLongClickUp");
           //     recordSecond = mProgressbar.getProgress() * PLUSH_PROGRESS;//录制多少毫秒
                timer.cancel();
                mProgressbar.reset();
                mCameraSurfaceView.stop(true);
            }

            @Override
            public void onPointerDown(float rawX, float rawY) {
                LogUtil.log("onPointerDown");
            }
        });

    }

}
