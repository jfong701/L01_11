package assignment;
import java.io.*;

public class Assignment {
	Question question[];
	int qCount;
	public void Questions(int qCount) {
		this.question = new Question [qCount];
		this.qCount = qCount;
	}
	/*
	 * initialize a question at index
	*/
	public void addQuestion(int index, String q, String ans) {
		question[index] = new Question(q, ans);
	}
	/*
	 * FileWrite (FileWriter outFile)
	 * Take a valid file writer and write the question's data to the
	 * file referred to by outFile
	 */
	public void fileWrite(FileWriter outFile) {
		for (int i = 0; i < qCount; i++) {
			question[i].fileWrite(outFile);
		}
	}
}
