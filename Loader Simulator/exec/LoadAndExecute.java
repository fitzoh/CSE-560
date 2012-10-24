package exec;

import java.io.IOException;
import java.util.zip.DataFormatException;

import vm.VirtualMachine;
import loader.VMLoader;
import loader.Wi12VMLoader;

/**
 * Executable program that makes calls to create the loader and check the
 * input/output files. It makes a call to parse the memory and make a virtual
 * machine with the data from the loader. It only contains a main. If there is
 * an IllegalArgumentException, IOException, or a DataFormat Exception they are
 * caught and a message is logged. Finally, the logs are closed.
 * 
 * @author Dragon Slayer
 */
public class LoadAndExecute {

	public static void main(String[] args) {

		VMLoader loader = null;
		VirtualMachine vm = null;
		try {
			loader = new Wi12VMLoader(args);
			// throws IllegalArgumentException for wrong number of files
			loader.checkFiles();// throws IllegalArgumentException if input or
								// output files aren't valid
			vm = loader.parseMemMakeVM();// throws DataFormatException if exec
											// file is invalid
			// throws IOException if there's a problem opening files (shouldn't
			// be possible)
			vm.run();
		} catch (IllegalArgumentException e) {
			// Error has already been logged
		} catch (DataFormatException e) {
			// Info sent to log set log text
		} catch (IOException e) {
			// Info sent to log set log text
		} finally {
			if (loader != null) {
				loader.cleanUp();// close the logs
			}
		}
	}
}
