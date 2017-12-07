/*
 *          Copyright (C) 2016 jarlen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import com.march.gifmaker.utils.Util;
import com.view.animation.AnimationProvider;
import com.view.model.FontListEntity;
import com.view.model.LayerEntity;
import com.view.model.TextStyleEntity;
import com.view.model.styleModel;
import com.view.scale.IScaleView;
import com.view.scale.ScaleView;
import com.view.util.FileManager;
import com.view.util.MipmapUtil;
import com.view.util.ScreenUtils;
import com.view.util.ToolPaint;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * @auther lizhongxin
 * @data 2017/6/27 初始化时，根据气体的宽高去计算出所有的宽高。 增加一个气泡框后，气泡框的宽高就确定下来了。 这时候，如何增加文字，按原来的字号可能导致文字，在指定区域放不下。 此时，要对文字的字号进行相应的缩放。
 * <p>
 * 现在的逻辑更新-->计算出文字-->文字宽高-->控件宽高==底图宽高
 */
public class TextStyleView extends View implements IScaleView {
    int offsetY;
    private boolean nullInput = false;
    public boolean isAudio = false;
    private int backgroudRes = 0;
    int preBackgroundInfo;
    Bitmap backgroundBitmap;
    private String text;
    private Context context;
    public Paint paint = new Paint();
    public Paint strokepaint = new Paint();
    public String fontName = "";
    private int paddingLeft, paddingTop, textSpacing, lineSpacing;
    int emojiWidth = 100;

    private String[] ids = {"text_style_0", "text_style_1"};
    public TextStyleView copy() {
        TextStyleView textStyleView = new TextStyleView(context);
        textStyleView.paint = new Paint();
        textStyleView.paint.set(paint);
        textStyleView.strokepaint = new Paint();
        textStyleView.strokepaint.set(strokepaint);
        textStyleView.backgroudRes = backgroudRes;
        textStyleView.fontName = fontName;
        textStyleView.text = text;
        textStyleView.fixEmoji();
        textStyleView.calculateWidthHeight();
        textStyleView.setTextAnimationType(currentType, duration, delay, speed);
        textStyleView.resetAnimationPaint();
        textStyleView.addEvent();
        return textStyleView;
    }


    public boolean setTextEntry(LayerEntity.ItemArrayBean item, float width) {
        int opViewWidth = ScreenUtils.getScreenWidth();
        this.text = item.getText();
        fixEmoji();
        this.fontName = item.getTextFontName();

        paint.setTypeface(getTypeface(Typeface.DEFAULT));
        strokepaint.setTypeface(getTypeface(Typeface.DEFAULT));
        TextStyleEntity textStyleEntity = item.getTextStyleModel();

        String bgId = textStyleEntity.getImageName();
        if (!TextUtils.isEmpty(bgId)) {
            this.backgroudRes = MipmapUtil.getResource(bgId);
            //backgroudRes = Integer.parseInt(bgId);
        }

        styleModel styleModel = textStyleEntity.getStyleModel();
        paint.setColor(Util.getColor(styleModel.getTextColor(), 0xffffff));
        strokepaint.setColor(Util.getColor(styleModel.getStrokeColor(), 0xffffff));

        paint.setStrokeWidth(getPaintStrokeWidth(styleModel.getTextSize()));
        strokepaint.setStrokeWidth(getStrokePaintStrokeWidth(styleModel.getTextSize(), styleModel.getStrokeSize()));
        paint.setAlpha((int) (styleModel.getTextAlpha() * 255));
        strokepaint.setAlpha((int) (styleModel.getMiaobianAlpha() * 255));

        float maxLength = (float) (item.getSizeWidth() * item.getXScale() *opViewWidth / width);
        calculateWidthHeight();

        if(!TextUtils.isEmpty(styleModel.getStartColor()) && !TextUtils.isEmpty(styleModel.getEndColor())){
            setLinearGradient(styleModel.getStartColor(), styleModel.getEndColor());
        }

        item.setXScale((maxLength/finalWidth) * width/opViewWidth);
        item.setYScale((maxLength/finalWidth) * width/opViewWidth);
        item.setSizeWidth(finalWidth);
        item.setSizeHeight(finalHeight);

        addEvent();
        return true;
    }

