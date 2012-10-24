package linker;

import java.io.IOException;


/**
 * Interface implemented by Wi12FileProcessor. See Wi12FileProcessor or
 * programmer's guide for more details.
 * @author Dragon Slayer
 */
public interface FileProcessor {

	/**
	 * Description: Passes the array of input files to ExecutableFile. Makes calls to receive 
	 * the load address from the user, process the object-input files and create the executable-
	 * output file. If the output-error-log file is valid, errors are sent there, otherwise to 
	 * system.err.
	 * @requires true
	 * @alters ExecutableFile
	 * @ensures object-input files are processed and executable-output file created. If there are 
	 * errors, they are sent to output-error-log, if that file is valid, else the errors go to 
	 * system.err.
	 * @throws IOException
	 * @throws Exception
	 */
	public abstract void processFiles()
			throws IOException, Exception;
}