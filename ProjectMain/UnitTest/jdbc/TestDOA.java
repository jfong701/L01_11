package jdbc;

import static org.junit.Assert.*;

import org.junit.Test;

// TESTING ONLY ONE FUNCTION THAT DOESN'T USE A DATABASE. BIG CHANGES TO DOA.JAVA AFTER CODE REVIEW.
public class TestDOA {

	@Test
	public void testIsStudentNoValidAllNumbers() {
		boolean allNumbers = DOA.isStudentNumberValid("1000000000");
		assertTrue("Output is supposed to be true given 10 digit string.", allNumbers);
	}
	
	@Test
	public void testIsStudentNoValidAllLetters() {
		boolean allLetters = DOA.isStudentNumberValid("aaaaaaaaaa");
		assertFalse("Output is supposed to be false given 10 letters.", allLetters);

	}
	
	@Test
	public void testIsStudentNoValidMixNumLetters() {
		boolean mixNumLetters = DOA.isStudentNumberValid("a5a5a5a5a5");
		assertFalse("Output is supposed to be false given a mix of digits and letters", mixNumLetters);
	}
	
	@Test
	public void testIsStudentNoValidlessThan10Numbers() {
		boolean lessThan10Num = DOA.isStudentNumberValid("1000");
		assertFalse("Output is supposed to be false given less than 10 digits.", lessThan10Num);
	}
}