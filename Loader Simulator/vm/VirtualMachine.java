package vm;

import java.io.IOException;
import java.util.logging.Logger;

import utilities.MachineInfo;

/**
 * This class creates the virtual machine for the Wi12 Machine. It contains
 * fields for the Memory, Registers, the total number of instructions executed,
 * an IOGroup (input and output) and a Logger. It contains methods to run the
 * virtual machine and to execute instructions for the simulator.
 * 
 * @author Dragon Slayer
 */
public class VirtualMachine {
	private Memory mem;
	private Registers registers;
	private int totalInstructionsExecuted;
	private IOGroup IO;
	private Logger LOG;

	/**
	 * Description: Creates the virtual machine. Sets the memory of the string
	 * to the values from seg. The input and output for the machine is set to
	 * IO. The logger is set to log. The total number of instructions is set to
	 * 0. The initial value of the Program counter is set.
	 * 
	 * @requires seg, IO, and log exist.
	 * @alters initializes the memory, registers, IOGroup, Logger, and the total
	 *         number of Instructions.
	 * @ensures true
	 * @param seg
	 *            - segment being placed into memory
	 * @param IO
	 *            - input and output for the virtual machine
	 * @param log
	 *            - logger for error messages.
	 * @throws IOException
	 */
	public VirtualMachine(HexWordSegment seg, IOGroup IO, Logger log)
			throws IOException {
		this.mem = new Memory(seg);
		this.IO = IO;
		this.LOG = log;
		this.totalInstructionsExecuted = 0;
		this.registers = new Registers(seg.execStart);
	}

	/**
	 * Description: runs the virtual machine. The initial contents of memory,
	 * and the registers are logged as info. It executes instructions until an
	 * exception is caught. The exceptions that could be caught are
	 * maxInstructionsExceededException, HaltException, EOFException and
	 * IOException. If HaltException is caught, nothing is done. If any of the
	 * other 3 exceptions are caught an error is sent to the Logger as severe.
	 * Once execution stops, the final contents of memory and the registers are
	 * logged as info.
	 * 
	 * @requires true
	 * @alters: The process output, process trace file, memory, registers, Log,
	 *          and total instructions executed based on the instructions being
	 *          executed and the number of instructions executed. The initial
	 *          and final contents of memory and the registers are logged.
	 * @ensures If an error occurs it is sent to the logger.
	 */
	public void run() {
		//execute instruction
		this.LOG.fine(this.toString());
		//produce errors
		try {
			while (true) {
				this.executeInstruction();
			}
		} catch (MaxInstructionsExceededException e) {
			this.LOG.fine("Maximum number of instructions exceeded.");
			try {
				IO.write("Maximum number of instructions exceeded.");
			} catch (IOException e1) {
			}
		} catch (HaltException e) {
			LOG.fine(e.getTrace());
		} catch (EOFException e) {
			this.LOG.fine("Fatal IO error: end of IO file reached");
			try {
				IO.write("Maximum number of instructions exceeded.");
			} catch (IOException e1) {
			}
		} catch (IOException e) {
			this.LOG.fine("Fatal IO error.");
			try {
				IO.write("Maximum number of instructions exceeded.");
			} catch (IOException e1) {
			}
		} finally {
			this.LOG.fine(this.toString());
			IO.cleanUp();
		}
	}

	/**
	 * Description: If the total number of instructions executed equals the
	 * MAX_EXECUTABLE_INSTRUCTIONS then a MaxInstructtionsExceededException is
	 * thrown. Else, the program counter is incremented and the Word at the next
	 * address is executed. For each instruction a new Trace of the effected
	 * memory and registers is created and the trace is logged as fine.
	 * 
	 * @requires true
	 * @alters IO, memory, registers based on the instruction being executed.
	 *         (See Machine Instructions) Log is updated with a new trace for
	 *         the instruction.
	 * @ensures The rest of the memory and registers are unchanged.
	 * @throws HaltException
	 * @throws IOException
	 * @throws MaxInstructionsExceededException
	 * @throws EOFException
	 */
	public void executeInstruction() throws HaltException, IOException,
			MaxInstructionsExceededException, EOFException {
		this.totalInstructionsExecuted++;
		if (this.totalInstructionsExecuted > MachineInfo.MAX_EXECUTABLE_INSTRUCTIONS) {
			throw new MaxInstructionsExceededException();
		}
		int nextInst = this.registers.nextInstruction();
		BitField instWord = null;
		try {
			instWord = this.mem.getWordAtAddr(nextInst);
		} catch (IllegalMemoryAddressException e) {
			// No exception should occur here
		}
		InstructionInfo opInfo = InstructionInfo.decodeInstruction(instWord);
		Executioner headsman = new Executioner(this.mem, this.registers,
				this.IO);
		Trace trace = headsman.execute(opInfo);
		this.LOG.fine(trace.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer(this.registers.toString() + "\n"
				+ this.mem.toString()).toString()
				+ "\n";
	}

	/**
	 * Extends Exception and is thrown if the maximum number of instructions
	 * (200) is exceeded.
	 * 
	 * @author Dragon Slayer
	 */
	private class MaxInstructionsExceededException extends Exception {

		private static final long serialVersionUID = -1879760799869265571L;

	}

}
