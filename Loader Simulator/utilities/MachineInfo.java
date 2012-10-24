package utilities;

/**
 * This class does not have any methods. It sets variable names to many of the
 * machine characteristics so that they can be used in other classes.
 * 
 * @author Dragon Slayer
 */
public class MachineInfo {
	// 1 word is 20 bits
	public static final int BITS_IN_WORD = 20;
	// Memory contains 256 words
	public static final int WORDS_IN_MEM = 256;
	// VM has 4 registers
	public static final int NUM_REGISTERS = 4;
	// An address is 8 bits
	public static final int BITS_IN_ADDRESS = 8;
	// The Program counter is also 8 bits long
	public static final int BITS_IN_PC = BITS_IN_ADDRESS;
	// no more than 200 instructions may be executed														 
	public static final int MAX_EXECUTABLE_INSTRUCTIONS = 200;
																
}
