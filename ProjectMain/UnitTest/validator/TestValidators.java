package validator;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestValidators {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsAllDigitsFalse() {
		boolean actual = Validators.isAllDigits("1b20c030v0c");
		assertFalse("isAllDigits did not return false on alphanumeric input.", actual);
	}
	
	@Test
	public void testIsAllDigitsTrue() {
		boolean actual = Validators.isAllDigits("0000000000");
		assertTrue("isAllDigits did not return true on an all digit input.", actual);
	}
	
	@Test 
	public void testIsStudentNumberValidFalse() {
		boolean actual = Validators.isStudentNumberValid("123456");
		assertFalse("isStudentNumberValid returned true on a 6 digit number.", actual);
	}
	
	@Test
	public void testIsStudentNumberValidTrue() {
		boolean actual = Validators.isStudentNumberValid("1234567890");
		assertTrue("isStudentNumberValid returned false on a 10 digit number.", actual);
	}
	
	@Test
	public void testIsSingleAnswerQuestionValidFalse() {
		boolean actual = Validators.isSingleAnswerQuestionValid("", "");
		assertFalse("isSingleAnswerQuestionValid returned true on an empty question and answer.", actual);
	}
	
	@Test
	public void testIsSingleAnswerQuestionValidTrue() {
		boolean actual = Validators.isSingleAnswerQuestionValid("4+4", "8");
		assertTrue("isSingleAnswerQuestionValid returned false on a valid question.", actual);
	}
	
	@Test 
	public void testIsValidRangeFalse() {
		boolean actual = Validators.isValidRange("3", "2");
		assertFalse("isValidRange returned true on an invalid range.", actual);
	}
	
	@Test
	public void testIsValidRangeTrue() {
		boolean actual = Validators.isValidRange("1", "4"); 
		assertTrue("isValidRange returned false on a valid range.", actual);
	}
	
	@Test
	public void testIsValidDateFalse() {
		boolean actual = Validators.isValidDate("1999-20-41");
		assertFalse("isValidDate returned true on an invalid date.", actual);
	}
	
	@Test
	public void testIsValidDateTrue() {
		boolean actual = Validators.isValidDate("2017-11-20");
		assertTrue("isValidDate returned false on a valid date.", actual);
	}
}
