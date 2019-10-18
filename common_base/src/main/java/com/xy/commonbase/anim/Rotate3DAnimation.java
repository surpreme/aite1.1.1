package com.xy.commonbase.anim;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.xy.commonbase.helper.LogHelper;

public class Rotate3DAnimation extends Animation {

    private float fromDegree;
    private float toDegree;
    private Camera mCamera;
    //动画执行中心
    private float mCenterX;
    private float mCenterY;
    //z轴上的深度
    private float mDepthZ;
    //由远及近   还是  由近及远
    private boolean mReverse;

    public Rotate3DAnimation(float fromDegree, float toDegree, float depthZ,
                             float mCenterX, float mCenterY,boolean reverse) {
        super();
        this.fromDegree = fromDegree;
        this.toDegree = toDegree;
        this.mDepthZ = depthZ;
        this.mCenterX = mCenterX;
        this.mCenterY = mCenterY;
        this.mReverse = reverse;
        System.out.println("centerX ：" + mCenterX);
        System.out.println("centerY ：" + mCenterY);
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        float degrees = fromDegree + (fromDegree - toDegree)*interpolatedTime;
        mCamera.save();

        if(interpolatedTime < 0.5){
            //由近到远的效果
            mCamera.translate(0, 0, mDepthZ*interpolatedTime);
        }else{
            //由远及近的效果
            mCamera.translate(0, 0, mDepthZ * (1-interpolatedTime));
        }
        Matrix matrix = t.getMatrix();
        mCamera.rotateY(degrees);
        mCamera.getMatrix(matrix);
        mCamera.restore();

        //总体效果是   以mCenterX,mCenterY为中心执行
        //执行前平移
        matrix.preTranslate(-mCenterX, -mCenterY);
        //执行后平移
        matrix.postTranslate(mCenterX, mCenterY);
    }
}
