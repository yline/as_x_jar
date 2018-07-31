package com.google.zxing.client.android;

import android.content.Intent;

/**
 * This class provides the constants to use when sending an Intent to Barcode Scanner.
 * These strings are effectively API and cannot be changed.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class Intents {
	private Intents() {
	}
	
	/**
	 * Constants related to the {@link Scan#ACTION} Intent.
	 */
	public static final class Scan {
		/**
		 * Send this intent to open the Barcodes app in scanning mode, find a barcode, and return
		 * the results.
		 */
		public static final String ACTION = "com.google.zxing.client.android.SCAN";
		
		/**
		 * Optional parameter to specify the id of the camera from which to recognize barcodes.
		 * Overrides the default camera that would otherwise would have been selected.
		 * If provided, should be an int.
		 */
		public static final String CAMERA_ID = "SCAN_CAMERA_ID";
		
		/**
		 * Optional parameters to specify the width and height of the scanning rectangle in pixels.
		 * The app will try to honor these, but will clamp them to the size of the preview frame.
		 * You should specify both or neither, and pass the size as an int.
		 */
		public static final String WIDTH = "SCAN_WIDTH";
		
		public static final String HEIGHT = "SCAN_HEIGHT";
		
		/**
		 * Desired duration in milliseconds for which to pause after a successful scan before
		 * returning to the calling intent. Specified as a long, not an integer!
		 * For example: 1000L, not 1000.
		 */
		public static final String RESULT_DISPLAY_DURATION_MS = "RESULT_DISPLAY_DURATION_MS";
		
		/**
		 * Prompt to show on-screen when scanning by intent. Specified as a {@link String}.
		 */
		public static final String PROMPT_MESSAGE = "PROMPT_MESSAGE";
		
		/**
		 * If a barcode is found, Barcodes returns {@link android.app.Activity#RESULT_OK} to
		 * {@link android.app.Activity#onActivityResult(int, int, Intent)}
		 * of the app which requested the scan via
		 * {@link android.app.Activity#startActivityForResult(Intent, int)}
		 * The barcodes contents can be retrieved with
		 * {@link Intent#getStringExtra(String)}.
		 * If the user presses Back, the result code will be {@link android.app.Activity#RESULT_CANCELED}.
		 */
		public static final String RESULT = "SCAN_RESULT";
		
		/**
		 * Call {@link Intent#getStringExtra(String)} with {@code RESULT_FORMAT}
		 * to determine which barcode format was found.
		 * See {@link com.google.zxing.BarcodeFormat} for possible values.
		 */
		public static final String RESULT_FORMAT = "SCAN_RESULT_FORMAT";
		
		/**
		 * Call {@link Intent#getStringExtra(String)} with {@code RESULT_UPC_EAN_EXTENSION}
		 * to return the content of any UPC extension barcode that was also found. Only applicable
		 * to {@link com.google.zxing.BarcodeFormat#UPC_A} and {@link com.google.zxing.BarcodeFormat#EAN_13}
		 * formats.
		 */
		public static final String RESULT_UPC_EAN_EXTENSION = "SCAN_RESULT_UPC_EAN_EXTENSION";
		
		/**
		 * Call {@link Intent#getByteArrayExtra(String)} with {@code RESULT_BYTES}
		 * to get a {@code byte[]} of raw bytes in the barcode, if available.
		 */
		public static final String RESULT_BYTES = "SCAN_RESULT_BYTES";
		
		/**
		 * Key for the value of {@link com.google.zxing.ResultMetadataType#ORIENTATION}, if available.
		 * Call {@link Intent#getIntArrayExtra(String)} with {@code RESULT_ORIENTATION}.
		 */
		public static final String RESULT_ORIENTATION = "SCAN_RESULT_ORIENTATION";
		
		/**
		 * Key for the value of {@link com.google.zxing.ResultMetadataType#ERROR_CORRECTION_LEVEL}, if available.
		 * Call {@link Intent#getStringExtra(String)} with {@code RESULT_ERROR_CORRECTION_LEVEL}.
		 */
		public static final String RESULT_ERROR_CORRECTION_LEVEL = "SCAN_RESULT_ERROR_CORRECTION_LEVEL";
		
		/**
		 * Prefix for keys that map to the values of {@link com.google.zxing.ResultMetadataType#BYTE_SEGMENTS},
		 * if available. The actual values will be set under a series of keys formed by adding 0, 1, 2, ...
		 * to this prefix. So the first byte segment is under key "SCAN_RESULT_BYTE_SEGMENTS_0" for example.
		 * Call {@link Intent#getByteArrayExtra(String)} with these keys.
		 */
		public static final String RESULT_BYTE_SEGMENTS_PREFIX = "SCAN_RESULT_BYTE_SEGMENTS_";
		
		private Scan() {
		}
	}
	
	// Not the best place for this, but, better than a new class
	// Should be FLAG_ACTIVITY_NEW_DOCUMENT in API 21+.
	// Defined once here because the current value is deprecated, so generates just one warning
	public static final int FLAG_NEW_DOC = Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
}
