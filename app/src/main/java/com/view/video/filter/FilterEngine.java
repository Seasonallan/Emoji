package com.view.video.filter;

import android.content.Context;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glGetError;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUseProgram;

/**
 * Created by lb6905 on 2017/6/12.
 */

public abstract class FilterEngine {

    public static FilterEngine getEngine(int type, int OESTextureId){
        return new JackEngine(OESTextureId);
    }

    protected int mOESTextureId = -1;

    protected int aPositionLocation = -1;
    protected int aTextureCoordLocation = -1;
    protected int uTextureMatrixLocation = -1;
    protected int uTextureSamplerLocation = -1;

    public FilterEngine(int OESTextureId) {
        mOESTextureId = OESTextureId;

        setShader();
    }

    public abstract void setShader();
    public abstract void drawTexture(float[] transformMatrix);


    public FloatBuffer createBuffer(float[] vertexData) {
        FloatBuffer buffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        buffer.put(vertexData, 0, vertexData.length).position(0);
        return buffer;
    }

    private int loadShader(int type, String shaderSource) {
        int shader = glCreateShader(type);
        if (shader == 0) {
            throw new RuntimeException("Create Shader Failed!" + glGetError());
        }
        glShaderSource(shader, shaderSource);
        glCompileShader(shader);
        return shader;
    }

    private int linkProgram(int verShader, int fragShader) {
        int program = glCreateProgram();
        if (program == 0) {
            throw new RuntimeException("Create Program Failed!" + glGetError());
        }
        glAttachShader(program, verShader);
        glAttachShader(program, fragShader);
        glLinkProgram(program);

        glUseProgram(program);
        return program;
    }

    protected int createProgram(String ver, String frag){
        return linkProgram(loadShader(GL_VERTEX_SHADER, ver), loadShader(GL_FRAGMENT_SHADER, frag));
    }

}

