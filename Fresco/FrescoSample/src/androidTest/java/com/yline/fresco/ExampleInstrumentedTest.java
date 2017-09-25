package com.yline.fresco;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "xxx-jUnitTest";

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        Random random = new Random();
        int result = 0;
        int app = 0;
        for (int i = 0; i < 50; i++) {
            result = random.nextInt(Integer.MAX_VALUE) % 15;
            Log.i(TAG, "useAppContext: " + result);
            if (result > 0) {
                app++;
            }
        }
        assertEquals("com.yline.fresco", appContext.getPackageName());

        Log.i(TAG, "useAppContext: " + String.format("%02d", 123));
        Log.i(TAG, "useAppContext: app = " + app);

    }
}
