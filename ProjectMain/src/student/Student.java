package student;

import java.sql.*;

/**
 * 
 */

/**
 * @author DAVID
 *
 */
public class Student {

	/**
	 * 
	 */
	String studentNo;
	String studentFirstName;
	String studentLastName;
	String studentUtroid;
	String studentPassword;
	
	public Student(String studentNo, String studentFirstName, String studentLastName) {
		// TODO Auto-generated constructor stub
		this.studentNo = studentNo;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
	}

	
	/**
	 * Metamorphose Stuent() into searching for existing student instead of creating new object.
	 * @param studentNo
	 */
	public Student(String studentNo) {
		this.studentNo = studentNo;
	}
	
	
	/**
	 * @return the studentNo
	 */
	public String getStudentNo() {
		return studentNo;
	}


	/**
	 * @param studentNo the studentNo to set
	 */
	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}
	

	/**
	 * @return the studentFirstName
	 */
	public String getStudentFirstName() {
		return studentFirstName;
	}


	/**
	 * @param studentFirstName the studentFirstName to set
	 */
	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}


	/**
	 * @return the studentLastName
	 */
	public String getStudentLastName() {
		return studentLastName;
	}


	/**
	 * @param studentLastName the studentLastName to set
	 */
	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
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
	

	/**
	 * @return string format of this object
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String format;
		format = this.studentNo + "," + this.studentFirstName + "," + this.studentLastName;
		return format;
	}
	

}