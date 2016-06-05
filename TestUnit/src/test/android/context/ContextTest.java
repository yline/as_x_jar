package test.android.context;

import android.test.AndroidTestCase;

public class ContextTest extends AndroidTestCase
{
    public void testContext()
    {
        assertNotNull(mContext);
        // 测试用例
        System.out.println("Context 测试");
    }
}
