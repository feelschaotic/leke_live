package com.tim.shadow.ball;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ramo.campuslive.R;
import com.ramo.campuslive.SwipeBackActivity;

public class BallActivity extends SwipeBackActivity implements SensorEventListener {

    private float mPreviousY;
    private float mPreviousX;
    private float mPreviousY2;
    private float mPreviousX2;

    // 重力感应
    private SensorManager sensorManager;
    private Sensor magneticSensor;
    private Sensor accelerometerSensor;
    private Sensor gyroscopeSensor;
    // 将纳秒转化为秒
    private static final float NS2S = 1.0f / 1000000000.0f;
    private long timestamp;
    private float angle[] = new float[3];

    protected String extraName = "ballImg";
    protected LinearLayout openGLView;
    protected ImageButton changeSceneBtn;
    protected ImageButton changeInteractionWayBtn;

    GLSurfaceView mGlSurfaceView;
    Ball mBall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ball);
        initGlSurface();
        initView();
        initListener();
    }

    private void initView() {
        openGLView = (LinearLayout) findViewById(R.id.openGLView);
        changeSceneBtn = (ImageButton) findViewById(R.id.changeSceneBtn);
        changeInteractionWayBtn = (ImageButton) findViewById(R.id.changeInteractionWayBtn);
        openGLView.addView(mGlSurfaceView);
        dragOrRotating(!BallBase.isDragged);
    }

    private void initGlSurface() {
        mGlSurfaceView = new GLSurfaceView(this);
        mGlSurfaceView.setEGLContextClientVersion(2);
        mBall = new Ball(this);
        mBall.setBall1Img(getIntent().getIntExtra(extraName, BallBase.ballImgRes[0]));
        mGlSurfaceView.setRenderer(mBall);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mGlSurfaceView) {
            mGlSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mGlSurfaceView) {
            mGlSurfaceView.onPause();
        }
        unregisterSensorListener();
    }

    private void initListener() {
        changeInteractionWayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dragOrRotating(BallBase.isDragged);
                BallBase.isDragged = !BallBase.isDragged;
            }
        });
        changeSceneBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BallActivity.this, BallActivity.class);

                intent.putExtra(extraName, BallBase.ballImgRes[++BallBase.imgPos]);
                if (BallBase.imgPos >= BallBase.ballImgRes.length - 1)
                    BallBase.imgPos = -1;

                startActivity(intent);
                finish();
            }
        });
    }

    private void dragOrRotating(boolean action) {
        if (action) {
            changeInteractionWayBtn.setImageResource(R.drawable.drag_selector);
            initSensor();
        } else {
            changeInteractionWayBtn.setImageResource(R.drawable.rotating_phone_selector);
            unregisterSensorListener();
        }
    }

    private void unregisterSensorListener() {
        if (sensorManager != null)
            sensorManager.unregisterListener(BallActivity.this);
    }

    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        magneticSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometerSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // 注册陀螺仪传感器，并设定传感器向应用中输出的时间间隔类型是SensorManager.SENSOR_DELAY_GAME(20000微秒)
        // SensorManager.SENSOR_DELAY_FASTEST(0微秒)：最快。最低延迟，一般不是特别敏感的处理不推荐使用，该模式可能在成手机电力大量消耗，由于传递的为原始数据，诉法不处理好会影响游戏逻辑和UI的性能
        // SensorManager.SENSOR_DELAY_GAME(20000微秒)：游戏。游戏延迟，一般绝大多数的实时性较高的游戏都是用该级别
        // SensorManager.SENSOR_DELAY_NORMAL(200000微秒):普通。标准延时，对于一般的益智类或EASY级别的游戏可以使用，但过低的采样率可能对一些赛车类游戏有跳帧现象
        // SensorManager.SENSOR_DELAY_UI(60000微秒):用户界面。一般对于屏幕方向自动旋转使用，相对节省电能和逻辑处理，一般游戏开发中不使用
        sensorManager.registerListener(this, gyroscopeSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, magneticSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, accelerometerSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;// 计算触控笔Y位移
                float dx = x - mPreviousX;// 计算触控笔X位移
                mBall.yAngle += dx * 0.3f;// 设置填充椭圆绕y轴旋转的角度
                mBall.xAngle += dy * 0.3f;// 设置填充椭圆绕x轴旋转的角度
        }
        mPreviousY = y;// 记录触控笔位置
        mPreviousX = x;// 记录触控笔位置
        return true;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // x,y,z分别存储坐标轴x,y,z上的加速度
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            // 根据三个方向上的加速度值得到总的加速度值a
            float a = (float) Math.sqrt(x * x + y * y + z * z);
            System.out.println("a---------->" + a);
            // 传感器从外界采集数据的时间间隔为10000微秒
            System.out.println("magneticSensor.getMinDelay()-------->"
                    + magneticSensor.getMinDelay());
            // 加速度传感器的最大量程
            System.out.println("event.sensor.getMaximumRange()-------->"
                    + event.sensor.getMaximumRange());

            System.out.println("x------------->" + x);
            System.out.println("y------------->" + y);
            System.out.println("z------------->" + z);

            Log.d("TAG", "x------------->" + x);
            Log.d("TAG", "y------------>" + y);
            Log.d("TAG", "z----------->" + z);


            // showTextView.setText("x---------->" + x + "\ny-------------->" +
            // y + "\nz----------->" + z);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // 三个坐标轴方向上的电磁强度，单位是微特拉斯(micro-Tesla)，用uT表示，也可以是高斯(Gauss),1Tesla=10000Gauss
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            // 手机的磁场感应器从外部采集数据的时间间隔是10000微秒
            System.out.println("magneticSensor.getMinDelay()-------->"
                    + magneticSensor.getMinDelay());
            // 磁场感应器的最大量程
            System.out.println("event.sensor.getMaximumRange()----------->"
                    + event.sensor.getMaximumRange());
            System.out.println("x------------->" + x);
            System.out.println("y------------->" + y);
            System.out.println("z------------->" + z);
