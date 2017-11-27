package jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import assignment.Assignment;
import assignment.SingleAnswerQuestion;
import gui.MessageBox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;

import jdbc.OldMySQLAccess;
import user.Professor;
import user.Student;

public class DOA {

	//MYSQL FUNCTION EXAMPLES AT THE END
	
	private final static String dbName = "cscc43f17_manogar7_sakila";
	private final static String stu = "STUDENTS";
	private final static String asmt = "ASSIGNMENTS";
	private final static String ques = "QUESTIONS";
	private final static String stu_asmt = "STUDENT_ASSIGNMENTS";
	private final static String prof = "PROFESSORS";
	private final static String course_stu = "COURSE_STUDENTS";
	
	private static MySQLAccess db = new MySQLAccess();
	
	public static void main(String[] args) throws SQLException {
			//start();
			//a.dropTable(stu);
			//close();
			//initDatabase();
			//close();
			/*
			a.dropTable(stu);
			a.dropTable(asmt);
			a.dropTable(ques);
			initDatabase();
			*/
	}
	
	public static void initDatabase() throws SQLException {

		System.out.println("Initializing database");
		db.loadAndConnect(dbName);
		
		db.createTable(stu,
				"student_id VARCHAR(10) NOT NULL",
				"utor_id VARCHAR(8) UNIQUE NOT NULL",
				"first_name VARCHAR(255) NOT NULL",
				"last_name VARCHAR(255) NOT NULL",
				"student_password VARCHAR(25) DEFAULT ''",
				"PRIMARY KEY ( student_id )"
				);
		db.createTable(asmt,
				"course_id VARCHAR(8) NOT NULL",
				"assignment_id INTEGER NOT NULL",
				"num_questions INTEGER NOT NULL",
				"assignment_name VARCHAR(225)",
				"deadline DATE",
				"PRIMARY KEY ( course_id, assignment_id )"
				);
		db.createTable(ques,
				"question_id INTEGER NOT NULL AUTO_INCREMENT",
				"course_id VARCHAR(8) NOT NULL",
				"assignment_id INTEGER NOT NULL",
				"question VARCHAR(1000) NOT NULL",
				"answer_function VARCHAR(1000) NOT NULL",
				"lower_range INTEGER",
				"upper_range INTEGER",
				"decimal_places INTEGER",
				"FOREIGN KEY ( course_id, assignment_id ) REFERENCES ASSIGNMENTS ( course_id, assignment_id )",
				"PRIMARY KEY ( question_id )"
				);
		db.createTable(stu_asmt,
				"student_id CHAR(10) NOT NULL",
				"course_id VARCHAR(8) NOT NULL",
				"assignment_id INTEGER NOT NULL",
				"mark INTEGER",
				"FOREIGN KEY ( student_id ) REFERENCES STUDENTS ( student_id )",
				"FOREIGN KEY ( course_id, assignment_id ) REFERENCES ASSIGNMENTS ( course_id, assignment_id )",
				"PRIMARY KEY ( student_id, course_id, assignment_id )"
				);
		db.createTable(prof,
				"professor_id CHAR(10) NOT NULL",
				"professor_first_name VARCHAR(225)",
				"professor_last_name VARCHAR(225)",
				"professor_password VARCHAR(25)",
				"PRIMARY KEY ( professor_id )"
				);
		db.createTable(course_stu,
				"course_id VARCHAR(8) NOT NULL",
				"student_id CHAR(10) NOT NULL",
				"FOREIGN KEY ( student_id ) REFERENCES STUDENTS ( student_id )",
				"FOREIGN KEY ( course_id ) REFERENCES ASSIGNMENTS ( course_id )",
				"PRIMARY KEY ( student_id, course_id )"
				);

		System.out.println("Finished initialization");
	}
	
