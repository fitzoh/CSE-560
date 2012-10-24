package executableFile;

public class InvalidExecutableException extends Exception {
	
	private static final long serialVersionUID = 1L;
	String message;
	
	/**
	 * Description: Creates an InvalidExecutableException with the 
	 * message set to string
	 * @requires true
	 * @alters exception message is assigned
	 * @ensures string is unchanged
	 * @param message message of the exception
	 */
	public InvalidExecutableException(String message) {
		this.message = message;
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
