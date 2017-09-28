package com.season.emoji.ui.view.gif.movie;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;

import com.season.emoji.ui.view.gif.frame.GifFrame;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author liao
 * @hide
 */
public class DelayDecoder {

    public static int getDelay(Resources res, int resId) {
        int delay = 100;
        try {
            InputStream mInputStream = res.openRawResource(resId);
            boolean done = false;
            while (!done) {
                int code = mInputStream.read();
                switch (code) {
                    case 0x2C: // image separator
                        break;
                    case 0x21: // extension
                        code = mInputStream.read();
                        switch (code) {
                            case 0xf9: // graphics control extension
                                mInputStream.read(); // block size
                                mInputStream.read(); // packed fields

                                int s = mInputStream.read();
                                int f = mInputStream.read();
                                int t = s | (f << 8);

                                delay = t * 10;
                                if (delay == 0) {
                                    delay = 100;
                                }
                                done = true;
                                break;
                            default: // uninteresting extension
                        }
                        break;
                    case 0x3b: // terminator
                        done = true;
                        break;
                    case 0x00: // bad byte, but keep going and see what happens
                        break;
                    default:
                        break;
                }
            }
            if (null != mInputStream)
                mInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return delay;
    }

}
