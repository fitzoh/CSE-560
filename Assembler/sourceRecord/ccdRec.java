package sourceRecord;

/**
 * Implements SourceRecord according to the specifications of the CCD 
 * pseudo op (See Users Guide). It contains an immutable constructor 
 * and one private method. 
 * @author Dragon Slayer
 */
public class ccdRec implements SourceRecord {
	String label;
	String operand;
	String fullLine; //the source record
	int location;
	
	/**
	 * Description: Creates a SourceRecord with dynamic type ccdRec. The ccdRec fields 
	 * are set to the value of the parameters passed in. If the constructor is 
	 * unable to create an ccdRec, an InvalidRecordException is thrown.
	 * @requires true
	 * @ensures Creates an immutable SourceRecord of dynamic type ccdRec
	 * @param label the label from the assembly file
	 * @param operand operand from the assembly record
	 * @param fullLine the complete line from the assembly file
	 * @param location the location counter value associated with the SourceRecord
	 * @throws InvalidRecordException
	 */
	public ccdRec(String label, String operand, String fullLine, int location)
			throws InvalidRecordException {
		this.label = (label+' ').substring(0,(label+' ').indexOf(' '));
			this.operand=operand;
		this.fullLine = fullLine;
		this.location=location;
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
	public void addLabel(SymbolTable labels) throws LabelAlreadyDefinedException, InvalidRecordException {
		if(label!=null){
			labels.addLabel(label, new LabelValue(location, false));
		}
	}

	@Override
	public void replaceLabels(SymbolTable labels) {
		// Do nothing, no labels need to be replaced here

	}

	@Override
	public void addLiteral(LiteralPool literals) {
		// Do nothing, no literals in CCD

	}

	/**
	 * Description: This method uses the operand field to create the hex string value 
	 * of the object code instruction.
	 * @requires true
	 * @alters Creates the object code instruction in hex
	 * @ensures the ccdRec is unchanged 
	 * @return The object code instruction in hex
	 */
	private String hexString(){
		return String.format("%02X", (int)operand.charAt(0))+String.format("%02X", (int)operand.charAt(1))+0;
	}
	@Override
	public String toExecutableString(Boolean absolute) {
		return 'T'+String.format("%02X", location)+hexString()+"\n";
	}

	@Override
	public String getLabel() {
		return label;
	}


	@Override
	public String toListingString(Boolean absolute, int line) {
		return String.format("%-15s",String.format("%02X ", location)+hexString())+String.format("%03d "+fullLine+'\n', line);
	}


}
