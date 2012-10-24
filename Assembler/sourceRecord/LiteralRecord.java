package sourceRecord;

/**
 * Implements SourceRecord. Provides a way to produce records in memory for literals 
 * in the literal table. Contains implementations for all the methods in SourceRecord. 
 * It also contains an immutable constructor and one private class. A LiteralRecord has 
 * 3 fields: String valueStr, int valueInt, Integer location. valueStr is the name of 
 * the literal and valueInt is the value assigned to the literal. Location will be the 
 * record line number from the assembly file. 
 * @author Dragon Slayer
 */
public class LiteralRecord implements SourceRecord {

	String valueStr;
	int valueInt;
	int location;
	
	/**
	 * Creates a SourceRecord with dynamic type LiteralRecord. The LiteralRecord 
	 * field location is set to location which is passed in. The LiteralRecord 
	 * field valueStr is set to the value of value which is passed in. The 
	 * LiteralRecord valueInt field is set to have the numeric value of the 
	 * valueStr. If the constructor is unable to create the LiteralRecrd  an 
	 * InvalidRecordException is thrown.
	 * @requires true
	 * @ensures Creates an immutable SourceRecord of dynamic type LiteralRecord
	 * @param value the name of the literal
	 * @param location the location counter value associated with the SourceRecord
	 */
	public LiteralRecord(String value, Integer location) {
		this.valueStr=value;
		this.valueInt=Integer.parseInt(value.substring(1)); //Ignore the "=" in literal
		this.location=location;
	}

	@Override
	public RecordType getType() {
		return RecordType.LITERAL;
	}

	@Override
	public int getLocOffset() {
		return 1;
	}

	@Override
	public String getLabel() {
		return null;
	}

	@Override
	public void addLabel(SymbolTable labels) throws LabelAlreadyDefinedException {
		// Do nothing

	}

	@Override
	public void replaceLabels(SymbolTable labels) throws InvalidRecordException,
			LabelNotDefinedException {
		// Do nothing

	}

	@Override
	public void addLiteral(LiteralPool literals) {
		// Do nothing

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
		String hex =  String.format("%05X", valueInt);
		return hex.substring(hex.length()-5);
	}
	@Override
	public String toExecutableString(Boolean absolute) {
		return 'T'+String.format("%02X", location)+hexString()+'\n';
	}

	@Override
	public String toListingString(Boolean absolute, int line) {
		return String.format("%02X", location)+" \""+valueStr+"\"\n";
	}

}
