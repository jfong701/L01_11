package user;
/**
 * 
 */

/**
 * @author DAVID
 *
 */
public class Student {

	/**
	 * 
	 */
	String studentNo;
	String studentFirstName;
	String studentLastName;
	String studentUTORID;
	String password;
	
	public Student(String studentNo, String utor, String studentFirstName, String studentLastName, String password) {
		// TODO Auto-generated constructor stub
		this.studentNo = studentNo;
		this.studentUTORID = utor;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.password = password;
	}

	
	/**
	 * @return the studentNo
	 */
	public String getStudentNo() {
		return studentNo;
	}


	/**
	 * @param studentNo the studentNo to set
	 */
	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}
	
	public String getStudentUTORID() {
		return studentUTORID;
	}
	
	public void setStudentUTORID(String utor) {
		this.studentUTORID = utor;
	}

	/**
	 * @return the studentFirstName
	 */
	public String getStudentFirstName() {
		return studentFirstName;
	}


	/**
	 * @param studentFirstName the studentFirstName to set
	 */
	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}


	/**
	 * @return the studentLastName
	 */
	public String getStudentLastName() {
		return studentLastName;
	}


	/**
	 * @param studentLastName the studentLastName to set
	 */
	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return string format of this object
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String format;
		format = this.studentNo + "," + this.studentFirstName + "," + this.studentLastName;
		return format;
	}
	

}