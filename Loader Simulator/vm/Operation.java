package vm;

/**
 * This is an enum class which contains all of the Assembler Instructions for
 * the Wi12 Machine. It contains the enum for LD, LDI, ADD, SUB, MUL, DIV, OR,
 * AND, SHL, SHR, IO, BR, BRZ, BRN, BRS, NOP. This class makes it easier to
 * identify and look up operations.
 * 
 * @author Dragon Slayer
 */
public enum Operation {
	LD, LDI, ST, ADD, SUB, MUL, DIV, OR, AND, SHL, SHR, IO, BR, BRZ, BRN, BRS, NOP
}
