package com.view.scale;

import android.animation.TimeInterpolator;
import android.graphics.Matrix;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Administrator on 2017/11/24.
 */

public class MatrixAnimation {

    private float progress = 0;
    private float[] matrixBefore, matrixAfter;
    private TimeInterpolator timeInterpolator;
    MatrixAnimation(Matrix matrix, float[] matrixAfter){
        matrixBefore = new float[9];
        matrix.getValues(matrixBefore);
        this.matrixAfter = matrixAfter;
        animating = true;
       // timeInterpolator = new OvershootInterpolator(2f);
        timeInterpolator = new AccelerateDecelerateInterpolator();
    }

    public boolean isAnimating(){
        return animating;
    }

    boolean animating = false;
    public float[] getValues(){
        if (progress > 100){
            progress = 100;
            animating = false;
        }
        progress += 8.0f;
        float[] currentMatrix = new float[9];
        for (int i =0; i< 9;i++){
            float valueBefore = matrixBefore[i];
            float valueAfter = matrixAfter[i];
            float valueCurrent = valueBefore + (valueAfter - valueBefore) * timeInterpolator.getInterpolation(progress/100);
            currentMatrix[i] = valueCurrent;
        }
        return currentMatrix;
    }

}
