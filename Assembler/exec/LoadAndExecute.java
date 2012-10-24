package exec;

import assembler.FileChecker;
import assembler.FileProcessor;
import assembler.Wi12FileChecker;

/**
 * Executable program that makes calls to check the input/output files. 
 * It only contains a main.  Makes a call to process the assembly-input 
 * file and print the output files. Any errors are sent to Standard.err.
 * @author Dragon Slayer
 */
public class LoadAndExecute {

	public static void main(String[] args) throws Exception {
		try{
		//Check the validity of the input files
		FileChecker checker = new Wi12FileChecker(args);
		checker.checkFiles();
		FileProcessor processor = checker.getProcessor();
		processor.processFiles();
		}
		catch(Exception e){
			System.err.print(e.getMessage());
		}
	}
}
