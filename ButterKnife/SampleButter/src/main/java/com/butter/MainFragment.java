package com.butter;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yline.base.BaseFragment;

import java.util.Arrays;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * BindColor()
 * BindBitmap()
 * OnClick()
 * OnLongClick()
 *
 * @author yline 2017/2/27 --> 23:10
 * @version 1.0.0
 */
public class MainFragment extends BaseFragment {
    @BindView(R.id.fragment_main_one_tv)
    public TextView tvOne;

    @BindView(R.id.fragment_main_two_tv)
    public TextView tvTwo;

    @BindString(R.string.string_fragment)
    public String string;

    @BindArray(R.array.city)
    public String[] stringArray;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        tvOne.setText(string);

        tvTwo.setText(Arrays.toString(stringArray));
    }
}





























