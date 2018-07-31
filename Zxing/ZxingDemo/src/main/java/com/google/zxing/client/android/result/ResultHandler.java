package com.google.zxing.client.android.result;

import com.google.zxing.Result;
import com.google.zxing.client.android.R;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;

/**
 * A base class for the Android-specific barcode handlers. These allow the app to polymorphically
 * suggest the appropriate actions for each data type.
 * <p>
 * This class also contains a bunch of utility methods to take common actions like opening a URL.
 * They could easily be moved into a helper object, but it can't be static because the Activity
 * instance is needed to launch an intent.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public class ResultHandler {
	private final ParsedResult result;
	
	public static ResultHandler makeResultHandler(Result rawResult) {
		ParsedResult parsedResult = ResultParser.parseResult(rawResult);
		return new ResultHandler(parsedResult);
	}
	
	private ResultHandler(ParsedResult result) {
		this.result = result;
	}
	
	/**
	 * Create a possibly styled string for the contents of the current barcode.
	 *
	 * @return The text to be displayed.
	 */
	public String getDisplayContents() {
		return result.getDisplayResult();
	}
	
	/**
	 * A convenience method to get the parsed type. Should not be overridden.
	 *
	 * @return The parsed type, e.g. URI or ISBN
	 */
	public final ParsedResultType getType() {
		return result.getType();
	}
}
