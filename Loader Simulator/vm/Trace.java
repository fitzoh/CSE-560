package vm;

import utilities.BinHexConverter;
import utilities.MachineInfo;

/**
 * This class is used for keeping track of the memory, registers, and
 * instruction information that needs sent to the trace process output file. It
 * formats the trace for dumping the memory contents and the contents of the
 * registers. It Contains fields for the operation, r index, s index, x index,
 * and s(x). As well as the value of the pc before an operation, and the values
 * of the r word, x word, s(x) word, and the value of the s(x) word in memory.
 * Trace, also contains fields to keep track of r word, x word, pc, s(x) word,
 * and the value of the s(x) word in memory after an operation. It also contains
 * fields for memory, registers, a string for error messages, and a boolean to
 * determine if an error has occurred. These are all used to make sure the
 * relevant information is displayed when an operation is run. (see instruction
 * information in programmer's guide)
 * 
 * @author Dragon Slayer
 */
public class Trace {

	public final Operation op;
	public final int rIndex;
	public final int xIndex;
	public final int sIndex;
	public final int sOfX;
	public final int pcBefore;
	public final BitField rWord;
	public final BitField xWord;
	public final BitField sOfXWord;
	public BitField sOfXWordMem;

	public BitField rWordAfter;
	public BitField xWordAfter;
	public BitField sOfXWordAfter;
	private int pcAfter;
	private Memory mem;
	private Registers reg;
	private boolean errorOccured;
	private String errorString;
	private BitField sOfXWordMemAfter;

	/**
	 * Description: Sets the fields of Trace to contain the different parts of
	 * the instruction (figure 1), memory, and the registers. It sets a field
	 * for S(x). If the x part of the instruction is zero, then s(x) is set to
	 * the s part of the instruction, else S(x) is the result of adding the
	 * value of the S Field to register X (xIndex)
	 * 
	 * @requires true
	 * @alters the fields of Trace to contain the different parts of the
	 *         instruction (Programmer's guide figure 1), memory and the
	 *         registers. Also sets a field for S(x).
	 * @ensures mem, reg, and opInfo are unchanged.
	 * @param mem
	 *            - contents of memory
	 * @param reg
	 *            - contents of registers
	 * @param opInfo
	 *            - the instruction information
	 */
	public Trace(Memory mem, Registers reg, InstructionInfo opInfo) {
		this.mem = mem;
		this.reg = reg;
		this.op = opInfo.op;
		this.pcBefore = reg.nextInstruction();
		this.rIndex = opInfo.rIndex;
		this.xIndex = opInfo.xIndex;
		this.sIndex = opInfo.sIndex;
		this.rWord = reg.getReg(this.rIndex);
		this.xWord = reg.getReg(this.xIndex);
		
		//setting up value of s or s+x
		if (this.xIndex == 0) {
			this.sOfX = this.sIndex;
		} else {
			this.sOfX = (new BitField(this.sIndex, 8).extendToWord()).add(
					reg.getReg(this.xIndex)).toDecInt();
		}
		this.sOfXWord = new BitField(this.sOfX, MachineInfo.BITS_IN_WORD);
		try {
			this.sOfXWordMem = mem.getWordAtAddr(this.sOfXWord.toDecInt());
		} catch (IllegalMemoryAddressException e) {
			this.sOfXWordMem = null;
			// Error is caught and handled in executioner
		}
	}

	/**
	 * Description: Used if an error has occurred during an operation. The error
	 * string field is set to the message and the boolean flag for if there was
	 * an error is set to true.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures message is unchanged from when it was passed in. The boolean
	 *          field for keeping track if there was an error is set to true,
	 *          and the error string is set to message.
	 * @param message
	 *            - an informative error message that specifies what error
	 *            occurred.
	 */
	public void errorOccured(String message) {
		this.errorOccured = true;
		this.errorString = message;
	}

