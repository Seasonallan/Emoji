package com.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.march.gifmaker.GifMaker;
import com.march.gifmaker.utils.Util;
import com.view.model.FontListEntity;
import com.view.model.TextStyleEntity;
import com.view.scale.IScaleView;
import com.view.scale.ScaleView;
import com.view.util.AreaAveragingScale;
import com.view.util.FileManager;
import com.view.util.ScreenUtils;
import com.view.video.VideoTextureView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-09-27 14:44
 */
public class ContainerView extends RelativeLayout {


    public Bitmap getCacheBitmap()
    {
        Bitmap bitmap ;
        if (backgroundView.currentOperate.visible3 == View.VISIBLE){
            //bitmap = backgroundView.videoView.getBitmap();
            bitmap = backgroundView.currentOperate.bitmap;
        }else{
            bitmap = backgroundView.currentOperate.bitmap;
        }
        if (bitmap != null && !bitmap.isRecycled()){
            Bitmap bitmapResult = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
            bitmap = scaleBitmap(bitmap, getWidth(), getHeight(), false);
            Canvas canvas = new Canvas(bitmapResult);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth())/2, (getHeight() - bitmap.getHeight())/2, null);
            return bitmapResult;
        }
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        if (backgroundView.currentOperate.color == Color.TRANSPARENT){
            canvas.drawColor(Color.WHITE);
        }else{
            canvas.drawColor(backgroundView.currentOperate.color);
        }
        draw(canvas);
        return bitmap;
    }

    public boolean autoStart = false;
    private ScaleView focusView;

    private GifMaker mGifMaker;
    private IScaleView relyView;
    private boolean isFullScreen =false;
    private float left = Float.MAX_VALUE, top = Float.MAX_VALUE, right = Float.MIN_VALUE, bottom = Float.MIN_VALUE;
    private int makeType = 1; //1是默认静图720 动图360   2是微信分享静图300 动图240   3是本地，文件保存地址修正
    private int widthHeight = 360;
    public static final int WEIXIN = 2;
    public static final int DEFAULT = 1;
    public static final int LOCAL = 3;

    void makeSize(boolean isGif){
        if (isGif){
            widthHeight = makeType==WEIXIN? 240:360;
        }else{
            widthHeight = makeType==WEIXIN? 300:720;
        }
    }
    public void start(int makeType, final GifMaker.OnGifMakerListener listener) {
        if (autoStart) {
            return;
        }
        this.makeType = makeType;
       // deleteFocus();
        //清空内存缓存
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startDelay(listener);
            }
        }, 300);
    }
    void startDelay(final GifMaker.OnGifMakerListener listener) {
        if (mGifMaker != null){
            mGifMaker.reset();
            mGifMaker = null;
        }
        relyView = backgroundView.getBackgroundGifView();
        if (relyView != null){
            isFullScreen = true;
            makeSize(true);
            relyView.startRecord();
            startGifMaker(listener, true);
        }else{
            isFullScreen = backgroundView.isBackgroundImageViewVisible();
            left = videoWidthHeight + offsetX; top = videoWidthHeight + offsetY; right = offsetX; bottom = offsetY;
            for (int i = 0; i< getChildCount(); i++){
                View scaleView = getChildAt(i);
                if (scaleView instanceof ScaleView && ((ScaleView) scaleView).getChildCount() > 0){
                    //((ScaleView) scaleView).startRecord();
                    View view = ((ScaleView) scaleView).getChildAt(0);
                    if (!isFullScreen){
                        float[] points = ((ScaleView) scaleView).mOpView.desPoints;
                        checkPoint(points[0], points[1]);
                        checkPoint(points[2], points[3]);
                        checkPoint(points[4], points[5]);
                        checkPoint(points[6], points[7]);
                    }
                    if (view instanceof IScaleView){
                        if (relyView == null){
                            relyView = (IScaleView) view;
                        }
                        int duration = ((IScaleView) view).getDuration();
                        if (relyView.getDuration() < duration){
                            relyView = (IScaleView) view;
                        }
                    }
                }
            }
            if (isFullScreen){
                left = offsetX;   right = videoWidthHeight + offsetX;
                top = offsetY; bottom = videoWidthHeight + offsetY;
            }else{
                left = Math.max(offsetX, left);
                right = Math.min(right, videoWidthHeight + offsetX);
                top = Math.max(offsetY, top);
                bottom = Math.min(bottom, videoWidthHeight + offsetY);
            }
            if (relyView == null || relyView.getDuration() <= 0){
                makeSize(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (backgroundView.currentOperate != null) {
                            if (backgroundView.currentOperate.visible2 == View.VISIBLE){
                                Bitmap bitmap = backgroundView.currentOperate.bitmap;
                                if (bitmap != null && !bitmap.isRecycled()){
                                    Bitmap tBitmap = Bitmap.createBitmap(widthHeight, widthHeight, Bitmap.Config.RGB_565);
                                    Canvas canvas = new Canvas(tBitmap);
                                    canvas.drawBitmap(bitmap, null, new RectF(0, 0, widthHeight, widthHeight), null);
                                    drawItem(canvas, videoWidthHeight, offsetX, offsetY);
                                    saveAndNotify(tBitmap, listener);
                                    return;
                                }
                            }else if (backgroundView.currentOperate.color != Color.TRANSPARENT){
                                float realShowWidth = right - left;
                                float realShowHeight = bottom - top;
                                float height = realShowHeight * widthHeight/realShowWidth;
                                Bitmap tBitmap = Bitmap.createBitmap(widthHeight, (int) height, Bitmap.Config.RGB_565);
                                Canvas canvas = new Canvas(tBitmap);
                                canvas.drawColor(backgroundView.currentOperate.color);
                                drawItem(canvas, realShowWidth, left, top);
                                saveAndNotify(tBitmap, listener);
                                return;
                            }
                        }
                        float realShowWidth = right - left;
                        float realShowHeight = bottom - top;
                        float height = realShowHeight * widthHeight/realShowWidth;
                        Bitmap tBitmap = Bitmap.createBitmap(widthHeight, (int) height, Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(tBitmap);
                        drawItem(canvas, realShowWidth, left, top);
                        saveAndNotify(tBitmap, listener);
                    }
                }, 300);
                return;
            }
            makeSize(true);
            relyView.startRecord();
            startGifMaker(listener, false);
        }
    }

    private void saveAndNotify(Bitmap bitmap, GifMaker.OnGifMakerListener listener){
        File file;
        if (makeType == LOCAL){
            file = FileManager.getShareLocalFile(".png");
        }else{
            file = FileManager.getDiyFile(".png");
        }
        String filePath = Util.saveBitmap(file, bitmap);
        if (filePath == null){
            listener.onMakeGifFail();
            return;
        }
        listener.onMakeGifSucceed(filePath);
    }

    /**
     * 视频合成多1帧用于显示视频最后一帧
     * @param listener
     * @param isVideo
     */
    private void startGifMaker(GifMaker.OnGifMakerListener listener, boolean isVideo){
        File outFile;
        if (makeType == LOCAL){
            outFile = FileManager.getShareLocalFile(".gif");
        }else{
            outFile = FileManager.getDiyFile(".gif");
        }
        if (outFile == null){
            listener.onMakeGifFail();
            return;
        }
        String absolutePath = outFile.getAbsolutePath();
        int duration = relyView.getDuration();
        int delay = relyView.getDelay();
        int count = 1;
        if (delay != 0){
            count = duration/delay;
            if (duration%delay != 0){
                count ++;
            }
        }
        if (isVideo){
            //count ++;
        }
        mGifMaker = new GifMaker(count, relyView.getDelay(),  Executors.newCachedThreadPool())
                .setOutputPath(absolutePath);
        if (mGifMaker != null){
            if (mGifMaker.isGifMaded){
                listener.onMakeGifSucceed(mGifMaker.mOutputPath);
            }else{
                mGifMaker.setGifMakerListener(listener);
            }
        }
        autoStart = true;
        startTime = System.currentTimeMillis();
    }

    private long startTime;
    public int offsetX=0, offsetY=0;
    public int videoWidthHeight = ScreenUtils.getScreenWidth();

    private void onRecordFinish(){
        autoStart = false;
        if (relyView instanceof VideoTextureView){
            relyView.stopRecord();
        }
        recordViewFinish();
    }

    private void checkPoint(float x, float y){
        if (Float.isNaN(x) || Float.isNaN(y)){
            return;
        }
        left = Math.min(left, x); right = Math.max(right, x);
        top = Math.min(top, y); bottom = Math.max(bottom, y);
    }

    public void deleteFocus() {
        if (focusView != null){
            focusView.removeFocus();
            focusView = null;
        }
    }

    public int getViewIndex(){
        if (focusView == null){
            return -1;
        }
        return indexOfChild(focusView);
    }


    public void stop(){
        mGifMaker = null;
    }


    public View getFocusView() {
        if (running != 1){
            return null;
        }
        if (focusView != null){
            return focusView.getChildAt(0);
        }
        return focusView;
    }
    public ScaleView getView() {
        return focusView;
    }

    public ContainerView(Context context) {
        super(context);
        init();
    }

    public ContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    int running = -1;
    @Override
    public void dispatchWindowVisibilityChanged(int visibility) {
        if (visibility == GONE || visibility == INVISIBLE) {
            running = 0;
        } else if (visibility == VISIBLE) {
            running = 1;
        }
        super.dispatchWindowVisibilityChanged(visibility);
    }

    private void refreshView(){
        for (int i = 0; i< getChildCount(); i++){
            View view = getChildAt(i);
            if (view instanceof ScaleView){
                ((ScaleView)view).refresh();
            }
        }
    }
    private void recordView(int time){
        for (int i = 0; i< getChildCount(); i++){
            View view = getChildAt(i);
            if (view instanceof ScaleView){
                ((ScaleView)view).record(time);
            }
        }
    }
    private void recordViewFinish(){
        for (int i = 0; i< getChildCount(); i++){
            View view = getChildAt(i);
            if (view instanceof ScaleView){
                ((ScaleView)view).recordFinish();
            }
        }
    }

    private void delay(int time){
        try {
            Thread.sleep(time);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    boolean isDrawing = false;
    private void drawVideo(){
        try {
            isDrawing = true;
            Bitmap bitmap = ((VideoTextureView)relyView).getBitmap();
            Bitmap tBitmap = Bitmap.createBitmap(widthHeight, widthHeight, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(tBitmap);
            canvas.drawBitmap(bitmap, null, new RectF(0, 0, widthHeight, widthHeight), null);
            drawItem(canvas, videoWidthHeight, offsetX, offsetY);
            mGifMaker.addBitmap(tBitmap);
            isDrawing = false;
        }catch (Exception e){
            //PAD必须在主线程中调用TextureView.getBitmap
            //否则出现错误java.lang.IllegalStateException: Hardware acceleration can only be used with a single UI thread.
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    drawVideo();
                }
            });
        }
        while(isDrawing){
            delay(10);
        }
    }

    private void drawItem(Canvas canvas, float showWidth, float left, float top){
        float scale = widthHeight * 1.0f/ showWidth;
        for (int i = 0; i< getChildCount(); i++){
            View view = getChildAt(i);
            if (view instanceof ScaleView){
                ScaleView scaleView = (ScaleView) view;
                Matrix matrix = new Matrix();
                matrix.set(scaleView.mCurrentMatrix);
                matrix.postScale(scale, scale, 0, 0);
                matrix.postTranslate(-left * scale, -top * scale);
                canvas.save();
                canvas.concat(matrix);
                if (scaleView.getChildCount() > 0){
                    View childView = scaleView.getChildAt(0);
                    if (childView instanceof IScaleView){
                        ((IScaleView)childView).drawCanvas(canvas);
                    }
                }
                canvas.restore();
            }
        }
    }

    class RefreshRecordThread extends Thread {
        @Override
        public void run() {
            while (true){
                if (running < 0){
                    return;
                }
                if (running == 1 && !autoStart){
                    refreshView();
                }
                if (running == 1 && autoStart){
                    if (mGifMaker != null && !mGifMaker.isBitmapFull()){
                        if (System.currentTimeMillis() - startTime > mGifMaker.getTotalSize() * 1000){
                            onRecordFinish();
                            if (mGifMaker != null && mGifMaker.mOnGifMakerListener != null){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mGifMaker.mOnGifMakerListener.onMakeGifFail();
                                        mGifMaker.reset();
                                    }
                                });
                            }
                        }else if (relyView instanceof VideoTextureView){
                            int time = mGifMaker.getFrameCountNow() * relyView.getDelay();
                            recordView(time);
                            ((VideoTextureView)relyView).recordFrameCallback(time);
                            int waitCount = 10;
                            while (relyView.isSeeking()){//视频的seekTo方法需要耗时，等待seekTo完毕
                                delay(10);
                                if (waitCount <= 0){
                                    break;
                                }
                                waitCount --;
                            }
                            drawVideo();
                        }else{
                            int time = mGifMaker.getFrameCountNow() * relyView.getDelay();
                            recordView(time);

                            Bitmap tBitmap = null;
                            if (backgroundView.currentOperate != null) {
                                if (backgroundView.currentOperate.visible2 == View.VISIBLE){
                                    Bitmap bitmap = backgroundView.currentOperate.bitmap;
                                    if (bitmap != null && !bitmap.isRecycled()){
                                        tBitmap = Bitmap.createBitmap(widthHeight, widthHeight, Bitmap.Config.RGB_565);
                                        Canvas canvas = new Canvas(tBitmap);
                                        canvas.drawBitmap(bitmap, null, new RectF(0, 0, widthHeight, widthHeight), null);
                                        drawItem(canvas, videoWidthHeight, offsetX, offsetY);
                                    }
                                }else if (backgroundView.currentOperate.color != Color.TRANSPARENT){
                                    float realShowWidth = right - left;
                                    float realShowHeight = bottom - top;
                                    float height = realShowHeight * widthHeight/realShowWidth;
                                    tBitmap = Bitmap.createBitmap(widthHeight, (int) height, Bitmap.Config.RGB_565);
                                    Canvas canvas = new Canvas(tBitmap);
                                    canvas.drawColor(backgroundView.currentOperate.color);
                                    drawItem(canvas, realShowWidth, left, top);
                                }
                            }
                            if (tBitmap == null){
                                float realShowWidth = right - left;
                                float realShowHeight = bottom - top;
                                float height = realShowHeight * widthHeight/realShowWidth;
                                tBitmap = Bitmap.createBitmap(widthHeight, (int) height, Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(tBitmap);
                                drawItem(canvas, realShowWidth, left, top);
                            }
                            mGifMaker.addBitmap(tBitmap);
                        }
                    }else{
                        onRecordFinish();
                        delay(10);
                    }
                }else{
                    delay(10);
                }
            }
        }
    }

    Handler handler;
    Thread refreshThread;
    private void init(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        running = 1;
        refreshThread = new RefreshRecordThread();
        refreshThread.start();
        handler = new Handler();
        // mHandler.sendEmptyMessageDelayed(0, 10);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (child instanceof ScaleView){
            boolean callback = ((ScaleView)child).initViewOffset(getWidth(), getHeight());
            startOp((ScaleView) child, true, callback);
            ((ScaleView) child).getFocus();
            isEventAttaching = false;
            ((ScaleView) child).setClickListener(new ScaleView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickListener != null){
                        mClickListener.onClick(view);
                    }
                }

                @Override
                public void onDoubleClick(View view) {
                    if (mClickListener != null){
                        mClickListener.onDoubleClick(view);
                    }
                }

                @Override
                public void onDelete(View view) {
                    deleteView((ScaleView) view);
                }
            });
        }
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        if (view == focusView){
            focusView = null;
        }
    }


    private ScaleView.OnClickListener mClickListener;
    public void setClickListener(ScaleView.OnClickListener listener) {
        mClickListener = listener;
    }


    public void reset(){
        Operate oprate = new Operate(IType.RESET);
        int count = getChildCount();
        oprate.childs = new View[count];
        for (int i = 0; i< count; i++){
            View child = getChildAt(i);
            if (child instanceof ScaleView){
                oprate.childs[i] = child;
            }
        }
        for (int i = 0; i< oprate.childs.length;i++){
            if (oprate.childs[i] != null)
                removeView(oprate.childs[i]);
        }
        addEvent(oprate);
        backgroundView.reset();
        //removeAllViews();
    }

    public void onMove(ScaleView view, MotionEvent event) {
        if (delView != null){
            boolean isInDelRect = delView.checkPosition(event);
            view.setDelPosition(isInDelRect);
        }
    }

    public void onMoveEnd() {
        if (delView != null){
            delView.hide();
        }
        isEventAttaching = false;
    }

    public interface  IType{
        int RESET = 0;
        int ADD = 1;
        int REMOVE = 2;
        int MOVE = 3;
        int UP_LAYER = 4;
        int DOWN_LAYER = 5;

        int BACKGROUND = 6;
        int TEXT_OBJECT = 7;
    }
    class Operate{
        public int type;
        public ScaleView scaleView;
        public TextStyleView textView;
        public View[] childs;
        public float[] matrix;
        public Operate(int op, ScaleView scaleView, Matrix ma){
            matrix = new float[9];
            if (ma != null){
                ma.getValues(matrix);
            }
            type = op;
            this.scaleView = scaleView;
        }

        public Operate(int op) {
            this.type = op;
        }

        public Operate(TextStyleView textView) {
            this.type = IType.TEXT_OBJECT;
            this.textView = textView;
        }
    }

    int position = -1;
    List<Operate> list = new ArrayList<>();

    public void deleteView(ScaleView view){
        if (view.getParent() != null){
            addEvent(IType.REMOVE, view, view.mCurrentMatrix);
            removeView(view);
        }
    }


    public interface IFocusChangeListener{
        void onFocusLose(ViewGroup view);
        void onFocusGet(ViewGroup view);
        void onFocusClear();
    }
    private IFocusChangeListener mOnFocusChangeListener;
    public void setFocusChangeListener(IFocusChangeListener listener) {
        mOnFocusChangeListener = listener;
    }
    public void startOp(ScaleView view, boolean isFocus, boolean callback){
        if (isFocus){
            if (this.focusView != null && this.focusView != view){
                this.focusView.removeFocus();
            }
            this.focusView = view;
            if (mOnFocusChangeListener != null && callback){
                mOnFocusChangeListener.onFocusGet(view);
            }
            isEventAttaching = true;
        }else{
            if (view == this.focusView){
                this.focusView.removeFocus();
                this.focusView = null;
                if (mOnFocusChangeListener != null && callback){
                    mOnFocusChangeListener.onFocusLose(view);
                }
            }
            int index = indexOfChild(view);
            if (index == 0){
                if (focusView == null && mOnFocusChangeListener != null && callback){
                    mOnFocusChangeListener.onFocusClear();
                }
            }
        }
    }
    //多点的时候拦截全部down到第一点的焦点VIew
    public boolean isEventAttaching = false;
    public boolean isEventAttaching(){
        if (focusView == null){
            return false;
        }
        return isEventAttaching;
    }
    public int upLayer(View view, boolean event)
    {
        if (view  == null){
            return -1;
        }
        int i = indexOfChild(view);
        if (i != -1) {
            if (i >= getChildCount() - 1){
                return 0;
            }else{
                removeViewAt(i);
                addView(view, i + 1);
                if (event)
                    addEvent(IType.UP_LAYER, view, null);
                return i - 1;
            }
        }
        return -1;
    }

    public int downLayer(View view, boolean event)
    {
        if (view  == null){
            return -1;
        }
        int i = indexOfChild(view);
        if (i != -1) {
            if (i <= 0) {
                return 0;
            } else {
                removeViewAt(i);
                addView(view, i - 1);
                if (event)
                    addEvent(IType.DOWN_LAYER, view, null);
                return i - 0;
            }
        }
        return -1;
    }

    private void addEvent(Operate operate) {
        if (position < list.size() - 1){
            for (int i = list.size() - 1 ; i>position ; i--){
                list.remove(i);
            }
        }
        list.add(operate);
        position = list.size() - 1;
    }

    public void addEvent(int type, View scaleView, Matrix matrix){
        if (scaleView instanceof ScaleView){
            addEvent(new Operate(type, (ScaleView) scaleView, matrix));
        }
    }

    public boolean canPre(){
        return position >= 0;
    }
    public boolean canPro(){
        return position < list.size()-1;
    }

    private int getPreScaleViewPosition(int position){
        ScaleView scaleView = list.get(position).scaleView;
        for (int i = position - 1; i >= 0; i --){
            if (list.get(i).scaleView == scaleView){
                return i;
            }
        }
        return position;
    }

    public void pre(){
        switch (list.get(position).type){
            case IType.MOVE:
                int pos = getPreScaleViewPosition(position);
                ScaleView scaleView = list.get(pos).scaleView;
                float[] data  = list.get(pos).matrix;
                scaleView.resetMatrix(data);
                break;
            case IType.ADD:
                removeView(list.get(position).scaleView);
                break;
            case IType.REMOVE:
                int posPre = getPreScaleViewPosition(position);
                ScaleView scaleViewPre = list.get(position).scaleView;
                float[] dataPre  = list.get(posPre).matrix;
                scaleViewPre.resetMatrix(dataPre);
                int index = list.get(position).scaleView.index;
                if (index == 0){
                    addView(scaleViewPre, 0);
                }else{
                    addView(scaleViewPre);
                }
                break;
            case IType.DOWN_LAYER:
                upLayer(list.get(position).scaleView, false);
                break;
            case IType.UP_LAYER:
                downLayer(list.get(position).scaleView, false);
                break;
            case IType.RESET:
                View[] views = list.get(position).childs;
                for (int i = 0; i< views.length;i++){
                    if (views[i] != null)
                        addView(views[i]);
                }
                if(backgroundView.pre()){
                    changeAnimationTime();
                }
                break;
            case IType.BACKGROUND:
                if(backgroundView.pre()){
                    changeAnimationTime();
                }
                break;
            case IType.TEXT_OBJECT:
                TextStyleView textView = list.get(position).textView;
                textView.pre();
                break;
        }
        position --;
    }

    public void pro(){
        position ++;
        switch (list.get(position).type){
            case IType.MOVE:
                ScaleView scaleView = list.get(position).scaleView;
                float[] data  = list.get(position).matrix;
                scaleView.resetMatrix(data);
                break;
            case IType.ADD:
                int index = list.get(position).scaleView.index;
                if (index == 0){
                    addView(list.get(position).scaleView, 0);
                }else{
                    addView(list.get(position).scaleView);
                }
                break;
            case IType.REMOVE:
                removeView(list.get(position).scaleView);
                break;
            case IType.DOWN_LAYER:
                downLayer(list.get(position).scaleView, false);
                break;
            case IType.UP_LAYER:
                upLayer(list.get(position).scaleView, false);
                break;
            case IType.RESET:
                View[] views = list.get(position).childs;
                for (int i = 0; i< views.length;i++){
                    if (views[i] != null)
                        removeView(views[i]);
                }
                if(backgroundView.pro()){
                    changeAnimationTime();
                }
                //removeAllViews();
                break;
            case IType.BACKGROUND:
                if(backgroundView.pro()){
                    changeAnimationTime();
                }
                break;
            case IType.TEXT_OBJECT:
                TextStyleView textView = list.get(position).textView;
                textView.pro();
                break;
        }
    }


    public DiyBackgroundView backgroundView;
    public void bindBgView(View parentView, final MediaPlayer.OnPreparedListener listener) {
        backgroundView = new DiyBackgroundView(parentView);
        backgroundView.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                changeAnimationTime();
                if (listener!= null){
                    listener.onPrepared(mp);
                }
            }
        });
    }
    public void bindBgView(ImageView imageView) {
        backgroundView = new DiyBackgroundView(imageView);
    }

    public void showVideoView(String url, String videoPath, float rate){
        if(backgroundView.showVideoView(url, videoPath, rate))
            addEvent(new Operate(IType.BACKGROUND));
    }
    public void showImage(String url, String path) {
        if(backgroundView.showImage(url, path))
            addEvent(new Operate(IType.BACKGROUND));
        changeAnimationTime();
    }
    public void showBackground(int color){
        if(backgroundView.showBackground(color))
            addEvent(new Operate(IType.BACKGROUND));
        changeAnimationTime();
    }
    public void showVideoOrImage(){
        if(backgroundView.showVideoOrImage())
            addEvent(new Operate(IType.BACKGROUND));
    }


    public void setTextAnimationType(int type)
    {
        View view = getFocusView();
        if (view != null && view instanceof TextStyleView) {
            TextStyleView textObjectView = (TextStyleView) view;
            int duration = backgroundView.getDuration();
            int delay = backgroundView.videoView.getDelay();
            float speed = backgroundView.videoView.getSpeed();
            if (textObjectView.setTextAnimationType(type,duration,delay,speed)){
                addEvent(new Operate(textObjectView));
            }
        }
    }

    /**
     * 改变所有文字动效的时间
     */
    public void changeAnimationTime()
    {
        for (int i = 0; i< getChildCount(); i++){
            View view = getChildAt(i);
            if (view instanceof ScaleView){
                ScaleView scaleView = (ScaleView) view;
                if (scaleView.getChildCount() > 0){
                    View childView = scaleView.getChildAt(0);
                    if (childView instanceof TextStyleView){
                        ((TextStyleView)childView).changeAnimationTime(backgroundView.getDuration(), backgroundView.videoView.getDelay(), backgroundView.getSpeed());
                    }
                }
            }
        }
    }

    public void setTextWidth(float size)
    {
        View view = getFocusView();
        if (view != null && view instanceof TextStyleView) {
            TextStyleView textObjectView = (TextStyleView) view;
            if (textObjectView.setPaintWidthByPercent(size)){
                addEvent(new Operate(textObjectView));
            }
        }
    }
    public void setTextColor(String textColor) {
        View view = getFocusView();
        if (view != null && view instanceof TextStyleView) {
            TextStyleView textObjectView = (TextStyleView) view;
            if (textObjectView.setTextcolor(textColor)){
                addEvent(new Operate(textObjectView));
            }
        }
    }
    public void setTextColor(int textColor) {
        View view = getFocusView();
        if (view != null && view instanceof TextStyleView) {
            TextStyleView textObjectView = (TextStyleView) view;
            if (textObjectView.setTextcolor(textColor)){
                addEvent(new Operate(textObjectView));
            }
        }
    }
    public void setTextColorAlpha(int alpha)
    {
        View view = getFocusView();
        if (view != null && view instanceof TextStyleView) {
            TextStyleView textObjectView = (TextStyleView) view;
            if (textObjectView.setTextalpha(alpha)){
                addEvent(new Operate(textObjectView));
            }
        }
    }
    public void editText(String params)
    {
        View view = getFocusView();
        if (view != null && view instanceof TextStyleView) {
            TextStyleView obj = (TextStyleView) view;
            obj.editText(params);
            obj.requestLayout();
            addEvent(new Operate(obj));
        }
    }
    public void setStrokeSize(float size)
    {
        View view = getFocusView();
        if (view != null && view instanceof TextStyleView) {
            TextStyleView textObjectView = (TextStyleView) view;
            if (textObjectView.setStrokeWidthByPercent(size)){
                addEvent(new Operate(textObjectView));
            }
        }
    }
    public void setStrokeColorAlpha(int alpha)
    {
        View view = getFocusView();
        if (view instanceof TextStyleView) {
            TextStyleView textObjectView = (TextStyleView) view;
            if (textObjectView.setStrokealpha(alpha)){
                addEvent(new Operate(textObjectView));
            }
        }
    }
    public void setStrokeColor(String color)
    {
        View view = getFocusView();
        if (view instanceof TextStyleView) {
            TextStyleView textObjectView = (TextStyleView) view;
            if (textObjectView.setStrokecolor(color)){
                addEvent(new Operate(textObjectView));
            }
        }
    }
    public void setStrokeColor(int color)
    {
        View view = getFocusView();
        if (view instanceof TextStyleView) {
            TextStyleView textObjectView = (TextStyleView) view;
            if (textObjectView.setStrokecolor(color)){
                addEvent(new Operate(textObjectView));
            }
        }
    }
    public void setTypeFace(Typeface typeface)
    {
        View view = getFocusView();
        if (view != null && view instanceof TextStyleView) {
            TextStyleView textObjectView = (TextStyleView) view;
            if (textObjectView.setTexttypeface(typeface)){
                addEvent(new Operate(textObjectView));
            }
        }
    }

    public void setTypeFace(FontListEntity.RecordsBean typeface) {
        View view = getFocusView();
        if (view != null && view instanceof TextStyleView) {
            TextStyleView textObjectView = (TextStyleView) view;
            if (textObjectView.setTexttypeface(typeface)){
                addEvent(new Operate(textObjectView));
            }
        }
    }

    public void setTextStyle(TextStyleEntity textStyle) {
        View view = getFocusView();
        if (view != null && view instanceof TextStyleView) {
            TextStyleView textObjectView = (TextStyleView) view;
            if (textObjectView.setTextStyle(textStyle)){
                addEvent(new Operate(textObjectView));
            }
        }
    }



    public DelView delView;
    public void bindDelView(DelView delView) {
        this.delView = delView;
    }



    public void release(){
        running = -1;
        backgroundView.release();
        release(this);
        removeAllViews();
    }

    private void release(ViewGroup parent){
        for (int i = 0; i< list.size(); i++){
            Operate view = list.get(i);
            if (view.scaleView != null && view.scaleView.getChildCount() > 0){
                View childView = view.scaleView.getChildAt(0);
                if (childView instanceof IScaleView){
                    ((IScaleView) childView).onRelease();
                }
            }
            if (view.textView != null){
                view.textView.onRelease();
            }
        }
        if (true){
            return;
        }
        for (int i = 0; i< parent.getChildCount(); i++){
            View view = parent.getChildAt(i);
            if (view instanceof ViewGroup){
                release((ViewGroup) view);
            }else{
                if (view instanceof IScaleView){
                    ((IScaleView) view).onRelease();
                }
            }
        }
    }

    /**
     */
    public Bitmap cutBitmap(Bitmap bitmap, float width, float height, float x, float y)
    {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        if (x > w){
            x = 0;
        }
        if (y > h){
            y = 0;
        }
        if (width + x > w){
            width = w - x;
        }
        if (height + y > h){
            height = h - y;
        }
        Bitmap bitmapResult = Bitmap.createBitmap(bitmap, (int)x, (int)y, (int)width, (int)height);
        bitmap.recycle();
        return bitmapResult;
    }
    /**
     */
    public Bitmap scaleBitmap(Bitmap bitmap, float width, float height, boolean recycle)
    {
        if (false){
            AreaAveragingScale areaAveragingScale = new AreaAveragingScale(bitmap);
            Bitmap bitmapRes = areaAveragingScale.getScaledBitmap(width, height);
            if (recycle)
                bitmap.recycle();
            return bitmapRes;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = width / w;
        float scale2 = height / h;
        scale = scale < scale2 ? scale : scale2;
        matrix.postScale(scale, scale);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

//        Bitmap bmp = Bitmap.createBitmap((int)width, (int)height, Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bmp);
//        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
//        canvas.drawBitmap(bitmap, matrix, null);
//        Bitmap bmp = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
        if (recycle)
            bitmap.recycle();
        return bmp;
    }
}
