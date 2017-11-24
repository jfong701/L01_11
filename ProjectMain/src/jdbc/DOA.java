package jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.Statement;

import assignment.Assignment;
import assignment.SingleAnswerQuestion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jdbc.MySQLAccess;
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
	
	private static MySQLAccess a;
	
	public static void main(String[] args) throws SQLException {
			start();
			a.dropTable(stu);
			a.dropTable(asmt);
			a.dropTable(ques);
			a.dropTable(stu_asmt);
			a.dropTable(prof);
			a.dropTable(course_stu);
			initDatabase();
			close();
	}
	
	public static void initDatabase() throws SQLException {
		a = new MySQLAccess();

		System.out.println("Initializing database");
		a.loadAndConnect(dbName);
		
		a.createTable(stu,
				"student_id VARCHAR(10) NOT NULL",
				"utor_id VARCHAR(9) UNIQUE NOT NULL",
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
		a = new MySQLAccess();
		a.loadAndConnect(dbName);
	}
	
	public static void close() {
		a.close();
	}
	
	public static void addStudent(String id, String utor_id, String first, String last) {
		start();
		String sql = a.preparedRecordsSQL(stu, 5, "student_id", "utor_id", "first_name", "last_name", "student_password");
		System.out.println(sql);
		Connection conn = a.getConn();
		try {
			PreparedStatement pr = conn.prepareStatement(sql);
			pr.setString(1,  id);
			pr.setString(2, utor_id);
			pr.setString(3, first);
			pr.setString(4, last);
			pr.setString(5, "password");
			pr.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public static boolean isStudentNumberValid(String studentNo) {
		if (studentNo.length() == 10) {
			int i = 0;
			boolean digit = true;
			// loop to check for string of all digits
			while (i < studentNo.length() && digit) {
				digit = digit && Character.isDigit(studentNo.charAt(i));
				i++;
			}
			if (!digit) {
				return false;
			} else {
				return true;
			}
		} 
		return false;
	}

	public static boolean isStudentInDatabase(String studentNo) {
		start();
		boolean inDB = false;
		try {
			PreparedStatement cmd = a.getConn().prepareStatement("SELECT student_id FROM STUDENTS WHERE student_id = '" + studentNo +"';");
			ResultSet result = cmd.executeQuery();
			if (result.first()) {
				inDB = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return inDB;
	}
	
	public static List<String> getErrorsInStudentFile(String abs_path) {
		FileReader file = null;
		BufferedReader buffer = null;
		List<String> errorMessages = new ArrayList<String>();
		// Error check IO calls 
		try {
			file = new FileReader(abs_path);
			buffer = new BufferedReader(file);
			String line;
			String[] splitLine;
			String studentNo, studentUtor, studentFirstName, studentLastName;
			int lineCount = 0;
			boolean validStudentNo, validUtor, validFirstName, validLastName, duplicate = false;
			// read line by line, split and trim the strings. ASSUMING WE'RE GIVEN COMMA SEPARATED CSV FILES 
			// format: studentNo, utorID, firstName, lastName
			while (((line = buffer.readLine()) != null)) {
				lineCount++;
				splitLine = line.split(",");
				if (splitLine.length != 4) {
					errorMessages.add(String.format("Line %d : Insufficient number of fields(needs studentNumber, UTORID, firstName, lastName).", lineCount));
					continue;
				}
				studentNo = splitLine[0].trim();
				studentUtor = splitLine[1].trim();
				studentFirstName = splitLine[2].trim();
				studentLastName = splitLine[3].trim();
				// check studentNo for 10 digits
				validStudentNo = isStudentNumberValid(studentNo);
				// check utorid for min 3 characters and max 10 characters
				validUtor = studentUtor.length() <= 10 && studentUtor.length() > 2;
				// check first and last name for at least 1 character each and max 40 characters each
				validFirstName = studentFirstName.length() >= 1 && studentFirstName.length() <= 40;
				validLastName = studentLastName.length() >= 1 && studentLastName.length() <= 40;
				// need to check database for duplicates
				duplicate = isStudentInDatabase(studentNo); // dummy value for now until implementing it
				if (!validStudentNo) 
					errorMessages.add(String.format("Line %d : Student Number is invalid(needs 10 digits).", lineCount));
				if (!validUtor) 
					errorMessages.add(String.format("Line %d : UTORID is invalid(min 3 characters, max 10 characters).", lineCount));
				if (!validFirstName) 
					errorMessages.add(String.format("Line %d : First Name is invalid(min 1 character, max 40 characters).", lineCount));
				if (!validLastName) 
					errorMessages.add(String.format("Line %d : Last Name is invalid(min 1 character, max 40 characters).", lineCount));
				if (duplicate)
					errorMessages.add(String.format("Line %d : This Student Number already exists in the database.", lineCount));
			}
		} catch (IOException error) {
			System.err.println("IOException: " + error.getMessage());
		} catch (ArrayIndexOutOfBoundsException error) {
			System.err.println("ArrayIndexOutOfBoundsException: " + error.getMessage());
		}
		return errorMessages;
	}
	
	public static void uploadStudentFile(String abs_path) {
		String sql = "LOAD DATA LOCAL INFILE '" + abs_path + "' INTO TABLE cscc43f17_manogar7_sakila.STUDENTS FIELDS TERMINATED BY ',' (student_id, utor_id, first_name, last_name)";
		a.executeSQL(sql);
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
		start();
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
		} finally {
			close();
		}
	}
	
	public static ArrayList<String> getCourseIds() {
		start();
		ArrayList<String> list = new ArrayList<String>();
		
		try {
			ResultSet rs =  a.selectRecords(asmt, "DISTINCT course_id");
			while (rs.next()) {
				String id = rs.getString("course_id");
				list.add(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return list;
	}
	
	public static ArrayList<Integer> getAssignmentIds(String course_id) {		
		start();
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		try {
			ResultSet rs =  a.selectRecordsWhere(asmt, "course_id='" + course_id + "'", "assignment_id");
			while (rs.next()) {
				Integer id = rs.getInt("assignment_id");
				list.add(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return list;
	}
	
	public static void uploadAssignmentFile(String abs_path) {
		String sql = "LOAD DATA LOCAL INFILE '"+ abs_path + "' INTO TABLE assignments FIELDS TERMINATED BY ',' (course_id, assignment_id, num_questions, assignment_name, deadline);";
		a.executeSQL(sql);
		System.out.println(sql + " completed.");
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
		start();
		ArrayList<Assignment> asmts = new ArrayList<Assignment>();
		ResultSet rs = a.selectRecords(asmt, "*");
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

	
	public static void addQuestion(String course_id, String assignment_id,  String question, String answer ) {
		start();
		String sql = a.preparedRecordsSQL(ques, 4, "course_id", "assignment_id", "question", "answer_function");
		System.out.println(sql);
		Connection conn = a.getConn();
		try {
			PreparedStatement pr = conn.prepareStatement(sql);
			pr.setString(1,  course_id);
			pr.setInt(2, Integer.parseInt(assignment_id));
			pr.setString(3, question);
			pr.setString(4, answer);
			
			pr.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public static void uploadQuestionFile(String abs_path) {
		String sql = "LOAD DATA LOCAL INFILE '"+ abs_path + "' INTO TABLE questions FIELDS TERMINATED BY ',' (course_id, assignment_id, question, answer_function, lower_range, upper_range, decimal_places);";
		a.executeSQL(sql);
		System.out.println(sql + " completed.");
	}

	
	public static ArrayList<ArrayList<String>> getQuestions(String course_id, String assignment_id) {
		ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
		ArrayList<String> innerArray;
		try {
			ResultSet rs =  a.selectRecordsWhere(ques, "course_id='" + course_id + "' AND assignment_id='" + assignment_id +"'", "*");
			int i = 0;
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
	
	public static String QuestionCount(String course_id, String assignment_id) {
		start();
		try {
			ResultSet rs =  a.selectRecordsWhere(ques, "course_id='" + course_id + "' AND assignment_id='" + assignment_id +"'", "DISTINCT question_id");
			rs.last();
			return Integer.toString(rs.getRow());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		

		
	}
	
	public static void addProfessor(String id, String first, String last) {
		start();
		String sql = a.preparedRecordsSQL(prof, 4, "professor_id", "professor_first_name", "professor_last_name", "professor_password");
		System.out.println(sql);
		Connection conn = a.getConn();
		try {
			PreparedStatement pr = conn.prepareStatement(sql);
			pr.setString(1,  id);
			pr.setString(2, first);
			pr.setString(3, last);
			pr.setString(4, "password");
			pr.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public static void addStudentToCourse(String course_id, String student_id) {
		start();
		String sql = a.preparedRecordsSQL(course_stu, 2, "course_id", "student_id");
		System.out.println(sql);
		Connection conn = a.getConn();
		try {
			PreparedStatement pr = conn.prepareStatement(sql);
			pr.setString(1,  course_id);
			pr.setString(2, student_id);
			pr.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
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
		start();
		ArrayList<Student> students = new ArrayList<Student>();
		ResultSet rs = a.selectRecords(stu, "*");
		try {
			while (rs.next()) {
				students.add(rsToStudent(rs)); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return students;
	}
	
	public static ArrayList<Student>getStudentsFromCourse(String course_id) {
		start();
		ArrayList<Student> students = new ArrayList<Student>();
		String sql = "SELECT"
				+ " STUDENTS.student_id, STUDENTS.utor_id, STUDENTS.first_name, STUDENTS.last_name, STUDENTS.student_password"
				+ " FROM STUDENTS INNER JOIN COURSE_STUDENTS"
				+ " ON STUDENTS.student_id=COURSE_STUDENTS.student_id"
				+ " WHERE COURSE_STUDENTS.course_id='" + course_id + "'"; 
		ResultSet rs = a.executeSQLQuery(sql);
		start();
		try {
			while (rs.next()) {
				students.add(rsToStudent(rs)); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
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
		start();
		ArrayList<SingleAnswerQuestion> questions = new ArrayList<SingleAnswerQuestion>();
		ResultSet rs = a.selectRecords(ques, "*");
		while (rs.next()) {
			questions.add(rsToQuestion(rs)); 
		}
		close();
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
	
	public static boolean loginStudent(String username, String passInput) throws SQLException {
		start();
		try {
			String login_command;
			login_command = "SELECT * FROM STUDENTS WHERE student_id='" +username+ "' AND student_password='" +passInput+"';";
			//System.out.println(login_command);
			Connection conn = a.getConn();
			PreparedStatement login_cmd = conn.prepareStatement(login_command);
			ResultSet valid = login_cmd.executeQuery();
			if (valid.first()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}
	
	public static boolean loginProf(String username, String passInput) throws SQLException {
		start();
		try {
			String login_command;
			login_command = "SELECT * FROM PROFESSORS WHERE professor_id='" +username+ "' AND professor_password='" +passInput+"';";
			Connection conn = a.getConn();
			PreparedStatement login_cmd = conn.prepareStatement(login_command);
			ResultSet valid = login_cmd.executeQuery();
			if (valid.first()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	public static int getAvg(String course_id, int aID) {
		start();
		int average = 0;
		try { 
			PreparedStatement cmd = a.getConn().prepareStatement("SELECT AVG(mark) FROM STUDENT_ASSIGNMENTS "
					+ "WHERE course_id = '"+course_id+"' AND assignment_id = "+aID+";");
			ResultSet avg = cmd.executeQuery();
			if (avg.first()) {
				average = avg.getInt(1);
			} else {
				System.out.println("No records available");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	
		
		return average;
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
