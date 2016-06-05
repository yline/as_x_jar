package test.java.mock;

import junit.framework.TestCase;

import org.junit.Test;

import com.java.mock.Login;
import com.java.mock.NoteDAO;
import com.java.mock.User;

public class NoteTest extends TestCase
{
    
    @Test
    public void testSaveNote()
    {
        Login login = new MockLoginImpl();
        NoteDAO noteDAO = new NoteDAO();
        
        User user = login.login("Mr.simple", "mine_pwd");
        noteDAO.save(user, "这是simple的笔记本内容");
    }
}
