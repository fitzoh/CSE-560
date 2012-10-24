package loader;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

import vm.HexWordSegment;
import vm.IOGroup;
import vm.VirtualMachine;

/**
 * This class implements the interface VMLoader. This class is used to check the
 * input and output files. It can be used to determine if the input files exist
 * and if they are readable. It can determine if the desired output file already
 * exists and if it is writable. It provides a way to process the
 * executable-input and create a VirtualMachine. It creates a reader for the
 * process-input file, and a writer for the process-trace file. It logs all the
 * loader error messages to a logger which is set to go to the process-trace
 * file.
 * 
 * @author Dragon Slayer
 */
public class Wi12VMLoader implements VMLoader {

	private final int numFiles = 4;
	private FileProcessor processor;
	private File execInput, processInput, processOutput, processTrace;
	private Logger LOG;
	private LogSetup logSet;

	/**
	 * Description: Creates and sets up the logger. If the correct number of
	 * command line arguments are passed in, it sets the 1st argument as the
	 * executable-input 2nd as the process-input, 3rd as the process-output, and
	 * the 4th as the process-trace files. If there are an incorrect number of
	 * arguments passed, an error is logged and an IllegalArgumentsException is
	 * thrown. A Wi12FileProcessor is also created.
	 * 
	 * @requires true
	 * @ensures if no Exception is thrown, that the correct number of arguments
	 *          were given at the command line
	 * @param fileNames
	 *            - command line arguments
	 * @throws IllegalArgumentException
	 */
	public Wi12VMLoader(String[] fileNames) throws IllegalArgumentException {
		this.logSet = new Wi12LoaderLogSetup(this.LOG);
		this.LOG = this.logSet.initializeToErr("LoadAndExecute");
		// set up an error log that goes to System.err

		if (fileNames.length != this.numFiles) {
			this.LOG.severe("001: Invalid number of arguments; Expected "
					+ this.numFiles + " received " + fileNames.length + " ");
			this.LOG.severe("Usage: LoadAndExecute executable-input process-input "
					+ "process-output process-trace");
			throw new IllegalArgumentException();
			// throw an exception if the wrong number of args are entered
		}
		if (!this.noDupes(fileNames)) {
			this.LOG.severe("021: Duplicate file names detected.");
			this.LOG.severe("Usage: LoadAndExecute executable-input process-input "
					+ "process-output process-trace");
			throw new IllegalArgumentException();
		}
		this.execInput = new File(fileNames[0]);
		this.processInput = new File(fileNames[1]);
		this.processOutput = new File(fileNames[2]);
		this.processTrace = new File(fileNames[3]);
		this.processor = new Wi12FileProcessor(this.LOG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see VMLoader#checkFiles()
	 */
	@Override
	public void checkFiles() throws IllegalArgumentException {
		boolean result;
		result = this.writeableOutput(this.processTrace);
		// make sure report is valid
		if (result) {
			result = this.logSet.setReportLog(this.processTrace);
			// if report is valid, link log to report file
		}
		result &= this.writeableOutput(this.processOutput);
		// check other files for validity

		result &= this.readableInput(this.execInput);
		result &= this.readableInput(this.processInput);
		if (!result) {
			throw new IllegalArgumentException();
			// if any of them are invalid throw exception
		}
	}

	/**
	 * Description: Checks that the file exists and is readable, and is not a
	 * directory. If it does exist and is readable, return true. If not, error
	 * messages get logged (for process trace), and the method returns false.
	 * 
	 * @requires true
	 * @alters true
	 * @ensures in - not changed. If no error is logged, then the input file is
	 *          readable
	 * @return true iff the input file exists and is readable and is not a
	 *         directory.
	 */
	private boolean readableInput(File in) {// check that input files exist and
											// are readable
		boolean canRead = false;
		boolean isDirectory = true;
		try {
			canRead = in.canRead();// checks for existence and readability
		} catch (SecurityException e) {// only throws exception if not readable,
										// which is fine
		}
		isDirectory = in.isDirectory();

		if (!canRead) {
			this.LOG.severe("002: " + in.getName() + " is not readable.");
			// if we can't use it log an error

		}
		if (isDirectory) {
			this.LOG.severe("003: " + in.getName() + " is a directory.");
			// if we can't use it log an error

		}
		return canRead && !isDirectory;
	}

	/**
	 * Description: Checks to make sure that the file does not already exist and
	 * that its parent is writable. If the file does already exist, it is
	 * deleted and a new file is created. Then the method will return true. If
	 * the file does exist and was not able to be deleted, or its parent is not
	 * writable, then it returns false and errors are logged.
	 * 
	 * @requires true
	 * @alters true
	 * @ensures out is not changed. If no error is logged, the file is writable
	 * @return true iff the file does not already exist and its parent is
	 *         writable or, if the file did exist and was able to be deleted.
	 * @param out
	 *            - the file that is bing checked to determine if it is a valid
	 *            output file.
	 */
	private boolean writeableOutput(File out) {// check that output files are
												// writeable and are in
												// writeable
												// directories
		File parent = out.getParentFile();// temp variable to get info on parent
											// directory
		if (parent == null) {
			parent = new File(".");
		}
		boolean thisExists = true;
		boolean thisIsDirectory = true;
		boolean parentWriteable = false;
		try {
			thisIsDirectory = out.isDirectory();
			thisExists = out.exists();
			if (thisExists && !thisIsDirectory) {
				thisExists = !out.delete();
			}
			parentWriteable = new File(".").canWrite();
		} catch (SecurityException e) {// catch security exception if there's a
										// read-only file
		}
		if (thisIsDirectory) {
			this.LOG.severe("004: " + out.getName() + " is a directory.");
			// record error if output is a directory

		} else if (thisExists) {
			this.LOG.severe("005: " + out.getName()
					+ " already exists and cannot be deleted.");
			// record error if output file already exists

		} else if (!parentWriteable) {
			this.LOG.severe("006: " + out.getName()
					+ " is in a directory which is not writeable");
			// record error if parent directory isn't writeable

		}
		return (!thisExists) && (!thisIsDirectory) && parentWriteable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see VMLoadert#processFiles()
	 */
	@Override
	public VirtualMachine parseMemMakeVM() throws DataFormatException,
			IOException {
		VirtualMachine vm = null;

		try {
			HexWordSegment seg = this.processor
					.processExecutableFile(this.execInput);
			vm = new VirtualMachine(seg, this.getVMIO(), this.LOG);
		} catch (IOException e) {
			this.LOG.severe("007: IO Exception");// Shouldn't occur, we already
			// checked files for validity
			throw e;
		}
		return vm;

	}

	/**
	 * Description: Creates a BufferedReader specifically for reading the
	 * process-input file, and a BufferedWriter for writing to the
	 * process-output file. These together form an IOGroup. If process-output
	 * cannot be written to, an IOException is thrown and a message is sent to
	 * System.out.
	 * 
	 * @requires processInput and processOutput files must be valid.
	 * @alters true
	 * @ensures IOGroup is created, or if process-output cannot be written to,
	 *          an IOException is thrown and a message is sent to System.out.
	 * @return a new IOGroup if an IOException is not thrown.
	 * @throws IOException
	 */
	private IOGroup getVMIO() throws IOException {
		Reader in = new BufferedReader(new FileReader(this.processInput));
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(this.processOutput));
		return new IOGroup(in, out);
	}

	/**
	 * Description: Closes the log and removes the log handlers
	 * 
	 * @requires true
	 * @alters handlers are removed and log is closed
	 * @ensures true
	 */
	@Override
	public void cleanUp() {
		this.logSet.closeLog();
	}

	/**
	 * Description: Checks if all the input and output file names are unique.
	 * Returns true iff they are unique.
	 * 
	 * @requires exactly 4 command line arguments
	 * @alters N/A
	 * @ensures arguments are unchanged
	 * @return true iff the names of the files are unique.
	 * @param - the input and output file names from the command line.
	 */
	private boolean noDupes(String[] f) {

		boolean result = f[0].equals(f[1]) || f[0].equals(f[2])
				|| f[0].equals(f[3]);
		result |= f[1].equals(f[2]) || f[1].equals(f[3]);
		result |= f[2].equals(f[3]);
		return !result;
	}

}
