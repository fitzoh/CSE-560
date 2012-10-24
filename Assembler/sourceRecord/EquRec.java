package sourceRecord;

/**
 * Implements SourceRecord to the specifications of the EQU pseudo op. It has an immutable 
 * constructor. An EquRec has 4 fields: string label, Operand operand, string fullLine, 
 * and int location. 
 * @author Dragon Slayer
 */
public class EquRec implements SourceRecord {
	String label;
	Operand operand;
	String fullLine; //The source record
	int location;

	/**
	 * Description: Creates a SourceRecord with dynamic type EquRec. The EquRec fields 
	 * label, fullLine, and location are set to the value of the parameters passed in. 
	 * The EquRec operand field is set to have the value of the operand with lower limit 
	 * 0 and upper limit 255 (decimal) If the constructor is unable to create the EquRec 
	 * an InvalidRecordException is thrown.
	 * @requires true
	 * @ensures Creates an immutable SourceRecord of dynamic type EquRec
	 * @param label the label from the assembly file
	 * @param operand operand from the assembly record
	 * @param fullLine the complete line from the assembly file
	 * @param location the location counter value associated with the SourceRecord
	 * @throws InvalidRecordException
	 */
	public EquRec(String label, String operand, String fullLine, int location) throws InvalidRecordException {
		this.label = (label+' ').substring(0,(label+' ').indexOf(' '));
		this.operand = new Operand(operand,0,255);
		this.location=location;
		this.fullLine=fullLine;
	}

	@Override
	public RecordType getType() {
		return RecordType.EQU;
	}

	@Override
	public int getLocOffset() {
		return 0; //The LC won't increase
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void addLabel(SymbolTable labels) throws LabelAlreadyDefinedException, LabelNotDefinedException, InvalidRecordException {
		if(operand.hasLabel()){
			labels.addLabel(label, new LabelValue(labels.getLabel(operand.getLabel()), labels.isAbsolute(operand.getLabel())));
		}
		else{
		labels.addLabel(label, new LabelValue(operand.getValue(), true));
		}
	}

	@Override
	public void replaceLabels(SymbolTable labels) throws InvalidRecordException, LabelNotDefinedException {
		operand.replaceLabel(labels);
		
	}

	@Override
	public void addLiteral(LiteralPool literals) {
		// Do nothing, no literals in EQU
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
