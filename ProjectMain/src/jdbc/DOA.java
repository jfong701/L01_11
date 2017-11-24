package jdbc;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import assignment.Assignment;
import assignment.SingleAnswerQuestion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jdbc.OldMySQLAccess;
import student.Student;

public class DOA {

	//MYSQL FUNCTION EXAMPLES AT THE END
	
	private final static String dbName = "cscc43f17_manogar7_sakila";
	private final static String stu = "STUDENTS";
	private final static String asmt = "ASSIGNMENTS";
	private final static String ques = "QUESTIONS";
	private final static String stu_asmt = "STUDENT_ASSIGNMENTS";
	private final static String prof = "PROFESSORS";
	private final static String course_stu = "COURSE_STUDENTS";
	
	private static OldMySQLAccess a;
	private static MySQLAccess db = new MySQLAccess();
	
	public static void main(String[] args) throws SQLException {
			//start();
			//a.dropTable(stu);
			//close();
			//initDatabase();
			//close();
		ArrayList<ArrayList<String>> a = getQuestions("CSCC01", "1");
		for (int i = 0; i < a.size(); i++) {
			System.out.println(a.get(i));
		}
		System.out.println(questionCount("CSCC01", "1"));
		addStudentToCourse("CSCC01", "11111");
	}
	
