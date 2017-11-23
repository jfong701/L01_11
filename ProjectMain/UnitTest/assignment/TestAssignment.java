package assignment;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestAssignment {



	@Test
	public void testQuestSetCorrectSize() {
		// testing for correct number of integers in array
		int[] a = new int[3]; 
		a = Assignment.questSet(3, 5);
		assertTrue("Did not create an array of 3 integers.", a.length == 3);
	}

	@Test
	public void testQuestSetDistinctAndLessThanMaxValues() {
		int[] a = new int[2];
		a = Assignment.questSet(2, 3);
		// testing for distinct values and less than or equal to the 'max' value which is 5 in this case
		boolean valid = true;
		valid = a[0] != a[1] && a[0] <= 3 && a[1] <= 3;
		assertTrue("Elements of array are not distinct or at least one is greater than max value.", valid);
	}
	
}
