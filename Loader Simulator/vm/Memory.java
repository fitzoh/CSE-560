package vm;

import java.util.Map;

import utilities.BinHexConverter;
import utilities.MachineInfo;

/**
 * This class represents the memory in the virtual machine. It keeps a list of
 * all the values in the memory of the virtual machine. The memory is
 * represented by an array of BitFields that are words (20 bits). The memory for
 * this virtual machine has 256 20-bit words, with addresses 0-255.
 * 
 * @author Dragon Slayer
 */
public class Memory {
	private static final int size = MachineInfo.WORDS_IN_MEM;
	private BitField[] rep;

	/**
	 * Description: Initializes all the words in memory to have the value of
	 * zero. In other words, all of the BitFields are given the default value of
	 * a BitField word (which is for all 20 bits in the BitField to be set to
	 * false).
	 * 
	 * @requires true
	 * @alters allocates memory for the virtual machine.
	 * @ensures each BitField in memory is given the default value of a BitField
	 *          word (20 bits).
	 */
	public Memory() {
		rep = new BitField[size];
		for (int i = 0; i < size; i++) {
			rep[i] = new BitField(MachineInfo.BITS_IN_WORD);
		}
	}

	/**
	 * Description: places the HexWordSegment segment into the memory of the
	 * virtual machine by calling the Memory constructor and then making a call
	 * to the method addMemSegment.
	 * 
	 * @requires true
	 * @alters the values in memory from the starting address of the segment to
	 *         the end of the segment to contain the values of the segment
	 * @ensures segment is unchanged and the segment is added to the memory.
	 * @param segment
	 *            - to be put into the memory of the virtual machine
	 */
	public Memory(HexWordSegment segment) {
		this();
		addMemSegment(segment);
	}

	/**
	 * Description: returns a copy of the BitField word that is stored in memory
	 * at the specified address, addr.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures Memory is unchanged
	 * @param addr
	 *            - the position of the BitFieldword in memory (array of
	 *            BitFields) to be returned.
	 * @return a copy of the BitField that is stored at the address addr in
	 *         memory.
	 * @throws IllegalMemoryAddressException
	 */
	public BitField getWordAtAddr(int addr)
			throws IllegalMemoryAddressException {
		//give address 
		if (addr >= size) {
			throw new IllegalMemoryAddressException();
		}
		BitField result = new BitField(rep[addr]);
		return result;
	}

	/**
	 * Description: places the HexWordSegment segment into the memory of the
	 * virtual machine starting at the start address given by HexWordSegment and
	 * continuing to the end of the segment. It does this by calling addWord as
	 * many times as necessary to add the whole segment to Memory.
	 * 
	 * @requires true
	 * @alters the values in memory starting at the start address given by
	 *         HexWordSegment and continuing to the end of the segment.
	 * @ensures segment remains unchanged
	 * @param segment
	 *            - values being put into memory.
	 */
	public void addMemSegment(HexWordSegment segment) {
		Map<Integer, String> values = segment.getVals();
		for (Integer address : values.keySet()) {
			try {
				addWord(values.get(address), address);
			} catch (IllegalMemoryAddressException e) {
				// Already checked when segment is created;
			}
		}
	}

	/**
	 * Description: places a copy of one BitField word in memory at the
	 * specified address. It throws an IllegalMamoryAddressExeption if the
	 * address is outside the range of (0 - 255)
	 * 
	 * @requires address must be between 0 and 255
	 * @alters one BitField word is added to memory at the specified address
	 * @ensures hex and address remain the same
	 * @param w
	 *            - BitField being added to memory
	 * @param address
	 *            - position in memory where the BitField is to be added.
	 * @throws IllegalMemoryAddressException
	 */
	public void addWord(BitField w, int address)
			throws IllegalMemoryAddressException {
		//illegal size check
		if (address >= size) {
			throw new IllegalMemoryAddressException();
		}
		rep[address] = new BitField(w);
	}

	/**
	 * Description: Converts hex into its boolean representation of a BitField,
	 * this is what is placed in memory.
	 * 
	 * @requires address must be between 0 and 255
	 * @alters one BitField word is added to memory at the specified address
	 * @ensures hex and address remain the same
	 * @param hex
	 *            - value being placed in memory
	 * @param address
	 *            - where the hex string will be placed in memory
	 * @throws IllegalMemoryAddressException
	 */
	public void addWord(String hex, int address)
			throws IllegalMemoryAddressException {
		//boolean rep of bitfield
		if (address >= size) {
			throw new IllegalMemoryAddressException();
		}
		rep[address] = new BitField(hex, MachineInfo.BITS_IN_WORD);
	}

	/**
	 * Description: Can be used to print out all the contents of memory. Returns
	 * the index of the memory location followed by the word at that location,
	 * for all of memory.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures Memory remains the same.
	 * @return the index of memory then ": ", followed by each BitField word
	 *         until all memory has been added to a string. Example:
	 *         "0xFF:12345"
	 */
	public String toString() {
		//printing value
		StringBuffer result = new StringBuffer("Memory:");
		for (int i = 0; i < size; i++) {
			if (i % 4 == 0) {
				result.append("\n");
			}
			result.append("0x" + BinHexConverter.toHexString(i, 8) + ": "
					+ rep[i] + "\t");
		}
		return result.toString() + "\n";
	}
}
