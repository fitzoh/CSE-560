package vm;

import java.io.IOException;

import utilities.CharChecker;
import utilities.MachineInfo;

/**
 * Executes the instructions according to the specifications of Machine
 * Instructions. Its internal representation consists of a Memory field, a
 * Registers field, and an IOGroup. First, the op code is evaluated. If the
 * op-code is IO or BR then the R value of the instruction is evaluated. If the
 * R in a BR instruction i s 0, then the X value is evaluated. The appropriate
 * Assembler instruction is then executed.
 * 
 * @author Dragon Slayer
 */
public class Executioner {

	private Memory mem;
	private Registers reg;
	private IOGroup IO;

	/**
	 * Description: Initializes the internal representation of the Executioner
	 * to contain mem, reg, and IO
	 * 
	 * @requires mem, reg, IO exist
	 * @alters internal representation of the Executioner
	 * @ensures mem, reg, IO remain unchanged
	 * @param mem
	 *            - internal representation of the Memory of the Executioner is
	 *            set to this.
	 * @param reg
	 *            - internal representation of the Registers of the Executioner
	 *            is set to this.
	 * @param IO
	 *            - internal representation of the IOGroup of the Executioner is
	 *            set to this.
	 */
	public Executioner(Memory mem, Registers reg, IOGroup IO) {
		this.mem = mem;
		this.reg = reg;
		this.IO = IO;
	}

