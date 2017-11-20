package assignment;
import java.io.*;
import java.util.Date;

public class SingleAnswerQuestion {
	
	private String courseID;
	private int assignmentID;
	private String question;
	private String answerFunction;
	private int lowerRange;
	private int upperRange;
	private int decimalPlaces;

	
	public SingleAnswerQuestion(String courseID, int assignmentID, String question, String answerFunction) {
		super();
		this.courseID = courseID;
		this.assignmentID = assignmentID;
		this.question = question;
		this.answerFunction = answerFunction;
	}
	
	
	public SingleAnswerQuestion(String courseID, int assignmentID, String question, String answerFunction,
			int lowerRange, int upperRange, int decimalPlaces) {
		super();
		this.courseID = courseID;
		this.assignmentID = assignmentID;
		this.question = question;
		this.answerFunction = answerFunction;
		this.lowerRange = lowerRange;
		this.upperRange = upperRange;
		this.decimalPlaces = decimalPlaces;
	}
	/**
	 * @return the courseID
	 */
	public String getCourseID() {
		return courseID;
	}
	/**
	 * @param courseID the courseID to set
	 */
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}
	/**
	 * @return the assignmentID
	 */
	public int getAssignmentID() {
		return assignmentID;
	}
	/**
	 * @param assignmentID the assignmentID to set
	 */
	public void setAssignmentID(int assignmentID) {
		this.assignmentID = assignmentID;
	}
	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	/**
	 * @return the answerFunction
	 */
	public String getAnswerFunction() {
		return answerFunction;
	}
	/**
	 * @param answerFunction the answerFunction to set
	 */
	public void setAnswerFunction(String answerFunction) {
		this.answerFunction = answerFunction;
	}
	/**
	 * @return the lowerRange
	 */
	public int getLowerRange() {
		return lowerRange;
	}
	/**
	 * @param lowerRange the lowerRange to set
	 */
	public void setLowerRange(int lowerRange) {
		this.lowerRange = lowerRange;
	}
	/**
	 * @return the upperRange
	 */
	public int getUpperRange() {
		return upperRange;
	}
	/**
	 * @param upperRange the upperRange to set
	 */
	public void setUpperRange(int upperRange) {
		this.upperRange = upperRange;
	}
	/**
	 * @return the decimalPlaces
	 */
	public int getDecimalPlaces() {
		return decimalPlaces;
	}
	/**
	 * @param decimalPlaces the decimalPlaces to set
	 */
	public void setDecimalPlaces(int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}
	/*
	 * boolean isCorrect
	   Return if the answer to this question is correct
	*/
	public boolean isCorrect(String ans) {
		return (answerFunction.equals(ans));
	}
	/*
	 * FileWrite (FileWriter outFile)
	 * Take a valid file writer and write the question's data to the
	 * file referred to by outFile
	 */
	public void fileWrite(FileWriter outFile) {
		try {
			outFile.write("\"" + question + "\"," + "\"" + answerFunction + "\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return "Question: " + question + "\nAnswer: " + answerFunction;
	}
}
