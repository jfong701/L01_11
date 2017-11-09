import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentAssignment {
	String studentNo;
	String classNo;
	String assignmentNo;
	
	public StudentAssignment(String studentNo, String classNo, String assignmentNo) {
		super();
		this.studentNo = studentNo;
		this.classNo = classNo;
		this.assignmentNo = assignmentNo;
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

	public String getAssignmentNo() {
		return assignmentNo;
	}

	public void setAssignmentNo(String assignmentNo) {
		this.assignmentNo = assignmentNo;
	}

	@Override
	public String toString() {
		return "StudentAssignment [studentNo=" + studentNo + ", classNo=" + classNo + ", assignmentNo=" + assignmentNo
				+ "]";
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
			
			stmt.executeUpdate("insert into student_assignment (sid, cid, aid)" +
											"values ('" + this.studentNo + "','" + 
														  this.classNo + "','" +
														  this.assignmentNo + "')");
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
																"cid = '" + this.classNo + "' and " + 
																"aid = '" + this.assignmentNo + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
