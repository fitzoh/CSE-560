package sourceRecord;

import utilities.InstructionGenerator;

/**
 * Implements SourceRecord according to the specifications of the RET synthetic instruction 
 * (See User's Guide). It has an immutable constructor and one private method. A RetRec 
 * has 4 fields: string label, Operand operand, string fullLine, and int location
 * @author Dragon Slayer
 */
public class RetRec implements SourceRecord {
	String label;
	Operand operand;
	String fullLine; //The source record
	int location;

	/**
	 * Description: Creates a SourceRecord with dynamic type RetRec. The RetRec 
	 * fields label, fullLine, and location are set to the value of the parameters 
	 * passed in. The RetRec Operand field is set to have the value of the operand 
	 * with lower limit 0 and upper limit 3 (decimal). If the constructor is unable 
	 * to create the RetRec an InvalidRecordException is thrown.
	 * @requires true
	 * @ensures Creates an immutable SourceRecord of dynamic type ResRec
	 * @param label the label from the assembly file
	 * @param operand operand from the assembly record
	 * @param fullLine the complete line from the assembly file
	 * @param location the location counter value associated with the SourceRecord
	 * @throws InvalidRecordException
	 */
	public RetRec(String label, String operand, String fullLine, int location) throws InvalidRecordException {
		this.label = (label+' ').substring(0,(label+' ').indexOf(' '));
		//The operand should be in the range of [1, 3]
		this.operand = new Operand(operand, 1,3);
		this.fullLine=fullLine;
		this.location=location;
	}

	@Override
	public RecordType getType() {
		return RecordType.EXEC;
	}

	@Override
	public int getLocOffset() {
		return 1; //The LC will increase by 1
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void addLabel(SymbolTable labels) throws LabelAlreadyDefinedException, InvalidRecordException {
		labels.addLabel(label, new LabelValue(location, false));
	}

	@Override
	public void replaceLabels(SymbolTable labels) throws InvalidRecordException, LabelNotDefinedException {
		operand.replaceLabel(labels);
		//The operand must not be relocatable
		if(!operand.isAbsolute()){
			throw new InvalidRecordException("Error_Code 308: X must not be relocatable");
		}
	}

	@Override
	public void addLiteral(LiteralPool literals) {
		//Do nothing, no literals in RET
	}

	@Override
	public String toExecutableString(Boolean absolute) {
		return 'T'+String.format("%02X", location)+hexString(absolute)+'\n';
	}

	@Override
	public String toListingString(Boolean absolute, int line) {
		return String.format("%-15s",String.format("%02X ", location)+hexString(absolute))+String.format("%03d "+fullLine+'\n', line);
	}

	/**
	 * Description: This method uses the operand field to create the hex string value 
	 * of the object code instruction.
	 * @requires true
	 * @alters Creates the object code instruction in hex
	 * @ensures the ccdRec is unchanged 
	 * @param absolute - flags if the operand is absolute or not
	 * @return The object code instruction in hex
	 */
	private String hexString(boolean absolute) {
		String result = InstructionGenerator.genInstruction("BR ", 3, 0, operand.getValue());
		return result;
	}

}
