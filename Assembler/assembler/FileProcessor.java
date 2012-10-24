package assembler;

import java.io.IOException;
import sourceRecord.InvalidRecordException;
import sourceRecord.LabelAlreadyDefinedException;
import sourceRecord.LabelNotDefinedException;

/**
 * Interface implemented by Wi12FileProcessor. See Wi12FileProcessor or
 * programmer's guide for more details.
 * @author Dragon Slayer
 */
public interface FileProcessor {

	/**
	 * Description: Processes the assembly-input file line by line creating a 
	 * SourceRecord for each line. SourceRecord labels are replaced with their 
	 * values, if possible, and the SourceRecords are added to a list. Once the 
	 * assembly-input file is completely processed, the literal SourceRecords are 
	 * added to the SourceRecord list. All labels are replaced and execution start 
	 * is set.
	 * @requires true
	 * @alters LiteralPool, SymbolTable, SourceRecord list, object-output, 
	 * listing-output
	 * @ensures assembly-input file is processed if no exceptions are thrown
	 * @throws IOException
	 * @throws InvalidRecordException
	 * @throws LabelAlreadyDefinedException
	 * @throws LabelNotDefinedException
	 */
	public abstract void processFiles()
			throws IOException, InvalidRecordException,
			LabelAlreadyDefinedException, LabelNotDefinedException;
}