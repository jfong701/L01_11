package user;

import static org.junit.Assert.*;

import org.junit.Test;

import user.Student;

public class TestStudent {

	@Test
	public void testConstructor() {
		Student std = new Student("1000000000", "abcdefgh", "abcd", "efgh", "password");
		boolean actual = std.getStudentNo().equals("1000000000") && std.getStudentUTORID().equals("abcdefgh")
				&& std.getStudentFirstName().equals("abcd") && std.getStudentLastName().equals("efgh")
				&& std.getPassword().equals("password");
		assertTrue("Constructor didn't initialize values properly.", actual);		
	}
}
