package assignment;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class Assignment extends student.Classes{
	String assignmentNo;
	String assignmentName;
	
	public Assignment(String classNo, String className, String assignmentNo, String assignmentName) {
		super(classNo, className);
		this.assignmentNo = assignmentNo;
		this.assignmentName = assignmentName;
	}


	/**
	 * Metamorphose Assignment() into searching for existing assignment instead of creating
	 * new object() into searching for existing student instead of creating new object.
	 * @param classNo
	 * @param className
	 * @param assignmentNo
	 */
	public Assignment(String classNo, String className, String assignmentNo) {
		super(classNo, className);
		this.assignmentNo = assignmentNo;
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
											"values ('" + super.getClassNo() + "','" + 
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
			
			stmt.executeUpdate("delete from assignment where cid = '" + super.getClassNo() + "' and " +
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
			PreparedStatement pstmt = conn.prepareStatement("select * from assignment where cid = '" + super.getClassNo() + "' and " +
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
			
			stmt.executeUpdate("update class set assignment = '" + this.assignmentName + "'" +
												"where cid = '" + super.getClassNo()+ "' and " +
													  "aid = '" + this.assignmentNo + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	Question question[];
	int qCount;
	public void Questions(int qCount) {
		this.question = new Question [qCount];
		this.qCount = qCount;
	}
	/*
	 * initialize a question at index
	*/
	public void addQuestion(int index, String q, String ans) {
		question[index] = new Question(q, ans);
	}
	/*
	 * FileWrite (FileWriter outFile)
	 * Take a valid file writer and write the question's data to the
	 * file referred to by outFile
	 */
	public void fileWrite(FileWriter outFile) {
		for (int i = 0; i < qCount; i++) {
			question[i].fileWrite(outFile);
		}
	}


	@Override
	public String toString() {
		return "Assignment [assignmentNo=" + assignmentNo + ", assignmentName=" + assignmentName + ", question="
				+ Arrays.toString(question) + ", qCount=" + qCount + "]";
	}
}
