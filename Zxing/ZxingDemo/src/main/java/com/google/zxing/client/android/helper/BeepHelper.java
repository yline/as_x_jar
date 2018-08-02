package com.google.zxing.client.android.helper;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.google.zxing.client.android.R;
import com.zxing.demo.MainApplication;

import java.io.Closeable;
import java.io.IOException;

/**
 * 使用：
 * onCreate --> new BeepManager(this);
 * onResume --> updatePrefs();
 * onPause --> close();
 * 调用：playBeepSoundAndVibrate()
 * <p>
 * 播放 扫描成功 的声音和震动
 *
 * @author yline
 * @times 2018/7/31 -- 16:47
 */
public final class BeepHelper implements MediaPlayer.OnErrorListener, Closeable {
	private static final boolean IS_PLAY_BEEP = true; // 是否 播放 声音
	
	private static final boolean IS_VIBRATE = false; // 是否 震动
	
	private static final float BEEP_VOLUME = 0.10f; // 声音 大小
	
	private static final long VIBRATE_DURATION = 200L; // 震动时长
	
	private final Context context;
	
	private MediaPlayer mediaPlayer;
	
	private boolean playBeep;
	
	public BeepHelper(Context context) {
		this.context = context;
		this.mediaPlayer = null;
		updatePrefs();
	}
	
	public synchronized void updatePrefs() {
		playBeep = shouldBeep();
		if (playBeep && mediaPlayer == null) {
			//	activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = buildMediaPlayer();
		}
	}
	
	public synchronized void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		
		if (IS_VIBRATE) {
			Vibrator vibrator = (Vibrator) MainApplication.getApplication().getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}
	
	@Override
	public synchronized boolean onError(MediaPlayer mp, int what, int extra) {
		if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
			// we are finished, so put up an appropriate error toast if required and finish
			if (context instanceof Activity) {
				((Activity) context).finish();
			}
		} else {
			// possibly media player error, so release and recreate
			close();
			updatePrefs();
		}
		return true;
	}
	
	@Override
	public synchronized void close() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	
	private MediaPlayer buildMediaPlayer() {
		MediaPlayer mediaPlayer = new MediaPlayer();
		try (AssetFileDescriptor file = MainApplication.getApplication().getResources().openRawResourceFd(R.raw.beep)) {
			mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
			mediaPlayer.setOnErrorListener(this);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setLooping(false);
			mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
			mediaPlayer.prepare();
			return mediaPlayer;
		} catch (IOException ioe) {
			mediaPlayer.release();
			return null;
		}
	}
	
	private static boolean shouldBeep() {
		boolean shouldPlayBeep = IS_PLAY_BEEP;
		if (IS_PLAY_BEEP) {
			// See if sound settings overrides this
			AudioManager audioService = (AudioManager) MainApplication.getApplication().getSystemService(Context.AUDIO_SERVICE);
			if (null != audioService && audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
				shouldPlayBeep = false;
			}
		}
		return shouldPlayBeep;
	}
}
