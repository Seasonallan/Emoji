package com.march.gifmaker;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.march.gifmaker.extend.LZWEncoderOrderHolder;
import com.march.gifmaker.extend.ThreadGifEncoder;
import com.march.gifmaker.utils.Util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * CreateAt : 7/12/17
 * Describe :
 * 多线程合成Gif每一帧，最后合并结果，加快合成速度
 *
 * @author chendong
 */
public class GifMaker {

    public static final String TAG = GifMaker.class.getSimpleName();

    private ByteArrayOutputStream       mFinalOutputStream; // 最终合并输出流
    private List<LZWEncoderOrderHolder> mEncodeOrders; // 存放线程处理结果，待全部线程执行完使用
    public String                      mOutputPath;// GIF 保存路径
    private Handler                     mHandler; // 回调回主线程使用
    private ExecutorService             mExecutor;  // 线程池

    private long mStartWorkTimeStamp; // 开始时间
    private int  mCurrentWorkSize; // 当前剩余任务长度
    private int  mTotalWorkSize; // 总任务长度
    private int  mDelayTime; // 每帧延时

    private OnGifMakerListener mOnGifMakerListener;

    public interface OnGifMakerListener {
        void onMakeGifSucceed(String outPath);
    }

    public GifMaker(int duration, int delayTime) {
        this(duration, delayTime, Executors.newCachedThreadPool());
    }

    public boolean isGifMaded = false;
    public GifMaker(int duration, int delayTime, ExecutorService executor) {

        mFinalOutputStream = new ByteArrayOutputStream();
        mEncodeOrders = new ArrayList<>();
        mExecutor = executor;
        mDelayTime = delayTime;
        this.mTotalWorkSize = duration/mDelayTime;
        this.mCurrentWorkSize = mTotalWorkSize;

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(final Message msg) {
                isGifMaded = true;
                if (msg.what == 100 && mOnGifMakerListener != null) {
                    mOnGifMakerListener.onMakeGifSucceed(mOutputPath);
                }
                super.handleMessage(msg);
            }
        };
    }

    public GifMaker setOutputPath(String outputPath){
        this.mStartWorkTimeStamp = System.currentTimeMillis();
        this.mOutputPath = outputPath;
        return this;
    }

    public void setGifMakerListener(OnGifMakerListener listener){
        this.mOnGifMakerListener = listener;
    }

    public void reset(){
        isGifMaded =false;
        mExecutor.shutdownNow();
    }

    public void finish(){
        isFinish = true;
    }

    private boolean isFinish = false;
    private int id = 0;
    private int runningCount = 0;

    public boolean isBitmapFull(){
        return id > mTotalWorkSize;
    }
    /***
     * 添加一个图片
     */
    public void addBitmap(Bitmap bitmap) {
        runningCount ++ ;
        if (id > mTotalWorkSize){
            return;
        }
        mExecutor.execute(new EncodeGifRunnable(bitmap, id++));
    }

    /**
     * 编码一帧
     */
    private class EncodeGifRunnable implements Runnable {

        int                   mOrder; // 当前顺序
        Bitmap                mBitmap; // 当前位图
        ThreadGifEncoder      mThreadGifEncoder; // 当前编码器
        ByteArrayOutputStream mCurrentOutputStream; // 当前数据输出流

        EncodeGifRunnable(Bitmap bitmap, int order) {
            mCurrentOutputStream = new ByteArrayOutputStream();
            mThreadGifEncoder = new ThreadGifEncoder();
            mThreadGifEncoder.setQuality(100);
            mThreadGifEncoder.setDelay(mDelayTime);
            mThreadGifEncoder.start(mCurrentOutputStream, order);
            mThreadGifEncoder.setFirstFrame(order == 0);
            mThreadGifEncoder.setRepeat(0);
            mBitmap = bitmap;
            mOrder = order;
        }

        @Override
        public void run() {
            try {
                Log.e(TAG, "开始编码第" + mOrder + "张");
                LZWEncoderOrderHolder holder = mThreadGifEncoder.addFrame(mBitmap, mOrder);
                boolean isLast = mOrder == (mTotalWorkSize - 1);
                if (isFinish){
                    isLast = mOrder == id - 1;
                }
                mThreadGifEncoder.finishThread(isLast, holder.getLZWEncoder());
                holder.setByteArrayOutputStream(mCurrentOutputStream);
                mEncodeOrders.add(holder);
                logCostTime(mOrder, mBitmap);
                Util.recycleBitmaps(mBitmap);
                workDone();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // 完成一帧
    private synchronized void workDone() throws IOException {
        mCurrentWorkSize--;
        runningCount --;
        if (mCurrentWorkSize == 0 || (isFinish && runningCount == 0)) {
            //排序 默认从小到大
            Collections.sort(mEncodeOrders);
            for (int i = 0; i< mEncodeOrders.size(); i++){
                mFinalOutputStream.write(mEncodeOrders.get(i).getByteArrayOutputStream().toByteArray());
            }
            // mFinalOutputStream.write(0x3b); // gif traile
            byte[] data = mFinalOutputStream.toByteArray();
            File file = new File(mOutputPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            BufferedOutputStream bosToFile = new BufferedOutputStream(new FileOutputStream(file));
            bosToFile.write(data);
            bosToFile.flush();
            bosToFile.close();
            logCostTime(-1, null);
            mHandler.sendEmptyMessage(100);
        }
    }


    private void logCostTime(int order, Bitmap bitmap) {
        long currentTimeMillis = System.currentTimeMillis();
        long timeCost = currentTimeMillis - mStartWorkTimeStamp;
        String msg = String.format(Locale.CHINA, "%d.%d s", timeCost / 1000, timeCost % 1000);

        Log.i(TAG, (order == -1 ? "合成完成" : "完成第" + order + "帧") + ",耗时:" + msg + (bitmap == null ? "" : (" - bitmap [" + bitmap.getWidth() + "," + bitmap.getHeight() + "]")));
    }

}
