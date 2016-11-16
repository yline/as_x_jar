package com.rxandroid.activity;

import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.rxandroid.R;
import com.yline.base.BaseAppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func0;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class MainActivity extends BaseAppCompatActivity
{
	private static final String TAG = "RxAndroidSamples";

	private Looper backgroundLooper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_activity);

		BackgroundThread backgroundThread = new BackgroundThread();
		backgroundThread.start();
		backgroundLooper = backgroundThread.getLooper();

		findViewById(R.id.button_run_scheduler).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onRunSchedulerExampleButtonClicked();
			}
		});
	}

	private void onRunSchedulerExampleButtonClicked()
	{
		sampleObservable()
				// Run on a background thread
				.subscribeOn(AndroidSchedulers.from(backgroundLooper))
				// Be notified on the main thread
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<String>()
				{
					@Override
					public void onCompleted()
					{
						Log.d(TAG, "onCompleted()");
					}

					@Override
					public void onError(Throwable e)
					{
						Log.e(TAG, "onError()", e);
					}

					@Override
					public void onNext(String string)
					{
						Log.d(TAG, "onNext(" + string + ")");
					}
				});
	}
	
	private rx.Observable<String> sampleObservable()
	{
		return rx.Observable.defer(new Func0<rx.Observable<String>>()
		{
			@Override
			public rx.Observable<String> call()
			{
				try
				{
					// Do some long running operation
					Thread.sleep(TimeUnit.SECONDS.toMillis(5));
				}
				catch (InterruptedException e)
				{
					throw OnErrorThrowable.from(e);
				}
				return rx.Observable.just("one", "two", "three", "four", "five");
			}
		});
	}

	private class BackgroundThread extends HandlerThread
	{
		BackgroundThread()
		{
			super("SchedulerSample-BackgroundThread", THREAD_PRIORITY_BACKGROUND);
		}
	}
}
