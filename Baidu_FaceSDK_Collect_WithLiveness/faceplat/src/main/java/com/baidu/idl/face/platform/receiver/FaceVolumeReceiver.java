package com.baidu.idl.face.platform.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

/**
 * 监听系统音量
 *
 * @author yline 2019/11/19 -- 14:11
 */
public class FaceVolumeReceiver extends BroadcastReceiver {
    private static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";

    private VolumeCallback mVolumeCallback;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent && VOLUME_CHANGED_ACTION.equals(intent.getAction())) {
            if (null != mVolumeCallback) {
                mVolumeCallback.onVolumeChanged();
            }
        }
    }

    public interface VolumeCallback {
        /**
         * 系统音量修改了
         */
        void onVolumeChanged();
    }

    public void setOnVolumeVolumeCallback(VolumeCallback callback) {
        this.mVolumeCallback = callback;
    }

    public static FaceVolumeReceiver registerReceiver(Context context) {
        FaceVolumeReceiver receiver = new FaceVolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(VOLUME_CHANGED_ACTION);
        context.registerReceiver(receiver, filter);
        return receiver;
    }

    public static void unRegisterReceiver(Context context, FaceVolumeReceiver receiver) {
        if (null != context && null != receiver) {
            context.unregisterReceiver(receiver);
        }
    }

    /**
     * 获取当前音量是否大于0，可以播放
     *
     * @param context 上下文
     * @return true 有音量
     */
    public static boolean isAudioSound(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (null == audioManager) {
            return false;
        }

        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return volume > 0;
    }
}
