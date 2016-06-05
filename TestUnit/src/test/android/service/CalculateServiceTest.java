package test.android.service;

import com.android.service.CalculateService;

import android.content.Intent;
import android.test.ServiceTestCase;

public class CalculateServiceTest extends ServiceTestCase<CalculateService>
{
    public CalculateServiceTest()
    {
        super(CalculateService.class);
    }
    
    public CalculateServiceTest(Class<CalculateService> serviceClass)
    {
        super(serviceClass);
    }
    
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();
        
        startService(
            new Intent(mContext, CalculateService.class).putExtra("param1", 2).putExtra("param2", 3));
        assertNotNull(getService());
    }
    
    public void testAdd()
    {
        assertEquals(5, getService().getResult());
    }
    
    public void testAdd2()
    {
        assertEquals(5, getService().getResult());
    }
}
