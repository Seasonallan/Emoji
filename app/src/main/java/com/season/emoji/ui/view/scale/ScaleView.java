package com.season.emoji.ui.view.scale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.season.emoji.ui.view.ContainerView;
import com.season.emoji.util.LogUtil;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-09-26 21:44
 */
public class ScaleView extends RelativeLayout {


    public ScaleView(Context context) {
        super(context);
        init();
    }

    public ScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int index = -1;
    public void setBackground(){
        index = 0;
    }

    private ScaleDetector mScaleDetector;
    private GestureDetector mGestureDetector;

    private float mCurrentScale = 1f;
    private float MIN_SCALE = Integer.MIN_VALUE;
    public Matrix mCurrentMatrix;
    private float mMidX;
    private float mMidY;
    private OnClickListener mClickListener;

    /**
     *获取两条线的夹角
     * @param centerX
     * @param centerY
     * @param xInView
     * @param yInView
     * @return
     */
    public static float getRotationBetweenLines(float centerX, float centerY, float xInView, float yInView) {
        float rotation = 0;

        float k1 =   (centerY - centerY) / (centerX * 2 - centerX);
        float k2 =   (yInView - centerY) / (xInView - centerX);
        float tmpDegree = (float) (Math.atan((Math.abs(k1 - k2)) / (1 + k1 * k2)) / Math.PI * 180);

        if (xInView > centerX && yInView < centerY) {  //第一象限
            rotation = 90 - tmpDegree;
        } else if (xInView > centerX && yInView > centerY) //第二象限
        {
            rotation = 90 + tmpDegree;
        } else if (xInView < centerX && yInView > centerY) { //第三象限
            rotation = 270 - tmpDegree;
        } else if (xInView < centerX && yInView < centerY) { //第四象限
            rotation = 270 + tmpDegree;
        } else if (xInView == centerX && yInView < centerY) {
            rotation = 0;
        } else if (xInView == centerX && yInView > centerY) {
            rotation = 180;
        }

        return rotation;
    }


    boolean isRandomed = false;
    public void randomPosition(int width, int height){
        if (isRandomed){
            return;
        }
        isRandomed = true;
        if (width > 0){
            width = width/2;
            height = height/2;
            if (getChildCount() > 0){
                View child = getChildAt(0);
                if (child instanceof IScaleView){
                    width = width - ((IScaleView) child).getShowWidth(width * 2)/2;
                    height = height - ((IScaleView) child).getShowHeight(height * 2)/2;

                }else{
                    width = width - child.getMeasuredWidth()/2;
                    height = height - child.getMeasuredHeight()/2;
                }
            }
            mCurrentMatrix.postTranslate(width, height);
        }
        ((ContainerView)getParent()).addEvent(ContainerView.IType.ADD, this, mCurrentMatrix);
        invalidate();
    }

