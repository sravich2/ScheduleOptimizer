import java.util.ArrayList;

public class Department {

	String deptCode;
	ArrayList<Course> coursesInDept;
	String deptName;
	String year;
	String session;

	public Department(String code, String name, String year, String session) {
		this.deptCode = code;
		this.deptName = name;
		this.year = year;
		this.session = session;
	}
}
