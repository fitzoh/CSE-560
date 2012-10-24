package loader;

import java.io.File;
import java.util.logging.Logger;

/**
 * Interface implemented by W12iLoaderLogSetup. See W12iLoaderLogSetup or
 * programmer's guide for more details.
 * 
 * @author Dragon Slayer
 */
public interface LogSetup {

	/**
	 * Description: Initializes an error handler with level severe and sets it
	 * logging level to fine.
	 * 
	 * @requires true
	 * @alters Logger is initialized, its level set, and handler added
	 * @ensures true
	 * @param logName
	 *            - name assigned to the logger
	 * @return Logger with error formatting.
	 */
	public abstract Logger initializeToErr(String logName);

	/**
	 * Description: Sets up an information logger to the process-trace file. If
	 * the file is not able to be opened or there is an IOException, the error
	 * is logged as severe, and the method returns false.
	 * 
	 * @requires true
	 * @alters logDest
	 * @ensures true
	 * @param logDest
	 *            - The file where the information/error that is logged will be
	 *            sent
	 * @return true iff the file being logged to was able to be opened.
	 */
	public abstract boolean setReportLog(File logDest);

	/**
	 * Description: Removes all the handlers from the logger.
	 * 
	 * @requires true
	 * @alters handlers are removed from the logger.
	 * @ensures: true
	 */
	public abstract void closeLog();

}