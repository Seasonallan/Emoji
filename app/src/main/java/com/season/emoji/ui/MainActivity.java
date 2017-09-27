package com.season.emoji.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.season.emoji.R;
import com.season.emoji.ui.view.GifView;
import com.season.emoji.permission.PermissionsManager;
import com.season.emoji.permission.PermissionsResultAction;
import com.season.emoji.ui.view.ScaleView;

public class MainActivity extends AppCompatActivity {

    RelativeLayout mContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mContainerView = (RelativeLayout) findViewById(R.id.container);

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

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//270, 270);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mContainerView.addView(scaleView, params);
    }

    public void 拍照(View view) {
        startActivity(new Intent(this, CameraActivity.class));
    }

    public void 添加GIF(View view) {
        addImage(R.raw.timg);
    }

    public void 添加文字(View view) {
    }
}