	/**
	 * Description: updates the values of after r word, after x word, and after
	 * s(x) word after an operation. The actual values of the registers and
	 * memory are not changed.
	 * 
	 * @requires true
	 * @alters after r word, after x word, and after s(x) word to the values of
	 *         the register at the r index, x index, and s(x) respectively.
	 * @ensures The values of the register, memory, and instruction are
	 *          unchanged.
	 */
	public void update() {
		this.pcAfter = this.reg.nextInstruction();
		this.rWordAfter = this.reg.getReg(this.rIndex);
		this.xWordAfter = this.reg.getReg(this.xIndex);
		try {
			this.sOfXWordMemAfter = this.mem.getWordAtAddr(this.sOfX);
		} catch (IllegalMemoryAddressException e) {
			// Error is caught and handled in executioner
		}
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(headerString());
		result.append(beforeString());
		if (this.errorOccured) {
			result.append("\n\n" + this.errorString + "\n");
		}
		result.append(afterString());
		return result.toString();
	}

	/**
	 * Description: Creates the header for a single operation trace. Puts the
	 * value of the pc, the memory word at the address of the pc, and the
	 * operation that is about to be performed into a string.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures values in registers and memory are not changed.
	 * @return the header for a single operation trace. Puts the value of the
	 *         pc, the memory word at the address of the pc, and the operation
	 *         that is about to be performed into a string and returns it.
	 */
	private String headerString() {
		StringBuffer result = new StringBuffer();
		result.append("\n\n*********BEGIN INSTRUCTION***********************");
		result.append("\nPC:\t\t\t\t0x"
				+ BinHexConverter.toHexString(this.pcBefore, 8));
		try {
			result.append("\nMEM[0x"
					+ BinHexConverter.toHexString(this.pcBefore, 8)
					+ "]:\t\t0x"
					+ this.mem.getWordAtAddr(this.pcBefore).toString());
		} catch (IllegalMemoryAddressException e) {
			// PC can't give us an invalid mem
		}
		result.append("\nOP:" + this.op + "\n");
		return result.toString();
	}

	/**
	 * Description: puts all the relevant information of the registers and
	 * memory of a single operation into a string. It is used before an
	 * operation (only sometimes will the value of the memory at the address s
	 * be added to the string (according to the operations that use in) refer to
	 * instruction information) in programmer's guide)
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures values in registers and memory are not changed.
	 * @return all the relevant information of the registers and memory of a
	 *         single operation
	 */
	private String beforeString() {
		StringBuffer result = new StringBuffer();
		//put into strings
		result.append("\nBefore instruction:");
		result.append("\nPC:\t\t\t\t0x"
				+ BinHexConverter.toHexString(this.pcBefore, 8));
		result.append("\nS(x):\t\t\t0x" + this.sOfXWord);
		result.append("\nR[r](r=" + this.rIndex + "):\t\t0x" + this.rWord);
		result.append("\nR[x](x=" + this.xIndex + "):\t\t0x" + this.xWord);
		if (this.sIndex < MachineInfo.WORDS_IN_MEM && this.sOfXWordMem != null) {
			result.append("\nMEM[S(x)]:\t\t0x" + this.sOfXWordMem);
		}
		return result.toString();
	}

	/**
	 * Description: puts all the relevant information of the registers and
	 * memory of a single operation into a string. It is used after an
	 * operation. (only sometimes will the value of the memory at the address s
	 * be added to the string (according to the operations that use in) refer to
	 * instruction information) in programmer's guide)
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures values in registers and memory are not changed.
	 * @return all the relevant information of the registers and memory of a
	 *         single operation
	 */
	private String afterString() {
		//put into strings
		StringBuffer result = new StringBuffer();
		result.append("\n\nAfter instruction:");
		result.append("\nPC:\t\t\t\t0x"
				+ BinHexConverter.toHexString(this.pcAfter, 8));
		result.append("\nR[r](r=" + this.rIndex + "):\t\t0x" + this.rWordAfter);
		if (this.sIndex < MachineInfo.WORDS_IN_MEM
				&& this.sOfXWordMemAfter != null) {
			result.append("\nMEM[S(x)]:\t\t0x" + this.sOfXWordMemAfter);
		}
		result.append("\n*********END INSTRUCTION*************************\n");
		return result.toString();
	}

}
