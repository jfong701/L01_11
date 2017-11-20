package assignment;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestAssignment {

	Assignment a1;

	@Before
	public void setUp() throws Exception {
		a1 = new Assignment(3);
	}

	@After
	public void tearDown() throws Exception {
		a1 = null;
	}

	@Test
	public void testAddQuestion() {
		a1.addQuestion(0, "1+1?", "2");
		a1.addQuestion(1, "2+2?", "4");
		a1.addQuestion(2, "3*3?", "9");
		boolean valid = true;
		for (int i=0; i<3; i++) {
			valid = (a1.getQuestion(i) != null) && valid;
		}
		assertTrue("Question(s) were not added properly.", valid);
	}
	
	@Test
	public void testGetQuestion() {
		// test if this getter returns the correct information
		a1.addQuestion(0, "1+1?", "2");
		Question q1 = a1.getQuestion(0);
		assertTrue("Did not receive correct question and/or answer.", 
					q1.question.equals("1+1?") && q1.answer.equals("2"));
	}
	
	@Test
	public void testQuestSetCorrectSize() {
		// testing for correct number of integers in array
		int[] a = new int[3]; 
		a = Assignment.questSet(3, 5);
		assertTrue("Did not create an array of 3 integers.", a.length == 3);
	}

	@Test
	public void testQuestSetDistinctAndLessThanMaxValues() {
		int[] a = new int[3];
		a = Assignment.questSet(2, 3);
		// testing for distinct values and less than or equal to the 'max' value which is 5 in this case
		boolean valid = true;
		valid = a[0] != a[1] && a[0] <= 3 && a[1] <= 3;
		assertTrue("Elements of array are not distinct or at least one is greater than max value.", valid);
	}
	
}
