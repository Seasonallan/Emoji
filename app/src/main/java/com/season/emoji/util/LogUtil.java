package com.season.emoji.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 *
 * 
 */
public class LogUtil {

	/**
	 *
	 * @param t
	 * @param id
	 * @param _this
	 * @return
	 */
	private static Context mContext;

	private static String logFilterRegex = ".*";
	private static String charset = "UTF-8";
	//
	private static Handler handler;
	private static int logLength = 7;
	private static boolean isDebug = true;

	public static void setCharset(String charset) {
		LogUtil.charset = charset;
	}

	public static void setLogFilterRegex(String logFilterRegex) {
		LogUtil.logFilterRegex = logFilterRegex;
	}

	public static boolean isNull(EditText et) {

		if (et.getText().toString() == null) {
			return true;
		}
		if (et.getText().toString().trim().equals("")) {
			return true;
		}
		return false;
	}

	public static void v() {
		LogUtil.performV(" ", 3);
	}

	public static void v(Object... values) {
		String str = getLogValues(getLogLength(), values);
		LogUtil.performV(str, 3);
	}

	public static void performV(Object msg, int i) {
		if (!isDebug) {
			return;
		}
		String tag = createLogTag(i);

		if (!isPass(tag)) {
			return;
		}
		if (msg != null) {
			Log.v(tag, msg.toString());
		} else {
			Log.v(tag, "null");
		}
	}

	public static void d() {
		LogUtil.performD(" ", 3);
	}

	public static void d(Object... values) {
		String str = getLogValues(getLogLength(), values);
		LogUtil.performD(str, 3);
	}

	public static void performD(Object msg, int i) {
		if (!isDebug) {
			return;
		}
		String tag = createLogTag(i);

		if (!isPass(tag)) {
			return;
		}
		if (msg != null) {
			Log.d(tag, msg.toString());
		} else {
			Log.d(tag, "null");
		}
	}

	public static void i() {
		LogUtil.performI(" ", 3);
	}

	public static void i(Object... msg) {

		String str = getLogValues(getLogLength(), msg);
		LogUtil.performI(str, 3);
	}

	public static void performI(Object msg, int i) {

		if (!isDebug()) {
			return;
		}
		String tag = createLogTag(i);
		if (!isPass(tag)) {
			return;
		}
		if (msg != null) {
			Log.i(tag, msg.toString());
		} else {
			Log.i(tag, "null");
		}
	}

	public static void w() {

		LogUtil.performW(" ", 3);
	}

	public static void w(Object... msg) {

		String str = getLogValues(getLogLength(), msg);
		LogUtil.performW(str, 3);
	}

	public static void performW(Object msg, int i) {

		if (!isDebug) {
			return;
		}
		String tag = createLogTag(i);

		if (!isPass(tag)) {
			return;
		}
		if (msg != null) {

			Log.w(tag, msg.toString());
		} else {
			Log.w(tag, "null");
		}
	}

	public static void e() {
		LogUtil.performE(" ", 3);
	}

	public static void e(Object... msg) {
		String str = getLogValues(getLogLength(), msg);
		LogUtil.performE(str, 3);
	}

	public static void performE(Object msg, int i) {

		if (!isDebug) {
			return;
		}
		String tag = createLogTag(i);

		if (!isPass(tag)) {
			return;
		}
		if (msg != null) {

			Log.e(tag, msg.toString());
		} else {
			Log.e(tag, "null");
		}
	}

	public static void log(Object msg, int i) {

		if (!isDebug) {
			return;
		}
		String tag = createLogTag(i);

		if (!isPass(tag)) {
			return;
		}
		if (msg != null) {

			Log.e(tag, msg.toString());
		} else {
			Log.e(tag, "null");
		}
	}

	private static boolean isPass(String tag) {
		if (tag.matches(logFilterRegex)) {
			return true;
		}
		return false;
	}

	public static void log(Object msg) {
		log(msg, 3);
	}

	public static void log() {
		log("   ", 3);
	}

	public static String createLogTag(int stackCount) {
		StackTraceElement stack[] = new Throwable().getStackTrace();
		StackTraceElement stackMsg = stack[stackCount];
		String className = stackMsg.getClassName();
		className = className.substring(className.lastIndexOf(".") + 1);
		String name = Thread.currentThread().getName();
		if (name.length() > 15) {
			name = name.substring(0, 15);
		}
		String threadTag = "[" + name + "] ";
		return "Y->" + threadTag + className + "." + stackMsg.getMethodName()
				+ " line: " + stackMsg.getLineNumber();
	}
	public static void logThreadInfo() {
		String info = Thread.currentThread().getId() + " : "
				+ Thread.currentThread().getName();
		LogUtil.log(info, 3);
	}

