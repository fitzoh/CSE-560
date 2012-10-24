package loader;

import java.io.IOException;
import java.util.zip.DataFormatException;

import vm.VirtualMachine;

/**
 * @author Dragon Slayers
 */
public interface VMLoader {

	/**
	 * Description: Checks that all the files that are passed as command line
	 * arguments are valid input and output files. If the process-trace file is
	 * valid, all errors will be sent to this file, via the logger. If the
	 * process-trace file is not valid, errors are sent to the console.
	 * 
	 * @requires true
	 * @alters logger is set up to the trace-process file.
	 * @ensures that the input files are readable and that the output files are
	 *          writable, if they are not, an IllegalArgumentException is
	 *          thrown.
	 * @return true iff executable-input file, process-input file,
	 *         process-output file, and process-trace file are valid. Otherwise
	 *         return false.
	 */
	public abstract void checkFiles();

	/**
	 * Description: Attempts to create a VirtualMachine from the data processed
	 * from the executable file. If it is not successful, then an error is
	 * logged as severe. Calls processExecutableFile to get a HexWordSegment and
	 * then uses the HexWordSegment to make a call to create a VirtualMachine.
	 * 
	 * @requires valid executable file
	 * @alters creates a new VirtualMachine, logs an error if not able to create
	 *         a VirtualMachine with a HexWordSegment.
	 * @ensures See "Returns". If a virtual machine was not able to be
	 *          initialized, an error is logged as severe.
	 * @return If the executable file was able to be processed and a
	 *         VirtualMachine initialized, it returns that VirtualMachine. If
	 *         not, a null VirtualMachine is returned.
	 * @throws DataFormatException
	 * @throws IOException
	 */
	public abstract VirtualMachine parseMemMakeVM() throws DataFormatException,
			IOException;

	/**
	 * Description: Closes the log and removes the log handlers.
	 * 
	 * @requires true
	 * @alters handlers are removed and log is closed.
	 * @ensures true.
	 */
	public void cleanUp();
}