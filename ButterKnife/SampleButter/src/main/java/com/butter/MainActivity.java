package com.butter;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.yline.base.BaseAppCompatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseAppCompatActivity {
//    @BindView(R.id.main_btn_one)
//    public Button btnOne;
//
//    @BindViews({R.id.main_one_tv, R.id.main_two_tv, R.id.main_three_tv})
//    public List<TextView> tvList;
//
//    @OnClick(R.id.main_btn_one)
//    public void showToast() {
//        MainApplication.toast("this is a on click");
//    }

    private Unbinder mUnBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUnBinder = ButterKnife.bind(this);
//
//        btnOne.setText("this is the one");
//
//        tvList.get(0).setText("this is text view one");
//        tvList.get(1).setText("this is text view two");
//        tvList.get(2).setText("this is text view three");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 解除绑定
        if (null != mUnBinder) {
            mUnBinder.unbind();
        }
    }
}
