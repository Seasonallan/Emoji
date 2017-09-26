package com.season.emoji.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.season.emoji.R;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);


    }

    public void 拍照(View view) {

        finish();
    }

    public void 添加GIF(View view) {
    }

    public void 添加文字(View view) {
    }
}
