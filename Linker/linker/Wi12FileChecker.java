package linker;

import java.io.File;

/**
 * This class implements the interface FileChecker. This class is used to check the input 
 * and output files. It can be used to determine if the input files exist and if they are 
 * readable. It can determine if the desired output file already exists and if it is writable. 
 * It provides a way to process the files. 
 * @author Dragon Slayer
 */
public class Wi12FileChecker implements FileChecker {

	private final int minNumFiles = 3;
	private File outputObjectFile, errorFile;
	private File[] inputObjectFiles;


	/**
	 * Description:  If the correct number of command line arguments are passed in, it sets 
	 * the 1st argument as the executable-output, 2nd as the output-error-log. The remaining 
	 * one or more arguments are placed into the inputObjectFiles array, and are object-input 
	 * files. If less than 3 arguments are passed in or there are duplicate file names, an 
	 * IllegalArgumentException is thrown. 
	 * @requires true
	 * @alters N/A
	 * @ensures if no Exception is thrown, then the correct number of arguments were given at 
	 * the command line and the files are set as the executable-output, output-error-log and 
	 * an array of object-input files.
	 * @param fileNames command line arguments (array of strings)
	 * @throws IllegalArgumentException
	 */
	public Wi12FileChecker(String[] fileNames) throws IllegalArgumentException {

		if (fileNames.length < this.minNumFiles) {//minimum of 3 files
			throw new IllegalArgumentException("Error_Code 001: Usage: prog-name " +
					"output-executable output-error-log obj-1 . . . obj-n");
		}
		if (!this.noDupes(fileNames)) {
			throw new IllegalArgumentException("Error_Code 007: Duplicate File Names. " +
					"Usage: prog-name output-executable output-error-log obj-1 . . . obj-n");
		}
		this.outputObjectFile = new File(fileNames[0]);
		this.errorFile = new File(fileNames[1]);
		inputObjectFiles = new File[fileNames.length-2];
		for(int i=0;i<inputObjectFiles.length;i++){//create array of object files
			inputObjectFiles[i] = new File(fileNames[i+2]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see VMLoader#checkFiles()
	 */
	@Override
	public void checkFiles() throws IllegalArgumentException {
		
		//make sure output files are writeable
		this.writeableOutput(this.outputObjectFile);
		this.writeableOutput(this.errorFile);
		//and input files are readable
		for(File f: inputObjectFiles){
			this.readableInput(f);
		}
	}

	/**
	 * Description: Checks that the file exists, is readable, and is not a directory. 
	 * If it does exist and is readable, return true. If not, the method returns false.
	 * @requires true
	 * @alters true
	 * @ensures (see returns)
	 * @throws IllegalArgumentException
	 * @param in the file that is being checked to determine if it is valid input file.
	 * @return true iff the input file exists and is readable and is not a directory.
	 */
	private boolean readableInput(File in) throws IllegalArgumentException {// check that input files exist and
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
			throw new IllegalArgumentException("Error_Code 002: "+ in.getName() + " is not readable.");

		}
		if (isDirectory) {
			throw new IllegalArgumentException("Error_Code 003: "+ in.getName() + " is a directory.");
			// if we can't use it throw an exception

		}
		return canRead && !isDirectory;
	}

	/**
	 * Description: Checks to make sure that the file does not already exist and that 
	 * its parent is writable. If the file does already exist, it is deleted and a new 
	 * file is created. This is when the method will return true. If the file does exist 
	 * and was not able to be deleted, or its parent is not writable, then it returns false.
	 * @requires true
	 * @alters true
	 * @ensures out - not changed. 
	 * @throws IllegalArgumentException
	 * @param out the file that is being check to determine if it is a valid output file.
	 * @return true iff the file does not already exist and its parent is writable or, if 
	 * the file did exist and was able to be deleted. 
	 */
	private boolean writeableOutput(File out) throws IllegalArgumentException {// check that output files are
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
			throw new IllegalArgumentException("Error_Code 004: "+ out.getName() + " is a directory.");

		} else if (thisExists) {
			throw new IllegalArgumentException("Error_Code 005: "+ out.getName() + " already exists and cannot be deleted.");
			// record error if output file already exists

		} else if (!parentWriteable) {
			throw new IllegalArgumentException("Error_Code 006: "+ out.getName() + " is in a directory which is not writeable.");
			// record error if parent directory isn't writeable

		}
		return (!thisExists) && (!thisIsDirectory) && parentWriteable;
	}

	/**
	 * Description: Checks if all the input and output file names are unique. Returns 
	 * true iff they are unique.
	 * @requires 3 or more command line arguments
	 * @alter N/A
	 * @ensures arguments are unchanged
	 * @param f the input and output file names from the command line.
	 * @return true iff the names of the files are unique.
	 */
	private boolean noDupes(String[] f) {

		boolean result = true;
		
		for(int i=0;i<f.length;i++){//2 nested loops to check every file combination
			for(int j=i+1;j<f.length;j++){
				result&=(f[i]!=f[j]);
			}
		}
		return result;
	}

	@Override
	public FileProcessor getProcessor() {
		return new Wi12FileProcessor(outputObjectFile, errorFile, inputObjectFiles);
	}

}
