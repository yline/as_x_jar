package test.java.unit;

import junit.framework.TestCase;

import com.java.unit.AdderImpl;

public class AdderTest2 extends TestCase
{
    private AdderImpl adder;
    
    @Override
    protected void setUp()
        throws Exception
    {
        adder = new AdderImpl();
        super.setUp();
    }
    
    public void testAdd()
    {
        assertEquals(0, adder.add(0, 0));
        assertEquals(1, adder.add(1, 0));
    }
    
    @Override
    protected void tearDown()
        throws Exception
    {
        adder = null;
        super.tearDown();
    }
}
