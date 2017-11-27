/**
 * 
 */
package user;

/**
 * @author Mathu
 *
 */
public class Professor {
	
	private String profID;
	private String profFirstName;
	private String profLastName;
	private String profPassword;

	public Professor(String profID, String profFirstName, String profLastName, String profPassword) {
		super();
		this.profID = profID;
		this.profFirstName = profFirstName;
		this.profLastName = profLastName;
		this.profPassword = profPassword;
	}

	/**
	 * @return the profID
	 */
	public String getProfID() {
		return profID;
	}

	/**
	 * @param profID the profID to set
	 */
	public void setProfID(String profID) {
		this.profID = profID;
	}

	/**
	 * @return the profFirstName
	 */
	public String getProfFirstName() {
		return profFirstName;
	}

	/**
	 * @param profFirstName the profFirstName to set
	 */
	public void setProfFirstName(String profFirstName) {
		this.profFirstName = profFirstName;
	}

	/**
	 * @return the profLastName
	 */
	public String getProfLastName() {
		return profLastName;
	}

	/**
	 * @param profLastName the profLastName to set
	 */
	public void setProfLastName(String profLastName) {
		this.profLastName = profLastName;
	}

	/**
	 * @return the profPassword
	 */
	public String getProfPassword() {
		return profPassword;
	}

	/**
	 * @param profPassword the profPassword to set
	 */
	public void setProfPassword(String profPassword) {
		this.profPassword = profPassword;
	}
	
	
	
}
