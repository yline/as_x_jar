package com.java.mock;

public class NoteDAO
{
    public void save(User user, String note)
    {
        if (null != note && !"".equals(note))
        {
            System.out.println("NoteDAO" + "save ok" + ",note = " + note + ",User = " + user.toString());
        }
    }
}
