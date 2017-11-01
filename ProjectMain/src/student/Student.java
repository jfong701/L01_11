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
	
	public Student(String studentNo, String studentFirstName, String studentLastName) {
		// TODO Auto-generated constructor stub
		this.studentNo = studentNo;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
	}

	
	public Student(String studentNo) {
		this.studentNo = studentNo;
		
		this.selectSQL();
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
	
	
	/**
	 * @param studentNo the stundentNo to insert to SQL student table
	 * @param studentFirstName the studentFirstName to insert to SQL student table
	 * @param studentLastName the studentLastName to insert to SQL student table
	 * @param studentUTROID the studentUTROID to insert to SQL student table
	 */
	public void insertSQL() {
		String studentUTROID = " ";
		String studentPassword = " ";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//Connection conn = DriverManager.getConnection("jdbc:mysql://mathlab.utoronto.ca:3306/cscc43f17_manogar7_sakila?useSSL=False", "manogar7", "manogar7");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CSCC01?useSSL=False", "root", "MySQL");
			
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("insert into student (sid, fname, lname, utroid, student_password) values ('" + studentNo + "','" + studentFirstName + "','" + studentLastName + "','" + studentUTROID +"','" + studentPassword +"')");
			
			conn.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
	}

	
	/**
	 * @param studentNo the stundentNo to delete from SQL student table
	 */
	public void deleteSQL() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//Connection conn = DriverManager.getConnection("jdbc:mysql://mathlab.utoronto.ca:3306/cscc43f17_manogar7_sakila?useSSL=False", "manogar7", "manogar7");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CSCC01?useSSL=False", "root", "MySQL");

			Statement stmt = conn.createStatement();
			stmt.executeUpdate("delete from student where sid =" + studentNo);
			
			conn.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
	}

	
	/**
	 * Select student information based on input studentNo
	 */
	public void selectSQL() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CSCC01?useSSL=False", "root", "MySQL");
	
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from student where sid = '" + studentNo + "'");
			
			if (rs.next() ) {
				this.studentFirstName = rs.getString(2);
				this.studentLastName = rs.getString(3);
			}
	
			conn.close();
		
		} catch(SQLException ex) {
		ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
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