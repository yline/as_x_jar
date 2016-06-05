package test.java.unit;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AdderSuiteTest
{
    public static Test suite()
    {
        TestSuite testSuite = new TestSuite();
        testSuite.addTest(new JUnit4TestAdapter(AdderTest.class));
        testSuite.addTest(new JUnit4TestAdapter(AdderTest2.class));
        return testSuite;
    }
}
