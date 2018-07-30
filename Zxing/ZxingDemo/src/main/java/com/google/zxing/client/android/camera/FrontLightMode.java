package com.google.zxing.client.android.camera;

import com.zxing.demo.manager.DBManager;

/**
 * Enumerates settings of the preference controlling the front light.
 */
public enum FrontLightMode {
	
	/**
	 * Always on.
	 */
	ON,
	/**
	 * On only when ambient light is low.
	 */
	AUTO,
	/**
	 * Always off.
	 */
	OFF;
	
	public static FrontLightMode readPref() {
		String modeString = DBManager.getInstance().getFrontLightMode();
		return (modeString == null ? OFF : valueOf(modeString));
	}
}
