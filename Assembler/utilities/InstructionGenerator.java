package utilities;

/**
 * This class can be used to take the Assembler Instruction fields, op, 
 * r, s, and x and covert them into their corresponding object code 
 * instruction values. 
 * @author Dragon Slayer
 */
public class InstructionGenerator {

	/**
	 * Description: Converts the op, r, s, and x fields of an assembler instruction 
	 * into the corresponding object code instruction. If the Instruction contains an 
	 * external symbol, then the s =  -1. If s = -1, then the s field of the instruction is 
	 * set to zero.
	 * @requires true
	 * @alters N/A
	 * @ensures op, r, s, and x are unchanged. Object code instruction of the parameters
	 *  is returned.
	 * @param op op field of the assembler instruction to be converted to the op field 
	 * of the object code instruction
	 * @param r field of the assembler instruction to be converted to the r field of the 
	 * object code instruction
	 * @param s s field of the assembler instruction to be converted to the s field of the 
	 * object code instruction
	 * @param x field of the assembler instruction to be converted to the x field of the 
	 * object code instruction
	 * @return the Object code instruction of the parameters op, r, s, and x as a string
	 */
	public static String genInstruction(String op, int r, int s, int x) {
		String opHex = decodeOp(op);
		int xDec = x*4; //Shift the bits for 2 positions to the left
		String xHex = String.format("%01X", xDec);
		if(s==-1){
			s=0;
		}
		String sHex = String.format("%02X", s);
		return opHex + r + xHex + sHex;
	}

	/**
	 * Description: Converts the Operation field of an Assembler instruction into 
	 * its corresponding OP object code instruction. The string value of the object 
	 * code OP is returned.
	 * @requires true
	 * @alters N/A
	 * @ensures op is unchanged
	 * @param op op field of the assembler instruction to be converted to the op field 
	 * of the object code instruction
	 * @return the hex opcode corresponding to the OP as a string
	 */
	private static String decodeOp(String op) {
		String result = "X";
		if (op.equals("LD ")) {
			result = "0";
		}
		if (op.equals("LDI")) {
			result = "1";
		}
		if (op.equals("ST ")) {
			result = "2";
		}
		if (op.equals("ADD")) {
			result = "3";
		}
		if (op.equals("SUB")) {
			result = "4";
		}
		if (op.equals("MUL")) {
			result = "5";
		}
		if (op.equals("DIV")) {
			result = "6";
		}
		if (op.equals("OR ")) {
			result = "7";
		}
		if (op.equals("AND")) {
			result = "8";
		}
		if (op.equals("SHL")) {
			result = "9";
		}
		if (op.equals("SHR")) {
			result = "A";
		}
		if (op.equals("IO "))  {
			result = "B";
		}
		if (op.equals("BR ")) {
			result = "C";
		}
		if (op.equals("BRZ")) {
			result = "D";
		}
		if (op.equals("BRN")) {
			result = "E";
		}
		if (op.equals("BRS")) {
			result = "F";
		}

		return result;
	}
}
