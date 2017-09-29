package com.season.emoji.ui.view.gif.frame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.season.emoji.ui.view.scale.IScaleView;

import java.util.ArrayList;
import java.util.List;

/**
 * GifView，一个显示gif图片的view。<br>
 * gif图片可以是字节，资源或者文件的形式，可以设置播放次数，也可以设置循环播放。在播放过程中可以进行暂停<br>
 * 本类进行了各种优化设计，并且能支持帧数超过100以上的大gif图片的播放。 请注意在适当的时候要调用destroy方法来释放资源<br>
 * 对gifview的其它使用（如设置大小等），和ImageView一样
 * 
 * @author smartliao
 * 
 */
public class GifFrameView extends View implements IScaleView {

	/** gif解码器 */
	private GifDecoder gifDecoder = null;
	private boolean fullScreen = true;
	private Paint mPaint;
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
		gifDecoder = new GifDecoder();
	}

	public void setMovieResource(int resId) {
		gifDecoder.setGifImage(getContext().getResources(), resId);
		gifDecoder.start();
	}

	public void setFrameList(List<GifFrame> frameList){
		gifDecoder.setFrameList(frameList);
	}

	/**
	 * 以文件形式设置gif图片
	 * 
	 * @param strFileName
	 *            gif图片路径，此图片必须有访问权限
	 */
	public void setMovieResource(String strFileName) {
		gifDecoder.setGifImage(strFileName);
		gifDecoder.start();
	}

	/**
	 * 清理，不使用的时候，调用本方法来释放资源<br>
	 * <strong>强烈建议在退出或者不需要gif动画时，调用本方法</strong>
	 */
	public void destroy() {
		stopDecodeThread();
		gifDecoder.destroy();
		gifDecoder = null;
	}

	/**
	 * 中断解码线程
	 */
	private void stopDecodeThread() {
		if (gifDecoder != null && gifDecoder.getState() != Thread.State.TERMINATED) {
			gifDecoder.interrupt();
			gifDecoder.destroy();
		}
	}


	/** 显示gift图片的动态效果的开始时间 */
	private long mMovieStart;
	/** 动态图当前显示第几帧 */
	private int mCurrentAnimationTime = 0;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		updateAnimationTime();
		int index = gifDecoder.getFrameIndex(mCurrentAnimationTime);
		if (index == recordIndex){
			recordCurrentData = false;
		}else{
			recordIndex = index;
			recordCurrentData = true;
		}
		GifFrame gifFrame = gifDecoder.getFrame(mCurrentAnimationTime);
		if (gifFrame != null){
			if (gifFrame.image != null && gifFrame.image.isRecycled() == false) {
				if (fullScreen){
					canvas.drawBitmap(gifFrame.image, new Rect(0, 0, gifFrame.image.getWidth(), gifFrame.image.getHeight()), new Rect(0, 0, getWidth(), getHeight()), mPaint);
				}else{
					canvas.drawBitmap(gifFrame.image, 0, 0, mPaint);
				}
			}
		}
	}

	/**
	 * 更新当前显示进度
	 */
	private void updateAnimationTime() {
		long now = android.os.SystemClock.uptimeMillis();
		// 如果第一帧，记录起始时间
		if (mMovieStart == 0) {
			mMovieStart = now;
		}
		// 取出动画的时长
		int dur = gifDecoder.getDuration();
		if (dur == 0) {
			dur = 3000;
		}
		// 算出需要显示第几帧
		mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
	}

	@Override
	public int getShowWidth(int width) {
		return width;
	}

	@Override
	public int getShowHeight(int height) {
		return height;
	}

	@Override
	public int getDuration() {
		return gifDecoder.getDuration();
	}

	@Override
	public void startRecord() {
		recordCurrentData = true;
		mMovieStart = android.os.SystemClock.uptimeMillis();
	}

	int recordIndex = -1 ;
	boolean recordCurrentData = false;

	@Override
	public boolean recordOrNot() {
		return recordCurrentData;
	}
}
