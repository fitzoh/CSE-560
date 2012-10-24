package sourceRecord;

/**
 * This is thrown when a label that has not been defined is found 
 * while parsing through the assembly-language-source file.
 * @author Dragon Slayer
 */
public class LabelNotDefinedException extends Exception {

	public String message;
	
	/**
	 * Description: Creates a LabelNotDefinedException with the message 
	 * set to string
	 * @requires true
	 * @ensures string is unchanged, exception message is assigned
	 * @param string message of the exception
	 */
	public LabelNotDefinedException(String string) {
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

	private static final long serialVersionUID = -3096115239430752663L;

}