	/**
	 * Description: Based on the op code of the instruction (from opInfo), the
	 * appropriate instruction is executed, except if the op code is an IO or
	 * BR, in which case the appropriate method is called to further evaluate
	 * the instruction. Returns an updated trace of the registers and memory
	 * affected by the instructions execution
	 * 
	 * @requires true
	 * @alters mem, reg, and/or IO as specified in the Machine Instructions.
	 * @ensures opInfo is unchanged.
	 * @return Returns an updated Trace of the registers and memory affected by
	 *         the instruction. If an exception is caught, an error is added to
	 *         the trace.
	 * @param opInfo
	 *            - the instruction information being evaluated and executed.
	 * @return a Trace object of all the s
	 * @throws IOException
	 * @throws EOFException
	 * @throws HaltException
	 */
	public Trace execute(InstructionInfo opInfo) throws IOException,
			EOFException, HaltException {
		Trace trace = new Trace(this.mem, this.reg, opInfo);
		BitField sWord = trace.sOfXWord;
		this.reg.incPC();
		//choosing machine instructions
		switch (opInfo.op) {
		case LD: {
			try {
				this.reg.setReg(this.mem.getWordAtAddr(sWord.toDecInt()),
						opInfo.rIndex);
			} catch (IllegalMemoryAddressException e) {
				String error = "WARNING: Illegal memory address detected. No operation "
						+ "will be performed.";
				this.IO.write(error);
				trace.errorOccured(error);
				
			}
			break;
		}
		case LDI: {
			this.reg.setReg(sWord, opInfo.rIndex);

			break;
		}
		case ST: {
			try {
				this.mem.addWord(this.reg.getReg(opInfo.rIndex),
						sWord.toDecInt());
			} catch (IllegalMemoryAddressException e) {
				String error = "WARNING: Illegal memory address detected. No operation "
						+ "will be performed.";
				this.IO.write(error);
				trace.errorOccured(error);
			}
			break;
		}
		case ADD: {
			BitField result = null;
			try {
				result = this.reg.getReg(opInfo.rIndex).add(
						this.mem.getWordAtAddr(sWord.toDecInt()));
			} catch (IllegalMemoryAddressException e) {
				String error = "WARNING: Illegal memory address detected. No operation "
						+ "will be performed.";
				this.IO.write(error);
				trace.errorOccured(error);
			}
			this.reg.setReg(result, opInfo.rIndex);
			break;
		}
		case SUB: {
			BitField result;
			try {
				result = this.reg.getReg(opInfo.rIndex).subtract(
						this.mem.getWordAtAddr(sWord.toDecInt()));
				this.reg.setReg(result, opInfo.rIndex);
			} catch (IllegalMemoryAddressException e) {
				// log Auto-generated catch block
				String error = "WARNING: Illegal memory address detected. No operation will "
						+ "be performed.";
				this.IO.write(error);
				trace.errorOccured(error);
			}
			break;
		}
		case MUL: {
			BitField result;
			try {
				result = this.reg.getReg(opInfo.rIndex).multiply(
						this.mem.getWordAtAddr(sWord.toDecInt()));
				this.reg.setReg(result, opInfo.rIndex);
			} catch (IllegalMemoryAddressException e) {
				//log Auto-generated catch block
				String error = "WARNING: Illegal memory address detected. No operation will "
						+ "be performed.";
				this.IO.write(error);
				trace.errorOccured(error);
			}
			break;

		}
		case DIV: {
			try {
				BitField result = this.reg.getReg(opInfo.rIndex).divideBy(
						this.mem.getWordAtAddr(sWord.toDecInt()));
				this.reg.setReg(result, opInfo.rIndex);
			} catch (IllegalArgumentException e) {
				String error = "WARNING: attempt to divide by zero encountered. No operation "
						+ "will be performed";
				this.IO.write(error);
				trace.errorOccured(error);
			} catch (IllegalMemoryAddressException e) {
				// log
				String error = "WARNING: Illegal memory address detected. No operation will "
						+ "be performed.";
				this.IO.write(error);
				trace.errorOccured(error);
			}

			break;
		}
		case OR: {
			BitField result;
			try {
				result = this.reg.getReg(opInfo.rIndex).or(
						this.mem.getWordAtAddr(sWord.toDecInt()));
				this.reg.setReg(result, opInfo.rIndex);
			} catch (IllegalMemoryAddressException e) {
				// log Auto-generated catch block
				String error = "WARNING: Illegal memory address detected. No operation will "
						+ "be performed.";
				this.IO.write(error);
				trace.errorOccured(error);
			}
			break;
		}
		case AND: {
			BitField result;
			try {
				result = this.reg.getReg(opInfo.rIndex).and(
						this.mem.getWordAtAddr(sWord.toDecInt()));
				this.reg.setReg(result, opInfo.rIndex);
			} catch (IllegalMemoryAddressException e) {
				// log Auto-generated catch block
				String error = "WARNING: Illegal memory address detected. No operation will "
						+ "be performed.";
				this.IO.write(error);
				trace.errorOccured(error);
			}
			break;
		}
		case SHL: {
			try {
				BitField result = this.reg.getReg(opInfo.rIndex).shiftLeft(
						sWord.toDecInt());
				this.reg.setReg(result, opInfo.rIndex);
			} catch (IllegalArgumentException e) {
				String error = "WARNING: Illegal shift amount detected. No operation will "
						+ "be performed.";
				this.IO.write(error);
				trace.errorOccured(error);
			}
			break;
		}
		case SHR: {
			try {
				BitField result = this.reg.getReg(opInfo.rIndex).shiftRight(
						sWord.toDecInt());
				this.reg.setReg(result, opInfo.rIndex);
			} catch (IllegalArgumentException e) {
				String error = "WARNING: Illegal shift amount detected. No operation will "
						+ "be performed.";
				this.IO.write(error);
				trace.errorOccured(error);
			}
			break;
		}
		case IO: {
			try {
				this.performIO(opInfo, sWord);
			} catch (NumberFormatException e) {
				String error = "WARNING: Invalid number entered. No operation will "
						+ "be performed.";
				this.IO.write(error);
				trace.errorOccured(error);
			} catch (IllegalMemoryAddressException e) {
				String error = "WARNING: Illegal memory address detected. No operation will "
						+ "be performed.";
				this.IO.write(error);
				trace.errorOccured(error);
			}
			break;
		}
		case BR: {
			try {
				this.performBranchUnconditional(opInfo, sWord);
			} catch (IllegalMemoryAddressException e) {
				String error = "WARNING: Illegal memory address detected. No operation will "
						+ "be performed.";
				this.IO.write(error);
				trace.errorOccured(error);
			} catch (HaltException e) {
				trace.update();
				e.setTrace(trace.toString());
				throw e;
			}
			break;
		}
		case BRZ: {
			if (this.reg.getReg(opInfo.rIndex).isZero()) {
				try {
					this.reg.setPC(sWord);
				} catch (IllegalMemoryAddressException e) {
					String error = "WARNING: Illegal memory address detected. No operation will "
							+ "be performed.";
					this.IO.write(error);
					trace.errorOccured(error);
				}
			}
			break;
		}
		case BRN: {
			if (!this.reg.getReg(opInfo.rIndex).isPositive()) {
				try {
					this.reg.setPC(sWord);
				} catch (IllegalMemoryAddressException e) {
					String error = "WARNING: Illegal memory address detected. No operation will "
							+ "be performed.";
					this.IO.write(error);
					trace.errorOccured(error);
				}
			}
			break;
		}
		case BRS: {
			this.reg.storePC(opInfo.rIndex);
			try {
				this.reg.setPC(sWord);
			} catch (IllegalMemoryAddressException e) {
				String error = "WARNING: Illegal memory address detected. No operation will "
						+ "be performed.";
				this.IO.write(error);
				trace.errorOccured(error);
			}
		}
			break;
		case NOP: {
			break;

		}
		}
		trace.update();
		return trace;
	}

