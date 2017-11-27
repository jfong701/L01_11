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
import user.Professor;

public class Validators {
	
	public static boolean isAllDigits(String value) {
		int i = 0;
		// loop to check for all digits 
		while (i < value.length()) {
			if (!Character.isDigit(value.charAt(i))) return false;
			i++;
		}
		return true;
	}
	
	public static boolean isStudentNumberValid(String studentNo) {
		// student number can only be 9 or 10 digits long
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
				splitLine = line.split(",", -1);
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
				duplicate = DOA.isUserInDatabase(studentNo); 
				if (!validStudentNo) 
					errorMessages.add(String.format("Line %d : Student Number is invalid(needs 10 digits).", lineCount));
				if (!validUtor) 
					errorMessages.add(String.format("Line %d : UTORID is invalid(min 3 characters, max 9 characters).", lineCount));
				if (!validFirstName) 
					errorMessages.add(String.format("Line %d : First Name is invalid(min 1 character, max 40 characters).", lineCount));
				if (!validLastName) 
					errorMessages.add(String.format("Line %d : Last Name is invalid(min 1 character, max 40 characters).", lineCount));
				if (duplicate)
					errorMessages.add(String.format("Line %d : This Student/Professor number already exists in the database.", lineCount));
			}
			buffer.close();
			file.close();
		} catch (IOException error) {
			System.err.println("IOException: " + error.getMessage());
		} catch (ArrayIndexOutOfBoundsException error) {
			System.err.println("ArrayIndexOutOfBoundsException: " + error.getMessage());
		}
		return errorMessages;
	}
	
	public static boolean checkStudent(String username, String passInput) throws SQLException {
		return DOA.getStudent(username, passInput) != null;
	}
	
	public static boolean checkProf(String username, String passInput) throws SQLException {
		return DOA.getProfessor(username, passInput) != null;
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
			buffer.close();
			file.close();
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
	
	public static boolean isProfessorValid(String professorID, String firstName, String lastName) {
		if (professorID.length() < 9 || professorID.length() > 10 || firstName.length() < 1 
				|| lastName.length() < 1) return false;
		// checks for duplicate prof id's and student id's
		if (!(isAllDigits(professorID)) || DOA.isUserInDatabase(professorID)) return false;
		return true;
	}
	
	public static List<String> getErrorsInProfessorFile(String abs_path) { 
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
			String professorID, professorFirstName, professorLastName;
			while ((line = buffer.readLine()) != null) {
				lineCount++;
				splitLine = line.split(",", -1);
				professorID = splitLine[0];
				professorFirstName = splitLine[1];
				professorLastName = splitLine[2];
				// id must be 9-10 digits 
				if (professorID.length() < 8 || professorID.length() > 10 || !isAllDigits(professorID)) 
					errorMessages.add(String.format("Line %d : Professor ID must be 9 to 10 digits.", lineCount));
				// names must be >= 1 character
				if (professorFirstName.length() < 1 || professorLastName.length() < 1) 
					errorMessages.add(String.format("Line %d : First Name or Last Name are < 1 characters.", lineCount));
				// id must not be a duplicate in the database
				if (DOA.isUserInDatabase(professorID))
					errorMessages.add(String.format("Line %d : Student or Professor ID already exists.", lineCount));
			}
			buffer.close();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return errorMessages;
	}
	
	public static List<String> getErrorsInAssignmentFile(String abs_path) {
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
			String courseID, assignmentID, title, numQuestions, date;
			while ((line = buffer.readLine()) != null) {
				lineCount++;
				splitLine = line.split(",", -1);
				courseID = splitLine[0];
				assignmentID = splitLine[1];
				numQuestions = splitLine[2];
				title = splitLine[3];
				date = splitLine[4];
				// course id must be = 6 characters
				if (courseID.length() != 6) 
					errorMessages.add(String.format("Line %d : Course ID does not have 6 characters.", lineCount));
				// assignment id must be >= 1 digits
				if (assignmentID.length() < 1 || !isAllDigits(assignmentID))
					errorMessages.add(String.format("Line %d : Assignment ID must have all digits.", lineCount));
				// num questions must be > 0 
				if (!isAllDigits(numQuestions) || Integer.parseInt(numQuestions) < 1) 
					errorMessages.add(String.format("Line %d : Number of Questions must be > 0.", lineCount));
				// title > 0 characters
				if (title.length() < 1) 
					errorMessages.add(String.format("Line %d : Assignment Title must have at least 1 character.", lineCount));
				// date must be of the form yyyy-mm-dd
				if (!isValidDate(date))
					errorMessages.add(String.format("Line %d : Date must be of the format yyyy-mm-dd.", lineCount));
				// another assignment can't exist
				if (DOA.getAssignment(courseID, assignmentID) != null)
					errorMessages.add(String.format("Line %d : This assignment already exists.", lineCount));
			}
			buffer.close();
			file.close();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
		return errorMessages;
	}
		
	public static boolean isValidDate(String date) {
		String[] dateSplit = date.split("-", -1);
		for (String i:dateSplit) {
			if (i.equals("")) return false;
		}
		// if format isn't correct or not all digits
		if (dateSplit.length != 3 || !isAllDigits(dateSplit[0] + dateSplit[1] + dateSplit[2])) return false;
		int year, month, day;
		year = Integer.parseInt(dateSplit[0]);
		month = Integer.parseInt(dateSplit[1]);
		day = Integer.parseInt(dateSplit[2]);
		// valid days/months/year
		if (day < 1 || day > 31 || month < 1 || month > 12 || year < 0) return false;
		return true;
	}

	public static void main(String[] args) throws SQLException {
	}
}
