package validator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jdbc.DOA;

public class Validators {
	
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
				validUtor = studentUtor.length() <= 9 && studentUtor.length() > 3;
				// check first and last name for at least 1 character each and max 40 characters each
				validFirstName = studentFirstName.length() >= 1 && studentFirstName.length() <= 40;
				validLastName = studentLastName.length() >= 1 && studentLastName.length() <= 40;
				// need to check database for duplicates
				duplicate = DOA.isStudentInDatabase(studentNo); // dummy value for now until implementing it
				if (!validStudentNo) 
					errorMessages.add(String.format("Line %d : Student Number is invalid(needs 10 digits).", lineCount));
				if (!validUtor) 
					errorMessages.add(String.format("Line %d : UTORID is invalid(min 3 characters, max 9 characters).", lineCount));
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
	
	public static boolean loginStudent(String username, String passInput) throws SQLException {
		try {
			String login_command;
			login_command = "SELECT * FROM STUDENTS WHERE student_id='" +username+ "' AND student_password='" +passInput+"';";
			//System.out.println(login_command);
			ResultSet valid = DOA.getMySQLAccess().execute(login_command);
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
			ResultSet valid = DOA.getMySQLAccess().execute(login_command);
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
	
	public static boolean isSingleAnswerQuestionValid(String question, String answer) {
		return question.length() > 0 && answer.length() > 0;
	}
	
	public static boolean isAssignmentValid(String course_id, String assignment_id, String num_questions, String assignment_name, LocalDate localDate) throws NumberFormatException, SQLException {
		// checks for course_id and assignment_id having values for checking duplicate assignments
		if (course_id.length() != 6 || assignment_id.length() < 1 ||
			num_questions.length() < 1 || Integer.parseInt(num_questions) < 0 
			|| assignment_name.length() < 1 || localDate == null) return false;
		System.out.println("CHECKING DUPES");
		return (DOA.getAssignment(course_id, assignment_id) == null) ? true : false;
	}
	
	public static boolean isAssignmentUnique(String course_id, String assignment_id) throws SQLException {
		return DOA.getAssignment(course_id, assignment_id) == null;
	}
	
	
	
}
