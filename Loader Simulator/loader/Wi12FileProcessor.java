package loader;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

import utilities.CharChecker;
import vm.HexWordSegment;

/**
 * This class implements the interface, FileProcessor. It processes the files.
 * It initializes a logger to keep track of errors. It processes the executable
 * file by reading in one line at a time. As it processes the executable file,
 * it checks that the executable file is formatted correctly. If the header
 * record and test records in the file meet the specifications given in their
 * definitions, then the records are parsed and the different parts of the
 * records are stored. If the file does not have the correct format, an error is
 * logged, and a DataFormatException is thrown.
 * 
 * @author Dragon Slayer
 */
public class Wi12FileProcessor implements FileProcessor {

	Logger LOG;

	/**
	 * Description: Assigns the logger, making it immutable.
	 * 
	 * @requires true
	 * @alters true
	 * @ensures the logger is assigned.
	 * @param log
	 *            - is assigned to LOG
	 */
	public Wi12FileProcessor(Logger log) {
		LOG = log;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see loader.FileProcessor#processExecutableFile(java.io.File)
	 */
	@Override
	public HexWordSegment processExecutableFile(File exec) throws IOException,
			DataFormatException {
		BufferedReader in = new BufferedReader(new FileReader(exec));
		HexWordSegment seg = null;
			seg = processHeader(in);
		try {
			while (processRecord(in, seg))
				;
		} catch (NullPointerException e) {
			// end of file expected
		}
		closeQuietly(in);
		return seg;
	}

	/**
	 * Description: Reads in one line from in (the executable file) this line
	 * should be the header record. This method then evaluates each part of the
	 * header record to determine if the header record is in the correct form.
	 * (refer to definition of a header record) If it is not in the correct
	 * form, then a DataFormatException is thrown and a message is logged to the
	 * Logger as severe. If the header record is correct, then it is separated
	 * into the five parts given in the definition. The address of where to
	 * start execution, the load address, and the length of the segment are
	 * converted to ints. The segment name is saved. These are then used to
	 * create a HexWordSegment (See class HexWordSegment).
	 * 
	 * @requires in must be open
	 * @alters changes the reader�s position in the file
	 * @ensures in remains unchanged
	 * @param in
	 *            - stream of hex characters to be checked and parsed
	 * @return a HexWordSegment which contains the data from the first line of
	 *         in, which is the header record, if the header record was valid.
	 *         If it is not valid, an exception is thrown
	 * @throws IOException
	 * @throws DataFormatException
	 */
	private HexWordSegment processHeader(BufferedReader in) throws IOException,
			DataFormatException {
		int headerLength = 13;
		String execStart = "";
		String name = "";
		String startAdd = "";
		String length = "";

		String fullLine = in.readLine();
		boolean valid = fullLine != null;
		if (!valid) {
			LOG.severe("012: Empty executable file detected");
			throw new DataFormatException();
		}
		valid &= fullLine.length() == headerLength;
		if (!valid) {
			LOG.severe("009: Invalid header length. Header must be "
					+ headerLength + " characters");
			throw new DataFormatException();
		}
		int lineIndex = 0;
		char c = fullLine.charAt(lineIndex);
		// first char must be 'H'
		valid = (c == 'H');
		if (!valid) {
			LOG.severe("010: Invalid Header. First character of Header must be 'H'");
			throw new DataFormatException();
		}
		lineIndex++;
		// 2 hex char address execution starts at
		for (int i = 0; i < 2; i++) {
			c = fullLine.charAt(lineIndex);
			valid &= CharChecker.isHexChar(c);
			execStart += c;
			lineIndex++;
		}
		if (!valid) {
			LOG.severe("011: Invalid Header. Execution starting address must be "
					+ "2 valid hex characters.");
			throw new DataFormatException();
		}
		// 6 char segment name
		for (int i = 0; i < 6; i++) {
			c = fullLine.charAt(lineIndex);
			valid &= CharChecker.isValidChar(c);
			name += c;
			lineIndex++;
		}
		if (!valid) {
			LOG.severe("013: Invalid Header. Segment name must consist of "
					+ "6 valid characters");
			throw new DataFormatException();
		}
		// 2 hex char load address
		for (int i = 0; i < 2; i++) {
			c = fullLine.charAt(lineIndex);
			valid &= CharChecker.isHexChar(c);
			startAdd += c;
			lineIndex++;
		}
		if (!valid) {
			LOG.severe("014: Invalid header. Segment load address must be 2 "
					+ "valid hex characters");
			throw new DataFormatException();
		}
		// 2 hex char segment length
		for (int i = 0; i < 2; i++) {
			c = fullLine.charAt(lineIndex);
			valid &= CharChecker.isHexChar(c);
			length += c;
			lineIndex++;
		}
		if (!valid) {
			LOG.severe("015: Invalid Header. Segment length must be 2 valid hex characters");
			throw new DataFormatException();
		}
		// convert strings to decimal and create mem segment
		int ExecStart = Integer.parseInt(execStart, 16);
		int StartAdd = Integer.parseInt(startAdd, 16);
		int Length = Integer.parseInt(length, 16);
		HexWordSegment segment = null;
		try{
		segment = HexWordSegment.getHexWordSegment(ExecStart,
				name, StartAdd, Length);}
		catch(DataFormatException e){
			LOG.severe("022: Execution start address and maximum load address must be less than memory size");
		}
		return segment;
	}

	/**
	 * Description: Reads in one line from in (the executable file). The line
	 * read in should be a test record. This method then evaluates each part of
	 * the test record to determine if it is in the correct form (see definition
	 * of test record). If the line read in does not fit the specifications of a
	 * test record, then this method returns false, a DataFormatException is
	 * thrown, and the error is logged to the logger as severe. If the test
	 * record is valid, the address at which the information is stored is
	 * converted from hex, to dec and stored as an int. The initial value at
	 * that address is stored as a string of hex characters. Then, segment is
	 * updated according to the specifications of addWord(String word, int
	 * Address) in the class HexWordSegment.
	 * 
	 * @requires in must be open
	 * @alters segment is altered according to the specifications of addWord
	 *         (String word, int Address) in the class HexWordSegment.
	 * @ensures in remains unchanged
	 * @param in
	 *            - stream of hex characters to be checked and parsed
	 * @param segment
	 *            - if the test record is valid, the record is added to segment.
	 * @return true if the test record is of the correct form (see definition).
	 *         False otherwise.
	 * @throws IOException
	 * @throws DataFormatException
	 */
	private boolean processRecord(BufferedReader in, HexWordSegment segment)
			throws IOException, DataFormatException {
		int recordLength = 8;
		String address = "";
		String word = "";
		String fullLine = in.readLine();
		boolean valid = fullLine.length() == recordLength;
		if (!valid) {
			LOG.severe("016: Invalid text record length. Text record must "
					+ "be " + recordLength + " characters");
			throw new DataFormatException();
		}
		int lineIndex = 0;
		char c = fullLine.charAt(lineIndex);
		valid = (c == 'T');// First character must be 'T'
		if (!valid) {
			LOG.severe("017: First Character of record must be 'T'");
			throw new DataFormatException();
		}
		lineIndex++;
		// 2 hex char address
		for (int i = 0; i < 2; i++) {
			c = fullLine.charAt(lineIndex);
			valid &= CharChecker.isHexChar(c);
			address += c;
			lineIndex++;
		}
		if (!valid) {
			LOG.severe("018: Invalid Text Record. Record memory address must be 2 "
					+ "valid hex characters");
			throw new DataFormatException();
		}
		// 5 hex char word
		for (int i = 0; i < 5; i++) {
			c = fullLine.charAt(lineIndex);
			valid &= CharChecker.isHexChar(c);
			word += c;
			lineIndex++;
		}
		int Address = Integer.parseInt(address, 16);
		if (!valid) {
			LOG.severe("019: Invalid text record. Value of word must be 5 valid hex characters");
			throw new DataFormatException();
		}
		valid &= segment.addWord(Address, word);
		if (!valid) {
			LOG.severe("020: Invalid text record. Address must be [0,255] U [segment load "
					+ "address, segment load address + segment length]");
			throw new DataFormatException();
		}
		return valid;
	}

	/**
	 * Description: Closes the stream
	 * 
	 * @requires stream is open
	 * @alters stream - closes the stream
	 * @ensures Stream is closed.
	 * @param stream
	 *            - stream that is being read from
	 */
	private void closeQuietly(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				// ignore, we've got everything we need
			}
		}
	}
}
