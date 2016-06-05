package test.android.activity;

import com.android.activity.CaculateActivity;
import com.ziptestunit.R;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CaculateActivityTest extends ActivityUnitTestCase<CaculateActivity>
{
    private EditText mETParams1;
    
    private EditText mETParams2;
    
    private Button mBtnCaculate;
    
    private TextView mTVResult; // 计算结果
    
    public CaculateActivityTest()
    {
        super(CaculateActivity.class);
    }
    
    public CaculateActivityTest(Class<CaculateActivity> activityClass)
    {
        super(activityClass);
    }
    
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();
        
        Intent intent = new Intent(getInstrumentation().getTargetContext(), CaculateActivity.class);
        startActivity(intent, null, null);
        
        checkWidgets();
    }
    
    private void checkWidgets()
    {
        mETParams1 = (EditText)findViewById(R.id.et_param1);
        assertNotNull(mETParams1);
        assertEquals("", mETParams1.getText().toString());
        
        mETParams2 = (EditText)findViewById(R.id.et_param2);
        assertNotNull(mETParams2);
        assertEquals("", mETParams2.getText().toString());
        
        mBtnCaculate = (Button)findViewById(R.id.btn_caculate);
        assertNotNull(mETParams1);
        
        mTVResult = (TextView)findViewById(R.id.tv_result);
        assertNotNull(mTVResult);
    }
    
    public void testAdd()
    {
        // 更新UI
        mETParams1.setText("2");
        mETParams2.setText("3");
        // 执行点击
        mBtnCaculate.performClick();
        // 判断
        assertEquals("5", mTVResult.getText().toString());
    }
    
    @SuppressWarnings("unchecked")
    private <T extends View> T findViewById(int id)
    {
        return (T)getActivity().findViewById(id);
    }
}