    public void resetMatrix(float[] matrix){
        mCurrentMatrix.setValues(matrix);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            if (ev.getPointerCount() == 1){
                if (getChildCount() > 0){
                    View child = getChildAt(0);
                    Rect rect = new Rect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
                    //canvas.drawRect(rect, paint);
                    isFocus =  isTouchPointInView(child, (int)ev.getX(), (int) ev.getY());
                    if (isFocus){
                        ((ContainerView)getParent()).startOp();
                    }
                }
            }
        }
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            isFocus = false;
            ((ContainerView)getParent()).addEvent(ContainerView.IType.OP, this, mCurrentMatrix);
            invalidate();
        }
        mScaleDetector.onTouchEvent(ev);
        if (!mScaleDetector.isInProgress()) {
            mGestureDetector.onTouchEvent(ev);
        }
        return isFocus;
    }

    boolean isFocus = false;


    //(x,y)是否在view的区域内
    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        float[] leftTop = {view.getLeft(), view.getTop()};
        mCurrentMatrix.mapPoints(leftTop);

        float[] rightTop = {view.getRight(), view.getTop()};
        mCurrentMatrix.mapPoints(rightTop);

        float[] leftBottom = {view.getLeft(), view.getBottom()};
        mCurrentMatrix.mapPoints(leftBottom);

        float[] rightBottom = {view.getRight(), view.getBottom()};
        mCurrentMatrix.mapPoints(rightBottom);


        Path path = new Path();
        path.moveTo(leftTop[0], leftTop[1]);
        // 连接路径到点
        path.lineTo(rightTop[0], rightTop[1]);
        path.lineTo(rightBottom[0], rightBottom[1]);
        path.lineTo(leftBottom[0], leftBottom[1]);
        // 闭合曲线
        path.close();

        RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        Region region = new Region();
        region.setPath(path, new Region((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom));

        LogUtil.d("x=" + x + "         y=" + y);
        LogUtil.d("leftTop="+leftTop[0] +","+leftTop[1]+"         rightTop="+rightTop[0] +","+rightTop[1]+"         leftBottom="+leftBottom[0] +","+leftBottom[1]+"         rightBottom="+rightBottom[0] +","+rightBottom[1]);

        if (region.contains(x, y)) {
            LogUtil.d("in");
            return true;
        }
        LogUtil.d("out of view");
        return false;
    }

    private Paint paint  = new Paint() ;
    @Override
    protected void dispatchDraw(Canvas canvas) {
        int saveCount = canvas.save();
        canvas.concat(mCurrentMatrix);

        super.dispatchDraw(canvas);

        if (getChildCount() > 0 && isFocus){
            View child = getChildAt(0);
//            if (child instanceof IScaleView){
//                Rect rect = new Rect(child.getLeft(), child.getTop(), child.getLeft() + ((IScaleView) child).getShowWidth(getRight() - getLeft()), ((IScaleView) child).getShowHeight(getBottom() - getTop()));
//                canvas.drawRect(rect, paint);
//            }
            Rect rect = new Rect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
            canvas.drawRect(rect, paint);
        }

        canvas.restoreToCount(saveCount);
    }

    private void init() {
        //加粗
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        paint.setStyle(Paint.Style.STROKE);
        // 消除锯齿
        paint.setAntiAlias(true);
        // 设置画笔的颜色
        paint.setColor(Color.RED);
        // 设置paint的外框宽度
        paint.setStrokeWidth(2);

        mCurrentMatrix = new Matrix();
        ScaleDetector.OnScaleGestureListener scaleListener = new ScaleDetector
                .SimpleOnScaleGestureListener() {

            @Override
            public boolean onScale(ScaleDetector detector) {



                float preDegree = getRotationBetweenLines(detector.preX2, detector.preY2, detector.preX1, detector.preY1);
                float newDegree = getRotationBetweenLines(detector.currentX2, detector.currentY2, detector.currentX1, detector.currentY1);

                float degree = newDegree - preDegree;
                mCurrentMatrix.postRotate(degree, (detector.preX2 + detector.preX1) / 2, (detector.preY2 + detector.preY1) / 2);
              //  LogUtil.d(preDegree + ",,,,," + newDegree + "      =    " + degree);

                float scaleFactor = detector.getScaleFactor();

                mCurrentScale *= scaleFactor;
                if (mMidX == 0f) {
                    mMidX = getWidth() / 2f;
                }
                if (mMidY == 0f) {
                    mMidY = getHeight() / 2f;
                }
                mCurrentMatrix.postScale(scaleFactor, scaleFactor, mMidX, mMidY);
                invalidate();

                return true;
            }

            @Override
            public void onScaleEnd(ScaleDetector detector) {
                super.onScaleEnd(detector);

                if (mCurrentScale < MIN_SCALE) {
                    reset();
                }
               // checkBorder();
            }
        };
        mScaleDetector = new ScaleDetector(getContext(), scaleListener);

        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mClickListener != null) {
                    mClickListener.onClick();
                }
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (mCurrentScale > MIN_SCALE) {
                    mCurrentMatrix.postTranslate(-distanceX, -distanceY);
                    invalidate();
                    //checkBorder();
                }
                return true;
            }
        };
        mGestureDetector = new GestureDetector(getContext(), gestureListener);
    }

    /**
     * 检查图片边界是否移到view以内
     * 目的是让图片边缘不要移动到view里面
     */
    private void checkBorder() {
        RectF rectF = getDisplayRect(mCurrentMatrix);
        boolean reset = false;
        float dx = 0;
        float dy = 0;

        if (rectF.left > 0) {
            dx = getLeft() - rectF.left;
            reset = true;
        }
        if (rectF.top > 0) {
            dy = getTop() - rectF.top;
            reset = true;
        }
        if (rectF.right < getRight()) {
            dx = getRight() - rectF.right;
            reset = true;
        }
        if (rectF.bottom < getHeight()) {
            dy = getHeight() - rectF.bottom;
            reset = true;
        }
        if (reset) {
            mCurrentMatrix.postTranslate(dx, dy);
            invalidate();
        }
    }

    /**
     * Helper method that maps the supplied Matrix to the current Drawable
     *
     * @param matrix - Matrix to map Drawable against
     * @return RectF - Displayed Rectangle
     */
    private RectF getDisplayRect(Matrix matrix) {
        RectF rectF = new RectF(getLeft(), getTop(), getRight(), getBottom());
        matrix.mapRect(rectF);
        return rectF;
    }

    /**
     * Resets the zoom of the attached image.
     * This has no effect if the image has been destroyed
     */
    private void reset() {
        mCurrentMatrix.reset();
        mCurrentScale = 1f;
        invalidate();
    }

    public interface OnClickListener {
        void onClick();
    }

    public void setOnClickListener(OnClickListener listener) {
        mClickListener = listener;
    }
}
