package professor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Professor {
	String professorNo;
	String professorFirstName;
	String professorLastName;
	String professorPassword;
	
	/**
	 * 
	 * @param professorNo
	 * @param professorFirstName
	 * @param professorLastName
	 */
	public Professor(String professorNo, String professorFirstName, String professorLastName) {
		super();
		this.professorNo = professorNo;
		this.professorFirstName = professorFirstName;
		this.professorLastName = professorLastName;
	}

	
	/**
	 * Metamorphose Metamorphose Professor() into searching for existing student instead of creating new object.t() into searching for existing student instead of creating new object.
	 * @param professorNo
	 */
	public Professor(String professorNo) {
		super();
		this.professorNo = professorNo;
	}


	public String getProfessorNo() {
		return professorNo;
	}


	public void setProfessorNo(String professorNo) {
		this.professorNo = professorNo;
	}


	public String getProfessorFirstName() {
		return professorFirstName;
	}


	public void setProfessorFirstName(String professorFirstName) {
		this.professorFirstName = professorFirstName;
	}


	public String getProfessorLastName() {
		return professorLastName;
	}


	public void setProfessorLastName(String professorLastName) {
		this.professorLastName = professorLastName;
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
			
			stmt.executeUpdate("insert into professor (pid, fname, lname, professor_password)" +
											"values ('" + this.professorNo +"','" + 
														  this.professorFirstName + "','" +
														  this.professorLastName + "','" +
														  this.professorPassword +"')");
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
			
			stmt.executeUpdate("delete from professor where pid = '" + this.professorNo + "'");
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
			PreparedStatement pstmt = conn.prepareStatement("select * from professor where pid = '" + this.professorNo + "'");
			ResultSet rs = pstmt.executeQuery();
				
			if(rs.next()) {
				this.professorFirstName = rs.getString(2);
				this.professorLastName = rs.getString(3);
				this.professorPassword = rs.getString(4);
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
			
			stmt.executeUpdate("update professor set fname = '" + this.professorFirstName + "'," +
			            						    "lname = '" + this.professorLastName + "'," +
			            						    "professor_password = '" + this.professorPassword + "'" +
												    "where pid = '" + this.professorNo + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public String toString() {
		return "Professor [professorNo=" + professorNo + ", professorFirstName=" + professorFirstName
				+ ", professorLastName=" + professorLastName + "]";
	}
}
