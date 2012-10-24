package sourceRecord;

/**
 * This is thrown when there is a label that has already 
 * been defined and placed in the Symbol Table. 
 * @author Dragon Slayer
 */
public class LabelAlreadyDefinedException extends Exception {
	String message;

	/**
	 * Description: Creates a LabelAlreadyDefinedException with the message 
	 * set to string
	 * @requires true
	 * @ensures string is unchanged, exception message is assigned
	 * @param string message of the exception
	 */
	public LabelAlreadyDefinedException(String string) {
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
		return message;
	}

	private static final long serialVersionUID = 4060730332455055231L;

}
