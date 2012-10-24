package vm;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

import utilities.MachineInfo;

/**
 * This class can be used to represent a list of Text Records. (see definition)
 * HexWordSegment is represented by a Map whose key value is an integer (the
 * address) and whose dependent value is a String, which is a TextRecord.
 */
public class HexWordSegment {
	private Map<Integer, String> rep;
	private static final int maxAddress = MachineInfo.WORDS_IN_MEM;
	public final int execStart;
	public final int startAdd;
	public final int size;
	public final String name;

	/**
	 * Description: creates a HexWordSegment which is represented by a map with
	 * key value the address and dependent value a string (Text Record). If
	 * (execStartAddress < maxAddress(256)) and (loadAddressStart +
	 * segmentLength < maxAddress(256)), then a call is made to the constructor
	 * to create the HexWordSegment and this is returned. segmentLength is the
	 * number of segments being read from the executable file. If the above does
	 * not hold, then a null HexWordSegment is returned.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures immutability
	 * @return If (execStartAddress < maxAddress(256)) and (loadAddressStart +
	 *         segmentLength < maxAddress(256)), then a call is made to the
	 *         constructor to create the HexWordSegment. If the above does not
	 *         hold, then a null HexWordSegment is returned.
	 * @param execStartAddress
	 *            - the starting address for execution which was taken from the
	 *            Header Record
	 * @param name
	 *            - character segment name from the Header Record
	 * @param loadAddressStart
	 *            - the segment load address from the Header Record
	 * @param segmentLength
	 *            - the length of the segment that will be read into a
	 *            HexWordSegment
	 * @throws DataFormatException
	 */
	public static HexWordSegment getHexWordSegment(int execStartAddress,
			String name, int loadAddressStart, int segmentLength)
			throws DataFormatException {
		HexWordSegment seg = null;
		if (!(execStartAddress >= maxAddress || loadAddressStart
				+ segmentLength >= maxAddress)) {
			seg = new HexWordSegment(execStartAddress, name, loadAddressStart,
					segmentLength);
		} else {
			throw new DataFormatException();
		}
		return seg;
	}

	/**
	 * Description: Immutable constructor creates a HexWordSegment which is
	 * represented by an array of Strings. The size is set to the number of
	 * lines to be processed from the executable file, the length of the segment
	 * from the executable file.
	 * 
	 * @requires (execStartAddress < maxAddress(256)) and (loadAddressStart +
	 *           segmentLength < maxAddress(256))
	 * @alters HexWordSegment object is created.
	 * @ensures immutability
	 * @param execStartAddress
	 *            - the starting address for execution which was taken from the
	 *            Header Record
	 * @param name
	 *            - character segment name from the Header Record
	 * @param loadAddressStart
	 *            - the segment load address from the Header Record
	 * @param segmentLength
	 *            - the length of the segment that will be read into a
	 *            HexWordSegment
	 * 
	 */
	private HexWordSegment(int execStartAddress, String name,
			int loadAddressStart, int segmentLength) {
		execStart = execStartAddress;
		startAdd = loadAddressStart;
		size = segmentLength;
		this.name = name;
		rep = new HashMap<Integer, String>();

	}

	/**
	 * Description: adds an Address and word (a text record) to the
	 * HexWordSegment (map). Each address has a corresponding BitField word. If
	 * the map previously contained a mapping for the key Address, the old word
	 * is replaced by the new word.
	 * 
	 * @requires true
	 * @alters adds a word (Text Record) to the map representing HexWordSegment.
	 * @ensures word and Address remain unchanged
	 * @return true if the word was successfully added to memory
	 * @param address
	 *            - the Text Record being added to the HexWordSegment
	 *            corresponding to the Address.
	 * @param word
	 *            - key value added to the Map, (will be the position in memory
	 *            where the string is to be added)
	 */
	public Boolean addWord(int address, String word) {
		rep.put(address, word);
		return address >= startAdd && address <= maxAddress
				&& address <= startAdd + size;
	}

	/**
	 * Description: returns a copy of the map of HexWordSegment
	 * 
	 * @requires true
	 * @alters Nothing
	 * @ensures original HexWordSegment is unchanged.
	 * @return a copy of the map of HexWordSegment
	 */
	public Map<Integer, String> getVals() {
		Map<Integer, String> copy = new HashMap<Integer, String>();
		for (Integer address : rep.keySet()) {
			copy.put(address, rep.get(address));
		}
		return copy;
	}
}
