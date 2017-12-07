package com.march.gifmaker;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

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
import java.util.concurrent.ExecutorService;

/**
 * CreateAt : 7/12/17
 * Describe :
 *
 * @author chendong
 */
public class GifMaker
{

    public static final String TAG = GifMaker.class.getSimpleName();

    private ByteArrayOutputStream mFinalOutputStream;
    private List<LZWEncoderOrderHolder> mEncodeOrders;
    private LZWEncoderOrderHolder mStartEncoder, mEndEncoder;
    public String mOutputPath;
    private Handler mHandler;
    private ExecutorService mExecutor;

    private int mCurrentWorkSize;
    private int mTotalWorkSize;
    private int mDelayTime;

    public OnGifMakerListener mOnGifMakerListener;

    public interface OnGifMakerListener
    {
        void onMakeProgress(int index, int count);
        void onMakeGifSucceed(String outPath);
        void onMakeGifFail();
    }

    public boolean isGifMaded = false;
    public boolean isLowerDivice = false;
    public GifMaker(int count, int delayTime, ExecutorService executor) {
        isLowerDivice = false;
        mFinalOutputStream = new ByteArrayOutputStream();
        mEncodeOrders = new ArrayList<>();
        mExecutor = executor;
        mDelayTime = delayTime;
        this.mTotalWorkSize = count;
        this.mCurrentWorkSize = mTotalWorkSize;

        mHandler = new Handler(Looper.getMainLooper())
        {
            @Override
            public void handleMessage(final Message msg) {
                isGifMaded = true;
                if (msg.what == 200 && mOnGifMakerListener != null) {
                    mOnGifMakerListener.onMakeGifSucceed(mOutputPath);
                }else if (msg.what == 404 && mOnGifMakerListener != null) {
                    mOnGifMakerListener.onMakeGifFail();
                    mExecutor.shutdownNow();
                }else if (mOnGifMakerListener != null) {
                    mOnGifMakerListener.onMakeProgress(msg.what, getTotalSize());
                }
                super.handleMessage(msg);
            }
        };
    }

    public GifMaker setOutputPath(String outputPath) {
        this.mOutputPath = outputPath;
        return this;
    }

    public void setGifMakerListener(OnGifMakerListener listener) {
        this.mOnGifMakerListener = listener;
    }

    public void reset() {
        isGifMaded = false;
        mExecutor.shutdownNow();
    }

    private int id = 0;

    public boolean isBitmapFull() {
        if (id * repeatCount >= mTotalWorkSize){
            return true;
        }
        return false;
    }

    public int getTotalSize(){
        return mTotalWorkSize;
    }

    public int getFrameCountNow() {
        return id;
    }

    public int getDelay(){
        return mDelayTime;
    }

    /***
     */
    public void addBitmap(Bitmap bitmap) {
        if (id >= mTotalWorkSize) {
            return;
        }
        mExecutor.execute(new EncodeGifRunnable(bitmap, id++));
        mHandler.sendEmptyMessage(id);
    }

    int repeatCount = 1;
    public void setRepeatCount(int count){
        this.repeatCount = count;
        if (mCurrentWorkSize%repeatCount != 0){
            this.mCurrentWorkSize = mCurrentWorkSize/repeatCount + 1;
        }else{
            this.mCurrentWorkSize = mCurrentWorkSize/repeatCount;
        }
    }

    /**
     */
    private class EncodeGifRunnable implements Runnable
    {

        int mOrder;
        Bitmap mBitmap;

        EncodeGifRunnable(Bitmap bitmap, int order) {
            mBitmap = bitmap;
            mOrder = order;
        }

