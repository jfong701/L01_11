import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Question {
	String classNo;
	String assignmentNo;
	String questionNo;
	String filePath;
	byte[] content;
	double assignmentGrade;
	
	public Question(String classNo, String assignmentNo, String questionNo) {
		super();
		this.classNo = classNo;
		this.assignmentNo = assignmentNo;
		this.questionNo = questionNo;
	}

	public Question(String classNo, String assignmentNo, String questionNo, String filePath) {
		super();
		this.classNo = classNo;
		this.assignmentNo = assignmentNo;
		this.questionNo = questionNo;
		this.filePath = filePath;
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

	public String getQuestionNo() {
		return questionNo;
	}

	public void setQuestionNo(String questionNo) {
		this.questionNo = questionNo;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

		public void setAssignmentGrade(double assignmentGrade) {
		this.assignmentGrade = assignmentGrade;
	}

	@Override
	public String toString() {
		return "Question [classNo=" + classNo + ", assignmentNo=" + assignmentNo + ", questionNo=" + questionNo
				+ ", assignmentGrade=" + assignmentGrade + "]";
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
			
			File inFile = new File(this.filePath);
			
			this.content = new byte[(int) inFile.length()];
			
			PreparedStatement pstmt = conn.prepareStatement("insert into question (cid, aid, qid, content) " +
																		"values (?, ?,  ?, ?)");
			pstmt.setString(1, this.classNo);
			pstmt.setString(2, this.assignmentNo);
			pstmt.setString(3, this.questionNo);
			pstmt.setBytes(4, this.content);
																					  
			Statement stmt = conn.createStatement();
			
			pstmt.executeUpdate();
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
			
			stmt.executeUpdate("delete from question where cid = '" + this.classNo + "' and " +
														  "aid = '" + this.assignmentNo + "; and " +
														  "qid = '" + this.questionNo + "'");
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
			PreparedStatement pstmt = conn.prepareStatement("select * from question where cid = '" + this.classNo + "' and " +
																						 "aid = '" + this.assignmentNo + "' and " +
																						 "qid = '" + this.questionNo + "'");
			ResultSet rs = pstmt.executeQuery();
				
			if(rs.next()) {
				this.assignmentGrade= rs.getDouble(5);
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
			
			//stmt.executeUpdate("update question set content = " + this.getContent() + "'" +
			//									"where cid = '" + this.classNo+ "' and " +
			//										  "aid = '" + this.assignmentNo + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
