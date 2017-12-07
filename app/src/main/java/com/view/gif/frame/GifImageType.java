package com.view.gif.frame;

public enum GifImageType {

	WAIT_FINISH(0),
	SYNC_DECODER(1),
	COVER(2);

	GifImageType(int i) {
		nativeInt = i;
	}

	final int nativeInt;
}
