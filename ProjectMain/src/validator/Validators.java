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
	
	private static boolean isAllDigits(String value) {
		int i = 0;
		while (i < value.length()) {
			if (!Character.isDigit(value.charAt(i))) return false;
			i++;
		}
		return true;
	}
	public static boolean isStudentNumberValid(String studentNo) {
		if (studentNo.length() == 10 || studentNo.length() == 9) {
			if(isAllDigits(studentNo)) return true;
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
			boolean validStudentNo, validUtor, validFirstName, validLastName, duplicate;
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
		// checking if assignment is unique
		return (DOA.getAssignment(course_id, assignment_id) == null) ? true : false;
	}
	
	public static List<String> getErrorsInQuestionFile(String abs_path) {
		FileReader file = null;
		BufferedReader buffer = null;
		List<String> errorMessages = new ArrayList<String>();
		// Error check IO calls 
		try {
			file = new FileReader(abs_path);
			buffer = new BufferedReader(file);
			String line;
			String[] splitLine;
			int lineCount = 0;
			String courseID, assignmentID, question, answerFunction, lowerRange, upperRange, decimalPlaces;
			String prevCourseID = "", prevAssignmentID = "";
			while ((line = buffer.readLine()) != null) {
				lineCount++;
				splitLine = line.split(",", -1);
				courseID = splitLine[0];
				assignmentID = splitLine[1];
				question = splitLine[2];
				answerFunction = splitLine[3]; 
				lowerRange = splitLine[4];
				upperRange = splitLine[5];
				decimalPlaces = splitLine[6];
				// valid course id must have 6 characters
				if (!(courseID.length() == 6)) 
					errorMessages.add(String.format("Line %d : Course ID does not have 6 characters.", lineCount));
				// valid assignment id must be a digit > 0 (maybe also all have to be the same assignment ids)
				if (!(assignmentID.length() > 0 && isAllDigits(assignmentID))) 
					errorMessages.add(String.format("Line %d : Assignment ID is not all digits.", lineCount));
				// valid question must have more than or equal to 1 character
				if (!(question.length() > 0)) 
					errorMessages.add(String.format("Line %d : Question does not have any value.", lineCount));
				// valid answer function must have more than or equal to 1 character
				if (!(answerFunction.length() > 0)) 
					errorMessages.add(String.format("Line %d : Answer function does not have any value.", lineCount));
				// valid range must have lower < upper (if both are not "")
				if (!(isValidRange(lowerRange, upperRange))) 
					errorMessages.add(String.format("Line %d : Ranges are invalid.", lineCount));
				// valid decimal can be empty string or all digits
				if (!(decimalPlaces.length() == 0 || isAllDigits(decimalPlaces))) 
					errorMessages.add(String.format("Line %d : Decimal Places is either not empty or has letters.", lineCount));
				if (!prevCourseID.equals(courseID) || !prevAssignmentID.equals(assignmentID)) {
					if (prevCourseID.equals("") && prevAssignmentID.equals("")) {
						prevCourseID = courseID; prevAssignmentID = assignmentID;
					} else {
						errorMessages.add(String.format("Line %d : Course ID or Assignment ID not consistent with the rest above.", lineCount));
					}
					
				}
			}
		} catch (IOException error) {
			System.err.println("IOException: " + error.getMessage());
		} catch (ArrayIndexOutOfBoundsException error) {
			System.err.println("ArrayIndexOutOfBoundsException: " + error.getMessage());
		}
		return errorMessages;
	}
	
	public static boolean isValidRange(String lower, String upper) {
		// range is still valid if they're both empty strings otherwise have to check for other conditions
		if (lower.equals("") && upper.equals("")) return true;
		if ((lower.equals("") && !upper.equals("") || !lower.equals("") && upper.equals("")) || 
				!isAllDigits(lower) || !isAllDigits(upper) || Double.parseDouble(lower) >
				Double.parseDouble(upper))	return false;	
		return true;
	}
}
