package taghere.project.helloworld.taghere.CameraSources;

import android.content.Context;

import android.util.AttributeSet;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import taghere.project.helloworld.taghere.R;

/**
 * Created by DongKyu on 2015-10-11.
 */
public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera = null;

    public CameraSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public Camera getCamera() {
        return mCamera;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("SURFACE_CREATED", "CREATED");
        try {
            releaseCameraAndPreview();
            if(mCamera == null)
                mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mHolder);
        } catch (Exception e) {
            e.printStackTrace();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * If other application use camera than release it
     */
    public void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCameraAndPreview();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
        Log.i("SURFACE_CHANGED", "CHANGED");
        Parameters params = mCamera.getParameters();
        params.setPreviewSize(720, 480);
        params.setPictureSize(720, 480);
        params.setRotation(90);
        try {
            mCamera.setParameters(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
    }
}