package com.java.mockito;

public class StudentController
{
    private StudentDAO mStudentDAO;
    
    /**
     * @param id 编号
     * @return Student
     */
    public Student getStudentInfo(int id)
    {
        Student student = null;
        // 数据库中获取
        if (null != mStudentDAO)
        {
            student = mStudentDAO.getStudentFromDB(id);
        }
        // 网络中获取
        if (null == student)
        {
            student = fetchStudentInfo(id);
        }
        
        return student;
    }
    
    /**
     * 从网络中获取数据
     * @param id 编号
     * @return Student
     */
    private Student fetchStudentInfo(int id)
    {
        System.out.println("从网络中获取数据");
        Student student = new Student();
        student.setId(id);
        student.setName(id + "username");
        return student;
    }
    
    /**
     * @param studentDAO 该接口的实现类
     */
    public void setStudentDAO(StudentDAO studentDAO)
    {
        this.mStudentDAO = studentDAO;
    }
}
