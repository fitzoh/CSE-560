package sourceRecord;

/**
 * This is thrown when there is an invalid record. i.e. an 
 * incorrectly formatted line in the assembly-language-source file 
 * @author Dragon Slayer
 */
public class InvalidRecordException extends Exception {

	
	private static final long serialVersionUID = 1L;
	String message;
	
	/**
	 * Description: Creates an InvalidRecordException with the message 
	 * set to string
	 * @requires true
	 * @ensures string is unchanged, exception message is assigned
	 * @param string message of the exception
	 */
	public InvalidRecordException(String string) {
		this.message=string;
	}
	
	/**
	 * Description: Returns the exception message
	 * @requires true
	 * @alters N/A
	 * @ensures message is unchanged
	 * @return the exception message
	 */
	public String getMessage(){
		return this.message;
	}

}
