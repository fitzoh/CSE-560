package objectFile;

/**
 * This is thrown when an object-input file is incorrectly formatted or invalid.
 * @author Dragon Slayer
 */
public class InvalidObjectFileException extends Exception {

	private static final long serialVersionUID = 1L;
	String message;

	/**
	 * Description: Creates an InvalidObjectFileException with the 
	 * message set to string
	 * @requires true
	 * @alters exception message is assigned
	 * @ensures string is unchanged
	 * @param string message of the exception
	 */
	public InvalidObjectFileException(String string) {
		this.message=string;
	}

	/**
	 * Description: Returns the exception message
	 * @requires true
	 * @alters N/A
	 * @ensures message is unchanged
	 * @return The exception message
	 */
	public String getMessage(){
		return message;
	}
}
