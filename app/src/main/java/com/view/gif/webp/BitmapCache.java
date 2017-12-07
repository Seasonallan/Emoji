package com.view.gif.webp;

import android.graphics.Bitmap;
import android.graphics.Movie;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/1.
 */

public class BitmapCache {


    private List<Bitmap> bitmaps;

    public BitmapCache(){
        bitmaps = new ArrayList<>();
    }

    public void decodeFile(String filePath) throws FileNotFoundException {
        bitmaps = decodeAllFrames();
    }

    /**
     * @return byte[]
     */
    private byte[] getGiftBytes(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len;
        try {
            while ((len = is.read(b, 0, 1024)) != -1) {
                baos.write(b, 0, len);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return baos.toByteArray();
    }

    public List<Bitmap> decodeAllFrames() {

        List<Bitmap> bitmaps =
                new ArrayList<>();

        return bitmaps;
    }


    Movie mMovie;
    public int getWidth() {
        return mMovie.width();
    }

    public int getHeight() {
        return mMovie.height();
    }

    public int getDuration() {
        return mMovie.duration();
    }

    public int getDelay() {
        return mMovie.duration()/getFrameCount();
    }

    public int getFrameCount() {
        return bitmaps.size();
        //return mMovie.getFrameCount();
    }

    public void release() {
        if (bitmaps != null && bitmaps.size() > 0){

        }
    }

    public int size() {
        return bitmaps.size();
    }

    public Bitmap get(int index) {
        return bitmaps.get(index);
    }
}
