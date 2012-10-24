package linker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

import executableFile.ExecutableFile;

/**
 * Implements the interface, FileProcessor. This class processes the files. If the 
 * output-error-log can be written to, errors are sent here, else they go to system.err
 * @author Dragon Slayer
 */
public class Wi12FileProcessor implements FileProcessor {

	File execOut, errorOut;
	File[] inputFiles;

	/**
	 * Description: Sets the files as the executable-output as outputFile, 
	 * output-error-log as errorOut, and the input files as an array of 
	 * object-input files.
	 * @requires true
	 * @ensures (see description)
	 * @param outputFile file used as executable-output file
	 * @param errorFile file used as output-error-log file
	 * @param inputObjectFiles array of object-input files
	 */
	public Wi12FileProcessor(File outputFile, File errorFile, File[] inputObjectFiles) {
		this.inputFiles=inputObjectFiles;
		this.execOut=outputFile;
		this.errorOut=errorFile;
	}

	@Override
	public void processFiles() throws Exception{
		try{
		ExecutableFile main = new ExecutableFile(inputFiles);//parsing
		main.getLoadAddress();//get user input
		main.processAndGenOutput(execOut);//process and print
		} catch(Exception e){
			BufferedWriter error = null;
			try {//catch all exceptions and log them here. If something prevents this, rethrow to main and print to Sys.err
				error= new BufferedWriter(new FileWriter(errorOut));
				error.write(e.getMessage());
				error.close();
			} catch (IOException e1) {
				throw e;
			}
	}
	}
}
