package com.baidu.idl.face.platform.config;

import com.baidu.idl.face.platform.model.LivenessTypeEnum;
import com.baidu.idl.face.platform.model.FaceStatusEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 本工程，可配置的内容
 *
 * @author yline 2019/11/27 -- 17:41
 */
public class FaceFlatConfig {
    private static final List<LivenessTypeEnum> DEFAULT_TYPE_LIST = new ArrayList<>(); // 活体检测默认的内容
    private static final String SDK_VERSION = "3.3.0.0";

    private static final long DETECT_MAX_TIME = 0; // 单次检测的最大时间
    private static final long DETECT_NO_FACE_MAX_TIME = 15_000; // 单次检测无脸，的最大时间

    private static final long SOUND_MIN_DURATION = 3000; // 播放相同的音频的间隔时间，ms

    private static HashMap<String, Integer> mTipsMap = new HashMap<>(); // 资源文件
    private static HashMap<String, Integer> mSoundMap = new HashMap<>(); // 资源文件

    private static boolean isSound = true; // 是否开启提示音
    private static boolean isLivenessTypeRandom = false; // 是否随机活体检测动作
    private static List<LivenessTypeEnum> livenessTypeList = new ArrayList<>(); // 活体检测的动作类型列表

    static {
        DEFAULT_TYPE_LIST.add(LivenessTypeEnum.Liveness_Eye);
        DEFAULT_TYPE_LIST.add(LivenessTypeEnum.Liveness_Mouth);
        DEFAULT_TYPE_LIST.add(LivenessTypeEnum.Liveness_HeadLeftOrRight);

        livenessTypeList.clear();
        livenessTypeList.addAll(DEFAULT_TYPE_LIST);
    }

    public static void putSoundId(Object object, int soundId) {
        String key = object2String(object);
        mSoundMap.put(key, soundId);
    }

    public static int getSoundId(Object object) {
        String key = object2String(object);
        Integer value = mSoundMap.get(key);
        return null == value ? -1 : value;
    }

    public static void putTipsId(Object object, int tipsId) {
        String key = object2String(object);
        mTipsMap.put(key, tipsId);
    }

    public static int getTipsId(Object object) {
        String key = object2String(object);
        Integer value = mTipsMap.get(key);
        return null == value ? -1 : value;
    }

    private static String object2String(Object obj) {
        if (obj instanceof FaceStatusEnum) {
            return ((FaceStatusEnum) obj).name();
        } else if (obj instanceof LivenessTypeEnum) {
            return ((LivenessTypeEnum) obj).name();
        }
        return null;
    }

    public static String getVersion() {
        return SDK_VERSION;
    }

    public static long getDetectMaxTime() {
        return DETECT_MAX_TIME;
    }

    public static long getDetectNoFaceMaxTime() {
        return DETECT_NO_FACE_MAX_TIME;
    }

    public static long getSoundMinDuration() {
        return SOUND_MIN_DURATION;
    }

    public static boolean isSound() {
        return isSound;
    }

    public static void setIsSound(boolean sound) {
        FaceFlatConfig.isSound = sound;
    }

    public static boolean isIsLivenessTypeRandom() {
        return isLivenessTypeRandom;
    }

    public static void setIsLivenessTypeRandom(boolean isLivenessTypeRandom) {
        FaceFlatConfig.isLivenessTypeRandom = isLivenessTypeRandom;
    }

    public static List<LivenessTypeEnum> getLivenessTypeList() {
        return new ArrayList<>(livenessTypeList);
    }

    public static void setLivenessTypeList(List<LivenessTypeEnum> typeList) {
        if (null == typeList || typeList.isEmpty()) {
            livenessTypeList.clear();
            livenessTypeList.addAll(DEFAULT_TYPE_LIST);
        } else {
            livenessTypeList.clear();
            livenessTypeList.addAll(typeList);
        }
    }
}
