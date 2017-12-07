package com.view.animation;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Administrator on 2017/11/11.
 */

public class AnimationProvider {

    protected int delayDefault = 80;
    protected float transMax = AutoUtils.getPercentWidthSize(24);
    protected int stayTime = 500;

    /**
     * 视频时间是否是有效时间，静图0秒或1.5秒以上有效
     * @param duration
     * @return
     */
    public static boolean isDurationValiable(int duration, float speed){
        if (duration > 0 && duration * speed < 1500){
            return false;
        }
        return true;
    }
    public static boolean isDurationValiable(float duration, float speed){
        if (duration > 0 && duration * speed < 1500){
            return false;
        }
        return true;
    }

    /**
     * 显示的顺序， 可调整
     */
    public static final String[] strsTextShow = {"无状态","突然闪现","从天而降","底部升起",
            "上下颠簸","左右晃动",
            "放大缩小","波浪跳动",
            "逐字放大","逐字跳动",
            "排队出现","排队登场","摇头晃脑","颜色闪变"};

    /**
     * 标注AnimationProvider要使用的动画顺序，用于switch中的位置判断，不可调整
     */
    public static final String[] strsText = {"无状态","底部升起","放大缩小","波浪跳动",
            "上下颠簸","突然闪现","逐字放大","逐字跳动",
            "排队出现","排队登场","摇头晃脑","颜色闪变",
            "从天而降","左右晃动"};

    public static AnimationProvider getProvider(int type) {
        if (type < 0 || type >= strsText.length){
            return null;
        }
        String clickText = strsTextShow[type];
        int position = 0;
        for (int i = 0; i< strsText.length;i++){
            if (strsText[i].equals(clickText)){
                position = i;
                break;
            }
        }
        switch (position){
            case 0:
                break;
            case 1:
                return new TranslateShowUpProvider();
            case 2:
                return new ScaleProvider();
            case 3:
                return new WaveProvider();
            case 4:
                return new TranslateTBProvider();
            case 5:
                return new FlushProvider();
            case 6:
                return new ScaleWaveProvider();
            case 7:
                return new WaveOneProvider();
            case 8:
                return new TextCutProvider();
            case 9:
                return new ScaleCutProvider();
            case 10:
                return new RotateProvider();
            case 11:
                return new ColorProvider();
            case 12:
                return new TranslateShowDownProvider();
            case 13:
                return new TranslateLRProvider();
        }
        return null;
    }

    public int getColor(){
        return -1;
    }

    public int getAlpha(){
        return -1;
    }

    Paint paintColor;
    public Paint getPaint(Paint paint){
        if (getColor() != -1 || getAlpha() != -1){
            if (paintColor == null){
                paintColor = new Paint(paint);
            }
            if (getColor() != -1){
                paintColor.setShader(null);
                paintColor.setColor(getColor());
            }
            if (getAlpha() != -1){
                paintColor.setAlpha(getAlpha());
            }
            return paintColor;

        }
        return paint;
    }

    Paint paintStrokeColor;
    public Paint getStrokePaint(Paint paint){
        if (getAlpha() != -1){
            if (paintStrokeColor == null){
                paintStrokeColor = new Paint(paint);
            }
            if (getAlpha() != -1){
                paintStrokeColor.setAlpha(getAlpha());
            }
            return paintStrokeColor;

        }
        return paint;
    }

    public void resetPaint(Paint paint, Paint strokePaint){
        if (paintColor != null){
            paintColor = new Paint(paint);
        }
        if (paintStrokeColor != null){
            paintStrokeColor = new Paint(strokePaint);
        }
    }

    //用于校验数据，保证文字动画时间跟视频时间成整数倍，以达到合成的时候完美连接
    public void init() {
    }

    public int getDelay() {
        return delayDefault;
    }

    public int getDuration() {
        return 0;
    }

    protected int position;
    public void setPosition(int position) {
        this.position = position;
    }

    public void preCanvas(Canvas canvas, int centerX, int centerY) {
        
    }

    public void proCanvas(Canvas canvas) {
    }


    public int setTime(int time, boolean record) {
        return totalSize;
    }

    /**
     * 每个字有不同的动画
     * @return
     */
    public boolean isWordSplited(){
        return false;
    }


    //配置动画信息

    protected int totalTime = 3000;

    /**
     * 设置视频时长
     * @param duration
     */
    public void setDurationDelay(int duration, int delay) {
        duration = duration == 0 ? 1600:duration;
        delay = delay == 0 ? 80:delay;
        duration = Math.min(3000, duration);
        this.totalTime = duration;
        if(delay > 80){
            delay = delay/2;
        }
        this.delayDefault = delay;
    }

    protected int totalSize = 3;

    /**
     * 设置文字数量
     * @param size
     */
    public void setTextCount(int size) {
        this.totalSize = size;
    }

    protected int textWidth;
    protected int textHeight;

    /**
     * 设置文字宽高
     * @param textWidth
     * @param textHeight
     */
    public void setTextWidthHeight(int textWidth, int textHeight) {
        this.textWidth = textWidth;
        this.textHeight = textHeight;
    }

    public boolean isRepeat() {
        return false;
    }
}
