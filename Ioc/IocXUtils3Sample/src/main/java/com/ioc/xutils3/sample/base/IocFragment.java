package com.ioc.xutils3.sample.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.base.BaseFragment;

/**
 * Fragment中注入
 * zc.view().inject(this, this.getView());
 * zc.view().inject(this, inflater, container);
 * zc.view().inject(mHolder, this.getView());
 */
public class IocFragment extends BaseFragment {
    private boolean injected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

}
