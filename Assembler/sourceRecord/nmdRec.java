package sourceRecord;

/**
 * Implements SourceRecord according to the specifications of the NMD pseudo op 
 * (See Users Guide). It has an immutable constructor and one private method. 
 * A nmdRec has 4 fields: string label, Operand operand, string fullLine, and 
 * int location. 
 * @author Dragon Slayer
 */
public class nmdRec implements SourceRecord {
	
	String label;
	Operand operand;
	String fullLine; //The source record
	int location;
	
	/**
	 * Description: Creates a SourceRecord with dynamic type nmdRec. The nmdRec fields 
	 * label, fullLine, and location are set to the value of the parameters passed in. 
	 * The GtcRec Operand field is set to have the value of the operand with lower limit 
	 * ((-1)*2^19) and upper limit ((2^19) - 1) (decimal). If the constructor is unable 
	 * to create the nmdRec an InvalidRecordException is thrown.
	 * @requires true
	 * @ensures Creates an immutable SourceRecord of dynamic type nmdRec
	 * @param label the label from the assembly file
	 * @param operand operand from the assembly record
	 * @param fullLine the complete line from the assembly file
	 * @param location the location counter value associated with the SourceRecord
	 * @throws InvalidRecordException
	 */
	public nmdRec(String label, String operand, String fullLine, int location) throws InvalidRecordException {
		this.label = (label+' ').substring(0,(label+' ').indexOf(' '));
		this.location=location;
		this.fullLine=fullLine;
		this.operand= new Operand(operand,(-1*(int)Math.pow(2, 19)),(int)Math.pow(2, 19)-1);
	}

	@Override
	public RecordType getType() {
		return RecordType.OTHER;
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
		labels.addLabel(label, new LabelValue(location,false));
		
	}

	@Override
	public void replaceLabels(SymbolTable labels) throws InvalidRecordException, LabelNotDefinedException {
		operand.replaceLabel(labels);
		
	}

	@Override
	public void addLiteral(LiteralPool literals) {
		// Do nothing, no literals in NMD
		
	}
	
	/**
	 * Description: This method uses the operand field to create the hex string value 
	 * of the object code instruction.
	 * @requires true
	 * @alters Creates the object code instruction in hex
	 * @ensures the ccdRec is unchanged 
	 * @return The object code instruction in hex
	 */
	public String hexString(){
		return String.format("%08X", operand.getValue()).substring(3); //Format the output string
	}

	@Override
	public String toExecutableString(Boolean absolute) {
		return 'T'+String.format("%02X", location)+hexString()+'\n';
	}

	@Override
	public String toListingString(Boolean absolute, int line) {		
		return String.format("%-15s",String.format("%02X ", location)+hexString())+String.format("%03d "+fullLine+'\n', line);
	}

}
