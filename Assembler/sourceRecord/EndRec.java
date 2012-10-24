package sourceRecord;

/**
 * Implements SourceRecord according to the specifications of the END pseudo op
 * (See Users Guide). It has an immutable constructor. An EndRec has 4 fields:
 * string label, Operand operand, string fullLine, and int location.
 * 
 * @author Dragon Slayer
 */
public class EndRec implements SourceRecord {

	String label;
	Operand operand;
	String fullLine; //The source record
	int location;

	/**
	 * Description: Creates a SourceRecord with dynamic type EndRec. The EndRec
	 * fields label, fullLine, and location are set to the value of the
	 * parameters passed in. The EndRec operand field is set to have the value
	 * of the operand with lower limit 0 and upper limit 255 (decimal) If the
	 * constructor is unable to create the EndRec an InvalidRecordException is
	 * thrown.
	 * 
	 * @requires true
	 * @ensures Creates an immutable SourceRecord of dynamic type endRec
	 * @param label
	 *            the label from the assembly file
	 * @param operand
	 *            operand from the assembly record
	 * @param fullLine
	 *            the complete line from the assembly file
	 * @param location
	 *            the location counter value associated with the SourceRecord
	 * @throws InvalidRecordException
	 */
	public EndRec(String label, String operand, String fullLine, int location)
			throws InvalidRecordException {
		this.label = label;
		if (operand != null) {
			if (operand.equals("-1")) {
				throw new InvalidRecordException(
						"Error_Code 301: Operand value must be between 0 and 255");
			}
		}
		if (operand ==null) {
			operand = "-1";
		}
		try {
			this.operand = new Operand(operand, -1, 255);
		} catch (InvalidRecordException e) {
			throw new InvalidRecordException(
					"Error_Code 301: Operand value must be between 0 and 255");
		}
		this.fullLine = fullLine;
		this.location = location;
	}

	@Override
	public RecordType getType() {
		return RecordType.END;
	}

	@Override
	public int getLocOffset() {
		return 0;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void addLabel(SymbolTable labels)
			throws LabelAlreadyDefinedException, InvalidRecordException {
		labels.addLabel(label, new LabelValue(location, false));

	}

	@Override
	public void replaceLabels(SymbolTable labels)
			throws InvalidRecordException, LabelNotDefinedException {
		if (operand.hasLabel()) {
			operand.replaceLabel(labels);
		}

	}

	@Override
	public void addLiteral(LiteralPool literals) {
		// Do nothing, no literals in End

	}

	/**
	 * Description: returns the value field of the Operand it is called on,
	 * which is the integer value of the execution start address. If END does
	 * not have an operand, -1 is returned
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures the Operand is unchanged
	 * @return the value field of the Operand it is called on, which is the
	 *         integer value of the execution start address, or -1 if END
	 *         doesn't have an Operand.
	 */
	public int getExecStart() {
		return operand.getValue();
	}

	@Override
	public String toExecutableString(Boolean absolute) {
		return "";
	}

	@Override
	public String toListingString(Boolean absolute, int line) {
		return "               " + String.format("%03d " + fullLine + '\n', line);

	}

}
