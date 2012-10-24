package linker;


/**
 * Interface for Wi12FileChecker
 * @author Dragon Slayers
 */
public interface FileChecker {

	/**
	 * Description: Checks that all the files that are passed as command line arguments are valid 
	 * input and output files.  If they are not, an IllegalArguemtException is thrown.
	 * @requires true
	 * @alters N/A
	 * @ensures that the input files are readable and that the output files are writable, if they are 
	 * not, an IllegalArgumentException is thrown.
	 */
	public abstract void checkFiles();


	/**
	 * Description: Returns a new Wi12FileProcessor with the executable-output file, output-
	 * error-log and the array of object-input files.
	 * @requires all files must be valid
	 * @alters N/A
	 * @ensures (see returns)
	 * @return a new Wi12FileProcessor with the executable-output file, output-error-log and 
	 * the array of object-input files as parameters.
	 */
	public FileProcessor getProcessor();


}