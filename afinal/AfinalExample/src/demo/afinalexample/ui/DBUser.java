package demo.afinalexample.ui;

/**
 * set get cannot be ignored
 */
//@Table(name="Test_User")    // 这个注解相当于让这个类单独成立表，成为一个底层的table
public class DBUser {
	private int id;
	private String name;
	private int age;
	
	//必须包含这个默认的构造方法，否则在进行数据查找时，会报错
	public DBUser() {
		
	}
	
	//这个构造方法是可以正常使用的，说明id是主键，且是自增长的
	public DBUser(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
