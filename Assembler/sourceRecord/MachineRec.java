package sourceRecord;

import utilities.InstructionGenerator;

/**
 * Implements SourceRecord according to the needed fields of a Machine 
 * Instruction (See User's Guide.). Contains implementations for all the methods in 
 * SourceRecord. It also contains an immutable constructor and one private class. 
 * A MachineRec has 7 fields: String label, String op, String fullLine, Operand r, 
 * Operand s, Operand x, and int location. 
 * @author Dragon Slayer
 */
public class MachineRec implements SourceRecord {
	String label;
	String op;
	String fullLine; //The source record
	Operand r;
	Operand s;
	Operand x;
	int location;

	/**
	 * Description: Creates a SourceRecord with dynamic type MachineRec. The MachineRec 
	 * fields label, fullLine, op, and location are set to the value of the parameters 
	 * passed in. The MachineRec r field is set to have the value of the r operand with 
	 * lower limit 0 and upper limit 3 (decimal). The MachineRec s field is set to have 
	 * the value of the s operand with lower limit 0 and upper limit 255 (decimal).  
	 * The MachineRec x field is set to have the value of the s operand with lower limit 
	 * 0 and upper limit 3 (decimal). If the constructor is unable to create the MachineRec  
	 * an InvalidRecordException is thrown.
	 * @requires true
	 * @ensures Creates an immutable SourceRecord of dynamic type MachineRec
	 * @param label the label from the assembly file
	 * @param op operand from the assembly record
	 * @param r string being used to set the Operand r field
	 * @param s sting being used to set the Operand s field
	 * @param x string being used to set the Operand x field
	 * @param fullLine the complete line from the assembly file
	 * @param location the location counter value associated with the SourceRecord
	 * @throws InvalidRecordException
	 */
	public MachineRec(String label, String op, String r, String s, String x,
			String fullLine, int location) throws InvalidRecordException {
		this.label = (label+' ').substring(0,(label+' ').indexOf(' '));
		this.op = op;
		this.r = new Operand(r, 0, 3);
		this.s = new Operand(s, -1, 255);
		if(x==null){
		x="0";
		}
		this.x = new Operand(x, 0, 3);
		this.fullLine=fullLine;
		this.location = location;		
		if(this.op.equals("LDI")||this.op.equals("ST ")||this.op.equals("BR ")||this.op.equals("SHL")||this.op.equals("SHR")||this.op.equals("IO ")||this.op.equals("BR ")||this.op.equals("BRZ")||this.op.equals("BRN")||this.op.equals("BRS")){
			//The literals are not allowed in LDI, store, Shift, br, or IO
			if(s.charAt(0)=='='){
				throw new InvalidRecordException("Error_Code 311: Literals are not allowed in LDI, ST, SHL, SHR, BR, or IO instructions");
			}
		}
	}

	@Override
	public RecordType getType() {
		return RecordType.EXEC;
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
		//Do nothing if the label contains only spaces
		if (!label.matches(" {6}")) {
			labels.addLabel(label, new LabelValue(location, false));
		}
	}

	@Override
	public void replaceLabels(SymbolTable labels) throws InvalidRecordException, LabelNotDefinedException {
		r.replaceLabel(labels);
		s.replaceLabel(labels);
		x.replaceLabel(labels);
		if(!x.isAbsolute()){
			throw new InvalidRecordException("Error_Code 308: X must not be relative");
		}
		if(!r.isAbsolute()){
			throw new InvalidRecordException("Error_Code 309: R must not be relative");
		}

	}

	@Override
	public void addLiteral(LiteralPool literals) throws InvalidRecordException {
		if(s.hasLabel()){
		if(s.getLabel().charAt(0)=='='){
			literals.addLiteral(s.getLabel());
		}
		}

	}

	/**
	 * Description: This method uses the operand field to create the hex string value of the 
	 * object code instruction. If the MachineRec is relocatable, an 'M' is printed on the 
	 * end of the string being created. If the MachineRec contains an external symbol. �X� 
	 * and then the symbol name is printed on the end of the string being created.
	 * @requires true
	 * @alters Creates the object code instruction in hex
	 * @ensures the ccdRec is unchanged 
	 * @param absolute - flags if the operand is absolute or not
	 * @return : If the MachineRec is relocatable, an 'M' is printed on the end of result. 
	 * If the MachineRec contains an external symbol. �X� and then the symbol name is printed
	 * on the end of result.
	 */
	private String hexString(Boolean absolute){
		String result = InstructionGenerator.genInstruction(op, r.getValue(), s.getValue(), x.getValue());
		if (s.getValue()==-1){
			result+= 'X'+s.getLabel();
		}
		else if(!absolute){
			if(!s.isAbsolute())
			result+='M';
		}
		return result;
	}
	@Override
	public String toListingString(Boolean absolute, int line) {		
		return String.format("%-15s",String.format("%02X ", location)+hexString(absolute))+String.format("%03d "+fullLine+'\n', line);
	}

	@Override
	public String toExecutableString(Boolean absolute) {
		return 'T'+String.format("%02X", location)+hexString(absolute)+"\n";
	}

}
