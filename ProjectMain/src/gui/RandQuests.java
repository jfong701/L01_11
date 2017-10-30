import java.util.*;

public class RandQuests {
	public static int[] questSet(int n, int max) {
		int[] set = new int[n];
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
	/*public static void main (String args[]) {
		int[] set = questSet(5, 100);
		for (int i = 0; i<5; i++) {
			System.out.println(set[i]);
		}
	}*/
}