    public TextStyleEntity getTextEntry() {
        TextStyleEntity textStyleEntity = new TextStyleEntity();
        textStyleEntity.setTextH(getHeight());
        textStyleEntity.setTextW(getWidth());
        textStyleEntity.setTextX(0);
        textStyleEntity.setTextY(0);
        textStyleEntity.animationType = currentType;
        //        textStyleEntity.setTextX(textX);
        //        textStyleEntity.setTextY(textY);//因为我都剧中显示,可能这些参数不正确
        //重要参数
        textStyleEntity.setImageWidth(getWidth());
        textStyleEntity.setImageHeight(getHeight());
        // getResources().getIdentifier()
        textStyleEntity.setFontName(fontName);
        for (String name : ids) {
            int id = MipmapUtil.getResource(name);
            if (backgroudRes == id) {
                textStyleEntity.setImageName(name);
            }
        }

        //这个模型的参数最大可能被单独更改
        //描边和字体的宽度，透明度，色值
        styleModel styleModel = new styleModel();

        LinearGradient shader = (LinearGradient) paint.getShader();
        if (shader != null){
            try {
                Bitmap bitmap = Bitmap.createBitmap(8, lineHeight, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
                styleModel.setStartColor(Util.getColorStr(bitmap.getPixel(0, 0)));
                styleModel.setEndColor(Util.getColorStr(bitmap.getPixel(bitmap.getWidth() - 1, lineHeight - 1)));
                Util.recycleBitmaps(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        styleModel.setTextColor(Util.getColorStr(paint.getColor()));
        styleModel.setStrokeColor(Util.getColorStr(strokepaint.getColor()));

        float paintSizeParams = getPaintStrokeWidthParam();
        styleModel.setTextColorSize(paintSizeParams);
        styleModel.setStrokeSize(getStrokePaintStrokeWidthParam(paintSizeParams));
        double d_textalpha = (double) paint.getAlpha() / 255;
        double d_strokealpha = (double) strokepaint.getAlpha() / 255;
        DecimalFormat fnum = new DecimalFormat("##0.0");
        d_textalpha = Double.valueOf(fnum.format(d_textalpha));
        d_strokealpha = Double.valueOf(fnum.format(d_strokealpha));
        styleModel.setMiaobianAlpha(d_strokealpha);
        styleModel.setTextAlpha(d_textalpha);
        textStyleEntity.setStyleModel(styleModel);
        return textStyleEntity;
    }


    /**
     * 在设置字体粗细的时候，描边的粗细要跟着变
     * 最小值
     */
    public boolean setPaintWidthByPercent(float paintwidthPercent) {
        float paintSizeParams = paintwidthPercent / 100;
        float oriParam = getPaintStrokeWidthParam();
        float newPaintWidth = getPaintStrokeWidth(paintSizeParams);
        float pw = paint.getStrokeWidth();
        if (pw == newPaintWidth) {
            return false;
        }
        paint.setStrokeWidth(newPaintWidth);
        strokepaint.setStrokeWidth(getStrokePaintStrokeWidth(paintSizeParams, getStrokePaintStrokeWidthParam(oriParam)));
        resetAnimationPaint();
        invalidate();
        return false;
    }

    public boolean setStrokeWidthByPercent(float strokeWidthPercent) {
        float newParams = strokeWidthPercent / 100;
        float paintParams = getPaintStrokeWidthParam();
        float oldParams = getStrokePaintStrokeWidthParam(paintParams);
        if (newParams == oldParams) {
            return false;
        }
        strokepaint.setStrokeWidth(getStrokePaintStrokeWidth(paintParams, newParams));
        resetAnimationPaint();
        invalidate();
        return false;
    }


    //外部描边百分比得到描边宽度
    private float getStrokePaintStrokeWidth(double paintSizeParams, double strokePaintSizeParams) {
        return (float) (strokePaintSizeParams * ToolPaint.getDefault().getPaintWidth() + getPaintStrokeWidth(paintSizeParams));
    }

    //描边宽度得到外部描边百分比
    public float getStrokePaintStrokeWidthParam(float paintSizeParams) {
        return (strokepaint.getStrokeWidth()  - getPaintStrokeWidth(paintSizeParams)) / ToolPaint.getDefault().getPaintWidth();
    }

    //内部描边百分比得到描边宽度
    private float getPaintStrokeWidth(double params) {
        return (float) (params * ToolPaint.getDefault().getStrokeWidth());
    }

    //描边宽度得到内部描边百分比
    public float getPaintStrokeWidthParam() {
        return paint.getStrokeWidth() / ToolPaint.getDefault().getStrokeWidth();
    }


    public TextStyleView(Context context) {
        super(context);
        init(context);
    }

    public TextStyleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextStyleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    long drawingCacheSize;
    public void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        drawingCacheSize = ViewConfiguration.get(context).getScaledMaximumDrawingCacheSize();
        textSpacing = ScreenUtils.getPercentWidthSize(4);
        lineSpacing = ScreenUtils.getPercentWidthSize(16);
        paddingLeft =  ScreenUtils.getPercentWidthSize(24);
        paddingTop =  ScreenUtils.getPercentWidthSize(24);
        offsetY = ScreenUtils.getPercentWidthSize(10);

        this.context = context;
        paint.setDither(true);//防抖
        paint.setAntiAlias(true);
        paint.setTypeface(getTypeface(Typeface.DEFAULT));
        paint.setColor(Color.WHITE);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(getPaintStrokeWidth(0.2));
        paint.setAlpha(255);
        paint.setShader(null);
        //文字居中写
        paint.setTextAlign(Paint.Align.CENTER);
        strokepaint.setTextAlign(Paint.Align.CENTER);

        strokepaint.setDither(true);
        strokepaint.setAntiAlias(true);
        strokepaint.setTypeface(getTypeface(Typeface.DEFAULT));
        strokepaint.setColor(Color.BLACK);

        strokepaint.setStyle(Paint.Style.FILL_AND_STROKE);
        strokepaint.setStrokeWidth(getStrokePaintStrokeWidth(0.2, 0.4));
        strokepaint.setAlpha(255);
        paint.setShader(null);

        paint.setTextSize(ToolPaint.getDefault().getPaintSize());
        strokepaint.setTextSize(ToolPaint.getDefault().getPaintSize());
    }

    public float getMaxScale(){
        if (paint == null){
            paint = new Paint();
            paint.setTextSize(ToolPaint.getDefault().getPaintSize());
        }
        int width = (int) paint.measureText("情");
        return ScreenUtils.getScreenWidth()/2 * 1.0f/width;
    }

    private int finalWidth = 0, finalHeight = 0, lineHeight = 1;
    private void calculateWidthHeight() {
        if (textEmojiList == null) {
            setMeasuredDimension(0, 0);
            return;
        }
        emojiWidth = (int) paint.measureText("情");
        finalWidth = 0;
        int offsetX = paddingLeft;
        finalHeight = paddingTop;
        int size = textEmojiList.size();
        lineHeight = paddingTop + emojiWidth + paddingTop + lineSpacing;
        for (int i = 0; i < size; i++) {
            TextEmoji emoji = textEmojiList.get(i);
            String itemText = emoji.text;
            if (itemText.equals("\n")) {
                finalWidth = Math.max(finalWidth, offsetX);
                offsetX = paddingLeft;
                finalHeight += (emojiWidth + lineSpacing);
            } else {
                int fontTotalWidth = (int) paint.measureText(itemText);
                emoji.offsetX = offsetX;
                emoji.offsetY = finalHeight;
                emoji.fontTotalWidth = (int) paint.measureText(itemText);
                emoji.fontTotalHeight = emojiWidth;
                offsetX += fontTotalWidth + textSpacing;
                if (offsetX >= ToolPaint.getDefault().getMaxTextLength() && i < size - 1){//fix problem: OpenGLRenderer: Bitmap too large to be uploaded into a texture 宽度太大无法绘制问题
                    TextEmoji emojiNext = textEmojiList.get(i + 1);
                    if (emojiNext.text == null || !emojiNext.text.equals("\n")){
                        TextEmoji emojiEnter = new TextEmoji();
                        emojiEnter.text = "\n";
                        textEmojiList.add(i + 1, emojiEnter);
                        size ++;
                    }
                }
            }
            emoji.ready = true;
        }
        finalHeight += (emojiWidth + lineSpacing);
        finalWidth = Math.max(finalWidth, offsetX);
        finalWidth += paddingLeft;
        finalHeight += paddingTop;
        resetAnimationPaint();

        ViewParent parent = getParent();
        if (parent != null && parent instanceof ScaleView){
            ((ScaleView) parent).disableHardWareWhenText2Long(this);
        }
    }


    public static class TextEmoji{
        public String text; 
        public boolean ready = false;
        public int offsetX, offsetY;
        public int fontTotalWidth, fontTotalHeight;
        public void onDraw(Canvas canvas, Paint paint, Paint strokepaint, int dx) {
            if (!ready)
                return;
            if (text.equals("\n")){
            }else{
                //canvas.translate(offsetX, offsetY);
                if (strokepaint.getStrokeWidth() >  paint.getStrokeWidth() + 0.01){
                    if (paint.getAlpha() < 255){
                        //此处必须先绘制在bitmap上，否则合成会无法透明
                        Bitmap bmSrc = Bitmap.createBitmap(fontTotalWidth * 3/2, fontTotalHeight * 3/2, Bitmap.Config.ARGB_8888);
                        Canvas cSrc = new Canvas(bmSrc);

                        int alpha = paint.getAlpha();
                        paint.setAlpha(255);
                        cSrc.drawText(text, fontTotalWidth/2, fontTotalHeight - dx, strokepaint);//画出描边
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                        cSrc.drawText(text, fontTotalWidth/2, fontTotalHeight - dx, paint);//写字
                        paint.setAlpha(alpha);

                        canvas.drawBitmap(bmSrc, offsetX, offsetY, null);
                        if (bmSrc != null && !bmSrc.isRecycled()){
                            bmSrc.recycle();
                        }
                    }else{
                        canvas.drawText(text, fontTotalWidth/2 + offsetX, fontTotalHeight - dx + offsetY, strokepaint);//画出描边
                    }
                }
                paint.setXfermode(null);
                canvas.drawText(text, fontTotalWidth/2 + offsetX, fontTotalHeight - dx + offsetY, paint);//写字
            }
        }

    }


    public void resetAnimationPaint(){
        if (animationProvider != null){
            animationProvider.resetPaint(paint, strokepaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(finalWidth, finalHeight);
    }


    public boolean resetPosition = false; // 由于初始位置没有确定，不矫正位置
    int preWidth, preHeight;


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getParent() instanceof ScaleView) {
            int width = right - left;
            int height = bottom - top;
            if (preWidth == width && preHeight == height) {
                ((ScaleView) getParent()).rebindOpView();
                return;
            }
            preWidth = width;
            preHeight = height;
            if (width > 0 && height > 0) {
                if (nullInput || isAudio) {
                    resetPosition = true;
                    int offsetY = ScreenUtils.getPercentWidthSize(10);
                    ((ScaleView) getParent()).showBottomCenter(width, height, offsetY, isAudio?getText():null);
                    isAudio = false;
                    ((ScaleView) getParent()).rebindOpView();
                } else {
                    float[] offset = ((ScaleView) getParent()).rebindOpView();
                    if (offset != null && offset.length == 2) {
                        if (offset[0] == 0 && offset[1] == 0) {
                        } else {
                            //文字长度变化的时候，对位置进行矫正
                            if (resetPosition) {
                                ((ScaleView) getParent()).changeOffset(getText(), offset[0], offset[1]);
                            }
                            resetPosition = true;
                        }
                    }
                }
            }
        }
    }


    @Override
    public void drawCanvas(Canvas canvas){
        if (textEmojiList == null || textEmojiList.size() == 0) {
            return;
        }
        drawBackground(canvas);
        if (animationProvider == null || !AnimationProvider.isDurationValiable(duration, speed)){
            drawText(canvas, textEmojiList.size(), paint, strokepaint);
        }else{
            updateAnimationTime();
            if (animationProvider.isWordSplited()){
                int i = 0;
                for (TextEmoji emoji : textEmojiList) {
                    i++;
                    animationProvider.setPosition(i - 1);
                    int drawTextCount;
                    if (recordTime >= 0){
                        drawTextCount = animationProvider.setTime(recordTime, true);
                    }else{
                        drawTextCount = animationProvider.setTime(mCurrentAnimationTime, false);
                    }
                    if (i > drawTextCount){
                        break;
                    }
                    animationProvider.preCanvas(canvas, emoji.offsetX + emojiWidth/2,
                            emoji.offsetY + emojiWidth/2);
                    emoji.onDraw(canvas, animationProvider.getPaint(paint), animationProvider.getStrokePaint(strokepaint), offsetY);
                    animationProvider.proCanvas(canvas);
                }
            }else{
                int showTextCount ;
                if (recordTime >= 0){
                    showTextCount = animationProvider.setTime(recordTime, true);
                }else{
                    showTextCount = animationProvider.setTime(mCurrentAnimationTime, false);
                }
                animationProvider.preCanvas(canvas, getViewWidth()/2, getViewHeight()/2);
                drawText(canvas, showTextCount, animationProvider.getPaint(paint), animationProvider.getStrokePaint(strokepaint));
                animationProvider.proCanvas(canvas);
            }
        }
        isSeeking = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCanvas(canvas);
    }

    void drawText(Canvas canvas, int drawTextCount, Paint paint, Paint strokePaint) {
        int i = 0;
        for (TextEmoji emoji : textEmojiList) {
            i++;
            if (i > drawTextCount){
                break;
            }
            emoji.onDraw(canvas, paint, strokePaint, offsetY);
        }
    }

    private long mMovieStart = 0;
    private int mCurrentAnimationTime = 0;
    private void updateAnimationTime() {
        long now = System.currentTimeMillis();
        if (mMovieStart == 0) {
            mMovieStart = now;
        }
        if (getDuration() > 0){
            mCurrentAnimationTime = (int) ((now - mMovieStart) % getDuration());
        }
    }

    void drawBackground(Canvas canvas){
        if (backgroudRes != 0) {
            if (preBackgroundInfo != backgroudRes) {
                Util.recycleBitmaps(backgroundBitmap);
                backgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroudRes);
            }
            if (backgroundBitmap != null && !backgroundBitmap.isRecycled()) {
                canvas.drawBitmap(backgroundBitmap, null, new Rect(0, 0, getWidth(), getHeight()), paint);
            }
        }
        preBackgroundInfo = backgroudRes;
    }

    AnimationProvider animationProvider;
    public int currentType = 0;
    int duration = 1600;
    float speed = 1.0f;
    int delay = 80;
    public boolean setTextAnimationType(int type, int duration, int delay, float speed) {
        if (currentType == type){
            return false;
        }
        setTextAnimationType(type, duration, speed,  delay, true);
        return true;
    }


    public boolean canAnimating(){
        if (currentType > 0){
            if (AnimationProvider.isDurationValiable(duration, speed)){
                return true;
            }
        }
        return false;
    }

    private void setTextAnimationType(int type, int duration, float speed, int delay, boolean addEvent){
        currentType = type;
        mMovieStart = System.currentTimeMillis();
        animationProvider = AnimationProvider.getProvider(type);
        changeAnimationTime(duration, delay, speed);
        if (addEvent){
            addEvent();
        }
    }

    /**
     * 重要方法：视频的时长，视频的播放快慢都将影响到文字动效。在时长和播放速度变化变化的时候，都要调用这个方法。
     * @param duration
     * @param delay
     * @param speed
     */
    public void changeAnimationTime(int duration, int delay, float speed){
        this.duration = duration;
        this.delay = delay;
        this.speed = speed;
        if (animationProvider != null){
            animationProvider.setDurationDelay(duration, delay);
            resetAnimationParams();
        }
        invalidate();
    }

    private void resetAnimationParams(){
        if (animationProvider != null){
            animationProvider.setTextWidthHeight(finalWidth, finalHeight);
            animationProvider.setTextCount(textEmojiList.size());
            animationProvider.init();
        }
    }

    public int getTotalTime(){
        return duration;
    }

    public boolean isRepeat(){
        if (animationProvider != null){
            return animationProvider.isRepeat();
        }
        return false;
    }

    @Override
    public int getDuration() {
        if (animationProvider == null || !AnimationProvider.isDurationValiable(duration, speed)){
            return 0;
        }
        return animationProvider.getDuration();
    }

    @Override
    public void startRecord() {
    }

    private int recordTime = -1;

    @Override
    public boolean isSeeking(){
        return isSeeking;
    }
    boolean isSeeking = false;
    @Override
    public void recordFrame(int time) {
        recordTime = time;
        isSeeking = true;
    }

    @Override
    public void stopRecord() {
        recordTime = -1;
    }

    @Override
    public int getDelay() {
        if (animationProvider == null){
            return 0;
        }
        return animationProvider.getDelay();
    }


    public int getVideoDelay() {
        return (int) (120/speed);
    }

    public Typeface getTypeface(Typeface typefaceDefault) {
        if (!TextUtils.isEmpty(fontName)) {
            File fontfile = FileManager.getDiyFontFile(fontName);
            if (fontfile != null && fontfile.exists()) {
                try {
                    Typeface typeface = Typeface.createFromFile(fontfile);
                    if (typeface != null) {
                        return typeface;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return typefaceDefault;
    }

    @Override
    public void onRelease() {
        Util.recycleBitmaps(backgroundBitmap);
    }

    public float getTextSize() {
        return paint.getTextSize();
    }

    public String getText() {
        return text;
    }

    public boolean setTexttypeface(Typeface texttypeface) {
        Typeface typeface = paint.getTypeface();
        if (texttypeface.equals(typeface)) {
            return false;
        }
        fontName = null;
        paint.setTypeface(getTypeface(texttypeface));
        strokepaint.setTypeface(getTypeface(texttypeface));
        calculateWidthHeight();
        addEvent();
        requestLayout();
        invalidate();
        return true;
    }

    public boolean setTexttypeface(FontListEntity.RecordsBean typeface) {
        String fontName = typeface.getName();
        if (fontName == null || fontName.equals(this.fontName)) {
            return false;
        }
        this.fontName = fontName;
        paint.setTypeface(getTypeface(Typeface.DEFAULT));
        strokepaint.setTypeface(getTypeface(Typeface.DEFAULT));
        calculateWidthHeight();
        addEvent();
        requestLayout();
        invalidate();
        return true;
    }


    //有可能在Style基础上去修改一部分属性
    public boolean setTextStyle(TextStyleEntity textStyle) {
        styleModel styleModel = textStyle.getStyleModel();
        String startColorStr = styleModel.getStartColor();
        String endColorStr = styleModel.getEndColor();
        String strokeColorStr = styleModel.getStrokeColor();
        String textColorStr = styleModel.getTextColor();
        Double strokeSize = styleModel.getStrokeSize();
        Double textSize = styleModel.getTextSize();

        paint.setStrokeWidth(getPaintStrokeWidth(textSize));
        strokepaint.setStrokeWidth(getStrokePaintStrokeWidth(textSize, strokeSize));

        fontName = textStyle.getFontName();
        paint.setTypeface(getTypeface(Typeface.DEFAULT));
        strokepaint.setTypeface(getTypeface(Typeface.DEFAULT));


        //有没有描边对应有没有描边的色值
        if (!TextUtils.isEmpty(strokeColorStr)) {
            int colorNew = -1;
            if (!TextUtils.isEmpty(strokeColorStr)) {
                if (strokeColorStr.startsWith("#")) {
                    colorNew = Color.parseColor(strokeColorStr);
                } else {
                    colorNew = Color.parseColor("#" + strokeColorStr);
                }
            }
            strokepaint.setColor(colorNew);
        } else {
            strokepaint.setStrokeWidth(getStrokePaintStrokeWidth(textSize, 0));
        }
        this.backgroudRes = MipmapUtil.getResource(textStyle.getImageName());
        calculateWidthHeight();
        /**
         * textsyle.txt文件来自ios端开发人员拟定。把他们的plist文件转成json。
         * 其中textcolor 和startcolor.endcolor不共存
         */
        if (!TextUtils.isEmpty(textColorStr)) {
            //字体颜色是单色
            if (!TextUtils.isEmpty(textColorStr)) {
                if (textColorStr.startsWith("#")) {
                    paint.setColor(Color.parseColor(textColorStr));
                } else {
                    paint.setColor(Color.parseColor("#" + textColorStr));
                }
            }
            paint.setShader(null);
        } else {
            setLinearGradient(startColorStr, endColorStr);
        }

        resetAnimationPaint();
        addEvent();
        requestLayout();
        invalidate();
        return true;
    }

    List<TextEmoji> textEmojiList;

    private void fixEmoji() {
        textEmojiList = getEmojis(getContext(), text);
    }


    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     */
    public static List<TextEmoji> getEmojis(Context context, String text) {
        List<TextEmoji> emojis = new ArrayList<>();
        if (text == null) {
            return emojis;
        }
        for (int i = 0; i < text.length(); i ++) {
            TextEmoji textEmoji = new TextEmoji();
            textEmoji.text = text.subSequence(i, i+1).toString();
            emojis.add(textEmoji);
        }
        return emojis;
    }

    public int setText(String text) {
        this.text = text;
        fixEmoji();
        calculateWidthHeight();

        addEvent();
        return finalWidth;
    }

    public void setPaintColorReverse(int color, int strokeColor){
        paint.setColor(color);
        strokepaint.setColor(strokeColor);
        resetAnimationPaint();
    }

    public boolean setStrokecolor(String strokecolor) {
        int colorNew = Util.getColor(strokecolor, paint.getColor());
        int color = strokepaint.getColor();
        if (color == colorNew) {
            return false;
        }

        //setColor之后透明度会重置为255, 需要重新设置
        int alpha = strokepaint.getAlpha();
        strokepaint.setColor(colorNew);
        strokepaint.setAlpha(alpha);

        resetAnimationPaint();
        invalidate();
        return false;
    }

    public boolean isText2Long(){
        return finalWidth * finalHeight * 4 > drawingCacheSize;
    }


    public int editText(String text) {
        if (text != null && text.equals(this.text)) {
            return finalWidth;
        }
        this.text = text;
        fixEmoji();
        calculateWidthHeight();
        resetAnimationParams();
        addEvent();
        requestLayout();
        invalidate();
        return finalWidth;
    }

    public int editTextWithCal(String text) {
        if (text != null && text.equals(this.text)) {
            return finalWidth;
        }
        this.text = text;
        fixEmoji();

        paint.setStrokeWidth(getPaintStrokeWidth(0.2f));
        strokepaint.setStrokeWidth(getStrokePaintStrokeWidth(0.2f, 0.4f));

        calculateWidthHeight();
        resetAnimationParams();

        addEvent();
        requestLayout();
        invalidate();
        return finalWidth;
    }

    public boolean setStrokealpha(int strokealpha) {
        int alpha = strokepaint.getAlpha();
        if (alpha == strokealpha) {
            return false;
        }
        strokepaint.setAlpha(strokealpha);
        resetAnimationPaint();
        invalidate();
        return false;
    }
    public boolean setStrokecolor(int strokecolor) {
        int color = strokepaint.getColor();
        if (color == strokecolor) {
            return false;
        }
        //setColor之后透明度会重置为255, 需要重新设置
        int alpha = strokepaint.getAlpha();
        strokepaint.setColor(strokecolor);
        strokepaint.setAlpha(alpha);
        resetAnimationPaint();
        invalidate();
        return false;
    }


    public boolean setTextcolor(int textcolor) {
        if (paint.getShader() != null){
            paint.setShader(null);
            paint.setColor(textcolor);
            resetAnimationPaint();
            invalidate();
            return true;
        }
        int color = paint.getColor();
        if (color != textcolor) {
            //setColor之后透明度会重置为255, 需要重新设置
            int alpha = paint.getAlpha();
            paint.setColor(textcolor);
            paint.setAlpha(alpha);
            resetAnimationPaint();
            invalidate();
            return false;
        }
        return false;
    }

    public boolean setTextalpha(int textalpha) {
        if (paint.getAlpha() == textalpha) {
            return false;
        }
        paint.setAlpha(textalpha);
        resetAnimationPaint();
        invalidate();
        return false;
    }

    public boolean setTextcolor(String textcolor) {
        int color = paint.getColor();
        if (!TextUtils.isEmpty(textcolor)) {
            int colorNew = Util.getColor(textcolor, paint.getColor());
            if (paint.getShader() != null){
                paint.setShader(null);
                paint.setColor(colorNew);
                resetAnimationPaint();
                invalidate();
                return true;
            }
            if (color != colorNew) {
                //setColor之后透明度会重置为255, 需要重新设置
                int alpha = paint.getAlpha();
                paint.setColor(colorNew);
                paint.setAlpha(alpha);
                resetAnimationPaint();
                invalidate();
                return false;
            }
        }
        return false;
    }


    @Override
    public int getViewWidth() {
        return finalWidth;
    }

    @Override
    public int getViewHeight() {
        return finalHeight;
    }


    int position = -1;
    List<TextOp> list = new ArrayList<>();


    private void addEvent() {
        if (position < list.size() - 1) {
            for (int i = list.size() - 1; i > position; i--) {
                list.remove(i);
            }
        }
        list.add(new TextOp(getText(), paint, strokepaint, backgroudRes, fontName, currentType));
        position = list.size() - 1;
    }

    public void pre() {
        position--;
        if (position < 0) {
            position = 0;
        }
        TextOp op = list.get(position);
        reset(op);
    }

    public void pro() {
        position++;
        if (position > list.size() - 1) {
            position = list.size() - 1;
        }
        TextOp op = list.get(position);
        reset(op);
    }

    private void reset(TextOp op) {
        this.paint = new Paint();
        this.paint.set(op.paint);
        this.strokepaint = new Paint();
        this.strokepaint.set(op.strokePaint);
        this.backgroudRes = op.background;
        this.fontName = op.fontName;
        this.text = op.text;
        fixEmoji();
        calculateWidthHeight();
        this.currentType = op.animationType;
        setTextAnimationType(currentType, duration, speed, delay,false);
        requestLayout();
        invalidate();
    }

    class TextOp {
        public Paint paint;
        public Paint strokePaint;
        public int background;
        public String text;
        public String fontName;
        public int animationType;

        TextOp(String text, Paint p, Paint sp, int bg, String fontName, int animationType) {
            this.text = text;
            this.fontName = fontName;
            this.paint = new Paint();
            this.paint.set(p);
            this.strokePaint = new Paint();
            this.strokePaint.set(sp);
            this.background = bg;
            this.animationType = animationType;
        }
    }



    public boolean getNullInput() {
        return nullInput;
    }


    public void setNullInput(boolean nullInput) {
        if (nullInput == this.nullInput) {
            return;
        }
        init(getContext());
        this.nullInput = nullInput;
        paint.setColor(Color.WHITE);
        if (nullInput) {
            strokepaint.setColor(Color.parseColor("#474747"));
            paint.setStrokeWidth(getPaintStrokeWidth(0.4f));
            strokepaint.setStrokeWidth(getStrokePaintStrokeWidth(0.4f, 0.2f));
            paint.setAlpha(100);
            strokepaint.setAlpha(108);
        } else {
            strokepaint.setColor(Color.BLACK);
            paint.setStrokeWidth(getPaintStrokeWidth(0.4f));
            strokepaint.setStrokeWidth(getStrokePaintStrokeWidth(0.4f, 0.2f));
            paint.setAlpha(255);
            strokepaint.setAlpha(255);
        }
    }

    public void setIsAudio(boolean isAudio) {
        init(getContext());
        this.isAudio = isAudio;
        paint.setColor(Color.WHITE);
        strokepaint.setColor(Color.BLACK);
        if (isAudio) {
            paint.setStrokeWidth(getPaintStrokeWidth(0.2f));
            strokepaint.setStrokeWidth(getStrokePaintStrokeWidth(0.2f, 0.4f));
        }
        paint.setAlpha(255);
        strokepaint.setAlpha(255);
    }

    public void setLinearGradient(String startcolorStr, String endcolorStr) {
        //字体颜色是双色
        int startcolor = Color.parseColor("#" + startcolorStr);
        int endcolor = Color.parseColor("#" + endcolorStr);
        int[] colrs = {startcolor, startcolor, endcolor};
        float[] positions = {};
        LinearGradient linearGradient = new LinearGradient(getViewWidth() / 2, 0, getViewWidth() / 2, lineHeight, startcolor, endcolor,
                Shader.TileMode.REPEAT);
        paint.setShader(linearGradient);
    }

}
