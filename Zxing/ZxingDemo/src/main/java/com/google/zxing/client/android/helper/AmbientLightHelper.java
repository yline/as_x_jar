package com.google.zxing.client.android.helper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.google.zxing.client.android.camera.CameraManager;
import com.zxing.demo.MainApplication;

/**
 * 使用：
 * onCreate --> new AmbientLightHelper(this)
 * onResume --> start(cameraManager)
 * onPause --> stop()
 * Detects ambient light and switches on the front light when very dark, and off again when sufficiently light.
 * 闪光灯自动管理
 *
 * @author yline
 * @times 2018/7/31 -- 17:23
 */
public final class AmbientLightHelper implements SensorEventListener {
	private static final float TOO_DARK_LUX = 45.0f; // 暗的程度
	
	private static final float BRIGHT_ENOUGH_LUX = 450.0f; // 亮的程度
	
	private Sensor lightSensor;
	
	private boolean isRunning = false;
	
	public void start() {
		isRunning = true;
		if (getLightMode() == LightMode.AUTO) {
			SensorManager sensorManager = (SensorManager) MainApplication.getApplication().getSystemService(Context.SENSOR_SERVICE);
			if (null != sensorManager) {
				lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
				if (null != lightSensor) {
					sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
				}
			}
		}
	}
	
	public void stop() {
		if (null != lightSensor) {
			SensorManager sensorManager = (SensorManager) MainApplication.getApplication().getSystemService(Context.SENSOR_SERVICE);
			if (null != sensorManager) {
				sensorManager.unregisterListener(this);
			}
			isRunning = false;
			lightSensor = null;
		}
	}
	
	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		float ambientLightLux = sensorEvent.values[0];
		if (isRunning) {
			if (ambientLightLux <= TOO_DARK_LUX) {
				CameraManager.getInstance().setTorch(true);
			} else if (ambientLightLux >= BRIGHT_ENOUGH_LUX) {
				CameraManager.getInstance().setTorch(false);
			}
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// do nothing
	}
	
	/**
	 * 直接设置 闪光灯模式
	 *
	 * @return 默认设置的模式
	 */
	public static LightMode getLightMode() {
		return LightMode.OFF;
	}
	
	public enum LightMode {
		/**
		 * Always on.
		 */
		ON, /**
		 * On only when ambient light is low.
		 */
		AUTO, /**
		 * Always off.
		 */
		OFF;
	}
}