	public static void addStudent(String id, String utor_id, String first, String last) {
		try {
			String query = "INSERT INTO " + stu + " values(?, ?, ?, ?, ?)";
			db.prepExecute(query, id, utor_id, first, last, "password");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	public static void uploadStudentFile(String abs_path) throws SQLException {
		String sql = "LOAD DATA LOCAL INFILE '" + abs_path + "' INTO TABLE cscc43f17_manogar7_sakila.STUDENTS FIELDS TERMINATED BY ',' (student_id, utor_id, first_name, last_name)";
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
			db.prepExecute(query, course_id, assignment_id, num_question, assignment_name, deadline);
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
			ResultSet rs =  db.execute(query); 
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
			ResultSet rs =  db.execute(query);
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
	
	public static void uploadAssignmentFile(String abs_path) {
		String sql = "LOAD DATA LOCAL INFILE '"+ abs_path + "' INTO TABLE ASSIGNMENTS FIELDS TERMINATED BY ',' (course_id, assignment_id, num_questions, assignment_name, deadline);";
		try {
			db.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		System.out.println(sql + " completed.");
	}

	
	public static Assignment rsToAssignment(ResultSet rs) throws SQLException {
		Assignment asmt = null;
		// columns in order: course id, assignment id, number of questions, assignment name and deadline
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
			ResultSet rs =  db.execute(query);
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
	
	public static ArrayList<Assignment> getAllAssignments(String course_id) throws SQLException {
		ArrayList<Assignment> asmts = new ArrayList<Assignment>();
		String query = "SELECT * FROM " + asmt + " WHERE course_id = '" + course_id + "';";
		ResultSet rs = db.execute(query);
		try {
			while (rs.next()) {
				asmts.add(rsToAssignment(rs)); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.close();
		return asmts;
	}
	
	public static Assignment getAssignment(String course_id, String assignment_id) throws SQLException {
	  Assignment asm = null;
	  String query = "SELECT * FROM " + asmt + " WHERE course_id = '" + course_id + "' AND assignment_id = " + assignment_id + ";";
	  System.out.println(query);
	  ResultSet rs = db.execute(query);
	  if (rs.next()) {
		  asm = rsToAssignment(rs);
	  }
	  
	  return asm;
	}

	
	public static void addQuestion(String course_id, String assignment_id,  String question, String answer ) {
		String query = "INSERT INTO " + ques + " (course_id, assignment_id, question, answer_function) values (? , ?, ?, ?)";
		try {
			db.prepExecute(query, course_id, Integer.parseInt(assignment_id), question, answer);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	public static void uploadQuestionFile(String abs_path){
		String sql = "LOAD DATA LOCAL INFILE '"+ abs_path + "' INTO TABLE QUESTIONS FIELDS TERMINATED BY ',' (course_id, assignment_id, question, answer_function, lower_range, upper_range, decimal_places);";
		try {
			db.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(sql + " completed.");
	}

	
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
		} finally {
			db.close();
		}
		return array;
	}
	
	public static String questionCount(String course_id, String assignment_id) {
		try {
			String query = "SELECT * FROM " + ques + " WHERE course_id = '" + course_id 
							+ "' AND assignment_id = '" + assignment_id + "';";
			ResultSet rs =  db.execute(query);
			rs.last();
			return Integer.toString(rs.getRow());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public static void addProfessor(String professor_id, String first_name, String last_name) {
		try {
			String query = "INSERT INTO " + prof + " values(?, ?, ?, ?);";
			db.prepExecute(query, professor_id, first_name, last_name, "password");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	public static void uploadProfessorFile(String abs_path) throws SQLException {
		String sql = "LOAD DATA LOCAL INFILE '" + abs_path + "' INTO TABLE cscc43f17_manogar7_sakila.PROFESSORS FIELDS TERMINATED BY ',' (professor_id, professor_first_name, professor_last_name)";
		db.execute(sql);
		System.out.println(sql + " completed.");
	}
	
	public static Professor rsToProfessor(ResultSet rs) throws SQLException {
		Professor prof = null;
		// columns in order: prof id, prof first name, prof last name and password
		String profID = rs.getString("professor_id");
		String first_name = rs.getString("professor_first_name");
		String last_name = rs.getString("professor_last_name");
		String password = rs.getString("professor_password");
		prof = new Professor(profID,  first_name, last_name, password);
		return prof;
	}
	
	public static ArrayList<Professor> getAllProfessors() {
		ArrayList<Professor> profs = new ArrayList<Professor>();
		try {
			String query = "SELECT * FROM " + prof + ";";
			ResultSet rs = db.execute(query);
			while (rs.next()) {
				profs.add(rsToProfessor(rs)); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return profs;
	}
	
	public static void addStudentToCourse(String course_id, String student_id) {
		try {
			String query = "INSERT INTO " + course_stu + " values(?, ?);";
			db.prepExecute(query, course_id, student_id);
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
		int lowR = rs.getInt("lower_range");
		int uppR = rs.getInt("upper_range");
		int dec = rs.getInt("decimal_places");
		question = new SingleAnswerQuestion(courseID, asmtID, ques, ans, lowR, uppR, dec);
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
		ArrayList<SingleAnswerQuestion> questions = new ArrayList<SingleAnswerQuestion>();
		String query = "SELECT * FROM " + ques + " WHERE course_id = '" + course_id + "';";
		ResultSet rs = db.execute(query);
		while (rs.next()) {
			questions.add(rsToQuestion(rs)); 
		}
		db.close();
		return questions;
	}
	
	public static ArrayList<SingleAnswerQuestion> getAllAssignmentQuestions(String course_id, String assignment_id) throws SQLException {
		ArrayList<SingleAnswerQuestion> questions = new ArrayList<SingleAnswerQuestion>();
		String query = "SELECT * FROM " + ques + " WHERE course_id = '" + course_id + "' AND assignment_id = " + assignment_id + ";";

		ResultSet rs = db.execute(query);
		while (rs.next()) {
			questions.add(rsToQuestion(rs)); 
		}
		db.close();
		return questions;
	}
	
	public static int getAvg(String course_id, int aID) {
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
	
	public static boolean isUserInDatabase(String userID) {
		try {
			String queryStudent = "SELECT student_id FROM STUDENTS WHERE student_id = '" + userID + "';";
			String queryProf = "SELECT professor_id from PROFESSORS WHERE professor_id = '" + userID + "';";
			ResultSet result = db.execute(queryStudent);
			if (result.first()) return true;
			result = db.execute(queryProf);
			if (result.first()) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return false;
	}
	
	public static Student getStudent(String username, String passInput) throws SQLException {
		Student result = null;
		try {
			String login_command;
			login_command = "SELECT * FROM STUDENTS WHERE student_id='" +username+ "' AND student_password='" +passInput+"';";
			//System.out.println(login_command);
			ResultSet valid = db.execute(login_command);
			if (valid.first()) {
				result = rsToStudent(valid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return result;
	}
	
	public static Professor getProfessor(String username, String passInput) throws SQLException {
		Professor result = null;
		try {
			String login_command;
			login_command = "SELECT * FROM PROFESSORS WHERE professor_id='" +username+ "' AND professor_password='" +passInput+"';";
			ResultSet valid = db.execute(login_command);
			if (valid.first()) {
				result = rsToProfessor(valid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return result;
	}
	public static void setMark(String sID, String cID, int aID, int mark) {
		int old_mark = 0;
		try {
			String query = "SELECT mark FROM STUDENT_ASSIGNMENTS "
					+ "WHERE course_id='"+cID+"' AND assignment_id="+String.valueOf(aID)+" AND student_id='"+sID+"';";
			ResultSet curr_result = db.execute(query); 
			if (curr_result.first()) {
				old_mark = curr_result.getInt(1);
			}
			if (old_mark < mark) {
				query = "INSERT INTO STUDENT_ASSIGNMENTS (student_id, " +
						"course_id, assignment_id) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE " +
						"mark="+mark+";";
				db.prepExecute(query, sID, cID, aID);
				System.out.println("Successfully updated mark");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			db.close();
		}
	}
	
	public static void deleteStudentRecord(String sID, String cID) {
		try {
			//PreparedStatement delCourse = a.getConn().prepareStatement("DELETE FROM COURSE_STUDENTS WHERE "+
			//		"course_id = '"+cID+"' AND student_id='"+sID+"';");
			//PreparedStatement delMark = a.getConn().prepareStatement("DELETE FROM STUDENT_ASSIGNMENTS WHERE "+
			//		"course_id = '"+cID+"' AND student_id='"+sID+"';");
			String delCourse = "DELETE FROM COURSE_STUDENTS WHERE " + "course_id = ? AND student_id  = ?;";
			String delMark = "DELETE FROM STUDENT_ASSIGNMENTS WHERE " + "course_id = ? AND student_id = ?;";
			db.prepExecute(delCourse, sID, cID);
			db.prepExecute(delMark, sID, cID);
			//MessageBox.show("Delete Success", "Entry successfully deleted.");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	public static int getMark(String sID, String cID, int aID) {
		int mark = 0;
		try {
			//PreparedStatement cmd = a.getConn().prepareStatement("SELECT mark FROM STUDENT_ASSIGNMENTS "
			//		+ "WHERE course_id='" + cID + "' AND assignment_id=" + aID + " AND student_id='" + sID + "';");
			String query = "SELECT mark FROM STUDENT_ASSIGNMENTS WHERE course_id ='"+cID + 
							"' AND assignment_id=" + String.valueOf(aID) + " AND student_id ='"+sID+"';";
			ResultSet curr_result = db.execute(query);
			if (curr_result.first()) {
				mark = curr_result.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return mark;
	}
}
