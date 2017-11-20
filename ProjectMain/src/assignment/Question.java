package assignment;
import java.io.*;

public class Question {
	
	String question;
	String answer;
	
	public Question(String q, String ans) {
		this.question = q;
		this.answer = ans;
	}
	/*
	 * boolean isCorrect
	   Return if the answer to this question is correct
	*/
	public boolean isCorrect(String ans) {
		return (answer.equals(ans));
	}
	
	public String toString() {
		return "Question: " + question + "\nAnswer: " + answer;
	}
}
