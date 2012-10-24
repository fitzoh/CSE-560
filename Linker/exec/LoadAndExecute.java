package exec;

import linker.FileChecker;
import linker.FileProcessor;
import linker.Wi12FileChecker;

/**
 * Executable program that makes calls to check the input/output files. 
 * Makes a call to process the object-input files and print 
 * the output files. If the output-error-log is valid, errors are logged there, 
 * if not, they are sent to system.err
 * @author Dragon Slayer
 */
public class LoadAndExecute {

	public static void main(String[] args) throws Exception {
		try{
		//check that all files are valid
		FileChecker checker = new Wi12FileChecker(args);
		checker.checkFiles();
		//create processor and process files
		FileProcessor processor = checker.getProcessor();
		processor.processFiles();
		}
		catch(Exception e){
			//If error is rethrown to main print it to System.err
			System.err.print("Unable to print to error file: "+e.getMessage());
		}
	}
}
