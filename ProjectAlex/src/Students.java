import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Students {
	String studentNo;
	String studentFirstName;
	String studentLastName;
	String studentUtroid;
	String studentPassword;
	
	public Students(String studentNo) {
		super();
		this.studentNo = studentNo;
	}

	public Students(String studentNo, String studentFirstName, String studentLastName, String studentUtroid) {
		super();
		this.studentNo = studentNo;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.studentUtroid = studentUtroid;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getStudentFirstName() {
		return studentFirstName;
	}

	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}

	public String getStudentLastName() {
		return studentLastName;
	}

	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

	public String getStudentUtroid() {
		return studentUtroid;
	}

	public void setStudentUtroid(String studentUtroid) {
		this.studentUtroid = studentUtroid;
	}

	public String getStudentPassword() {
		return studentPassword;
	}

	public void setStudentPassword(String studentPassword) {
		this.studentPassword = studentPassword;
	}

	@Override
	public String toString() {
		return "Students [studentNo=" + studentNo + ", studentFirstName=" + studentFirstName + ", studentLastName="
				+ studentLastName + ", studentUtroid=" + studentUtroid + ", studentPassword=" + studentPassword + "]";
	}
	
	public void insertSQL() {
		this.studentUtroid = null;
		this.studentPassword = null;
		
		try {
			Class.forName(com.mysql.jdbc.Driver.class.getName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cscc01?useSSL=false", "root", "MySQL");
			Statement stmt = conn.createStatement();
			
			stmt.executeUpdate("insert into student (sid, fname, lname, utroid, student_password)" +
											"values ('" + this.studentNo +"','" + 
														  this.studentFirstName + "','" +
														  this.studentLastName + "','" +
														  this.studentUtroid + "','" +
														  this.studentPassword + "')");
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
			
			stmt.executeUpdate("delete from student where sid = '" + this.studentNo + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void selectSQL() {
		try {
			Class.forName(com.mysql.jdbc.Driver.class.getName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cscc01?useSSL=false", "root", "MySQL");
			PreparedStatement pstmt = conn.prepareStatement("select * from student where sid = '" + this.studentNo + "'");
			ResultSet rs = pstmt.executeQuery();
				
			if(rs.next()) {
				this.studentFirstName = rs.getString(2);
				this.studentLastName = rs.getString(3);
				this.studentUtroid = rs.getString(4);
				this.studentPassword = rs.getString(5);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void updateSQL() {
		try {
			Class.forName(com.mysql.jdbc.Driver.class.getName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cscc01?useSSL=false", "root", "MySQL");
			Statement stmt = conn.createStatement();
			
			stmt.executeUpdate("update student set fname = '" + this.studentFirstName + "'," +
			            						  "lname = '" + this.studentLastName + "'," +
			            						  "utroid = '" + this.studentUtroid + "'," +
			            						  "student_password = '" + this.studentPassword + "'" +
												  "where sid = '" + this.studentNo + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
