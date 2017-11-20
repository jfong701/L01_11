package assignment;
import java.io.*;
import java.util.Random;

public class Assignment {
	Question question[];
	int qCount;
	
	public Assignment(int qCount) {
		this.question = new Question [qCount];
		this.qCount = qCount;
	}
	/*
	 * initialize a question at index
	*/
	public void addQuestion(int index, String q, String ans) {
		question[index] = new Question(q, ans);
	}
	
	public Question getQuestion(int index) {
		return question[index];
	}

	public static int[] questSet(int n, int max) {
		int[] set = new int[3];
		for (int i=0; i<n; i++) {
			Random rand = new Random();
			boolean inSet = false;
			do {
				int num = rand.nextInt(max) + 1;
				//System.out.println(num);
				set[i] = num;
				for (int j = 0; j<i; j++) {
					if (set[i] == set[j]) {
						inSet = true;
					}
				}
			} while (inSet);	
		}
	return set;
	}
}