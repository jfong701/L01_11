import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class StundentClasses{
	String studentNo;
	String classNo;
	
	public StundentClasses(String studentNo, String classNo) {
		super();
		this.studentNo = studentNo;
		this.classNo = classNo;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getClassNo() {
		return classNo;
	}

	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}

	@Override
	public String toString() {
		return "StundentClasses [studentNo=" + studentNo + ", classNo=" + classNo + "]";
	}
	
	public void insertSQL() {
		try {
			Class.forName(com.mysql.jdbc.Driver.class.getName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cscc01?useSSL=false", "root", "MySQL");
			Statement stmt = conn.createStatement();
			
			stmt.executeUpdate("insert into student_class (sid, cid)" +
											"values ('" + this.studentNo + "','" + 
														  this.classNo + "')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void deleteSQL() {
		try {
			Class.forName(com.mysql.jdbc.Driver.class.getName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cscc01?useSSL=false", "root", "MySQL");
			Statement stmt = conn.createStatement();
			
			stmt.executeUpdate("delete from student_class where sid = '" + this.studentNo + "' and " +
																"cid = '" + this.classNo +"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
