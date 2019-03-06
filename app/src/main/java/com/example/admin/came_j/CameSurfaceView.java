package com.example.admin.came_j;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author ygang
 **/
public class CameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    SurfaceHolder surfaceHolder;
    Canvas canvas;
    boolean isNew = false;

    public CameSurfaceView(Context context) {
        super(context);
        init();
    }

    public CameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isNew = true;
        CameraUtil.openFrontalCamera(CameraUtil.DESIRED_PREVIEW_FPS);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        CameraUtil.startPreviewDisplay(surfaceHolder);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isNew = false;
        CameraUtil.releaseCamera();

    }

    @Override
    public void run() {

    }

    void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }
}
