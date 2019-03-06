package com.example.admin.came_j;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CAMERA = 0x01;

    private CameSurfaceView mCameraSurfaceView;
    private Button mBtnTake;
    private Button mBtnSwitch;

    private int mOrientation;

    // CameSurfaceView 容器包装类
    private FrameLayout mAspectLayout;
    private boolean mCameraRequested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Android 6.0相机动态权限检查
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initView();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, REQUEST_CAMERA);
        }

    }

    /**
     * 初始化View
     */
    private void initView() {
        mAspectLayout = (FrameLayout) findViewById(R.id.layout_aspect);
        ;
        mCameraSurfaceView = new CameSurfaceView(this);
        mAspectLayout.addView(mCameraSurfaceView);
        mOrientation = CameraUtil.calculateCameraPreviewOrientation(MainActivity.this);
        mBtnTake = (Button) findViewById(R.id.btn_take);
        mBtnTake.setOnClickListener(this);
        mBtnSwitch = (Button) findViewById(R.id.btn_switch);
        mBtnSwitch.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // 相机权限
            case REQUEST_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mCameraRequested = true;
                    initView();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCameraRequested) {
            CameraUtil.startPreview();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CameraUtil.stopPreview();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take:
                takePicture();
                break;

            case R.id.btn_switch:
                switchCamera();
                break;
        }
    }

    /**
     * 拍照
     */
    private void takePicture() {
        CameraUtil.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        }, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                CameraUtil.startPreview();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                if (bitmap != null) {
                    bitmap = ImageUtils.getRotatedBitmap(bitmap, mOrientation);
                    String path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/"
                            + System.currentTimeMillis() + ".jpg";
                    try {
                        FileOutputStream fout = new FileOutputStream(path);
                        BufferedOutputStream bos = new BufferedOutputStream(fout);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                        fout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                CameraUtil.startPreview();
            }
        });
    }


    /**
     * 切换相机
     */
    private void switchCamera() {
        if (mCameraSurfaceView != null) {
            CameraUtil.switchCamera(1 - CameraUtil.getCameraID(), mCameraSurfaceView.getHolder());
            // 切换相机后需要重新计算旋转角度
            mOrientation = CameraUtil.calculateCameraPreviewOrientation(MainActivity.this);
        }
    }

}
