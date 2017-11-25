package assignment;

import java.util.Date;
import java.util.Random;

public class Assignment {
	
	private String courseID;
	private int assignmentID;
	private int numQuestions;
	private String assignmentName;
	private Date deadline;
	
	
	public Assignment(String courseID, int assignmentID, int numQuestions, String assignmentName, Date deadline) {
		this.courseID = courseID;
		this.assignmentID = assignmentID;
		this.numQuestions = numQuestions;
		this.assignmentName = assignmentName;
		this.deadline = deadline;
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
	 * @return the numQuestions
	 */
	public int getNumQuestions() {
		return numQuestions;
	}
	/**
	 * @param numQuestions the numQuestions to set
	 */
	public void setNumQuestions(int numQuestions) {
		this.numQuestions = numQuestions;
	}
	/**
	 * @return the assignmentName
	 */
	public String getAssignmentName() {
		return assignmentName;
	}
	/**
	 * @param assignmentName the assignmentName to set
	 */
	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}
	/**
	 * @return the deadline
	 */
	public Date getDeadline() {
		return deadline;
	}
	/**
	 * @param deadline the deadline to set
	 */
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public static int[] questSet(int n, int max) {
		int[] set = new int[n];
		for (int i=0; i<n; i++) {
			Random rand = new Random();
			boolean inSet = false;
			do {
				inSet = false;
				int num = rand.nextInt(max) + 1;
				//System.out.println(num);
				set[i] = num;
				for (int j = 0; j<i; j++) {
					System.out.println(set[i] + " " + set[j]);
					if (set[i] == set[j]) {
						inSet = true;
					}
				}
			} while (inSet);	
		}
	return set;
	}
}
