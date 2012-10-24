package loader;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

import vm.HexWordSegment;

/**
 * Interface implemented by Wi12FileProcessor. See Wi12FileProcessor or
 * programmer's guide for more details.
 * 
 * @author Dragon Slayer
 */
public interface FileProcessor {

	/**
	 * Description: Processes the executable file by making a call to
	 * processHeader and making calls to processRecord
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures exec remains unchanged
	 * @return HexWord segment from the executable file
	 * @param exec
	 *            - executable file to be processed
	 * @throws IOException
	 * @throws DataFormatException
	 */
	public abstract HexWordSegment processExecutableFile(File exec)
			throws IOException, DataFormatException;
}