package jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import assignment.Question;

import java.io.BufferedReader;
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
	
	private static String dbName = "cscc43f17_manogar7_sakila";
	private static String stu = "STUDENTS";
	private static String asmt = "ASSIGNMENTS";
	private static String ques = "QUESTIONS";
	private static String stu_asmt = "STUDENT_ASSIGNMENTS";
	private static String prof = "PROFESSORS";
	
	
	private static MySQLAccess a;
	
	public static void main(String[] args) throws SQLException {
			start();
			//a.dropTable(stu);
			//close();
			initDatabase();
			close();
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
		start();
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
		} finally {
			close();
		}
	}
	
	public static boolean validateStudentFile(String abs_path) {
		FileReader file = null;
		BufferedReader buffer = null;
		boolean valid = false;
		// Error check IO calls 
		try {
			file = new FileReader(abs_path);
			buffer = new BufferedReader(file);
			String line;
			String[] splitLine;
			String studentNo, studentUtor, studentFirstName, studentLastName;
			int lineCount = 0;
			valid = true;
			// read line by line, split and trim the strings. ASSUMING WE'RE GIVEN COMMA SEPARATED CSV FILES 
			// format: studentNo, utorID, firstName, lastName
			while (((line = buffer.readLine()) != null) && valid) {
				System.out.println(line);
				lineCount++;
				splitLine = line.split(",");
				studentNo = splitLine[0].trim();
				studentUtor = splitLine[1].trim();
				studentFirstName = splitLine[1].trim();
				studentLastName = splitLine[2].trim();
				// check studentNo for 10 digits
				if (studentNo.length() == 10) {
					int i = 0;
					boolean digit = true;
					// loop to check for string of all digits
					while (i < studentNo.length() && !digit) {
						digit = Character.isDigit(studentNo.charAt(i));
						i++;
					}
					if (!digit) {
						valid = false;
					}
				}
				
				// check utorid for max 10 characters
				if (studentUtor.length() >= 10 || studentUtor.length() < 1) {
					valid = false;
				}
				// need to check database for duplicates
			}
			// checks if the file is empty
			if (lineCount == 0) {
				valid = false;
			}
		} catch (IOException error) {
			System.err.println("IOException: " + error.getMessage());
		}
		return valid;
	}
	
	public static void uploadStudentFile(String abs_path) {
		String sql = "LOAD DATA LOCAL INFILE '" + abs_path + "' INTO TABLE cscc43f17_manogar7_sakila.STUDENTS FIELDS TERMINATED BY ',' (student_id, utor_id, first_name, last_name)";
		a.executeSQL(sql);
		System.out.println(sql + " completed.");
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

	
	public static void addQuestion(String course_id, String assignment_id, String question_id, String question, String answer ) {
		start();
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
		} finally {
			close();
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
		} finally {
			close();
		}
	}
	
	public Student rsToStudent(ResultSet rs) throws SQLException {
		Student std = null;
		while (rs.next()) {
			// columns in order: student_id,first_name,last_name,utorid
		    String studentNo = rs.getString("student_id");
		    String utor = rs.getString("utor_id");
		    String first_name = rs.getString("first_name");
		    String last_name = rs.getString("last_name");
		    std = new Student(studentNo, utor, first_name, last_name);
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