	public static void initDatabase() throws SQLException {
		a = new OldMySQLAccess();

		System.out.println("Initializing database");
		a.loadAndConnect(dbName);
		
		a.createTable(stu,
				"student_id CHAR(10) NOT NULL",
				"utor_id VARCHAR(10) UNIQUE NOT NULL",
				"first_name VARCHAR(255) NOT NULL",
				"last_name VARCHAR(255) NOT NULL",
				"student_password VARCHAR(25) DEFAULT ''",
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
		a.createTable(course_stu,
				"course_id VARCHAR(8) NOT NULL",
				"student_id CHAR(10) NOT NULL",
				"FOREIGN KEY ( student_id ) REFERENCES STUDENTS ( student_id )",
				"FOREIGN KEY ( course_id ) REFERENCES ASSIGNMENTS ( course_id )",
				"PRIMARY KEY ( student_id, course_id )"
				);

		System.out.println("Finished initialization");
	}
	
	public static void start() {
		a = new OldMySQLAccess();
		a.loadAndConnect(dbName);
	}
	
	public static void close() {
		db.close();
	}
	
	public static void addStudent(String id, String utor_id, String first, String last) {
		try {
			String query = "INSERT INTO " + stu + " values(?, ?, ?, ?, ?)";
			db.insert(query, id, utor_id, first, last, "password");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	public static void uploadStudentFile(String abs_path) throws SQLException {
		String sql = "LOAD DATA LOCAL INFILE '" + abs_path + "' INTO TABLE cscc43f17_manogar7_sakila.STUDENTS FIELDS TERMINATED BY ',' (student_id, utor_id, first_name, last_name)";
		//a.executeSQL(sql);
		db.execute(sql);
		System.out.println(sql + " completed.");
	}
	
	public static void uploadCourseStudents(String course_id, File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			addStudentToCourse(course_id, line.substring(0,line.indexOf(',')));
		}
	}
	
	public static void addAssignment(String course_id, String assignment_id, String num_question, String assignment_name, Date deadline ) {
		try {
			String query = "INSERT INTO " + asmt + " values(?, ?, ?, ?, ?)";
			db.insert(query, course_id, assignment_id, num_question, assignment_name, deadline);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	public static ArrayList<String> getCourseIds() {
		ArrayList<String> list = new ArrayList<String>();
		
		try {
			String query = "SELECT DISTINCT course_id FROM " + asmt + ";";
			ResultSet rs =  db.execute(query); //a.selectRecords(asmt, "DISTINCT course_id");
			while (rs.next()) {
				String id = rs.getString("course_id");
				list.add(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	public static ArrayList<Integer> getAssignmentIds(String course_id) {		
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		try {
			String query = "SELECT assignment_id FROM " + asmt + " WHERE course_id = '" + course_id + "';"; 
			ResultSet rs =  db.execute(query);//a.selectRecordsWhere(asmt, "course_id='" + course_id + "'", "assignment_id");
			while (rs.next()) {
				Integer id = rs.getInt("assignment_id");
				list.add(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	public static Assignment rsToAssignment(ResultSet rs) throws SQLException {
		Assignment asmt = null;
		// columns in order: student_id,first_name,last_name,utorid
		String courseID = rs.getString("course_id");
		int asmtID = rs.getInt("assignment_id");
		int numQues = rs.getInt("num_questions");
		String asmtName = rs.getString("assignment_name");
		Date deadline = rs.getDate("deadline");
		asmt = new Assignment(courseID, asmtID, numQues, asmtName, deadline);
		
		return asmt;
	}

	public static ArrayList<Assignment> getAllAssignments() {
		ArrayList<Assignment> asmts = new ArrayList<Assignment>();
		
		try {
			String query = "SELECT * FROM " + asmt + ";";
			ResultSet rs =  db.execute(query);// a.selectRecords(asmt, "*");
			while (rs.next()) {
				asmts.add(rsToAssignment(rs)); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return asmts;
	}
	
	public static ArrayList<Assignment> getAllAssignments(String course_id) {
		start();
		ArrayList<Assignment> asmts = new ArrayList<Assignment>();
		ResultSet rs = a.selectRecordsWhere(asmt, "course_id='" + course_id + "'", "*");
		try {
			while (rs.next()) {
				asmts.add(rsToAssignment(rs)); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return asmts;
	}
	
	public static Assignment getAssignment(String course_id, String assignment_id) throws SQLException {
	  start();
	  Assignment asm = null;
	  ResultSet rs = a.executeSQLQuery("select * from " + asmt + " where course_id='"+ course_id +"' and assignment_id="+ assignment_id + ";");
	  rs.next();
	  asm = rsToAssignment(rs);
	  close();
	  return asm;
	}

	
	public static void addQuestion(String course_id, String assignment_id, String question_id, String question, String answer ) {
		try {
			String query = "INSERT INTO " + ques + " (course_id, assignment_id, question_id, question, answer_function) values(?, ?, ?, ?, ?);";
			int a_id = Integer.parseInt(assignment_id);
			int q_id = Integer.parseInt(question_id);
			db.insert(query, course_id, a_id, q_id, question, answer);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	/*
	 * Returns an ArrayList of ArrayLists consisting of [question, answer_value] pairs.
	 * Ex.
	 * start();
	 * ArrayList<ArrayList<String>> array = getQuestions("CSCC01", "1");
	 * close();
	 * System.out.println();
	 * for (int i = 0; i < array.size(); i++) {
	 *     ArrayList<String> inner = array.get(i);
	 *     System.out.println(inner.get(0) + " " + inner.get(1));
	 * }
	 * 
	 * where inner.get(0) is the question for each row i and inner.get(1) is the answer function for each row i.
	 */
	public static ArrayList<ArrayList<String>> getQuestions(String course_id, String assignment_id) {
		ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
		ArrayList<String> innerArray;
		try {
			String query = "SELECT * FROM " + ques + " WHERE course_id = '" 
							+ course_id + "' AND assignment_id = '" 
							+ assignment_id + "';";
			ResultSet rs = db.execute(query);
			while (rs.next()) {
				innerArray = new ArrayList<String>();
				innerArray.add(rs.getString("question"));
				innerArray.add(rs.getString("answer_function"));
				array.add(innerArray);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return array;
	}
	
	public static String questionCount(String course_id, String assignment_id) {
		try {
			String query = "SELECT * FROM " + ques + " WHERE course_id = '" + course_id 
							+ "' AND assignment_id = '" + assignment_id + "';";
			ResultSet rs =  db.execute(query);//a.selectRecordsWhere(ques, "course_id='" + course_id + "' AND assignment_id='" + assignment_id +"'", "DISTINCT question_id");
			rs.last();
			return Integer.toString(rs.getRow());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
	}
	
	public static void addProfessor(String professor_id, String first_name, String last_name) {
		try {
			String query = "INSERT INTO " + prof + " values(?, ?, ?, ?);";
			db.insert(query, professor_id, first_name, last_name, "password");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	public static void addStudentToCourse(String course_id, String student_id) {
		try {
			String query = "INSERT INTO " + course_stu + " values(?, ?);";
			db.insert(query, course_id, student_id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	
	
	public static Student rsToStudent(ResultSet rs) throws SQLException {
		Student std = null;
		// columns in order: student_id,first_name,last_name,utorid
		String studentNo = rs.getString("student_id");
		String utor = rs.getString("utor_id");
		String first_name = rs.getString("first_name");
		String last_name = rs.getString("last_name");
		String password = rs.getString("student_password");
		std = new Student(studentNo, utor, first_name, last_name, password);
		
		return std;
	}
	
	public static ArrayList<Student> getAllStudents() {
		ArrayList<Student> students = new ArrayList<Student>();
		try {
			String query = "SELECT * FROM " + stu + ";";
			ResultSet rs = db.execute(query);
			while (rs.next()) {
				students.add(rsToStudent(rs)); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return students;
	}
	
	public static ArrayList<Student>getStudentsFromCourse(String course_id) {
		ArrayList<Student> students = new ArrayList<Student>();
		try {
			String sql = "SELECT"
					+ " STUDENTS.student_id, STUDENTS.utor_id, STUDENTS.first_name, STUDENTS.last_name, STUDENTS.student_password"
					+ " FROM STUDENTS INNER JOIN COURSE_STUDENTS"
					+ " ON STUDENTS.student_id=COURSE_STUDENTS.student_id"
					+ " WHERE COURSE_STUDENTS.course_id='" + course_id + "'"; 
			ResultSet rs = db.execute(sql);
			while (rs.next()) {
				students.add(rsToStudent(rs)); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return students;
	}
	
	public static SingleAnswerQuestion rsToQuestion(ResultSet rs) throws SQLException {
		SingleAnswerQuestion question = null;
		String courseID = rs.getString("course_id");
		int asmtID = rs.getInt("assignment_id");
		String ques = rs.getString("question");
		String ans = rs.getString("answer_function");
		question = new SingleAnswerQuestion(courseID, asmtID, ques, ans);
		
		return question;
	}

	public static ArrayList<SingleAnswerQuestion> getAllQuestions() throws SQLException {
		ArrayList<SingleAnswerQuestion> questions = new ArrayList<SingleAnswerQuestion>();
		String query = "SELECT * FROM " + ques + ";";
		ResultSet rs = db.execute(query);
		while (rs.next()) {
			questions.add(rsToQuestion(rs)); 
		}
		db.close();
		return questions;
	}

	public static ArrayList<SingleAnswerQuestion> getAllCourseQuestions(String course_id) throws SQLException {
		start();
		ArrayList<SingleAnswerQuestion> questions = new ArrayList<SingleAnswerQuestion>();
		ResultSet rs = a.selectRecordsWhere(ques,"course_id='"+ course_id + "'", "*");
		while (rs.next()) {
			questions.add(rsToQuestion(rs)); 
		}
		close();
		return questions;
	}
	
	public static ArrayList<SingleAnswerQuestion> getAllAssignmentQuestions(String course_id, String assignment_id) throws SQLException {
		start();
		ArrayList<SingleAnswerQuestion> questions = new ArrayList<SingleAnswerQuestion>();
		ResultSet rs = a.selectRecordsWhere(ques,"course_id='"+ course_id + "' AND assignment_id='" + assignment_id + "'" , "*");
		while (rs.next()) {
			questions.add(rsToQuestion(rs)); 
		}
		close();
		return questions;
	}
	
	public static int getAvg(String course_id, int aID) {
		start();
		int average = 0;
		try { 
			String query = "SELECT AVG(mark) FROM STUDENT_ASSIGNMENTS " + "WHERE course_id = '"+course_id+"' AND assignment_id = "+aID+";";
			ResultSet avg = db.execute(query);
			if (avg.first()) {
				average = avg.getInt(1);
			} else {
				System.out.println("No records available");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return average;
	}
	
	public static MySQLAccess getMySQLAccess() {
		return db;
	}
	
	public static boolean isStudentInDatabase(String studentNo) {
		boolean inDB = false;
		try {
			String query = "SELECT student_id FROM STUDENTS WHERE student_id = '" + studentNo +"';";
			ResultSet result = DOA.getMySQLAccess().execute(query);
			if (result.first()) {
				inDB = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DOA.getMySQLAccess().close();
		}
		return inDB;
	}
	
	public static boolean loginStudent(String username, String passInput) throws SQLException {
		try {
			String login_command;
			login_command = "SELECT * FROM STUDENTS WHERE student_id='" +username+ "' AND student_password='" +passInput+"';";
			//System.out.println(login_command);
			ResultSet valid = db.execute(login_command);
			if (valid.first()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DOA.getMySQLAccess().close();
		}
		return false;
	}
	
	public static boolean loginProf(String username, String passInput) throws SQLException {
		try {
			String login_command;
			login_command = "SELECT * FROM PROFESSORS WHERE professor_id='" +username+ "' AND professor_password='" +passInput+"';";
			ResultSet valid = db.execute(login_command);
			if (valid.first()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DOA.getMySQLAccess().close();
		}
		return false;
	}
	
	public static void setMark(String sID, String cID, int aID, int mark) {
		start();
		int old_mark = 0;
		try {
			PreparedStatement cmd = a.getConn().prepareStatement("SELECT mark FROM STUDENT_ASSIGNMENTS "
					+ "WHERE course_id='"+cID+"' AND assignment_id="+aID+" AND student_id='"+sID+"';");
			ResultSet curr_result = cmd.executeQuery();
			if (curr_result.first()) {
				old_mark = curr_result.getInt(1);
			}
			if (old_mark < mark) {
				PreparedStatement inst = a.getConn().prepareStatement("INSERT INTO STUDENT_ASSIGNMENTS (student_id, " +
						"course_id, assignment_id) VALUES('"+sID+"', '"+cID+"', "+aID+") ON DUPLICATE KEY UPDATE " +
						"mark="+mark+";");
				inst.executeUpdate();
				System.out.println("Successfully updated mark");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			close();
		}
	}
	
}
