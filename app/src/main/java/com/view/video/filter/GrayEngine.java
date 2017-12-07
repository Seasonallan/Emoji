package com.view.video.filter;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Administrator on 2017/12/1.
 */

public class GrayEngine extends FilterEngine{
    public GrayEngine(int OESTextureId) {
        super(OESTextureId);
    }


    private static final  float[] vertexData = new float[]{
            1f, 1f, 1f, 1f,
            -1f, 1f, 0f, 1f,
            -1f, -1f, 0f, 0f,
            1f, 1f, 1f, 1f,
            -1f, -1f, 0f, 0f,
            1f, -1f, 1f, 0f
    };
    protected FloatBuffer mBuffer;

    @Override
    public void setShader() {

        mBuffer = createBuffer(vertexData);
        int mShaderProgram = createProgram(getVertex(), getFragment());

        aPositionLocation = glGetAttribLocation(mShaderProgram, "aPosition");
        aTextureCoordLocation = glGetAttribLocation(mShaderProgram, "aTextureCoordinate");
        uTextureMatrixLocation = glGetUniformLocation(mShaderProgram, "uTextureMatrix");
        uTextureSamplerLocation = glGetUniformLocation(mShaderProgram, "uTextureSampler");

    }

    @Override
    public void drawTexture(float[] transformMatrix) {
        glActiveTexture(GLES20.GL_TEXTURE0);
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mOESTextureId);
        glUniform1i(uTextureSamplerLocation, 0);
        glUniformMatrix4fv(uTextureMatrixLocation, 1, false, transformMatrix, 0);

        if (mBuffer != null) {
            mBuffer.position(0);
            glEnableVertexAttribArray(aPositionLocation);
            glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 16, mBuffer);

            mBuffer.position(2);
            glEnableVertexAttribArray(aTextureCoordLocation);
            glVertexAttribPointer(aTextureCoordLocation, 2, GL_FLOAT, false, 16, mBuffer);

            glDrawArrays(GL_TRIANGLES, 0, 6);
        }
    }

    public String getVertex() {
        return "attribute vec4 aPosition;\n" +
                "uniform mat4 uTextureMatrix;\n" +
                "attribute vec4 aTextureCoordinate;\n" +
                "varying vec2 vTextureCoord;\n" +
                "void main()\n" +
                "{\n" +
                "  vTextureCoord = (uTextureMatrix * aTextureCoordinate).xy;\n" +
                "  gl_Position = aPosition;\n" +
                "}";
    }

    public String getFragment() {
        return "#extension GL_OES_EGL_image_external : require\n" +
                "precision mediump float;\n" +
                "uniform samplerExternalOES uTextureSampler;\n" +
                "varying vec2 vTextureCoord;\n" +
                "void main()\n" +
                "{\n" +
                "  vec4 vCameraColor = texture2D(uTextureSampler, vTextureCoord);\n" +
                "  float fGrayColor = (0.3*vCameraColor.r + 0.59*vCameraColor.g + 0.11*vCameraColor.b);\n" +
                "  gl_FragColor = vec4(fGrayColor, fGrayColor, fGrayColor, 1.0);\n" +
                "}\n";
    }
}
