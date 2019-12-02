package com.baidu.idl.face.platform.model;

/**
 * 扫脸状态
 *
 * @author yline 2019/11/18 -- 15:12
 */
public enum FaceStatusEnum {
    Detect_OK,
    Detect_NoFace, // 未检测到人脸
    Detect_PoorIllumintion, // 光线再亮些
    Detect_ImageBlured, // 请保持不动
    Detect_OccLeftEye, // 脸部有遮挡
    Detect_OccRightEye, // 脸部有遮挡
    Detect_OccNose, // 脸部有遮挡
    Detect_OccMouth, // 脸部有遮挡
    Detect_OccLeftContour, // 脸部有遮挡
    Detect_OccRightContour, // 脸部有遮挡
    Detect_OccChin, // 脸部有遮挡
    Detect_PitchOutOfUpMaxRange, // 建议略微低头
    Detect_PitchOutOfDownMaxRange, // 建议略微抬头
    Detect_PitchOutOfLeftMaxRange, // 建议略微向右转头
    Detect_PitchOutOfRightMaxRange, // 建议略微向左转头
    Detect_DataNotReady, // 还未初始化
    Detect_DataHitOne,
    Detect_DataHitLast,
    Detect_FaceNotComplete,

    Detect_FaceZoomIn, // 手机拿近一点
    Detect_FaceZoomOut, // 手机拿远一点
    Detect_FacePointOut, // 把脸移入框内

    Detect_Timeout // 检测超时
}