//			SensorInfo info = new SensorInfo();
//			info.setSensorX(x);
//			info.setSensorY(y);
//			info.setSensorZ(z);
//			Message msg = new Message();
//			msg.what = 101;
//			msg.obj = info;
//			mHandler.sendMessage(msg);
            //
            // Log.d("TAG","x------------->" + x);
            // Log.d("TAG", "y------------>" + y);
            // Log.d("TAG", "z----------->" + z);
            //
            // showTextView.setText("x---------->" + x + "\ny-------------->" +
            // y + "\nz----------->" + z);
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            // 从 x、y、z 轴的正向位置观看处于原始方位的设备，如果设备逆时针旋转，将会收到正值；否则，为负值
            if (timestamp != 0) {
                // 得到两次检测到手机旋转的时间差（纳秒），并将其转化为秒
                final float dT = (event.timestamp - timestamp) * NS2S;
                // 将手机在各个轴上的旋转角度相加，即可得到当前位置相对于初始位置的旋转弧度
                angle[0] += event.values[0] * dT;
                angle[1] += event.values[1] * dT;
                angle[2] += event.values[2] * dT;
                // 将弧度转化为角度
                float anglex = (float) Math.toDegrees(angle[0]);
                float angley = (float) Math.toDegrees(angle[1]);
                float anglez = (float) Math.toDegrees(angle[2]);

                System.out.println("anglex------------>" + anglex);
                System.out.println("angley------------>" + angley);
                System.out.println("anglez------------>" + anglez);
                SensorInfo info = new SensorInfo();
                info.setSensorX(angley);
                info.setSensorY(anglex);
                info.setSensorZ(anglez);
                Message msg = new Message();
                msg.what = 101;
                msg.obj = info;
                mHandler.sendMessage(msg);

                System.out.println("gyroscopeSensor.getMinDelay()----------->"
                        + gyroscopeSensor.getMinDelay());
            }
            // 将当前时间赋值给timestamp
            timestamp = event.timestamp;

        } else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {

        }

    }

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 101:
                    SensorInfo info = (SensorInfo) msg.obj;
                    float y = info.getSensorY();
                    float x = info.getSensorX();
                    float dy = y - mPreviousY2;// 计算触控笔Y位移
                    float dx = x - mPreviousX2;// 计算触控笔X位移
                    mBall.yAngle += dx * 1.0f;// 设置填充椭圆绕y轴旋转的角度
                    mBall.xAngle += dy * 1.0f;// 设置填充椭圆绕x轴旋转的角度
                    mPreviousY2 = y;// 记录触控笔位置
                    mPreviousX2 = x;// 记录触控笔位置
                    break;

                default:
                    break;
            }
        }

        ;
    };

}
