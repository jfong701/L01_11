import java.util.*;
import java.io.*;
import question

public class Assignment {
	Question question[];
	int qCount;
	public Question(int qCount) {
		this.question = new Question [qCount];
		this.qCount = qCount;
	}
	/*
	 * initialize a question at index
	*/
	public void addQuestion(int index, String q, String ans) {
		question[i] = new Question(q, ans);
	}
	/*
	 * FileWrite (FileWriter outFile)
	 * Take a valid file writer and write the question's data to the
	 * file refered to by outFile
	 */
	public void fileWrite(FileWriter outFile) {
		for (int i = 0; i < qCount; i++) {
			try {
				question[i].fileWrite(outFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
