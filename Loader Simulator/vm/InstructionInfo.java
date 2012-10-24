package vm;

/**
 * This class makes it much easier for a user to separate out the different
 * parts of an instruction for the virtual machine. An example of the
 * instruction format is given in the beginning of the programmers guide
 * (Figure.1) . InstructionInfo can break the instruction into its 4 parts, op,
 * r, x, and s for easy use later.
 * 
 * @author Dragon Slayer
 */
public class InstructionInfo {

	public final Operation op;
	public final int rIndex;
	public final int xIndex;
	public int sIndex;
	private final int opStart = 16;
	private final int opEnd = 20;
	private final int regStart = 12;
	private final int regEnd = 14;
	private final int xStart = 10;
	private final int xEnd = 12;
	private final int addStart = 0;
	private final int addEnd = 8;
	private final int[] noOp = { 8, 9, 14, 15 };
	private BitField instruction;

	/**
	 * Description: decodes the parameter instructionWord into its different
	 * parts according to the instruction format given in the beginning of the
	 * programmers guide by calling the InstructionInfo constructor.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensure the fields, op, rIndex, xIndex, and sIndex for an InstructionInfo
	 *         object are set from the instruction.
	 * @param instructionWord
	 *            - to be separated out into its separate op, r, x and s parts.
	 */
	public static InstructionInfo decodeInstruction(BitField instructionWord) {
		return new InstructionInfo(instructionWord);
	}

	/**
	 * Description: retrieves the op, r, x, and s parts of the instruction by
	 * making calls to the other methods in this class.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures sets the fields, op, rIndex, xIndex, and sIndex for an
	 *          InstructionInfo object from the instruction
	 * @param instructionWord
	 *            - to be separated out into its separate op, r, x and s parts.
	 */
	private InstructionInfo(BitField instructionWord) {
		this.instruction = instructionWord;
		op = opLookup();
		rIndex = getRegDec();
		xIndex = getXDec();
		sIndex = getAddrDec();
	}

	/**
	 * Description: returns the enum type that corresponds to the part of the
	 * BitField that is the op code.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures The instruction remains the same.
	 * @return the enum type that corresponds to the part of the BitField that
	 *         is the op code.
	 */
	public Operation opLookup() {
		Operation result = Operation.NOP;
		String bin = instruction.binSubstring(opStart, opEnd);
		int code = Integer.parseInt(bin, 2);
		//choosing machine ops
		switch (code) {
		case 0: {
			result = Operation.LD;
			break;
		}
		case 1: {
			result = Operation.LDI;
			break;
		}
		case 2: {
			result = Operation.ST;
			break;
		}
		case 3: {
			result = Operation.ADD;
			break;
		}
		case 4: {
			result = Operation.SUB;
			break;
		}
		case 5: {
			result = Operation.MUL;
			break;
		}
		case 6: {
			result = Operation.DIV;
			break;
		}
		case 7: {
			result = Operation.OR;
			break;
		}
		case 8: {
			result = Operation.AND;
			break;
		}
		case 9: {
			result = Operation.SHL;
			break;
		}
		case 10: {
			result = Operation.SHR;
			break;
		}
		case 11: {
			result = Operation.IO;
			break;
		}
		case 12: {
			result = Operation.BR;
			break;
		}
		case 13: {
			result = Operation.BRZ;
			break;
		}
		case 14: {
			result = Operation.BRN;
			break;
		}
		case 15: {
			result = Operation.BRS;
			break;
		}
		}

		boolean nop = false;
		for (int i : noOp) {
			nop |= instruction.getBit(i);
		}
		if (nop) {
			result = Operation.NOP;
		}

		return result;

	}

	/**
	 * Description: returns the decimal value of the register(r) part of the
	 * instruction
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures instruction remains the same.
	 * @return the decimal value of the register(r) part of the instruction.
	 */
	public int getRegDec() {
		//decimal for R
		String bin = instruction.binSubstring(regStart, regEnd);
		return Integer.parseInt(bin, 2);
	}

	/**
	 * Description: returns the decimal value of the x part of the instruction
	 * (see Figure.1 in Programmer's guide)
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures instruction remains the same.
	 * @return the decimal value of the x part of the instruction.
	 */
	public int getXDec() {
		//decimal for X 
		String bin = instruction.binSubstring(xStart, xEnd);
		return Integer.parseInt(bin, 2);
	}

	/**
	 * Description: returns the decimal value of the address(s) part of the
	 * instruction (see Figure.1 in Programmer's guide)
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures instruction remains the same.
	 * @return the decimal value of the address(s) part of the instruction.
	 */
	public int getAddrDec() {
		//decimal for S
		String bin = instruction.binSubstring(addStart, addEnd);
		return Integer.parseInt(bin, 2);
	}
}
