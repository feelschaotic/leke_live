package com.juss.live.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.hardware.Camera.Size;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.letv.recorder.bean.CameraParams;
import com.letv.recorder.callback.PublishListener;
import com.letv.recorder.callback.VideoRecorderDeviceListener;
import com.letv.recorder.controller.Publisher;
import com.letv.recorder.util.LeLog;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class LeGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer,OnFrameAvailableListener,VideoRecorderDeviceListener {

	private SurfaceTexture surfaceTexture;
	private Publisher publisher;
	private Context context;
	private DirectDrawer mDirectDrawer;  
	private boolean isVertical = false;
	
	public LeGLSurfaceView(Context context,boolean isVertical) {
		super(context);
		this.context = context; 
		setEGLContextClientVersion(2);
		setRenderer(this);
		setRenderMode(RENDERMODE_WHEN_DIRTY);
		this.isVertical = isVertical;
		init();
	}
	private void init() {
		publisher = Publisher.getInstance();
		publisher.initPublisher((Activity) context);
		publisher.getVideoRecordDevice().setVideoRecorderDeviceListener(this);
		publisher.getRecorderContext().setUseLanscape(isVertical);
		publisher.setPublishListener((PublishListener) this.context);
		publisher.setCameraView(this);
		publisher.setUrl("rtmp://216.mpush.live.lecloud.com/live/glSurfaceView");
	}
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		int textureId = createTextureID();
		surfaceTexture = new SurfaceTexture(textureId);
		mDirectDrawer = new DirectDrawer(textureId,isVertical);  
		surfaceTexture.setOnFrameAvailableListener(this);
		publisher.getVideoRecordDevice().bindingTexture(surfaceTexture);
		publisher.getVideoRecordDevice().start();
		((Activity)context).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				initPublish();
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		publisher.stopPublish();
		publisher.getVideoRecordDevice().stop();
	}
	public void onDestroy(){
		publisher.release();
	}
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height); 
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);  
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);  
        surfaceTexture.updateTexImage();  
        float[] mtx = new float[16];  
       	surfaceTexture.getTransformMatrix(mtx);  
        mDirectDrawer.draw(mtx);
	}
	
	private int createTextureID()  
    {  
        int[] texture = new int[1];  
  
        GLES20.glGenTextures(1, texture, 0);  
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);  
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);          
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);  
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);  
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);  
        return texture[0];  
    }
	private void initPublish() {
				publisher.publish();
	}
	@Override
	public void onFrameAvailable(SurfaceTexture surfaceTexture) {
		 this.requestRender();
	}
	@Override
	public void onSetFps(int cameraId, List<int[]> range,CameraParams cameraParams) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSetPreviewSize(int cameraId, List<Size> previewSizes,CameraParams cameraParams) {
		float ratio = 16.0f / 9.0f;
		float appropriate = 100;
		Size s = null;
		for (Size size : previewSizes) {
//			 Log.d(TAG,"可选择选择录制的视频有：宽为:"+size.width+",高："+size.height+"；比例："+((float)size.width)/size.height);
			if (size.width <= 1000) { // / 选择比较低的分辨率，保证推流不卡
				if (Math.abs(((float) size.width) / size.height - ratio) < appropriate) {
					appropriate = Math.abs(((float) size.width) / size.height - ratio);
					s = size;
				}
			}
		}
		cameraParams.setWidth(s.width);
		cameraParams.setHeight(s.height);
		LeLog.d("LeGLSurfaceView", "选择录制的视频宽为:" + s.width + ",高：" + s.height);

	}  
}
