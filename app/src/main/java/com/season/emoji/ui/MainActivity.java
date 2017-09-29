package com.season.emoji.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.march.gifmaker.GifMaker;
import com.season.emoji.ui.view.TextViewEx;
import com.season.emoji.ui.view.gif.frame.GifFrame;
import com.season.emoji.ui.view.gif.frame.GifFrameView;
import com.season.emoji.R;
import com.season.emoji.ui.view.ContainerView;
import com.season.emoji.ui.view.gif.movie.GifMovieView;
import com.season.emoji.permission.PermissionsManager;
import com.season.emoji.permission.PermissionsResultAction;
import com.season.emoji.ui.view.scale.ScaleView;
import com.season.emoji.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    ContainerView mContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtil.init(this);
        setContentView(R.layout.activity_main);

        mContainerView = (ContainerView) findViewById(R.id.container);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContainerView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = params.width;
        mContainerView.requestLayout();

        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });

      //  addImage(R.raw.clound);

    }

    private void addImage(int id){
        ScaleView scaleView = new ScaleView(this);
        GifMovieView gifView = new GifMovieView(this);
        gifView.setMovieResource(id);
        scaleView.addView(gifView);

        mContainerView.addView(scaleView);

    }

    private void addImageEx(String name){
        ScaleView scaleView = new ScaleView(this);
        GifMovieView gifView = new GifMovieView(this);
        String absolutePath = new File(Environment.getExternalStorageDirectory() + "/1/"
                , name).getAbsolutePath();
        gifView.setMovieResource(absolutePath);
        scaleView.addView(gifView);

        mContainerView.addView(scaleView);

    }

    private void addText(String text){
        ScaleView scaleView = new ScaleView(this);
        TextViewEx textView = new TextViewEx(this);
        textView.setPadding(16, 16, 16, 16);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
        textView.setText(text);
        scaleView.addView(textView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mContainerView.addView(scaleView);

    }

    public void 拍照(View view) {
        startActivityForResult(new Intent(this, CameraActivity.class), 1024);
    }

    public static List<GifFrame> sFrameList;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            if (requestCode == 1024) {
                LogUtil.log(data);
                if (data.hasExtra("outPath")){
                    String path = data.getStringExtra("outPath");
                    ScaleView scaleView = new ScaleView(this);
                    scaleView.setBackground();
                    GifFrameView gifView = new GifFrameView(this);
                    gifView.setMovieResource(path);
                    //  GifMovieView gifView = new GifMovieView(this, true);
                    //  gifView.setMovieResource(path);
                    scaleView.addView(gifView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    if (mContainerView.hasCameraView(mContainerView)){
                        mContainerView.reset();
                    }
                    mContainerView.addView(scaleView, 0);
                }else if (data.hasExtra("list")){
                    LogUtil.log("frameSize= "+ sFrameList.size());
                    ScaleView scaleView = new ScaleView(this);
                    scaleView.setBackground();
                    GifFrameView gifView = new GifFrameView(this);
                    gifView.setFrameList(sFrameList);
                    //  GifMovieView gifView = new GifMovieView(this, true);
                    //  gifView.setMovieResource(path);
                    scaleView.addView(gifView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    if (mContainerView.hasCameraView(mContainerView)){
                        mContainerView.reset();
                    }
                    mContainerView.addView(scaleView, 0);
                }

            }
        }
    }

    int[] ids = {R.raw.clound, R.raw.test, R.raw.timg, R.raw.mirror, R.raw.umbrella};
    String[] idsStr = {"umbrella.gif", "clound.gif"};
    int i = 0;
    public void 添加GIF(View view) {
        addImage(ids[i++ % ids.length]);
       // addImageEx(idsStr[i++ % idsStr.length]);
    }

    public void 添加文字(View view) {
        addText("表情科技Demo");
    }

    public void 撤销操作(View view) {
        if (mContainerView.canPre()){
            mContainerView.pre();
        }else{
            LogUtil.toast("第一个操作");
        }

    }

    public void 重复操作(View view) {
        if (mContainerView.canPro()){
            mContainerView.pro();
        }else{
            LogUtil.toast("已经是最后一个操作");
        }
    }

    private long recLen = 300;
    private Timer timer = new Timer();
    public void 合成(View view) {
        if (mContainerView.getChildCount() == 0){
            LogUtil.toast("画布上没东西");
            return;
        }
        LogUtil.toast("正在合成，请稍后");
        recLen = System.currentTimeMillis();
        mContainerView.start(new GifMaker.OnGifMakerListener() {
            @Override
            public void onMakeGifSucceed(String outPath) {
                timer.cancel();
                mContainerView.stop();
                LogUtil.toast("cost time = " + (System.currentTimeMillis() - recLen) +" ms");
                LogUtil.log("cost time = " + (System.currentTimeMillis() - recLen) +" ms");
                startActivity(new Intent(MainActivity.this, GifShowActivity.class).putExtra("path", outPath));
            }
        });
    }


}