	/**
	 * Description: further evaluates and executes the opInfo for opcode BR by
	 * evaluating the R part of the instruction. Changes the value of the
	 * program counter and/or IO as specified in the Machine Instructions as
	 * based on the R field. If it is not able to perform the BR as specified, a
	 * HaltExeption, IOException, or IllegalMemoryAddresException will be
	 * thrown.
	 * 
	 * @requires true
	 * @alters the registers and/or IO specified by the Machine Instructions
	 *         based off the R value of the opInfo under the BR Assembler
	 *         instruction.
	 * @ensures opInfo and sWord are unchanged.
	 * @param opInfo
	 *            - the instruction information being evaluated and executed
	 * @param sWord
	 *            - word used to change the value of the program counter as
	 *            specified in the Machine Instructions.
	 * @throws HaltException
	 * @throws IOException
	 * @throws IllegalMemoryAddressException
	 */
	private void performBranchUnconditional(InstructionInfo opInfo,
			BitField sWord) throws HaltException, IOException,
			IllegalMemoryAddressException {
		//choosing R value
		switch (opInfo.rIndex) {
		case 0: {
			this.halt(opInfo.xIndex);
			break;
		}
		case 1: {
			this.IO.write(this.reg.toString() + this.mem.toString());
			this.reg.setPC(sWord);
			break;
		}
		case 2: {
			this.reg.indexPC(sWord);
			break;
		}
		case 3: {
			this.reg.setPC(sWord);
			break;
		}
		}

	}

	/**
	 * Description: further evaluates and executes the instruction based on the
	 * xIndex of the instruction according to the specifications in the Machine
	 * Instructions. It changes IO accordingly.
	 * 
	 * @requires true
	 * @alters IO according to the Machine Instructions for BR with R = 0
	 * @ensures xIndex is unchanged
	 * @param xIndex
	 *            - x part of the instruction being evaluated.
	 * @throws HaltException
	 * @throws IOException
	 */
	private void halt(int xIndex) throws HaltException, IOException {
		switch (xIndex) {
		case 0: {
			// quiet halt
			break;
		}
		case 1: {
			this.IO.write(this.mem.toString());
			break;
		}
		case 2: {
			this.IO.write(this.reg.toString());
			break;
		}
		case 3: {
			this.IO.write(this.reg.toString() + this.mem.toString());
			break;
		}
		}
		throw new HaltException();

	}

	/**
	 * Description: further evaluates and executes an IO instruction based on
	 * the R value of the instruction. IO, Registers, and/or Memory are changed
	 * according to the description in the Machine Instructions for IO.
	 * 
	 * @requires true
	 * @alters IO, Registers, and/or Memory according to the Machine
	 *         Instructions based on the R value of the instruction.
	 * @ensures opInfo, sWord are unchanged.
	 * @param opInfo
	 *            - the instruction information being evaluated and executed
	 * @param sWord
	 *            - value of S(X) in the Machine Instructions.
	 * @throws IOException
	 * @throws EOFException
	 * @throws IllegalMemoryAddressException
	 * @throws NumberFormatException
	 */
	private void performIO(InstructionInfo opInfo, BitField sWord)
			throws IOException, EOFException, IllegalMemoryAddressException,
			NumberFormatException {
		//choosing io instructions 
		switch (opInfo.rIndex) {
		case 0: {
			while (this.IO.nextChar() != -1
					&& CharChecker.isSeparator(this.IO.nextChar())) {
				this.IO.read();
			}
			if (this.IO.nextChar() == -1) {
				throw new EOFException();
			}
			StringBuffer line = new StringBuffer();
			while (this.IO.nextChar() != -1
					&& !CharChecker.isSeparator(this.IO.nextChar())) {
				line.append((char) this.IO.nextChar());
				this.IO.read();
			}
			String lineString = line.toString();
			if (line.charAt(0) == '+') {
				lineString = line.substring(1);
			}
			int value = Integer.parseInt(lineString);
			if (BitField.isValid2sComp(value, MachineInfo.BITS_IN_WORD)) {
				boolean neg = value < 0;
				BitField w = new BitField(Math.abs(value),
						MachineInfo.BITS_IN_WORD);
				if (neg) {
					w = w.negate();
				}
				this.mem.addWord(w, sWord.toDecInt());
			}
			break;
		}
		case 1: {
			if (this.IO.nextChar() == -1) {
				throw new EOFException();
			}
			BitField b = new BitField(this.IO.nextChar(),
					MachineInfo.BITS_IN_ADDRESS);
			this.reg.storeIOByte(opInfo.xIndex, b);
			this.IO.read();
			break;

		}
		case 2: {
			int twosComp = this.mem.getWordAtAddr(sWord.toDecInt())
					.toDecInt2sComp();
			this.IO.write(Integer.toString(twosComp));
			break;
		}
		case 3: {
			this.IO.writeByte(this.reg.getSigIOByte(opInfo.xIndex));
			break;
		}
		}
	}
}