        @Override
        public void run() {
            try {
                if (repeatCount <= 1){
                    ByteArrayOutputStream currentStream = new ByteArrayOutputStream();
                    ThreadGifEncoder encoder = new ThreadGifEncoder();
                    encoder.setQuality(isLowerDivice?10:1);
                    encoder.setDelay(mDelayTime);
                    encoder.start(currentStream, mOrder);
                    encoder.setFirstFrame(mOrder == 0);
                    encoder.setRepeat(0);
                    LZWEncoderOrderHolder holder = encoder.addFrame(mBitmap, mOrder);
                    encoder.finishThread(mOrder == (mTotalWorkSize - 1), holder.getLZWEncoder());
                    holder.setByteArrayOutputStream(currentStream);
                    mEncodeOrders.add(holder);
                }else{
                    if (mOrder == 0){
                        ByteArrayOutputStream startStream = new ByteArrayOutputStream();
                        ThreadGifEncoder encoder = new ThreadGifEncoder();
                        encoder.setQuality(isLowerDivice?10:1);
                        encoder.setDelay(mDelayTime);
                        encoder.start(startStream, mOrder);
                        encoder.setFirstFrame(mOrder == 0);
                        encoder.setRepeat(0);
                        mStartEncoder = encoder.addFrame(mBitmap, mOrder);
                        encoder.finishThread(false, mStartEncoder.getLZWEncoder());
                        mStartEncoder.setByteArrayOutputStream(startStream);
                    }else if ((mOrder + 1) * repeatCount >= mTotalWorkSize){
                        ByteArrayOutputStream endStream = new ByteArrayOutputStream();
                        ThreadGifEncoder encoder = new ThreadGifEncoder();
                        encoder.setQuality(isLowerDivice?10:1);
                        encoder.setDelay(mDelayTime);
                        encoder.start(endStream, mOrder);
                        encoder.setFirstFrame(mOrder == 0);
                        encoder.setRepeat(0);
                        mEndEncoder = encoder.addFrame(mBitmap, mOrder);
                        encoder.finishThread(true, mEndEncoder.getLZWEncoder());
                        mEndEncoder.setByteArrayOutputStream(endStream);
                    }
                    ByteArrayOutputStream currentStream = new ByteArrayOutputStream();
                    ThreadGifEncoder encoder = new ThreadGifEncoder();
                    encoder.setQuality(isLowerDivice?10:1);
                    encoder.setDelay(mDelayTime);
                    encoder.start(currentStream, 1);
                    encoder.setFirstFrame(false);
                    encoder.setRepeat(0);

                    LZWEncoderOrderHolder holder = encoder.addFrame(mBitmap, mOrder);
                    encoder.finishThread(false, holder.getLZWEncoder());
                    holder.setByteArrayOutputStream(currentStream);

                    mEncodeOrders.add(holder);
                }
                Util.recycleBitmaps(mBitmap);
                workDone();
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(404);
            }
        }

    }

    private synchronized void workDone() throws IOException {
        mCurrentWorkSize--;
        if (mCurrentWorkSize == 0) {
            Collections.sort(mEncodeOrders);
            if (repeatCount <= 1){
                for (int i = 0; i < mEncodeOrders.size(); i++) {
                    mFinalOutputStream.write(mEncodeOrders.get(i).getByteArrayOutputStream().toByteArray());
                }
            }else{
                for (int index = 0; index < repeatCount; index ++){
                    if(index == 0){
                        mFinalOutputStream.write(mStartEncoder.getByteArrayOutputStream().toByteArray());
                        for (int i = 1; i < mEncodeOrders.size(); i++) {
                            mFinalOutputStream.write(mEncodeOrders.get(i).getByteArrayOutputStream().toByteArray());
                        }
                    }else if (index == repeatCount - 1){
                        for (int i = 0; i < mEncodeOrders.size() - 1; i++) {
                            mFinalOutputStream.write(mEncodeOrders.get(i).getByteArrayOutputStream().toByteArray());
                        }
                        mFinalOutputStream.write(mEndEncoder.getByteArrayOutputStream().toByteArray());
                    }else{
                        for (int i = 0; i < mEncodeOrders.size(); i++) {
                            mFinalOutputStream.write(mEncodeOrders.get(i).getByteArrayOutputStream().toByteArray());
                        }
                    }
                }
            }

            byte[] data = mFinalOutputStream.toByteArray();
            File file = new File(mOutputPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            BufferedOutputStream bosToFile = new BufferedOutputStream(new FileOutputStream(file));
            bosToFile.write(data);
            bosToFile.flush();
            bosToFile.close();
            mHandler.sendEmptyMessage(200);
        }
    }

}
