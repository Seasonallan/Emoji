package com.view.gif.frame;

import android.graphics.Bitmap;

public class GifFrame{
	public GifFrame(Bitmap im, int del) {
		image = im;
		delay = del;
	}

	public Bitmap image;
	public int delay;

	public void release(){
		if (image != null && !image.isRecycled()){
			image.recycle();
			image = null;
		}
	}

}
