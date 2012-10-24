package sourceRecord;

/**
 * Implements SourceRecord according to the specification of the RES pseudo op 
 * (See User's Guide). It has an immutable constructor. A ResRec has 4 fields: 
 * string label, Operand operand, string fullLine, and int location
 * @author Dragon Slayer
 */
public class ResRec implements SourceRecord {
	String label;
	Operand operand;
	String fullLine; //The source record
	int location;

	/**
	 * Description: Creates a SourceRecord with dynamic type ResRec. The ResRec 
	 * fields label, fullLine, and location are set to the value of the parameters 
	 * passed in. The ResRec Operand field is set to have the value of the operand 
	 * with lower limit 0 and upper limit 25 (decimal). If the constructor is unable 
	 * to create the PtcRec  an InvalidRecordException is thrown.
	 * @requires true
	 * @ensures Creates an immutable SourceRecord of dynamic type ResRec
	 * @param label the label from the assembly file
	 * @param operand operand from the assembly record
	 * @param fullLine the complete line from the assembly file
	 * @param location the location counter value associated with the SourceRecord
	 * @throws InvalidRecordException
	 */
	public ResRec(String label, String operand, String fullLine, int location) throws InvalidRecordException {

		this.label = (label+' ').substring(0,(label+' ').indexOf(' '));
		//The operand should be in the range of [1, 255]
		this.operand = new Operand(operand, 1,255);
		this.fullLine=fullLine;
		this.location=location;
	}

	@Override
	public RecordType getType() {
		return RecordType.RES;
	}

	@Override
	public int getLocOffset() {
		return operand.getValue();
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
			throw new InvalidRecordException("Error_Code 310: Reserve Operand must not be relocatable");
		}
	}

	@Override
	public void addLiteral(LiteralPool literals) {
		//Do nothing, no literals here
	}


	@Override
	public String toExecutableString(Boolean absolute) {
		return "";
	}

	@Override
	public String toListingString(Boolean absolute, int line) {
		return "               "+String.format("%03d "+fullLine+'\n', line);
	}

}
