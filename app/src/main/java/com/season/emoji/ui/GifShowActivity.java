package com.season.emoji.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.season.emoji.ui.view.ContainerView;
import com.season.emoji.ui.view.gif.frame.GifFrameView;
import com.season.emoji.ui.view.scale.ScaleView;
import com.season.emoji.util.LogUtil;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-09-28 16:39
 */
public class GifShowActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ContainerView mContainerView = new ContainerView(this);

        ScaleView scaleView = new ScaleView(this);
        final GifFrameView gifView = new GifFrameView(this);
        gifView.setMovieResource(getIntent().getStringExtra("path"));
        scaleView.addView(gifView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mContainerView.addView(scaleView);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(400, 400);
        setContentView(mContainerView, params);

new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
        LogUtil.log("duration = " + gifView.getDuration());
    }
}, 5000);
    }
}
