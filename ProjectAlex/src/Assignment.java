import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Assignment {
	String classNo;
	String assignmentNo;
	String assignmentName;
	
	public Assignment(String classNo, String assignmentNo) {
		super();
		this.classNo = classNo;
		this.assignmentNo = assignmentNo;
	}

	public Assignment(String classNo, String assignmentNo, String assignmentName) {
		super();
		this.classNo = classNo;
		this.assignmentNo = assignmentNo;
		this.assignmentName = assignmentName;
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

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	@Override
	public String toString() {
		return "Assignment [classNo=" + classNo + ", assignmentNo=" + assignmentNo + ", assignmentName="
				+ assignmentName + "]";
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
			
			stmt.executeUpdate("insert into assignment (cid, aid, assignment)" +
											"values ('" + this.classNo + "','" + 
														  this.assignmentNo + "','" +
														  this.assignmentName + "')");
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
			
			stmt.executeUpdate("delete from assignment where cid = '" + this.classNo + "' and " +
															"aid = '" + this.assignmentNo +"'");
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
			PreparedStatement pstmt = conn.prepareStatement("select * from assignment where cid = '" + this.classNo + "' and " +
																						   "aid = '" + this.assignmentNo + "'");
			ResultSet rs = pstmt.executeQuery();
				
			if(rs.next()) {
				this.assignmentName = rs.getString(3);
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
			
			stmt.executeUpdate("update assignment set assignment = '" + this.assignmentName + "'" +
												 "where cid = '" + this.classNo+ "' and " +
												 	   "aid = '" + this.assignmentNo + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
