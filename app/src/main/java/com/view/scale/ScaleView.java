package com.view.scale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.view.ContainerView;
import com.view.LayerImageView;
import com.view.TextStyleView;
import com.view.gif.frame.GifFrameView;
import com.view.gif.movie.GifMovieView;
import com.view.gif.webp.GifWebpView;
import com.view.model.LayerEntity;
import com.view.util.Constant;
import com.view.util.MathUtil;
import com.view.util.ScreenUtils;
import com.view.util.ToolPaint;


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

    public ScaleView(Context context, Handler handler) {
        super(context);
        init(handler);
    }

    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public int index = -1;
    public void setBackground(){
        index = 0;
    }

    private ScaleDetector mScaleDetector;
    private GestureDetector mGestureDetector;

    public Matrix mCurrentMatrix;
    private OnClickListener mClickListener;
    boolean copy = false;

    public void bindMatrix(LayerEntity.ItemArrayBean item, int opViewWidthEx, int offsetX, int offsetY, float width, float height, boolean scale){
//        Logger.d("itemArrayBean:  width=" + width+", height="+height +", sizeW="+ item.getSizeWidth() +"  ,sizeH="+item.getSizeHeight());
        if (height <= 1){
            height = width * 4/3;
        }
        int opViewWidth = ScreenUtils.getScreenWidth();
        float centerX = (float) (item.getCenterX() * opViewWidth / width);
        float centerY = (float) ((item.getCenterY() - (height - width)/2)* opViewWidth / width);
        centerX += offsetX;
        centerY += offsetY;

        if (scale){
            double transX = centerX - item.getSizeWidth() /2;
            double transY = centerY - item.getSizeHeight() /2;
            mCurrentMatrix.postTranslate((float)transX, (float)transY);

            float scaleX = (float) ( item.getXScale() * opViewWidth / width );
            mOpView.isRight = !item.isTurnOverH();
            if (!mOpView.isRight){
                scaleX = -scaleX;
            }
            mCurrentMatrix.postScale(scaleX, (float) ( item.getYScale() * opViewWidth / width ), centerX, centerY);
        }else{
            double transX = centerX - item.getSizeWidth() * (item.getXScale()   * opViewWidth /width) /2;
            double transY = centerY - item.getSizeHeight() * (item.getYScale() * opViewWidth /width) /2;

            //Logger.d("transX="+transX +" ,transY="+ transY);
            mCurrentMatrix.postTranslate((float)transX, (float)transY);
        }

        float degree = (float) (item.getAngle() * 180 / Math.PI);//(float) (rotation * Math.PI / 180);
        if (!mOpView.isRight){
            //degree += 180;
        }
        mCurrentMatrix.postRotate(degree, centerX, centerY);

        copy = true;
    }

    //适配IOS的viewWidth数值， 本来该字段应该是图片的宽高，可是实际IOS存储的图片宽高却是viewwidth * scaleX
    public void bindMatrixImage(LayerEntity.ItemArrayBean item, int opViewWidthEx,  int offsetX, int offsetY,  float width, float height, int imageWidth, int imageHeight){
        if (height <= 1){
            height = width * 4/3;
        }
        int opViewWidth = ScreenUtils.getScreenWidth();
        float viewWidth = (float) (item.getSizeWidth() * item.getXScale() * opViewWidth / width);
        float viewHeight = (float) (item.getSizeHeight() * item.getYScale() * opViewWidth / width);


        float centerX = (float) (item.getCenterX() * opViewWidth / width);
        float centerY = (float) ((item.getCenterY() - (height - width)/2)* opViewWidth / width);
        //TODO PAD需要减掉原来图层宽高差
        centerX += offsetX;
        centerY += offsetY;

        float transX = centerX - imageWidth /2;
        float transY = centerY - imageHeight /2;
        mCurrentMatrix.postTranslate(transX, transY);

        float scaleX = viewWidth/imageWidth*1.0f;
        float scaleY = viewHeight/imageHeight*1.0f;

        mOpView.isRight = !item.isTurnOverH();
        if (!mOpView.isRight){
            scaleX = -scaleX;
        }
        //mOpView.isRight = (item.getXScale() >= 0 ? true:false);
        mCurrentMatrix.postScale(scaleX, scaleY, centerX, centerY);

        float degree = (float) (item.getAngle() * 180 / Math.PI);//(float) (rotation * Math.PI / 180);
        if (!mOpView.isRight){
            //degree += 180;
        }
        mCurrentMatrix.postRotate(degree, centerX, centerY);

        copy = true;
    }


    float maxScale = Integer.MAX_VALUE;
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child instanceof TextStyleView){
            maxScale = ((TextStyleView) child).getMaxScale();
            disableHardWareWhenText2Long((TextStyleView) child);
        }
    }

    float scale = 1;
    public void scaleInit(float scale){
        this.scale = scale;
    }

    public void postScale(float scale, float x, float y){
        this.mCurrentMatrix.postScale(scale, scale, x, y);
        invalidate();
    }

    /**
     * 文字太长的时候禁止硬件加速，防止无法绘制问题
     */
    public void disableHardWareWhenText2Long(TextStyleView view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (view.isText2Long()){
                this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        }
    }

    /**
     */
    public static float getRotationBetweenLines(float centerX, float centerY, float xInView, float yInView) {
        float rotation = 0;

        float k1 =   (centerY - centerY) / (centerX * 2 - centerX);
        float k2 =   (yInView - centerY) / (xInView - centerX);
        float tmpDegree = (float) (Math.atan((Math.abs(k1 - k2)) / (1 + k1 * k2)) / Math.PI * 180);

        if (xInView > centerX && yInView < centerY) {
            rotation = 90 - tmpDegree;
        } else if (xInView > centerX && yInView > centerY)
        {
            rotation = 90 + tmpDegree;
        } else if (xInView < centerX && yInView > centerY) {
            rotation = 270 - tmpDegree;
        } else if (xInView < centerX && yInView < centerY) {
            rotation = 270 + tmpDegree;
        } else if (xInView == centerX && yInView < centerY) {
            rotation = 0;
        } else if (xInView == centerX && yInView > centerY) {
            rotation = 180;
        }

        return rotation;
    }

    boolean isViewOffseted = false;
    public boolean initViewOffset(int parentWidth, int parentHeight){
        if (isViewOffseted){
            return false;
        }
        isViewOffseted = true;
        boolean isTuyaAttach = false;
        if (!copy){
            if (parentWidth > 0){
                int offsetX = parentWidth/2;
                int offsetY = parentHeight/2;
                if (getChildCount() > 0){
                    View child = getChildAt(0);
                    if (child instanceof LayerImageView){
                        if (((LayerImageView) child).changeCenter){
                            offsetX = (int) ((LayerImageView) child).getCenterX();
                            offsetY = (int) ((LayerImageView) child).getCenterY();
                            isTuyaAttach = true;
                        }
                    }
                    if (child instanceof IScaleView){
                        offsetX = offsetX - ((IScaleView) child).getViewWidth()/2;
                        offsetY = offsetY - ((IScaleView) child).getViewHeight()/2;
                    }else{
                        offsetX = offsetX - child.getMeasuredWidth()/2;
                        offsetY = offsetY - child.getMeasuredHeight()/2;
                    }
                }
                mCurrentMatrix.postTranslate(offsetX, offsetY);
                mCurrentMatrix.postScale(scale, scale, parentWidth/2, parentHeight/2);
            }
        }
        isFocusNow = true;
        //mOpView.bindRect(this, mViewMatrix, true);
        if (getParent() instanceof ContainerView){
            ((ContainerView)getParent()).addEvent(ContainerView.IType.ADD, this, mCurrentMatrix);
        }
        invalidate();
        if (isTuyaAttach){
            return false;
        }
        return !copy;
    }

    public boolean isSeekingTo(){
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof IScaleView && ((IScaleView)view).isSeeking()){
                return true;
            }
        }
        return false;
    }

    public void refresh(){
        if (matrixAnimation != null && matrixAnimation.isAnimating()){
            postInvalidate();
        }
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof IScaleView && ((IScaleView) view).getDuration() > 0){
                view.postInvalidate();
            }
        }
    }
    public void record(int time){
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof IScaleView && ((IScaleView) view).getDuration() > 0){
                ((IScaleView)view).recordFrame(time);
            }
        }
    }
    public void recordFinish(){
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof IScaleView){
                ((IScaleView)view).stopRecord();
            }
        }
    }

    MatrixAnimation matrixAnimation;
    public void resetMatrix(float[] matrix){
        matrixAnimation = new MatrixAnimation(mCurrentMatrix, matrix);
        //mCurrentMatrix.setValues(matrix);
        invalidate();
    }

    public void copyMatrix(float[] matrix){
        mCurrentMatrix.setValues(matrix);
        mCurrentMatrix.postTranslate(20, 20);
        invalidate();
    }


    public void showCenter(int width, int height){
        mCurrentMatrix = new Matrix();
        mCurrentMatrix.setTranslate(getWidth()/2 - width/2, getHeight()/2 - height/2);
        invalidate();
    }

    public void showBottomCenter(int width, int height, int offsetY, String text){
        value = text;
        mCurrentMatrix.setTranslate(getWidth()/2 - width/2, getHeight() - height - offsetY);
        int centerX = getWidth()/2;
        int centerY = getHeight() - height - offsetY + height/2;
        if (value == null){
            mCurrentMatrix.postScale(0.6f, 0.6f, centerX, centerY);
        }else{
            float scale = ToolPaint.getDefault().getScale(value.length());
            mCurrentMatrix.postScale(scale, scale, centerX, centerY);
        }
        invalidate();
    }

    public void changeOffset(String text, float x, float y){
        value = text;
        mCurrentMatrix.postTranslate(x, y);
        invalidate();
    }

    public float[] rebindOpView(){
        if (mOpView != null){
            float[] res = mOpView.bindRect(this, mCurrentMatrix, true);
            invalidate();
            return res;
        }
        return null;
    }

    boolean isOpEnable = true;
    public void disableOpView(){
        isOpEnable = false;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (matrixAnimation != null && matrixAnimation.isAnimating()){
            mCurrentMatrix.setValues(matrixAnimation.getValues());
        }
        int saveCount = canvas.save();
        canvas.concat(mCurrentMatrix);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(saveCount);

        if (isOpEnable && getChildCount() > 0 && isFocusNow && value != null){
            mOpView.bindRect(this, mCurrentMatrix);
            mOpView.setPaintColor(0xffeeeeee);
            if (isScale){
                mOpView.draw(canvas, true, false, false);
            }else if (isZoom){
                mOpView.setPaintColor(!isOffsetDegree?0xffeeeeee:0xff00ff00);
                mOpView.draw(canvas, false, true, false);
            }else if(isOpe){
                mOpView.draw(canvas, false, false, false);
            }else{
                mOpView.draw(canvas, true, true, true);
            }
        }
    }

    private float offDegree = 0;
    private boolean isOffsetDegree = false;
    public OpView mOpView;
    public void init() {
        init(null);
    }
    public void init(Handler handler) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
           //this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        setClipChildren(false);
        mOpView = new OpView(getContext());

        mCurrentMatrix = new Matrix();
        ScaleDetector.OnScaleGestureListener scaleListener = new ScaleDetector
                .SimpleOnScaleGestureListener() {

            @Override
            public boolean onScale(ScaleDetector detector) {
                isMatrixChanged = true;
                if (value == null){
                    return true;
                }

                if (center == null){
                    center = new float[]{getChildAt(0).getWidth() / 2f, getChildAt(0).getHeight() / 2f};
                    mCurrentMatrix.mapPoints(center);
                }

                float preDegree = getRotationBetweenLines(detector.preX2, detector.preY2, detector.preX1, detector.preY1);
                float newDegree = getRotationBetweenLines(detector.currentX2, detector.currentY2, detector.currentX1, detector.currentY1);

                float degree = newDegree - preDegree;
              //  mViewMatrix.postRotate(degree, (detector.preX2 + detector.preX1) / 2, (detector.preY2 + detector.preY1) / 2);
                if (Math.abs(degree) < 18){
                    mCurrentMatrix.postRotate(degree, center[0], center[1]);
                }else{
                }

                float scaleFactor = detector.getScaleFactor();
                if (checkScaleOutOfBound(scaleFactor)){
                    mCurrentMatrix.postScale(scaleFactor, scaleFactor, center[0], center[1]);
                    invalidate();
                }
                return true;
            }

            @Override
            public void onScaleEnd(ScaleDetector detector) {
                super.onScaleEnd(detector);
            }
        };
        mScaleDetector = new ScaleDetector(getContext(), scaleListener, handler);

        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mClickListener != null){
                    mClickListener.onDoubleClick(getChildAt(0));
                }
                return super.onDoubleTap(e);

            }
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mClickListener != null){
                    if (simpleTextMode){
                        if (isDelete){
                            mClickListener.onDelete(ScaleView.this);
                        }else{
                            mClickListener.onClick(getChildAt(0));
                        }
                        //    isDelete = false;
                        return true;
                    }
                    if (isFocusBefore){
                        if (isDelete){
                            mClickListener.onDelete(ScaleView.this);
                        }else if (mClickListener != null) {
                            mClickListener.onClick(getChildAt(0));
                        }
                    }
                }
              //  isDelete = false;
                return true;
            }


            @Override
            public boolean onScroll(MotionEvent downEvent, MotionEvent currentEvent, float distanceX, float distanceY) {
                isMatrixChanged = true;
                if (simpleTextMode && value == null){
                    return true;
                }
                if (true) {
                    if (isZoom){
                        if (center == null){
                            center = new float[]{getChildAt(0).getWidth() / 2f, getChildAt(0).getHeight() / 2f};
                            mCurrentMatrix.mapPoints(center);
                        }
                        //   mCurrentScale *= scaleFactor;


                        float preDegree = getRotationBetweenLines(center[0], center[1], currentEvent.getX() + distanceX, currentEvent.getY() + distanceY);
                        float newDegree = getRotationBetweenLines(center[0], center[1], currentEvent.getX(), currentEvent.getY());

                        float degree = newDegree - preDegree;
                        float degreeBefore = mOpView.degree;
                        if (degreeBefore < 355 && degreeBefore > 5 || Math.abs(degree) >= 5){
                            offDegree = 0;
                            isOffsetDegree = false;
                            mCurrentMatrix.postRotate(degree, center[0], center[1]);
                        }else{
                            if (isOffsetDegree){
                                offDegree += degree;
                                if (Math.abs(offDegree) >= 5){
                                    mCurrentMatrix.postRotate(offDegree, center[0], center[1]);
                                    offDegree = 0;
                                    isOffsetDegree = false;
                                }
                            }else{
                                if (degreeBefore + degree >= 355 || degreeBefore + degree <= 5){
                                    mCurrentMatrix.postRotate(-degreeBefore, center[0], center[1]);
                                    offDegree = 0;
                                    isOffsetDegree = true;
                                }
                            }
                        }


                        float preDistance = MathUtil.getDistance(center[0], center[1], currentEvent.getX() + distanceX, currentEvent.getY() + distanceY);
                        float newDistance = MathUtil.getDistance(center[0], center[1], currentEvent.getX(), currentEvent.getY());
                      //  float scaleFactor = detector.getScaleFactor();

                        float scaleFactor = newDistance/preDistance;
                        if (checkScaleOutOfBound(scaleFactor)){
                            mCurrentMatrix.postScale(scaleFactor, scaleFactor, center[0], center[1]);
                            invalidate();
                        }
                    }else if (isScale){
                        if (center == null){
                            center = new float[]{getChildAt(0).getWidth() / 2f, getChildAt(0).getHeight() / 2f};
                            mCurrentMatrix.mapPoints(center);
                        }

                        float preDistance = MathUtil.getDistance(center[0], center[1], currentEvent.getX() + distanceX, currentEvent.getY() + distanceY);
                        float newDistance = MathUtil.getDistance(center[0], center[1], currentEvent.getX(), currentEvent.getY());
                        //  float scaleFactor = detector.getScaleFactor();

                        float cad = -1f;
                        if (mOpView.isRight){
                            if (currentEvent.getX() > center[0]){
                                cad = 1f;
                            }else{
                                cad = -1f;
                                mOpView.isRight = false;
                            }
                        }else{
                            if (currentEvent.getX() > center[0]){
                                cad = -1f;
                                mOpView.isRight = true;
                            }else{
                                cad = 1f;
                            }
                        }
                      //  mViewMatrix.postRotate(0);
                        mCurrentMatrix.postRotate(-mOpView.degree, center[0], center[1]);
                        mCurrentMatrix.postScale(newDistance/preDistance * cad, 1, center[0], center[1]);
                        mCurrentMatrix.postRotate(mOpView.degree, center[0], center[1]);
                        invalidate();

                    }else{
                        if (getParent() instanceof ContainerView)
                            ((ContainerView)getParent()).onMove(ScaleView.this, currentEvent);
                        mCurrentMatrix.postTranslate(-distanceX, -distanceY);
                        invalidate();
                    }
                    //checkBorder();
                }
                return true;
            }
        };
        mGestureDetector = new GestureDetector(getContext(), gestureListener, handler);
    }

    private boolean checkScaleOutOfBound(float scaleFactor){
        if (scaleFactor > 1){
            if (mOpView.scale != null && mOpView.scale.length > 0){
                float scale = mOpView.scale[0];
                if (scale >= maxScale){
                    invalidate();
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isMatrixChanged = false;
    public boolean simpleTextMode = false;
    public String value = "ex";
    float[] center;
    boolean isScale = false, isZoom = false, isOpe = false, isDelete = false;
    public boolean isFocusNow = false, isFocusBefore = false;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            boolean canAttach = true;
            if (getParent() instanceof ContainerView) {
                canAttach = !((ContainerView) getParent()).isEventAttaching();
            }
            if (getChildCount() > 0 && canAttach) {
                center = null;
                mOpView.bindRect(this, mCurrentMatrix);
                isFocusBefore = isFocusNow;
                isScale = false;
                isZoom = false;
                isDelete = false;
                isFocusNow = mOpView.isTouched((int) ev.getX(), (int) ev.getY());
                if (isFocusBefore && value != null) {
                    isScale = mOpView.isScaleTouched((int) ev.getX(), (int) ev.getY());
                    isZoom = mOpView.isRotateTouched((int) ev.getX(), (int) ev.getY());
                    isDelete = mOpView.isDeleteTouched((int) ev.getX(), (int) ev.getY());
                    if (isScale || isZoom || isDelete) {
                        isFocusNow = true;
                    }
                }
                if (mOnFocusChangeListener != null) {
                    if (isFocusNow) {
                        mOnFocusChangeListener.onFocusGet(this);
                    } else {
                        mOnFocusChangeListener.onFocusLose(this);
                    }
                }
                isOpe = true;
                invalidate();
            }
            if (getParent() instanceof ContainerView) {
                ((ContainerView) getParent()).startOp(this, isFocusNow, true);
            }
        }
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            isOpe = false;
            isScale = false;
            isZoom = false;
            if (getParent() instanceof ContainerView)
                ((ContainerView)getParent()).onMoveEnd();

            if (ev.getAction() == MotionEvent.ACTION_UP) {
//                if (getChildCount() > 0){
//                    View childView = getChildAt(0);
//                    if (childView instanceof TextStyleView){
//                        TextStyleView textView = (TextStyleView) childView;
//                        int preWidth = textView.getViewWidth();
//                        int changeWidth = preWidth;
//                        if (mOpView.scale != null && mOpView.scale.length > 0){
//                            float scale = mOpView.scale[0];
//                            if (scale >= maxScale * 2/4){
//                                changeWidth = textView.setTextSize(2);
//                            }else{
//                                changeWidth = textView.setTextSize(1);
//                            }
//                            if (center == null){
//                                center = new float[]{getChildAt(0).getWidth() / 2f, getChildAt(0).getHeight() / 2f};
//                                mCurrentMatrix.mapPoints(center);
//                            }
//                            float scaleX = preWidth * 1.0f/changeWidth;
//                            mCurrentMatrix.postScale(scaleX, scaleX, center[0], center[1]);
//                        }
//                    }
//                }
                invalidate();
                if (isInDelRect){
                    if (mClickListener != null){
                        mClickListener.onDelete(ScaleView.this);
                    }
                }else{
                    if (isMatrixChanged){
                        if (getParent() instanceof  ContainerView){
                            ((ContainerView)getParent()).addEvent(ContainerView.IType.MOVE, this, mCurrentMatrix);
                        }
                    }
                }
                isInDelRect = false;
                isMatrixChanged = false;
            }
        }
        if (isScale || isZoom){
            mGestureDetector.onTouchEvent(ev);
        }else{
            mScaleDetector.onTouchEvent(ev);
            if (!mScaleDetector.isInProgress()) {
                mGestureDetector.onTouchEvent(ev);
            }
        }
        return isFocusNow;
    }

    private boolean isInDelRect = false;
    public void setDelPosition(boolean isIn){
        isInDelRect = isIn;
    }


    private ContainerView.IFocusChangeListener mOnFocusChangeListener;
    public void setFocusChangeListener(ContainerView.IFocusChangeListener listener) {
        mOnFocusChangeListener = listener;
    }

    public void removeFocus() {
        isFocusBefore = false;
        isFocusNow = false;
        invalidate();
    }

    public void getFocus() {
        isFocusBefore = true;
        isFocusNow = true;
        isOpe = false;
        isScale = false;
        isZoom = false;
        invalidate();
    }

    public float[] getOffset(){
        return mOpView.center;
    }


    public LayerEntity.ItemArrayBean getItemInfro(int index)
    {
        LayerEntity.ItemArrayBean itemArrayBean = new LayerEntity.ItemArrayBean();
        itemArrayBean.setCenterX(mOpView.center[0]);
        itemArrayBean.setCenterY(mOpView.center[1]);

        float rotation = mOpView.degree;
        if (rotation < 0) {
            rotation += 360;
        }
        rotation = (float) (rotation * Math.PI / 180);
        itemArrayBean.setAngle(rotation);
        itemArrayBean.setIndex(index);

        //IOS适配，只有正数
        itemArrayBean.setXScale(Math.abs(mOpView.scale[0]));
        itemArrayBean.setYScale(mOpView.scale[1]);

        if (mOpView.scale[0] < 0) {
            itemArrayBean.setHMoveBtnPositionType(Constant.ToolViewsType.ButtonPositionTypeLeft);
            itemArrayBean.setTurnOverH(true);
        } else {
            itemArrayBean.setHMoveBtnPositionType(Constant.ToolViewsType.ButtonPositionTypeRight);
            itemArrayBean.setTurnOverH(false);
        }

        View view = getChildAt(0);
        //适配IOS的viewWidth数值， 本来该字段应该是图片的宽高，可是实际IOS存储的图片宽高却是viewwidth * scaleX
        itemArrayBean.setSizeWidth(view.getWidth());
        itemArrayBean.setSizeHeight(view.getHeight());
        if (view instanceof TextStyleView){
            //该字段应该是文字的宽高
            itemArrayBean.setSizeWidth(view.getWidth());
            itemArrayBean.setSizeHeight(view.getHeight());
            itemArrayBean.setContentViewType(Constant.contentViewType.ContentViewTypeTextbox);
            itemArrayBean.setText(((TextStyleView) view).getText());
            itemArrayBean.setTextFontSize(((TextStyleView) view).getTextSize());
            itemArrayBean.setTextStyleModel(((TextStyleView) view).getTextEntry());
            //itemArrayBean.setImageURL();
            if (((TextStyleView) view).fontName == null) {
                itemArrayBean.setTextFontName("");
            } else {
                //            itemArrayBean.setTextFontName(getTextStyle().getFontName());
                itemArrayBean.setTextFontName(((TextStyleView) view).fontName);
                if (!TextUtils.isEmpty(((TextStyleView) view).fontName)) {
//                    List<TextFontUrlEntity> allFontUrl = CommonPrefUtils.getAllFontUrl();
//                    if (allFontUrl != null && allFontUrl.size() != 0) {
//                        for (TextFontUrlEntity entity : allFontUrl) {
//                            boolean equals = entity.getTextfontname().equals(((TextStyleView) view).fontName);
//                            if (equals) {
//                                itemArrayBean.setTextFontPath(entity.getTextfontPath());
//                                return itemArrayBean;
//                            }
//                        }
//                    }
                }
            }
        }else if (view instanceof LayerImageView){
            LayerImageView layerImageView = (LayerImageView) view;
            if (layerImageView.isTuya){
                itemArrayBean.setContentViewType(Constant.contentViewType.ContentViewTypeDraw);
            }else{
                if (TextUtils.isEmpty(layerImageView.url)){
                    itemArrayBean.setContentViewType(Constant.contentViewType.ContentViewTypeLocaImage);
                }else{
                    itemArrayBean.setContentViewType(Constant.contentViewType.ContentViewTypeImage);
                }
            }
            itemArrayBean.filePath = layerImageView.filePath;
            itemArrayBean.setImageURL(layerImageView.url);
        }else if (view instanceof GifMovieView){
            GifMovieView gifMovieView = (GifMovieView) view;
            if (TextUtils.isEmpty(gifMovieView.url)){
                itemArrayBean.setContentViewType(Constant.contentViewType.ContentViewTypeLocaImage);
            }else{
                itemArrayBean.setContentViewType(Constant.contentViewType.ContentViewTypeImage);
            }
            itemArrayBean.filePath = gifMovieView.file;
            itemArrayBean.setImageURL(gifMovieView.url);
        }else if (view instanceof GifWebpView){
            GifWebpView gifMovieView = (GifWebpView) view;
            if (TextUtils.isEmpty(gifMovieView.url)){
                itemArrayBean.setContentViewType(Constant.contentViewType.ContentViewTypeLocaImage);
            }else{
                itemArrayBean.setContentViewType(Constant.contentViewType.ContentViewTypeImage);
            }
            itemArrayBean.filePath = gifMovieView.filePath;
            itemArrayBean.setImageURL(gifMovieView.url);
        }
        return itemArrayBean;
    }

    public ScaleView copy() {
        ScaleView scaleView = new ScaleView(getContext());
        View currentview = getChildAt(0);
        if (currentview instanceof LayerImageView) {
            LayerImageView layerImageView = ((LayerImageView) currentview).copy();
            scaleView.addView(layerImageView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (currentview instanceof TextStyleView) {
            TextStyleView textStyleView = ((TextStyleView) currentview).copy();
            scaleView.addView(textStyleView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (currentview instanceof GifMovieView) {
            GifMovieView textStyleView = ((GifMovieView) currentview).copy();
            scaleView.addView(textStyleView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (currentview instanceof GifFrameView) {
            GifFrameView textStyleView = ((GifFrameView) currentview).copy();
            scaleView.addView(textStyleView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (currentview instanceof GifWebpView) {
            GifWebpView textStyleView = ((GifWebpView) currentview).copy();
            scaleView.addView(textStyleView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        float[] matrix = new float[9];
        mCurrentMatrix.getValues(matrix);
        scaleView.copyMatrix(matrix);
        scaleView.copy = true;
        return scaleView;
    }

    public interface OnClickListener {
        void onClick(View view);
        void onDoubleClick(View view);
        void onDelete(View view);
    }

    public void setClickListener(OnClickListener listener) {
        mClickListener = listener;
    }
}
