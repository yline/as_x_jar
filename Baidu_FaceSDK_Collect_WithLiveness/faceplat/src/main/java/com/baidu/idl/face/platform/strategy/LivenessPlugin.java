package com.baidu.idl.face.platform.strategy;

import com.baidu.idl.face.platform.model.LivenessTypeEnum;
import com.baidu.idl.face.platform.model.FaceExtInfo;
import com.baidu.idl.face.platform.model.FaceStatusEnum;
import com.baidu.idl.face.platform.utils.FaceExtInfoUtil;

import java.util.ArrayDeque;
import java.util.List;

/**
 * 动态操作的帮助
 *
 * @author yline 2019/11/27 -- 10:07
 */
public class LivenessPlugin {
    private long mLastTipTime; // 上次tip的时间
    private static final long TIPS_DURATION = 600; // 每个操作之间的最小间隔

    // 利用队列的特征，对首个进行依次验证；成功再移除首个
    private ArrayDeque<LivenessTypeEnum> mDataDeque;

    public LivenessPlugin() {
        mDataDeque = new ArrayDeque<>();
    }

    public void setLivenessList(List<LivenessTypeEnum> list) {
        if (null == list || list.isEmpty()) {
            return;
        }

        mDataDeque.clear();
        mDataDeque.addAll(list);
    }

    public LivenessTypeEnum getCurrentLivenessType() {
        return mDataDeque.peek();
    }

    public boolean isLivenessSuccess() {
        return mDataDeque.isEmpty();
    }

    /**
     * 处理当前的状态和脸部信息，判断是否合规
     *
     * @param extInfo 脸部信息
     * @return null 处理失败，否则，返回当前处理成功的状态
     */
    public LivenessTypeEnum process(FaceExtInfo extInfo) {
        // 如果是首次进来，返回null，让上层给一个提示时间
        if (mLastTipTime == 0) {
            mLastTipTime = System.currentTimeMillis();
            return null;
        }

        // 提示的过度时间还没过
        if (System.currentTimeMillis() - mLastTipTime < TIPS_DURATION) {
            return null;
        }

        // 脸部信息为空 或 当前没有要处理的状态
        LivenessTypeEnum currentTypeEnum = mDataDeque.peek();
        if (null == extInfo || null == currentTypeEnum) {
            return null;
        }

        // 如果匹配成功了，则清空当前的状态
        boolean isMatch = FaceExtInfoUtil.isLivenessMatch(currentTypeEnum, extInfo);
        if (isMatch) {
            mLastTipTime = System.currentTimeMillis(); // 更新处理时间
            return mDataDeque.pollFirst();
        }

        return null;
    }

    public void reset() {
        if (null != mDataDeque) {
            mDataDeque.clear();
        }
        mLastTipTime = 0;
    }

    public FaceStatusEnum checkDetect(FaceExtInfo extInfo) {
        LivenessTypeEnum typeEnum = mDataDeque.peek();
        if (null == typeEnum || null == extInfo) {
            return null;
        }
        return FaceExtInfoUtil.checkHeadValid(typeEnum, extInfo);
    }
}
