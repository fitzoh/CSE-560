package sourceRecord;

/**
 * Implements SourceRecord according to the specification of the EXT pseudo op. It has 
 * an immutable constructor. An extRec has 4 fields: A String array to hold the names of 
 * the Operands, a string for the complete line read in from the assembly file, an integer 
 * for the number of operands, and an int for the address location of the SourceRecord.
 * @author Dragon Slayer
 */
public class extRec implements SourceRecord {
	String[] ops;
	int numOps;
	String fullLine;
	int location;

	/**
	 * Description: Creates a SourceRecord with dynamic type extRec. The extRec fields 
	 * label, fullLine, and location are set to the value of the parameters record and 
	 * location counter passed in. numOps is set to the number of operands passed in. The 
	 * extRec ops size is set to numOps and each position in the array is filled with an 
	 * operand name passed in. 
	 * @requires true
	 * @alters N/A
	 * @ensures creates an immutable SourceRecord of dynamic type extRec
	 * @param op1 name of first operand
	 * @param op2 name of second operand
	 * @param op3 name of third operand
	 * @param op4 name of fourth operand.
	 * @param record the complete line from the assembly file
	 * @param location the location counter value associated with the SourceRecord
	 */
	public extRec(String op1, String op2, String op3, String op4,
			String record, int location) {
		int size=1;
		//checking op
		if(op2!=null){
			size++;
		}
		if(op3!=null){
			size++;
		}
		if(op4!=null){
			size++;
		}
		numOps=size;
		ops = new String[size];
		ops[0]=op1;
		if(size>1){
			ops[1]=op2;
		}
		if(size>2){
			ops[2]=op3;
		}
		if(size>3){
			ops[3]=op4;
		}
		this.fullLine=record;
		this.location=location;
	}

	@Override
	public RecordType getType() {
		return RecordType.EXT;
	}

	@Override
	public int getLocOffset() {
		return 0;
	}

	@Override
	public String getLabel() {
		//no label for ext
		return null;
	}

	@Override
	public void addLabel(SymbolTable labels)
			throws LabelAlreadyDefinedException, LabelNotDefinedException,
			InvalidRecordException {
		for(int i=0;i<numOps;i++){
		labels.addLabel(ops[i], new LabelValue(-1,true));
		}

	}

	@Override
	public void replaceLabels(SymbolTable labels)
			throws InvalidRecordException, LabelNotDefinedException {
		// do nothing, no labels to replace

	}

	@Override
	public void addLiteral(LiteralPool literals) throws InvalidRecordException {
		// do nothing, no literals possible

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
