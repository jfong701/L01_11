package student;
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
	
	public Student(String studentNo, String studentFirstName, String studentLastName) {
		// TODO Auto-generated constructor stub
		this.studentNo = studentNo;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
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