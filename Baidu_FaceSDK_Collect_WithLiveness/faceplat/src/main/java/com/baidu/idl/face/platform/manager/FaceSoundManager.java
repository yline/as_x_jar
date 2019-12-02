package com.baidu.idl.face.platform.manager;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.SoundPool;
import android.net.Uri;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.config.FaceFlatConfig;
import com.baidu.idl.face.platform.injector.LogManager;
import com.baidu.idl.face.platform.model.FaceStatusEnum;
import com.baidu.idl.face.platform.model.LivenessTypeEnum;

/**
 * 声音管理类
 * 1，支持多音源，同时播放；
 * 2，播放是异步的；
 *
 * @author yline 2019/11/25 -- 18:38
 */
public class FaceSoundManager {
    private static FaceSoundManager sInstance;

    public static FaceSoundManager getInstance() {
        if (null == sInstance) {
            synchronized (FaceSoundManager.class) {
                sInstance = new FaceSoundManager();
            }
        }
        return sInstance;
    }

    private static final int MAX_STREAMS = 5;
    private final long TIME_TIPS_REPEAT;
    private SoundPool mSoundPool;

    private long mLastPlayTime;
    private long mLastPlayDuration;
    private int mLastRawId;
    private SparseArray<Long> mPayDurationArray = new SparseArray<>();
    private SparseIntArray mPoolCacheArray = new SparseIntArray();

    private FaceSoundManager() {
        TIME_TIPS_REPEAT = FaceFlatConfig.getSoundMinDuration(); // 播放相同的音频的间隔时间

        if (mSoundPool == null) {
            // 5.0 及 之后
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes.Builder sBuilder = new AudioAttributes.Builder();
                sBuilder.setUsage(AudioAttributes.USAGE_MEDIA);
                sBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);

                mSoundPool = new SoundPool.Builder()
                        .setMaxStreams(MAX_STREAMS)
                        .setAudioAttributes(sBuilder.build())
                        .build();
            } else { // 5.0 以前
                mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);  // 创建SoundPool
            }
        }
    }

    public long getPlayDuration() {
        return mLastPlayDuration;
    }

    public void release() {
        if (null != mSoundPool) {
            for (int i = 0; i < mPoolCacheArray.size(); i++) {
                mSoundPool.unload(mPoolCacheArray.valueAt(i));
            }
            mSoundPool.release();
            mPoolCacheArray.clear();
            mSoundPool = null;
        }
    }

    public void play(Context context, FaceStatusEnum statusEnum) {
        play(context, FaceSDKManager.getInstance().getSoundId(statusEnum));
    }

    public void play(Context context, LivenessTypeEnum typeEnum) {
        play(context, FaceSDKManager.getInstance().getSoundId(typeEnum));
    }

    private void play(Context context, int rawId) {
        // 资源文件异常
        if (rawId <= 0) {
            LogManager.e("rawId = " + rawId);
            return;
        }

        // 是否支持音频播放
        if (!FaceSDKManager.getInstance().isSound()) {
            LogManager.e("sound is closed");
            return;
        }

        playInner(context, rawId);
    }

    private void playInner(Context context, final int rawId) {
        // 上个音频还未播放完毕
        long costTime = System.currentTimeMillis() - mLastPlayTime;
        if (costTime < mLastPlayDuration) {
            LogManager.e("raw still playing, costTime = " + costTime + ", rawId = " + mLastRawId);
            return;
        }

        // 重复播放，并且还未达到间隔时间
        if (rawId == mLastRawId && costTime < TIME_TIPS_REPEAT) {
            LogManager.e("raw double play, costTime = " + costTime + ", rawId = " + mLastRawId);
            return;
        }

        mLastRawId = rawId;
        mLastPlayDuration = getSoundDurationInner(context, rawId);
        mLastPlayTime = System.currentTimeMillis();

        int cacheSoundId = mPoolCacheArray.get(rawId);
        LogManager.v("play rawId = " + rawId + ", soundId = " + cacheSoundId + ", duration = " + mLastPlayDuration);
        if (cacheSoundId == 0) {
            // 从未播放过
            final int finalCacheSoundId = mSoundPool.load(context, rawId, 1);

            // 加载完毕，就播放
            mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    if (status == 0 && sampleId == finalCacheSoundId) {
                        mPoolCacheArray.put(rawId, finalCacheSoundId);
                        playSoundInner(finalCacheSoundId);
                    }
                }
            });
        } else {
            // 已经播放过
            playSoundInner(cacheSoundId);
        }
    }

    private void playSoundInner(int soundId) {
        try {
            mSoundPool.play(soundId, 1.0f, 1.0f, MAX_STREAMS, 0, 1.0f);
        } catch (Exception ex) {
            LogManager.e(android.util.Log.getStackTraceString(ex));
        }
    }

    private long getSoundDurationInner(Context context, int rawId) {
        Long value = mPayDurationArray.get(rawId);
        if (null != value) {
            return value;
        }

        try {
            MediaMetadataRetriever mmRetriever = new MediaMetadataRetriever();

            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + rawId);
            mmRetriever.setDataSource(context, uri);
            String duration = mmRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long result = Long.parseLong(duration);
            mPayDurationArray.put(rawId, result);

            return result;
        } catch (Exception ex) {
            LogManager.e(android.util.Log.getStackTraceString(ex));
            return 600;
        }
    }
}
