package sourceRecord;

/**
 * Implements SourceRecord according to the specification of the ENT pseudo op. It has 
 * an immutable constructor. An entRec has 5 fields: A String array to hold the names of 
 * the Operands, an integer array to hold the values of the operands, a string for the 
 * complete line read in from the assembly file, an integer for the number of operands, 
 * and an int for the address location of the SourceRecord.
 * @author Dragon Slayer
 */
public class entRec implements SourceRecord {
	String[] ops;
	int[] vals;
	String fullLine;
	int numOps;
	int location;

	/**
	 * Description: Creates a SourceRecord with dynamic type entRec. The entRec fields 
	 * label, fullLine, and location are set to the value of the parameters record and 
	 * location counter passed in. numOps is set to the number of operands passed in. 
	 * The entRec ops size is set to numOps and each position in the array is filled 
	 * with an operand name passed in. The size of vals is also set to numOps.
	 * @requires true
	 * @alters N/A
	 * @ensures creates an immutable SourceRecord of dynamic type entRec
	 * @param op1 name of first operand
	 * @param op2 name of second operand
	 * @param op3 name of third operand
	 * @param op4 name of fourth operand
	 * @param record the complete line from the assembly file
	 * @param location the location counter value associated with the SourceRecord
	 */
	public entRec(String op1, String op2, String op3, String op4,
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
		ops = new String[size];
		numOps=size;
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
		vals = new int[numOps];
		this.fullLine=record;
		this.location=location;
	}

	@Override
	public RecordType getType() {
		return RecordType.ENT;
	}

	@Override
	public int getLocOffset() {
		return 0;
	}

	@Override
	public String getLabel() {
		//no label for ent;
		return null;
	}

	@Override
	public void addLabel(SymbolTable labels)
			throws LabelAlreadyDefinedException, LabelNotDefinedException,
			InvalidRecordException {
		// TODO Auto-generated method stub

	}

	@Override
	public void replaceLabels(SymbolTable labels)
			throws InvalidRecordException, LabelNotDefinedException {
		for(int i=0;i<numOps;i++){
			try{
			vals[i]=labels.getLabel(ops[i]);
			} catch(LabelNotDefinedException e){
				throw new InvalidRecordException("Error_Code 314: Undefined entry");
			}
		}

	}

	@Override
	public void addLiteral(LiteralPool literals) throws InvalidRecordException {
		// Do nothing, no literals possible

	}

	@Override
	public String toExecutableString(Boolean absolute) {
		String result ="";
		for(int i=0;i<numOps;i++){
			result+="E"+String.format("%-6s", ops[i])+String.format("%02X", vals[i])+'\n';
		}
		return result;
	}

	@Override
	public String toListingString(Boolean absolute, int line) {
		return "               "+String.format("%03d "+fullLine+'\n', line);
	}

}
