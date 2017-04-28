package com.yezi.testmedia.render;

import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.yezi.testmedia.filter.BaseFilter;
import com.yezi.testmedia.utils.enums.FilterType;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class VideoRender extends BaseRender {

    protected SurfaceTexture mSurfaceTexture;
    private int[] mTextures = new int[1];
    protected float[] mTransformMatrix = new float[16];

    public VideoRender() {
        super();
        mFilter.setFilterType(FilterType.VIDEO);
    }

    public VideoRender(BaseFilter filter) {
        mFilter = filter;
    }

    public void setDataSize(int width, int height) {
        mFilter.setDataSize(width, height);
    }

    @Override
    public int createTexture(int textureId) {
        if (textureId == BaseFilter.NO_FILTER) {
            GLES20.glGenTextures(1, mTextures, 0);
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextures[0]);

            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            return mTextures[0];
        }
        return textureId;
    }

    public interface onSurfaceCreatedListener {
        void onSurfaceCreated();
    }

    private onSurfaceCreatedListener mListener;

    public void setOnSurfaceCreatedListener(onSurfaceCreatedListener listener) {
        mListener = listener;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);

        mFilter.setTextureId(createTexture(mFilter.getTextureId()));
        mSurfaceTexture = new SurfaceTexture(mTextures[0]);

        if (mListener != null) {
            mListener.onSurfaceCreated();
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mSurfaceTexture.updateTexImage();

        mSurfaceTexture.getTransformMatrix(mTransformMatrix);
        mFilter.setTransformMatrix(mTransformMatrix);

        super.onDrawFrame(gl);
    }

    public SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }

    public void release() {
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }

        mFilter.release();
    }

}
