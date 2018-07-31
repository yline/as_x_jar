package com.zxing.demo.capture;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.google.zxing.client.android.camera.CameraManager;

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
	
	private final Context context;
	
	private CameraManager cameraManager;
	
	private Sensor lightSensor;
	
	public AmbientLightHelper(Context context) {
		this.context = context;
	}
	
	public void start(CameraManager cameraManager) {
		this.cameraManager = cameraManager;
		if (getLightMode() == LightMode.AUTO) {
			SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
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
			SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
			if (null != sensorManager) {
				sensorManager.unregisterListener(this);
			}
			cameraManager = null; // 关闭引用
			lightSensor = null;
		}
	}
	
	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		float ambientLightLux = sensorEvent.values[0];
		if (cameraManager != null) {
			if (ambientLightLux <= TOO_DARK_LUX) {
				cameraManager.setTorch(true);
			} else if (ambientLightLux >= BRIGHT_ENOUGH_LUX) {
				cameraManager.setTorch(false);
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
