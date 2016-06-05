package test.java.mock;

import com.java.mock.Login;
import com.java.mock.User;

public class MockLoginImpl implements Login
{
    
    @Override
    public User login(String username, String pwd)
    {
        return new User("123", username);
    }
}
