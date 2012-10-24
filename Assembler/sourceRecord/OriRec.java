package sourceRecord;


/**
 * Implements SourceRecord according to the specifications of the ORI pseudo op 
 * (See Users Guide). It has an immutable constructor, a method to set the execution 
 * start address, a method to set the segment length, and a method to get the load 
 * address of the segment. An OriRec has 6 fields: String label, String fullLine, 
 * int loadAddress, int segmentLength, int execAddress, and boolean absolute.
 * @author Dragon Slayer
 */
public class OriRec implements SourceRecord {
	private String label;
	private String fullLine; //The source record
	private int loadAddress;
	private int segmentLength;
	private int execAddress;
	private boolean absolute; //True iff the program is absolute

	/**
	 * Description: Creates a SourceRecord with dynamic type OriRec. The OriRec 
	 * fields label, location, and fullLine are set to the corresponding values 
	 * passed in. If String operand passed in is null, then the field loadAddress 
	 * is set to zero and the field absolute is set to false. Otherwise, loadAddress 
	 * is set to the integer value of the operand and absolute is set to true. If the 
	 * loadAddress is less than 0 or greater than 255 then an InvalidRecordExcption 
	 * is thrown.
	 * @requires true
	 * @ensures Creates an immutable SourceRecord of dynamic type OriRec with the fields 
	 * label, fullLine, loadAddress, and absolute set or an InvalidRecordException is thrown
	 * @param label the label from the assembly file
	 * @param operand operand from the assembly record
	 * @param fullLine the complete line from the assembly file
	 * @throws InvalidRecordException
	 */
	public OriRec(String label, String operand, String fullLine)
			throws InvalidRecordException {
		if (!label.matches(" {6}")) {
			this.label = (label+' ').substring(0,(label+' ').indexOf(' '));
		}
		else{
			throw new InvalidRecordException("Error_Code 302: ORI must have a label");
		}
		if (operand != null) {
				loadAddress = Integer.parseInt(operand);
				absolute = true;
				if (loadAddress < 0 || loadAddress > 255) {
					throw new InvalidRecordException("Error_Code 303: If ORI has an operand, it must be between 0 and 255");
				}
		}
		else{
			loadAddress = 0;
			absolute = false;
		}
		this.fullLine=fullLine;
	}

	/**
	 * Description: Sets the OriRec field execAddress to the decimal 
	 * value passed in.
	 * @requires true
	 * @alters the execAddress of the OriRec is set
	 * @ensures decimal is unchanged 
	 * @param decimal the value the execAddress is set too
	 * @throws InvalidRecordException 
	 */
	public void setExecStart(int decimal) throws InvalidRecordException{
		execAddress = decimal;
		//The execution starting address must not be less than the load address
		if(execAddress<loadAddress){
			throw new InvalidRecordException("Error_Code 313: Execution start address must be < Segment Load Address");
		}
	}
	
	/**
	 * Description: Sets the OriRec field segmentLength to the decimal 
	 * value passed in.
	 * @requires true
	 * @alters the segmentLength of the OriRec is set
	 * @ensures decimal is unchanged 
	 * @param decimal the value the segmentLength is set too
	 */
	public void setSegmentLength(int decimal){
		//Calculate the length of the segment
		segmentLength = decimal-loadAddress;
	}
	
	/**
	 * Description: Returns the loadAddress of the OriRec
	 * @requires true
	 * @alters N/A
	 * @ensures decimal and loadAddress are unchanged.
	 * @return the loadAddress of the OriRec
	 */
	public int getLoadAddress() {
		return loadAddress;
	}

	@Override
	public RecordType getType() {
		return RecordType.ORI;
	}

	@Override
	public int getLocOffset() {
		return loadAddress;
	}

	@Override
	public void addLabel(SymbolTable labels) throws LabelAlreadyDefinedException, InvalidRecordException {
		if (label != null) {
			labels.addLabel(label, new LabelValue(loadAddress, absolute));
		}

	}

	@Override
	public void replaceLabels(SymbolTable labels) {
		//Do nothing, no labels to replace in ORI

	}

	@Override
	public void addLiteral(LiteralPool literals) {
		//Do nothing, no literals in ORI

	}

	@Override
	public String getLabel() {
		return label;
	}


	@Override
	public String toExecutableString(Boolean absolute) {
		String result = 'H'+String.format("%02X", execAddress)+String.format("%-6s", label)+String.format("%02X", loadAddress)+String.format("%02X", segmentLength);
		if(!absolute){
			result+='M';
		}
		
		return result+'\n';
	}

	@Override
	public String toListingString(Boolean absolute, int line) {
		return "               "+String.format("%03d "+fullLine+'\n', line);
	}

	/**
	 * Description: returns true iff the Symbol is absolute 
	 * @requires true
	 * @alters N/A
	 * @ensures Nothing is changed.
	 * @return true iff the Symbol is absolute
	 */
	public boolean isAbsolute() {
		return absolute;
	}

}
