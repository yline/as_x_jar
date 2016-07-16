package com.ioc.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment中注入
 * zc.view().inject(this, this.getView());
 * zc.view().inject(this, inflater, container);
 * zc.view().inject(mHolder, this.getView());
 */
public class BaseFragment extends Fragment {
	private boolean injected = false;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (BaseApplication.isInject) {
			injected = true;
			return x.view().inject(this, inflater, container);
		}else {
			return super.onCreateView(inflater, container, savedInstanceState);
		}
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		if (BaseApplication.isInject && !injected) {
			x.view().inject(this, this.getView());
		}
	}
	
}
