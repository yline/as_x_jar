package com.baidu.idl.face.platform.ui.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.injector.LogManager;
import com.baidu.idl.face.platform.model.LivenessTypeEnum;
import com.baidu.idl.face.platform.ui.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FaceSettingsActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, FaceSettingsActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private CheckBox mCbRandom;
    private List<LivenessTypeEnum> mLivenessList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faceui_activity_settings);

        mCbRandom = this.findViewById(R.id.settings_liveness_random);

        CheckBox mCb1 = findViewById(R.id.settings_liveness_cb1);
        CheckBox mCb2 = findViewById(R.id.settings_liveness_cb2);
        CheckBox mCb3 = findViewById(R.id.settings_liveness_cb3);
        CheckBox mCb4 = findViewById(R.id.settings_liveness_cb4);
        CheckBox mCb5 = findViewById(R.id.settings_liveness_cb5);
        CheckBox mCb6 = findViewById(R.id.settings_liveness_cb6);
        CheckBox mCb7 = findViewById(R.id.settings_liveness_cb7);

        mCb1.setTag(LivenessTypeEnum.Liveness_Eye);
        mCb2.setTag(LivenessTypeEnum.Liveness_Mouth);
        mCb3.setTag(LivenessTypeEnum.Liveness_HeadUp);
        mCb4.setTag(LivenessTypeEnum.Liveness_HeadDown);
        mCb5.setTag(LivenessTypeEnum.Liveness_HeadLeft);
        mCb6.setTag(LivenessTypeEnum.Liveness_HeadRight);
        mCb7.setTag(LivenessTypeEnum.Liveness_HeadLeftOrRight);

        mCb1.setOnCheckedChangeListener(this);
        mCb2.setOnCheckedChangeListener(this);
        mCb3.setOnCheckedChangeListener(this);
        mCb4.setOnCheckedChangeListener(this);
        mCb5.setOnCheckedChangeListener(this);
        mCb6.setOnCheckedChangeListener(this);
        mCb7.setOnCheckedChangeListener(this);

        boolean isCheck = FaceSDKManager.getInstance().isIsLivenessTypeRandom();
        mCbRandom.setChecked(isCheck);

        mLivenessList = new ArrayList<>();
        List<LivenessTypeEnum> list = FaceSDKManager.getInstance().getLivenessTypeList();
        if (list != null) {
            mLivenessList.addAll(list);

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == LivenessTypeEnum.Liveness_Eye) {
                    mCb1.setChecked(true);
                }
                if (list.get(i) == LivenessTypeEnum.Liveness_Mouth) {
                    mCb2.setChecked(true);
                }
                if (list.get(i) == LivenessTypeEnum.Liveness_HeadUp) {
                    mCb3.setChecked(true);
                }
                if (list.get(i) == LivenessTypeEnum.Liveness_HeadDown) {
                    mCb4.setChecked(true);
                }
                if (list.get(i) == LivenessTypeEnum.Liveness_HeadLeft) {
                    mCb5.setChecked(true);
                }
                if (list.get(i) == LivenessTypeEnum.Liveness_HeadRight) {
                    mCb6.setChecked(true);
                }
                if (list.get(i) == LivenessTypeEnum.Liveness_HeadLeftOrRight) {
                    mCb7.setChecked(true);
                }
            }
        }

        findViewById(R.id.setting_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整
                boolean isCheck = mCbRandom.isChecked();

                FaceSDKManager.getInstance().setIsLivenessTypeRandom(isCheck);
                FaceSDKManager.getInstance().setLivenessTypeList(mLivenessList);

                LogManager.v("设置修改完成；list = " + Arrays.toString(mLivenessList.toArray()) + ", isCheck = " + isCheck);
                finish();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        LivenessTypeEnum type = (LivenessTypeEnum) buttonView.getTag();
        if (isChecked) {
            if (!mLivenessList.contains(type)) {
                mLivenessList.add(type);
            }
        } else {
            mLivenessList.remove(type);
        }
    }
}
