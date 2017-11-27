package user;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import user.Student;

public class TestProfessor {

	@Test
	public void testConstructor() {
		Professor prof = new Professor("1000000000", "abcdefgh", "abcd", "password");
		boolean actual = prof.getProfID().equals("1000000000") && prof.getProfFirstName().equals("abcdefgh")
				&& prof.getProfLastName().equals("abcd") && prof.getProfPassword().equals("password");
		assertTrue("Constructor didn't initialize values properly.", actual);		
	}

}
