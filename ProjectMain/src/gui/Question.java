import java.util.*;
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
	/*
	 * FileWrite (FileWriter outFile)
	 * Take a valid file writer and write the question's data to the
	 * file refered to by outFile
	 */
	public void fileWrite(FileWriter outFile) {
		try {
			outFile.write("\"" + question + "\"," + "\"" + answer + "\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
