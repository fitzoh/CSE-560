package assembler;


/**
 * interface for Wi12FileChecker
 * @author Dragon Slayers
 */
public interface FileChecker {

	/**
	 * Description: Checks that all the files that are passed as command line arguments are valid 
	 * input and output files.  If they are not, an IllegalArguemtException is thrown.
	 * @requires true
	 * @alters N/A
	 * @ensures that the input files are readable and that the output file is writable, if they are 
	 * not, an IllegalArgumentException is thrown.
	 */
	public abstract void checkFiles();

	/**
	 * Description: returns a new Wi12FileProcessor with the assembly-input, object-output, and 
	 * listing-output as parameters.
	 * @requires assembly-input, object-output, and listing-output must be valid.
	 * @alters N/A
	 * @return a new Wi12FileProcessor with the assembly-input, object-output, and 
	 * listing-output as parameters.
	 */
	public FileProcessor getProcessor();


}