package com.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.march.gifmaker.utils.Util;
import com.season.emoji.R;
import com.view.scale.IScaleView;
import com.view.util.ToolBitmapCache;
import com.view.video.VideoTextureView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/12.
 */

public class DiyBackgroundView {

    Context context;
    public VideoTextureView videoView;
    ImageView picture;
    View bgView;
    public BgOperate currentOperate;
    public DiyBackgroundView(View view){
        context = view.getContext();
        videoView = (VideoTextureView) view.findViewById(R.id.videoView);
        picture = (ImageView) view.findViewById(R.id.picture);
        bgView = view.findViewById(R.id.bg_view);
        bgView.setBackgroundColor(Color.TRANSPARENT);
        init();
    }

    public int getDuration(){
        if (videoView.getVisibility() == View.VISIBLE){
            return videoView.getDuration();
        }
        return 0;
    }

    public float getSpeed(){
        if (videoView.getVisibility() == View.VISIBLE){
            return videoView.getDuration();
        }
        return 1.0f;
    }

    public IScaleView getBackgroundGifView(){
        if (videoView.getVisibility() == View.VISIBLE){
            return videoView;
        }
        return null;
    }

    public boolean isBackgroundVideoImageViewVisible(){
        if (videoView == null || picture == null){
            return false;
        }
        return videoView.getVisibility() == View.VISIBLE || picture.getVisibility() == View.VISIBLE;
    }

    public boolean isBackgroundImageViewVisible(){
        return picture.getVisibility() == View.VISIBLE;
    }

    public void release() {
        if (list.size() > 0){
            for (BgOperate op: list){
                Util.recycleBitmaps(op.bitmap);
            }
            list.clear();
        }
        ToolBitmapCache.getDefault().release();
        bgView = null;
        picture = null;
        videoView = null;
        currentOperate = null;
    }

    private void init(){
        BgOperate op = new BgOperate(View.VISIBLE, View.GONE, View.GONE, Color.TRANSPARENT, null, null);
        reset(op);
        addEvent(op);
    }

    public boolean showVideoView(String url, String videoPath, float rate) {
        if (currentOperate != null){
            if (currentOperate.visible3 == View.VISIBLE && videoPath.equals(currentOperate.videoFile)){
                return false;
            }
        }
        BgOperate op = new BgOperate(url, videoPath, rate);
        op.videoFile = videoPath;
        reset(op);
        addEvent(op);
        return true;
    }

    public boolean showImage(String url, String path) {
        if (currentOperate != null){
            if (currentOperate.visible2 == View.VISIBLE && path.equals(currentOperate.imageFile)){
                return false;
            }
        }
        BgOperate op = new BgOperate(url, path);
        reset(op);
        addEvent(op);
        return true;
    }

    public boolean showBackground(int color){
        if (currentOperate != null){
            if (currentOperate.visible1 == View.VISIBLE && color == currentOperate.color){
                return false;
            }
        }
        BgOperate op = new BgOperate(color);
        reset(op);
        addEvent(op);
        return true;
    }

    public boolean showVideoOrImage() {
        if (currentOperate != null){
            if (currentOperate.visible1 == View.VISIBLE){
                for (int i = position - 1 ; i >= 0 ; i--){
                    BgOperate operate = list.get(i);
                    if (operate.visible1 == View.GONE){
                        reset(operate);
                        addEvent(operate);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean reset(BgOperate op){
        currentOperate = op;
        videoView.setVisibility(op.visible3);
        picture.setVisibility(op.visible2);
        bgView.setVisibility(op.visible1);
        if (op.visible1 == View.VISIBLE)
            bgView.setBackgroundColor(op.color);
        if (!TextUtils.isEmpty(op.imageFile) && op.visible2 == View.VISIBLE){
            if (op.bitmap != null && !op.bitmap.isRecycled()){
            }else{
                op.bitmap = BitmapFactory.decodeFile(op.imageFile);
            }
            picture.setImageBitmap(op.bitmap);
        }
        if (!TextUtils.isEmpty(op.videoFile) && op.visible3 == View.VISIBLE){
            videoView.setMovieResource(op.videoFile);
            videoView.setSpeed(op.rate);
        }
        //recycle
        if (op.visible3 != View.VISIBLE){
            videoView.destroy();
        }
        return op.visible3 != View.VISIBLE;
    }

    public void reset() {
        BgOperate op = new BgOperate(View.VISIBLE, View.GONE, View.GONE, Color.TRANSPARENT, null, null);
        reset(op);
        addEvent(op);
    }

    public static class BgOperate {
        public int visible1 = View.GONE, visible2 = View.GONE, visible3 = View.GONE;
        public int color = -1;
        public String imageFile;
        public String gifFile;
        public String videoFile;
        public String url;
        public Bitmap bitmap;
        public float rate;

        public BgOperate(int visible1, int visible2, int visible3, int color, String imageFile, String videoFile) {
            this.visible1 = visible1;
            this.visible2 = visible2;
            this.visible3 = visible3;
            this.color = color;
            this.imageFile = imageFile;
            this.gifFile = videoFile;
        }

        public BgOperate(String url, String imagePath){
            this.url = url;
            if (!TextUtils.isEmpty(imagePath)){
                this.visible2 = View.VISIBLE;
                this.imageFile = imagePath;
                bitmap = ToolBitmapCache.getDefault().getBitmapFromFile(imagePath);
            }
        }

        public BgOperate(String url, String videoPath, float rate){
            this.url = url;
            this.rate = rate;
            if (!TextUtils.isEmpty(videoPath)){
                this.visible3 = View.VISIBLE;
                this.videoFile = videoPath;
                try{
                    MediaMetadataRetriever media =new MediaMetadataRetriever();
                    media.setDataSource(videoPath);
                    bitmap = media.getFrameAtTime();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
//            if (!TextUtils.isEmpty(gifPath)){//这个需求暂时没有，背景只有视频
//                this.visible3 = View.VISIBLE;
//                this.gifFile = gifPath;
//                bitmap = new FrameDecoder(videoPath).getFrame();
//            }
        }

        public BgOperate(int color) {
            this.visible1 = View.VISIBLE;
            this.color = color;
        }
    }

    int position = -1;
    List<BgOperate> list = new ArrayList<>();


    private void addEvent(BgOperate operate) {
        if (position < list.size() - 1){
            for (int i = list.size() - 1 ; i>position ; i--){
                list.remove(i);
            }
        }
        list.add(operate);
        position = list.size() - 1;
    }

    public boolean pre() {
        position --;
        if (position < 0){
            position = 0;
        }
        BgOperate op = list.get(position);
        return reset(op);
    }

    public boolean pro() {
        position ++;
        if (position > list.size() - 1){
            position = list.size() - 1;
        }
        BgOperate op = list.get(position);
        return reset(op);
    }
}
