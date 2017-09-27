package com.season.emoji.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.season.emoji.R;
import com.season.emoji.ui.view.ContainerView;
import com.season.emoji.ui.view.GifView;
import com.season.emoji.permission.PermissionsManager;
import com.season.emoji.permission.PermissionsResultAction;
import com.season.emoji.ui.view.ScaleView;
import com.season.emoji.util.LogUtil;

public class MainActivity extends AppCompatActivity {

    ContainerView mContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtil.init(this);
        setContentView(R.layout.activity_main);

        mContainerView = (ContainerView) findViewById(R.id.container);

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

        addImage(R.raw.clound);

    }

    private void addImage(int id){
        ScaleView scaleView = new ScaleView(this);
        GifView gifView = new GifView(this);
        gifView.setMovieResource(id);
        scaleView.addView(gifView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mContainerView.addView(scaleView);

    }

    private void addText(String text){
        ScaleView scaleView = new ScaleView(this);
        TextView textView = new TextView(this);
        textView.setPadding(16, 16, 16, 16);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
        textView.setText(text);
        scaleView.addView(textView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mContainerView.addView(scaleView);

    }

    public void 拍照(View view) {
        startActivity(new Intent(this, CameraActivity.class));
    }

    public void 添加GIF(View view) {
        addImage(R.raw.timg);
    }

    public void 添加文字(View view) {
        addText("添加文字");
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
}
