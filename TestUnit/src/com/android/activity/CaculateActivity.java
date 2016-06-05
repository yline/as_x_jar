package com.android.activity;

import com.ziptestunit.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CaculateActivity extends Activity
{
    private EditText mETParams1;
    
    private EditText mETParams2;
    
    private Button mBtnCaculate;
    
    private TextView mTVResult; // 计算结果
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caculate);
        
        initView();
        
        mBtnCaculate.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                int param1 = parseInt(mETParams1);
                int param2 = parseInt(mETParams2);
                mTVResult.setText(param1 + param2 + "");
            }
        });
    }
    
    private void initView()
    {
        mETParams1 = (EditText)findViewById(R.id.et_param1);
        mETParams2 = (EditText)findViewById(R.id.et_param2);
        mBtnCaculate = (Button)findViewById(R.id.btn_caculate);
        mTVResult = (TextView)findViewById(R.id.tv_result);
    }
    
    private int parseInt(EditText etParam)
    {
        String paramStr = etParam.getText().toString().trim();
        if (!TextUtils.isEmpty(paramStr))
        {
            return Integer.parseInt(paramStr);
        }
        return 0;
    }
}
