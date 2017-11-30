package com.yline.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.test.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 底部显示器
 *
 * @author yline 2017/11/29 -- 10:55
 * @version 1.0.0
 */
public class PageView extends RelativeLayout {
    private int page;
    private List<PageModel> pageModelList;

    private ImageView ivPrevious, ivRotate, ivNext;
    private TextView tvHint;

    private OnPageChangeClickListener onPageChangeClickListener;

    public PageView(Context context) {
        super(context);

        initView(context);
    }

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_page, this, true);
        pageModelList = new ArrayList<>();
        page = 0;

        // 提示文字
        tvHint = findViewById(R.id.page_note);

        // 后退
        ivPrevious = findViewById(R.id.page_previous);
        ivPrevious.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                page--;
                updateNote();
            }
        });

        // 旋转
        ivRotate = findViewById(R.id.page_rotate);
        ivRotate.setVisibility(GONE);

        // 前进
        ivNext = findViewById(R.id.page_next);
        ivNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                updateNote();
            }
        });
    }

    public void setDataList(List<PageModel> list, boolean isNotify) {
        if (null != list && list.size() > 0) {
            pageModelList = new ArrayList<>(list);
            if (isNotify) {
                updateNote();
                invalidate();
            }
        }
    }

    public void setOnResetClickListener(final View.OnClickListener listener) {
        setOnSubClickListener(R.drawable.reset, listener);
    }

    public void setOnPlayClickListener(final View.OnClickListener listener) {
        setOnSubClickListener(R.drawable.play, listener);
    }

    public void setOnRotateClickListener(final View.OnClickListener listener) {
        setOnSubClickListener(R.drawable.rotate, listener);
    }

    private void setOnSubClickListener(int resId, final View.OnClickListener listener) {
        if (null != listener) {
            ivRotate.setVisibility(VISIBLE);
            ivRotate.setImageResource(resId);
            ivRotate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v);
                }
            });
        } else {
            ivRotate.setVisibility(GONE);
        }
    }

    private void updateNote() {
        if (page > pageModelList.size() - 1 || page < 0) {
            return;
        }

        tvHint.setText(String.format("%s :\n%s", pageModelList.get(page).getSubTitle(), pageModelList.get(page).getText()));
        ivPrevious.setVisibility(page > 0 ? VISIBLE : INVISIBLE);
        ivNext.setVisibility(page < pageModelList.size() - 1 ? VISIBLE : INVISIBLE);

        if (null != onPageChangeClickListener) {
            onPageChangeClickListener.onPageChange(page);
        }
    }

    public void setOnPageChangeClickListener(OnPageChangeClickListener listener) {
        this.onPageChangeClickListener = listener;
    }

    public interface OnPageChangeClickListener {
        /**
         * 页面变化
         *
         * @param page 当前页
         */
        void onPageChange(int page);
    }

    public static class PageModel implements Serializable {
        private final String subTitle;

        private final String text;

        public PageModel(String subTitle, String text) {
            this.subTitle = subTitle;
            this.text = text;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public String getText() {
            return text;
        }
    }
}
