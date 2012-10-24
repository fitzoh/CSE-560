package sourceRecord;

/**
 * Implements SourceRecord to hold a comment line from the Assembly input file. 
 * It has an immutable constructor. 
 * @author Dragon Slayer
 */
public class CommentLine implements SourceRecord {
	String fullLine;
	int location;

	/**
	 * Description: Creates a SourceRecord with dynamic type CommentLine. The 
	 * CommentLine field's fullLine, and location are set to the value of the 
	 * parameters passed in. If the constructor is unable to create the 
	 * CommentLine an InvalidRecordException is thrown.
	 * @requires true
	 * @ensures Creates an immutable SourceRecord of dynamic type CommentLine
	 * @param record the complete record line from the assembly file
	 * @param location the location counter value associated with the SourceRecord
	 */
	public CommentLine(String record, int location) {
		this.fullLine = record;
		this.location=location;
	}

	@Override
	public RecordType getType() {
		return RecordType.COMMENT;
	}

	@Override
	public int getLocOffset() {
		return 0;
	}

	@Override
	public void addLabel(SymbolTable labels) {
		// Do nothing, no comments in labels
		
	}

	@Override
	public void replaceLabels(SymbolTable labels) {
		// Do nothing, no comments in labels
		
	}

	@Override
	public void addLiteral(LiteralPool literals) {
		// Do nothing, no comments in labels
		
	}

	@Override
	public String getLabel() {
		//No label here
		return null;
	}

	@Override
	public String toExecutableString(Boolean absolute) {
		return "";
	}

	@Override
	public String toListingString(Boolean absolute, int line) {
		return fullLine+'\n';
	}
}
