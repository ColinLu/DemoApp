package com.colin.demo.app.activity.sensor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Message;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.colin.demo.app.R;
import com.colin.demo.app.base.BaseActivity;
import com.colin.demo.app.bean.ItemBean;
import com.colin.demo.app.other.MyHandler;
import com.colin.demo.app.utils.ToastUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SensorActivity extends BaseActivity implements View.OnClickListener {
    private ItemBean mItemBean;
    private TextView text_show_sensor_log;
    private SensorManager mSensorManager;
    private Sensor mSensor;                                 //传感器
    private MySensorEventListener mySensorEventListener;    //传感器监听变化
    private boolean isUnregisterListener = false;           //是否注册传感器监听

    //接收消息 输出打印日志  主线程操作
    @SuppressLint("HandlerLeak")
    private MyHandler mMyHandler = new MyHandler(this) {
        @Override
        public void weakHandleMessage(Message message) {
            if (isDestroy || null == message || null == message.obj) {
                return;
            }
            text_show_sensor_log.append("\n" + message.obj);
            int offset = text_show_sensor_log.getLineCount() * text_show_sensor_log.getLineHeight();
            if (offset > (text_show_sensor_log.getHeight() - text_show_sensor_log.getLineHeight() - 20)) {
                text_show_sensor_log.scrollTo(0, offset - text_show_sensor_log.getHeight() + text_show_sensor_log.getLineHeight() + 20);
            }
        }
    };

    @Override
    protected void onDestroy() {
        mItemBean = null;
        if (null != mSensorManager) {
            mSensorManager = null;
        }
        mySensorEventListener = null;
        isUnregisterListener = false;
        mMyHandler.removeCallbacksAndMessages(null);
        mMyHandler = null;
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
    }

    /**
     * 重新开始 注册
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (isUnregisterListener && null != mSensor && null != mSensorManager && null != mySensorEventListener) {
            registerListener();
        }
    }

    /**
     * 暂停 取消注册
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (!isUnregisterListener && null != mSensorManager && null != mySensorEventListener) {
            mSensorManager.unregisterListener(mySensorEventListener);
            isUnregisterListener = true;
        }

    }

    @Override
    protected void initView() {
        text_show_sensor_log = this.findViewById(R.id.text_show_sensor_log);
        text_show_sensor_log.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mItemBean = bundle.getParcelable("clazz");
        }

        setTitle(null == mItemBean ? "" : mItemBean.title);

        if (null == mSensorManager) {
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        }
        printSensorListMessage(getSensorList());
    }


    @Override
    protected void initListener() {
        findViewById(R.id.button_sensor_gyro).setOnClickListener(this);

    }

    @Override
    protected void initAsync() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sensor_gyro:
                showGyroSensor();
                break;
            default:
                break;
        }
    }

    /**
     * 显示操作陀螺仪传感器
     */
    private void showGyroSensor() {
        // 获取传感器管理器
        if (null == mSensorManager) {
            ToastUtil.showToast("获取失败");
            return;
        }
        // 获取陀螺仪感器
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (null == mSensor) {
            ToastUtil.showToast("获取陀螺仪失败");
            return;
        }

        if (!isUnregisterListener && null != mySensorEventListener) {
            mSensorManager.unregisterListener(mySensorEventListener);
        }
        setText("陀螺仪传感器监听");
        printSensorListMessage(mSensor);
        registerListener();
    }


    private void registerListener() {
        if (null != mSensor && null != mSensorManager) {
            if (null == mySensorEventListener) {
                mySensorEventListener = new MySensorEventListener();
            }
            mSensorManager.registerListener(mySensorEventListener, mSensor, 5 * 1000 * 1000);
            isUnregisterListener = false;
        }
    }

    private List<Sensor> getSensorList() {
        // 获取传感器管理器
        // 获取传感器管理器
        if (null == mSensorManager) {
            ToastUtil.showToast("获取失败");
            return null;
        }
        // 获取全部传感器列表
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
//        Collections.sort(sensorList, new Comparator<Sensor>() {
//            @Override
//            public int compare(Sensor o1, Sensor o2) {
//                return o1.getType() - o2.getType();
////                if (i >= 0) {
////                    return 1;
////                }
////                return -1;
//            }
//        });
        return sensorList;
    }


    /**
     * 显示标题内容
     *
     * @param title
     */
    private void setText(String title) {
        text_show_sensor_log.setText(title + '\n');
        text_show_sensor_log.scrollTo(0, 0);
        text_show_sensor_log.requestLayout();
    }

    private void printSensorListMessage(Sensor sensor) {
        if (null == sensor) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" Sensor Type - " + sensor.getType() + "\n");
        stringBuilder.append(" Sensor Name - " + sensor.getName() + "\n");
        stringBuilder.append(" Sensor Version - " + sensor.getVersion() + "\n");
        stringBuilder.append(" Sensor Vendor - " + sensor.getVendor() + "\n");
        stringBuilder.append(" Maximum Range - " + sensor.getMaximumRange() + "\n");
        stringBuilder.append(" Power - " + sensor.getPower() + "\n");
        stringBuilder.append(" Resolution - " + sensor.getResolution() + "\n");
        stringBuilder.append("\n");
        sendMessage(stringBuilder.toString());
    }

    private void printSensorListMessage(List<Sensor> sensorList) {
        setText("感应器信息");
        // 打印每个传感器信息
        StringBuilder strLog = new StringBuilder();
        if (null == sensorList || sensorList.size() == 0) {
            return;
        }
        int iIndex = 1;
        for (Sensor item : sensorList) {
            strLog.append(iIndex + "." + "\n\n");
            strLog.append(" Sensor Type - " + item.getType() + "\n");
            strLog.append(" Sensor Name - " + item.getName() + "\n");
            strLog.append(" Sensor Version - " + item.getVersion() + "\n");
            strLog.append(" Sensor Vendor - " + item.getVendor() + "\n");
            strLog.append(" Maximum Range - " + item.getMaximumRange() + "\n");
            strLog.append(" Minimum Delay - " + item.getMinDelay() + "\n");
            strLog.append(" Power - " + item.getPower() + "\n");
            strLog.append(" Resolution - " + item.getResolution() + "\n");
            strLog.append("\n");
            iIndex++;
        }
        sendMessage(strLog.toString());
    }

    /**
     * 传感器监听变化
     */
    private class MySensorEventListener implements SensorEventListener {
        private static final float NS2S = 1.0f / 1000000000.0f;
        private float timestamp;
        private float angle[] = new float[3];

        private float angleX;
        private float angleY;
        private float angleZ;

        public MySensorEventListener() {
            angleX = 0;
            angleY = 0;
            angleZ = 0;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            synchronized (this) {
                if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    showLogGypo(event);
                }
                //将当前时间赋值给timestamp
                timestamp = event.timestamp;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        //从 x、y、z 轴的正向位置观看处于原始方位的设备，如果设备逆时针旋转，将会收到正值；否则，为负值
        private void showLogGypo(SensorEvent event) {
            if (timestamp != 0) {
                // 得到两次检测到手机旋转的时间差（纳秒），并将其转化为秒
                final float dT = (event.timestamp - timestamp) * NS2S;
                // 将手机在各个轴上的旋转角度相加，即可得到当前位置相对于初始位置的旋转弧度
                angle[0] += event.values[0] * dT;
                angle[1] += event.values[1] * dT;
                angle[2] += event.values[2] * dT;
                // 将弧度转化为角度
                float angleX = (float) Math.toDegrees(angle[0]);
                float angleY = (float) Math.toDegrees(angle[1]);
                float angleZ = (float) Math.toDegrees(angle[2]);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\n")
                        .append("angleX:").append('\t').append(angleX).append('\n')
                        .append("angleY:").append('\t').append(angleY).append('\n')
                        .append("angleZ:").append('\t').append(angleZ).append('\n')
                        .append("-------changed------").append('\n')
                        .append("X").append(this.angleX == 0 ? "--------" : String.valueOf(angleX - this.angleX)).append('\n')
                        .append("Y").append(this.angleY == 0 ? "--------" : String.valueOf(angleY - this.angleY)).append('\n')
                        .append("Z").append(this.angleZ == 0 ? "--------" : String.valueOf(angleZ - this.angleZ)).append('\n')
                        .append("时间:").append('\t').append(event.timestamp).append("\n");
                this.angleX = angleX;
                this.angleY = angleY;
                this.angleZ = angleZ;
                sendMessage(stringBuilder.toString());
            }
        }
    }

    /**
     * 发送消息内容
     *
     * @param messageObj
     */
    private void sendMessage(@NonNull String messageObj) {
        Message message = Message.obtain();
        message.obj = messageObj;
        mMyHandler.sendMessage(message);
    }

}