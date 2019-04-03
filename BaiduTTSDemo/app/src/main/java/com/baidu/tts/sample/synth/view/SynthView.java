package com.baidu.tts.sample.synth.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.tts.sample.R;

public class SynthView extends RelativeLayout {
    private EditText mInputEditText;
    private TextView mShowTextView;

    public SynthView(Context context) {
        this(context, null);
    }

    public SynthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.synth_view, this, true);

        initView();
    }

    private void initView() {
        mInputEditText = findViewById(R.id.synth_view_input);
        mShowTextView = findViewById(R.id.synth_view_show);
    }

    public void setOnSpeakClickListener(View.OnClickListener listener) {
        findViewById(R.id.synth_view_speak).setOnClickListener(listener);
    }

    public void setOnSynthesizeClickListener(View.OnClickListener listener) {
        findViewById(R.id.synth_view_synthesize).setOnClickListener(listener);
    }

    public void setOnBatchSpeakClickListener(View.OnClickListener listener) {
        findViewById(R.id.synth_view_batch_speak).setOnClickListener(listener);
    }

    public void setOnLoadModelClickListener(View.OnClickListener listener) {
        findViewById(R.id.synth_view_load_model).setOnClickListener(listener);
    }

    public void setOnPauseClickListener(View.OnClickListener listener) {
        findViewById(R.id.synth_view_pause).setOnClickListener(listener);
    }

    public void setOnResumeClickListener(View.OnClickListener listener) {
        findViewById(R.id.synth_view_resume).setOnClickListener(listener);
    }

    public void setOnStopClickListener(View.OnClickListener listener) {
        findViewById(R.id.synth_view_stop).setOnClickListener(listener);
    }

    public void setOnHelpClickListener(View.OnClickListener listener) {
        findViewById(R.id.synth_view_help).setOnClickListener(listener);
    }

    public void setShowText(String text) {
        mShowTextView.setText(text);
    }

    public String getInputText() {
        return mInputEditText.getText().toString().trim();
    }

    public void appendShowText(String text) {
        mShowTextView.append(text);
    }
}