	/**
	 * @param length
	 * @param values
	 */
	public static void logValues(int length, Object... values) {

		String str = getLogValues(length, values);

		LogUtil.log(str, 3);
	}

	private static String getLogValues(int length, Object... values) {
		StringBuilder mainBuilder = new StringBuilder();

		for (Object object : values) {

			if (object == null) {
				object = "null";
			}

			StringBuilder sb = new StringBuilder(object.toString());

			while (sb.length() < length) {
				sb.append(" ");
			}

			sb.append("| ");

			mainBuilder.append(sb);
		}
		return mainBuilder.toString();
	}

	/**
	 *
	 * @param e
	 */
	public static void logError(Exception e) {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stackTrace = e.getStackTrace();
		sb.append(">>>>>>>>>>      " + e.toString() + " at :     <<<<<<<<<<"
				+ "\n");
		for (int i = 0; i < stackTrace.length; i++) {
			if (i < 10) {
				StackTraceElement stackTraceElement = stackTrace[i];
				String errorMsg = stackTraceElement.toString();
				sb.append(errorMsg).append("\n");
			} else {
				sb.append("more : " + (stackTrace.length - 10) + "..." + "\n");
				break;
			}
		}
		sb.append(">>>>>>>>>>     end of error     <<<<<<<<<<");
		log(sb.toString(), 3);
	}

	/**
	 *
	 * @param e
	 */
	public static void logError(String msg, Exception e) {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stackTrace = e.getStackTrace();
		sb.append(">>>>>>>>>>      " + e.toString() + " at :     <<<<<<<<<<"
				+ "\n");
		for (int i = 0; i < stackTrace.length; i++) {
			if (i < 15) {
				StackTraceElement stackTraceElement = stackTrace[i];
				String errorMsg = stackTraceElement.toString();
				sb.append(errorMsg).append("\n");
			} else {
				sb.append("more : " + (stackTrace.length - 15) + "..." + "\n");
				break;
			}
		}
		sb.append(">>>>>>>>>>     end of error     <<<<<<<<<<");
		log(msg==null?sb.toString(): msg+"---"+sb.toString()  , 4);
	}

	public static void toast(String msg) {

		if (mContext == null) {

			try {
				throw new Exception("234234");

			} catch (Exception e) {

				log(e, 2);
			}
		} else {
			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
		}

	}

	public static Toast result;

	/**
	 * 
	 * @param msg
	 * @param layoutRes
	 */
	public static void toast(String msg, int layoutRes) {

		if (result != null) {
			result.cancel();
		}
		result = new Toast(mContext);
		LayoutInflater inflate = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflate.inflate(layoutRes, null);

		// TextView tv = (TextView) v.findViewById(R.id.textView1);
		TextView tv = getTextView((ViewGroup) v);

		tv.setText(msg);

		result.setView(v);
		result.setGravity(Gravity.BOTTOM, 0, 50);
		result.setDuration(Toast.LENGTH_SHORT);
		result.show();
	}

	public static void toast(String msg, int layoutRes, int x, int y) {

		if (result != null) {
			result.cancel();
		}
		result = new Toast(mContext);
		LayoutInflater inflate = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflate.inflate(layoutRes, null);

		// TextView tv = (TextView) v.findViewById(R.id.textView1);
		TextView tv = getTextView((ViewGroup) v);

		tv.setText(msg);

		result.setView(v);
		result.setGravity(Gravity.BOTTOM, x, y);
		result.setDuration(Toast.LENGTH_SHORT);
		result.show();
	}

	private static TextView getTextView(ViewGroup vg) {

		for (int i = 0; i < vg.getChildCount(); i++) {

			View v = vg.getChildAt(i);
			if (v instanceof TextView) {

				return (TextView) v;

			} else if (v instanceof ViewGroup) {

				return getTextView((ViewGroup) v);
			}

		}
		return null;
	}

	public static void init(Context context) {

		initContext(context.getApplicationContext());

		initHandle();

	}

	public static Context getContext() {

		if (mContext == null) {
			throw new RuntimeException(
					"SDF");
		}
		return mContext;
	}

	/**
	 *
	 * @param context
	 */
	private static void initContext(Context context) {

		if (context == null) {
			return;
		}
		if (mContext != null) {
			return;
		}
		mContext = context;
	}

	public static void runOnUiThread(Runnable r) {
		handler.post(r);
	}

	public static String getCharset() {
		return charset;
	}

	public static boolean isDebug() {
		return isDebug;
	}

	public static void setDebug(boolean isDebug) {
		LogUtil.isDebug = isDebug;
	}

	private static void initHandle() {
		handler = new Handler();
	}

	public static int getLogLength() {
		return logLength;
	}

	public static void setLogLength(int logLength) {
		LogUtil.logLength = logLength;
	}
}
