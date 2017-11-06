package jdbc;

import java.sql.SQLException;
import java.util.Date;

import assignment.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jdbc.MySQLAccess;
import student.Student;

public class DOA {

	//MYSQL FUNCTION EXAMPLES AT THE END
	
	private static String dbName = "cscc43f17_manogar7_sakila";
	private static String stu = "STUDENTS";
	private static String asmt = "ASSIGNMENTS";
	private static String ques = "QUESTIONS";
	private static String stu_asmt = "STUDENT_ASSIGNMENTS";
	private static String prof = "PROFESSORS";
	
	
	private static MySQLAccess a;
	
	public static void main(String[] args) {
			try {
				initDatabase();
			} catch (SQLException e) {
				e.printStackTrace();
			}

	}
	
	public static void initDatabase() throws SQLException {
		a = new MySQLAccess();

		System.out.println("Initializing database");
		a.loadAndConnect(dbName);
		
		a.createTable(stu,
				"student_id CHAR(10) NOT NULL",
				"utor_id VARCHAR(10) UNIQUE NOT NULL",
				"first_name VARCHAR(255) NOT NULL",
				"last_name VARCHAR(255) NOT NULL",
				"student_password VARCHAR(25)",
				"PRIMARY KEY ( student_id )"
				);
		a.createTable(asmt,
				"course_id VARCHAR(8) NOT NULL",
				"assignment_id INTEGER NOT NULL",
				"num_questions INTEGER NOT NULL",
				"assignment_name VARCHAR(225)",
				"deadline DATE",
				"PRIMARY KEY ( course_id, assignment_id )"
				);
		a.createTable(ques,
				"course_id VARCHAR(8) NOT NULL",
				"assignment_id INTEGER NOT NULL",
				"question_id INTEGER NOT NULL",
				"question VARCHAR(1000) NOT NULL",
				"answer_function VARCHAR(1000) NOT NULL",
				"lower_range INTEGER",
				"upper_range INTEGER",
				"decimal_places INTEGER",
				"FOREIGN KEY ( course_id, assignment_id ) REFERENCES ASSIGNMENTS ( course_id, assignment_id )",
				"PRIMARY KEY ( course_id, assignment_id, question_id )"
				);
		a.createTable(stu_asmt,
				"student_id CHAR(10) NOT NULL",
				"course_id VARCHAR(8) NOT NULL",
				"assignment_id INTEGER NOT NULL",
				"mark INTEGER",
				"FOREIGN KEY ( student_id ) REFERENCES STUDENTS ( student_id )",
				"FOREIGN KEY ( course_id, assignment_id ) REFERENCES ASSIGNMENTS ( course_id, assignment_id )",
				"PRIMARY KEY ( student_id, course_id, assignment_id )"
				);
		a.createTable(prof,
				"professor_id CHAR(10) NOT NULL",
				"professor_first_name VARCHAR(225)",
				"professor_last_name VARCHAR(225)",
				"professor_password VARCHAR(25)",
				"PRIMARY KEY ( professor_id )"
				);
		System.out.println("Finished initialization");
	}
	
	public static void start() {
		a = new MySQLAccess();
		a.loadAndConnect(dbName);
	}
	
	public static void close() {
		a.close();
	}
	
	public static void addStudent(String id, String utor_id, String first, String last) {
		String sql = a.preparedRecordsSQL(stu, 4, "student_id", "utor_id", "first_name", "last_name");
		System.out.println(sql);
		Connection conn = a.getConn();
		try {
			PreparedStatement pr = conn.prepareStatement(sql);
			pr.setString(1,  id);
			pr.setString(2, utor_id);
			pr.setString(3, first);
			pr.setString(4, last);
			pr.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public static void addAssignment(String course_id, String assignment_id, String num_question, String assignment_name, Date deadline ) {
		String sql = a.preparedRecordsSQL(asmt, 5, "course_id", "assignment_id", "num_questions", "assignment_name", "deadline");
		System.out.println(sql);
		Connection conn = a.getConn();
		try {
			PreparedStatement pr = conn.prepareStatement(sql);
			pr.setString(1,  course_id);
			pr.setInt(2, Integer.parseInt(assignment_id));
			pr.setInt(3, Integer.parseInt(num_question));
			pr.setString(4, assignment_name);
			pr.setDate(5, new java.sql.Date(deadline.getTime()));
			pr.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	public static void addQuestion(String course_id, String assignment_id, String question_id, String question, String answer ) {
		String sql = a.preparedRecordsSQL(ques, 5, "course_id", "assignment_id", "question_id", "question", "answer_function");
		System.out.println(sql);
		Connection conn = a.getConn();
		try {
			PreparedStatement pr = conn.prepareStatement(sql);
			pr.setString(1,  course_id);
			pr.setInt(2, Integer.parseInt(assignment_id));
			pr.setInt(3, Integer.parseInt(question_id));
			pr.setString(4, question);
			pr.setString(5, answer);
			
			pr.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public static void addProfessor(String id, String first, String last) {
		String sql = a.preparedRecordsSQL(prof, 3, "professor_id", "professor_first_name", "professor_last_name");
		System.out.println(sql);
		Connection conn = a.getConn();
		try {
			PreparedStatement pr = conn.prepareStatement(sql);
			pr.setString(1,  id);
			pr.setString(2, first);
			pr.setString(3, last);
			pr.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public Student rsToStudent(ResultSet rs) throws SQLException {
		Student std = null;
		while (rs.next()) {
			// columns in order: student_id,first_name,last_name,utorid
		    String studentNo = rs.getString("student_id");
		    String first_name = rs.getString("first_name");
		    String last_name = rs.getString("last_name");
		    std = new Student(studentNo, first_name, last_name);
		}
		return std;
	}
	
	public Question rsToQuestion(ResultSet rs) throws SQLException {
		Question question = null;
		while (rs.next()) {
		    String question_description = rs.getString("question");
		    String question_answer = rs.getString("answer");
		    question = new Question(question_description, question_answer);
		}
		return question;
	}
	
	
}

//a = new MySQLAccess();
//access.createDatabase("hi");
//access.deleteDatabase("hi");
//a.loadAndConnect("db");
//access.createTable("registration", "id INTEGER not NULL", "first VARCHAR(255)", "last VARCHAR(255)", "age INTEGER", "PRIMARY KEY ( id )");
//access.insertRecords("registration", "113", "'Zaid'", "'Khan'", "29");
//access.updateRecords("registration", "age = 30", "last = 'Khan'");
//access.deleteRecords("registration", "age = 20");
//access.selectRecords("registration", "id", "first", "last");
//access.selectRecords("registration", "*");
//access.selectRecordsWhere("registration", "id >= 102", "*");
//access.selectRecordsWhereLike("registration", "first", "'%za%'", "*");
//access.selectRecordsOrderBy("registration", "last", "desc", "*");
