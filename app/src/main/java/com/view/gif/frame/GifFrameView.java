package com.view.gif.frame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.march.gifmaker.utils.Util;
import com.view.gif.movie.FrameDecoder;
import com.view.scale.IScaleView;

import java.util.List;

/**
 *
 */
public class GifFrameView extends View implements IScaleView {

	private GifDecoder gifDecoder = null;
	private Paint mPaint;
	public String url;

    public GifFrameView(Context context) {
		super(context);
		init();
	}

	public GifFrameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GifFrameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

	public void setMovieResource(int resId) {
		gifDecoder = new GifDecoder();
		gifDecoder.setGifImage(getContext().getResources(), resId);
		gifDecoder.start();
	}

	public void setFrameList(List<GifFrame> frameList){
		gifDecoder.setFrameList(frameList);
	}

	public Bitmap firstFrame;
	public String file;
	public void setMovieResource(String strFileName) {
		if (gifDecoder != null){
			destroy();
		}
		this.file = strFileName;
		firstFrame = new FrameDecoder(file).getFrame();
		gifDecoder = new GifDecoder();
		gifDecoder.setGifImage(strFileName);
		gifDecoder.start();
	}

	public void destroy() {
		stopDecodeThread();
		if (gifDecoder != null){
			gifDecoder.destroy();
			gifDecoder = null;
		}
		Util.recycleBitmaps(firstFrame);
	}

	@Override
	public void onRelease(){
		autoPlay = false;
		destroy();
	}

	/**
	 */
	private void stopDecodeThread() {
		if (gifDecoder != null && gifDecoder.getState() != Thread.State.TERMINATED) {
			gifDecoder.interrupt();
			gifDecoder.destroy();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (firstFrame != null){
			setMeasuredDimension(firstFrame.getWidth(), firstFrame.getHeight());
		}else{
			setMeasuredDimension(0, 0);
		}
	}

	private long mMovieStart;
	private int mCurrentAnimationTime = 0;

	public boolean autoPlay = false;
	@Override
	public void onDraw(Canvas canvas) {
		drawCanvas(canvas);
	}


	@Override
	public void drawCanvas(Canvas canvas) {
		updateAnimationTime();
		if (recordTime >= 0){
			mCurrentAnimationTime = recordTime;
		}
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG| Paint.FILTER_BITMAP_FLAG));
		GifFrame gifFrame = gifDecoder.getFrame(mCurrentAnimationTime);
		if (gifFrame != null){
			if (gifFrame.image != null && gifFrame.image.isRecycled() == false) {
				canvas.drawBitmap(gifFrame.image, 0, 0, mPaint);
			}
		}
		if (autoPlay){
			invalidate();
		}
		isSeeking = false;
	}

	/**
	 */
	private void updateAnimationTime() {
		long now = android.os.SystemClock.uptimeMillis();
		if (mMovieStart == 0) {
			mMovieStart = now;
		}
		int dur = gifDecoder.getDuration();
		if (dur == 0) {
			dur = 3000;
		}
		mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
	}

	@Override
	public int getViewWidth() {
		try {
//			GifFrame gifFrame = gifDecoder.getFrame(0);
//			return gifFrame.image.getWidth();
			return firstFrame.getWidth();
		}catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getViewHeight() {
		try {
//			GifFrame gifFrame = gifDecoder.getFrame(0);
//			return gifFrame.image.getHeight();
			return firstFrame.getHeight();
		}catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getDuration() {
		return gifDecoder.getDuration();
	}

	@Override
	public int getDelay() {
		return gifDecoder.getDelay();
	}

	@Override
	public void startRecord() {
		isRecordRelyView = true;
	}

	@Override
	public boolean isSeeking(){
		return isSeeking;
	}
	boolean isSeeking = false;
	@Override
	public void recordFrame(int time) {
		if (getDuration() <= 0){
			recordTime = 0;
			return;
		}
		if (time > getDuration()){
			if (isRecordRelyView){
				recordTime = getDuration();
			}else{
				recordTime = time % getDuration();
			}
		}else{
			recordTime = time;
		}
		isSeeking = true;
	}

	private int recordTime = -1;
	boolean isRecordRelyView = false;
	@Override
	public void stopRecord() {
		isRecordRelyView = false;
		recordTime = -1;
	}

	public GifFrameView copy() {
		GifFrameView gifView = new GifFrameView(getContext());
		if (!TextUtils.isEmpty(file)){
			gifView.setMovieResource(file);
		}else{
		}
		gifView.url = url;
		gifView.file = file;
		return gifView;
	}
}
