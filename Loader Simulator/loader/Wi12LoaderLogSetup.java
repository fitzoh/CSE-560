package loader;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Initializes and sets up the logger for errors and information messages.
 * Reformats errors according to whether they are severe error or info level
 * messages.
 * 
 * @author Dragon Slayer
 */
public class Wi12LoaderLogSetup implements LogSetup {
	private Logger LOG;

	/**
	 * Description: creates immutable log
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures true
	 * @param log
	 *            - to keep track of all Loader level errors and information
	 *            messages
	 */
	public Wi12LoaderLogSetup(Logger log) {
		LOG = log;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see proc.LogSetup#initializeToErr(java.lang.String)
	 */
	@Override
	public Logger initializeToErr(String logName) {
		LOG = Logger.getLogger(logName);
		// disable all default outputs
		LOG.setUseParentHandlers(false);
		ConsoleHandler errHandler = new ConsoleHandler();
		// send errors to System.err
		errHandler.setFormatter(new ErrorFormatter());// reformat errors
		// and set errHandler to log only severe logs
		errHandler.setLevel(Level.SEVERE);
											
		LOG.addHandler(errHandler);
		LOG.setLevel(Level.FINE);
		return LOG;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see proc.LogSetup#setInfoLog(java.io.File)
	 */
	@Override
	public boolean setReportLog(File logDest) {
		// Set up info log to report file
		boolean success = true;
		try {
			FileHandler infoHandler = new FileHandler(logDest.toString());
			infoHandler.setLevel(Level.FINE);
			infoHandler.setFormatter(new InfoFormatter());
			LOG.addHandler(infoHandler);

		} catch (SecurityException e) {
			success = false;
			LOG.severe("unable to open " + logDest.toString());
		} catch (IOException e) {
			LOG.severe("008: IO error with " + logDest.toString());
			success = false;
		}
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see proc.LogSetup#closeLog()
	 */
	@Override
	public void closeLog() {
		// remove all handlers from the log, and close any
		// files currently being held open
		Handler[] allHandlers = LOG.getHandlers();
		for (int i = 0; i < allHandlers.length; i++) {
			allHandlers[i].close();
			LOG.removeHandler(allHandlers[i]);
		}
	}

	/**
	 * This class reformats the error message from rec in the form: "Error_Code"
	 * + message from the logger record + "\n"
	 * 
	 * @author Dragon Slayer
	 */
	private class ErrorFormatter extends Formatter {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
		 */
		@Override
		public String format(LogRecord rec) {

			return ("Error_Code " + rec.getMessage() + "\n");
		}
	}

	/**
	 * Reformats the logger message for information messages. If rec is at the
	 * INFO level, it is reformatted in the form: "REPORT:	" + message from the
	 * logger record + "\n" If it is and error, it is reformatted in the form:
	 * "Error_Code" + message from the logger record + "\n"
	 * 
	 * @author Dragon Slayer
	 */
	private class InfoFormatter extends Formatter {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
		 */
		@Override
		public String format(LogRecord rec) {
			// set different messages for reports and errors
			String message = rec.getMessage();
			if (rec.getLevel() == Level.INFO) {
				message = "REPORT:\t" + message;
			} else if (rec.getLevel() == Level.SEVERE) {
				message = "Error_Code " + message;
			}
			return (message + "\n");
		}
	}

}
