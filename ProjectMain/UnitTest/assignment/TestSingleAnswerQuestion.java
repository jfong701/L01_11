package assignment;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSingleAnswerQuestion {

	SingleAnswerQuestion q;
	
	@Before
	public void setUp() throws Exception {
		q = new SingleAnswerQuestion("CSCC01F17", 1, "What is 1+1?", "2");
	}

	@After
	public void tearDown() throws Exception {
		q = null;
	}

	@Test
	public void testIncorrectAnswer() {
		boolean actual = q.isCorrect("4");
		assertFalse("Answer was supposed to be incorrect.", actual);
	}

	@Test
	public void testCorrectAnswer() {
		boolean actual = q.isCorrect("2");
		assertTrue("Answer was supposed to be correct.", actual);
	}
}
