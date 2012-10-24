package vm;

import utilities.MachineInfo;

/**
 * This class is used to represent the registers and the program counter for the
 * virtual machine. It contains methods for incrementing the program counter and
 * returning the decimal integer value of the program counter as well as a
 * method to return the value in a specified register. Registers internal
 * representation is an array of BitField words and the program counter is
 * represented by a BitField address.
 * 
 * @author Dragon Slayer
 */
public class Registers {
	int numRegisters = MachineInfo.NUM_REGISTERS;

	private BitField[] regArr;
	private BitField programCounter;

	/**
	 * Description: this constructor makes Registers immutable. It initializes
	 * each individual register in the array of BitField words with its name and
	 * length. There are NUM_REGISTERS (4) amount of registers. Each register is
	 * initialized to contain the boolean value of zero. This constructor also
	 * initializes the program counter as an Address with the name "PC", and the
	 * rep value of the PC is given BitField address value from the Header
	 * Record for the execution start.
	 * 
	 * @requires 0 <= excecStart <= 255
	 * @alters sets the values of the registers and program counter
	 * @ensures an array of registers and a program counter are created
	 *          according to the description.
	 * @param execStart
	 *            - the value the PC will be set to.
	 */
	public Registers(int execStart) {
		this.regArr = new BitField[this.numRegisters];
		for (int i = 0; i < this.numRegisters; i++) {
			this.regArr[i] = new BitField(MachineInfo.BITS_IN_WORD);
		}
		this.programCounter = new BitField(execStart,
				MachineInfo.BITS_IN_ADDRESS);
	}

	/**
	 * Description: this method returns the decimal integer value of the program
	 * counter. Used to see what the next instruction in memory is.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures true
	 * @return the decimal integer value of the program counter
	 */
	public int nextInstruction() {
		return this.programCounter.toDecInt();
	}

	/**
	 * Description: this method increments the program counter by one
	 * 
	 * @requires true
	 * @alters programCounter is one greater than it was before the call.
	 * @ensures the value of the program counter is incremented by one
	 */
	public void incPC() {
		this.programCounter = this.programCounter.increment();
	}

	/**
	 * Description: sets the Program Counter to the value of bits 0 -7 from the
	 * BitField word w. (the value of the program counter must be in the range
	 * (0-255), if it is not, the program counter is not set and an
	 * IllegalMemoryAddressException is thrown.)
	 * 
	 * @requires true
	 * @alters programCounter now contains the values of the bits 0 - 7 from the
	 *         BitField word.
	 * @ensures w remains the same.
	 * @param w
	 *            - bits 0-7 are set to the program counter.
	 * @throws IllegalMemoryAddressException
	 */
	public void setPC(BitField w) throws IllegalMemoryAddressException {
		if (w.toDecInt() >= MachineInfo.WORDS_IN_MEM) {
			throw new IllegalMemoryAddressException();
		}
		this.programCounter = w.parseAddress();
	}

	/**
	 * Description: this method returns a copy of the BitField word that is in
	 * the register array at the specified index, xIndex.
	 * 
	 * @requires 0 <= xIndex < NUM_REGISTERS
	 * @alters N/A
	 * @ensures immutability
	 * @return a copy of the BitField word that is in the register array at the
	 *         specified index, xIndex
	 * @param xIndex
	 *            - the number of the register to retrieve the BitField word
	 *            from
	 */
	public BitField getReg(int xIndex) {
		return new BitField(this.regArr[xIndex]);
	}

	/**
	 * Description: sets the register i, to contain the BitField word w
	 * 
	 * @requires 0 <= i <= 3
	 * @alters N/A
	 * @ensures w and i remain the same.
	 * @param w
	 *            - BitField word being copied into the register
	 * @param i
	 *            - register number to copy the word into
	 */
	public void setReg(BitField w, int i) {
		BitField newVal = new BitField(w);
		this.regArr[i] = newVal;
	}

	/**
	 * Description: extends the BitField address in the program counter to a
	 * BitField word which has the same value as the BitField address. It then
	 * stores this BitField word in the register indicated by reg.
	 * 
	 * @requires 0 <= reg <= 3
	 * @alters register number reg, contains the BitField word from the PC
	 * @ensures value of PC and reg remain the same
	 * @param reg
	 *            - register number where the Address in PC, (extended to Word)
	 *            is stored.
	 */
	public void storePC(int reg) {
		this.regArr[reg] = this.programCounter.extendToWord();
	}

	/**
	 * Description: takes the value of the PC, extends it to be a BitField word
	 * of the same value and adds sWord to it. Bits 0-7 of this word are set as
	 * the new value of the program counter. (The value of the program counter
	 * must be in the range (0-255), if it is not, the program counter is not
	 * indexed and an IllegalMemoryAddressException is thrown.)
	 * 
	 * @requires true
	 * @alters PC = #PC + sWord
	 * @ensure true
	 * @param sWord
	 *            - value to be added to the PC
	 * @throws IllegalMemoryAddressException
	 */
	public void indexPC(BitField sWord) throws IllegalMemoryAddressException {
		BitField newAdd = sWord.add(this.programCounter.extendToWord());
		if (newAdd.toDecInt() >= MachineInfo.WORDS_IN_MEM) {
			throw new IllegalMemoryAddressException();
		}
		this.programCounter = newAdd.parseAddress();
	}

	/**
	 * Description: replaces the least significant 8 bits of register xIndex
	 * with the least significant 8 bits of the BitField address add, leaving
	 * the rest of the bits in register xIndex alone.
	 * 
	 * @requires 0 <= xIndex <= 3
	 * @alters the 8 least significant register xIndex
	 * @ensures add and xIndex are unchanged.
	 * @param xIndex
	 *            - register number whose value will be changed.
	 * @param add
	 *            - BitField address from which the 8 least significant bits are
	 *            from.
	 */
	public void storeIOByte(int xIndex, BitField add) {
		BitField addWord = add.extendToWord();
		BitField mask = new BitField("FFF00", MachineInfo.BITS_IN_WORD);
		BitField masked = mask.and(this.regArr[xIndex]);
		this.regArr[xIndex] = masked.or(addWord);

	}

	/**
	 * Description: returns the decimal value of the 8 most significant bits of
	 * register xIndex.
	 * 
	 * @requires 0 <= xIndex <= 3
	 * @alter N/A
	 * @ensures xIndex and the value in its register remain unchanged.
	 * @param xIndex
	 *            - register number from which to get the most significant bits
	 * @return the decimal value of the 8 most significant bits of register
	 *         xIndex.
	 */
	public int getSigIOByte(int xIndex) {
		BitField w = this.regArr[xIndex];
		String binStr = w.binSubstring(MachineInfo.BITS_IN_WORD - 8,
				MachineInfo.BITS_IN_WORD);
		return Integer.parseInt(binStr, 2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer regString = new StringBuffer("PC: 0x"
				+ this.programCounter.toString());
		regString.append("\nRegisters:\n");
		for (int i = 0; i < this.numRegisters; i++) {
			regString.append("R[" + i + "]: 0x" + this.regArr[i].toString()
					+ "\t");
		}
		return regString.toString() + "\n";
	}
}
