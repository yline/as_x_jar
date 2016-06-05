package test.java.mockito;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.java.mockito.Student;
import com.java.mockito.StudentController;
import com.java.mockito.StudentDAO;

import junit.framework.TestCase;

/**
 * 
 * @author YLine
 *
 * @2016年6月3日 @上午10:37:40
 */
public class StudentControllerTest extends TestCase
{
    StudentController mStudentController;
    
    StudentDAO mStudentDAO;
    
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();
        
        mStudentController = new StudentController();
        
        mStudentDAO = mock(StudentDAO.class);
        
        mStudentController.setStudentDAO(mStudentDAO);
    }
    
    public void testGetStudentInfo()
    {
        Student reStudent = new Student();
        reStudent.setId(123);
        reStudent.setName("123-username");
        
        // 调用Mockito
        when(mStudentDAO.getStudentFromDB(anyInt())).thenReturn(reStudent);
        
        Student student = mStudentController.getStudentInfo(123);
        
        assertEquals(123, student.getId());
        assertEquals("123-username", student.getName());
    }
    
    public void testGetStudentInfoFromServer()
    {
        when(mStudentDAO.getStudentFromDB(anyInt())).thenReturn(null);
        
        Student student = mStudentController.getStudentInfo(456);
        assertEquals(456, student.getId());
        assertEquals(456 + "username", student.getName());
    }
    
    public void testCaptureParam()
    {
        StudentController mockController = mock(StudentController.class);
        
        doAnswer(new Answer<Void>()
        {
            
            @Override
            public Void answer(InvocationOnMock invocation)
                throws Throwable
            {
                int studentId = (Integer)invocation.getArguments()[0];
                System.out.println("学生id = " + studentId);
                
                assertEquals(666, studentId);
                return null;
            }
        }).when(mockController).getStudentInfo(anyInt());
        
        mockController.getStudentInfo(666);
    }
}
