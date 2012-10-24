package sourceRecord;

import utilities.InstructionGenerator;

/**
 * Implements SourceRecord according to the specifications of the GTC synthetic 
 * instruction. Contains implementations for all the methods in SourceRecord. 
 * It also contains an immutable constructor and one private class. A GtcRec has 4 
 * fields: string label, Operand operand, string fullLine, and int location
 * @author Dragon Slayer
 */
public class GtcRec implements SourceRecord {
	String label;
	Operand operand;
	String fullLine;
	int location;
	
	/**
	 * Description: Creates a SourceRecord with dynamic type GtcRec. The GtcRec 
	 * fields label, fullLine, and location are set to the value of the parameters 
	 * passed in. The GtcRec Operand field is set to have the value of the operand 
	 * with lower limit 0 and upper limit 3 (decimal). If the constructor is unable 
	 * to create the GtcRec  an InvalidRecordException is thrown.
	 * @requires true
	 * @ensures Creates an immutable SourceRecord of dynamic type GtcRec
	 * @param label the label from the assembly file
	 * @param operand operand from the assembly record
	 * @param fullLine the complete line from the assembly file
	 * @param location the location counter value associated with the SourceRecord
	 * @throws InvalidRecordException
	 */
	public GtcRec(String label, String operand, String fullLine, int location) throws InvalidRecordException {
		this.label = (label+' ').substring(0,(label+' ').indexOf(' '));
		this.operand = new Operand(operand,0,3);
		this.location=location;
		this.fullLine=fullLine;
	}

	@Override
	public RecordType getType() {
		return RecordType.EXEC;
	}

	@Override
	public int getLocOffset() {
		return 1;
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
		if(!operand.isAbsolute()){
			throw new InvalidRecordException("Error_Code 308: X must not be relocatable");
		}
		
	}

	@Override
	public void addLiteral(LiteralPool literals) {
		// Do nothing, no literals in GTC
		
	}

	/**
	 * Description: This method uses the operand field to create the hex string value 
	 * of the object code instruction.
	 * @requires true
	 * @alters Creates the object code instruction in hex
	 * @ensures the ccdRec is unchanged 
	 * @return The object code instruction in hex
	 */
	private String hexString(Boolean absolute){
		return InstructionGenerator.genInstruction("IO ", 1, 0, operand.getValue());
	}

	@Override
	public String toExecutableString(Boolean absolute) {
		return String.format("T%02X", location)+hexString(absolute)+'\n';
	}

	@Override
	public String toListingString(Boolean absolute, int line) {
		return String.format("%-15s",String.format("%02X ", location)+hexString(absolute))+String.format("%03d "+fullLine+'\n', line);
	}

}
