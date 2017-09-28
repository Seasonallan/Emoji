package com.season.emoji.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.season.emoji.R;
import com.season.emoji.ui.view.ContainerView;
import com.season.emoji.ui.view.gif.frame.GifFrameView;
import com.season.emoji.ui.view.scale.ScaleView;
import com.season.emoji.util.LogUtil;

import java.io.File;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-09-28 16:39
 */
public class GifShowActivity  extends AppCompatActivity {


    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        path = getIntent().getStringExtra("path");

        setContentView(R.layout.activity_show);
        ContainerView mContainerView = (ContainerView) findViewById(R.id.container);

        ScaleView scaleView = new ScaleView(this);
        GifFrameView gifView = new GifFrameView(this);
        gifView.setMovieResource(path);
        scaleView.addView(gifView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mContainerView.addView(scaleView);

        TextView button = (TextView) findViewById(R.id.camera);


        File file = new File(path);
        button.setText("Gif info: \nFilePath: "+path +"  \nFileSize: "+file.length()/1024+ "KB,  \nClick me to open file>>");

    }

    public void fileShow(View view){
        File file = new File(path);
        if(null==file || !file.exists()){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file.getParentFile()), "file/*");
        try {
            startActivity(intent);
            startActivity(Intent.createChooser(intent, "Choose File"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
