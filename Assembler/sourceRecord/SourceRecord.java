package sourceRecord;

/**
 * Interface implemented by machineRec, oriRec, endRec, equRec, nmdRec, ccdRec, 
 * resRec, retRec, gtcRec, ptcRec, CommentLine, and LiteralRec. A SourceRecord 
 * holds all of the information relevant to the type of record. See the individual 
 * dynamic types for more information on each. 
 * @author Dragon Slayer
 */
public interface SourceRecord {

	/**
	 * Description: returns the enum type of the SourceRecord
	 * @requires true
	 * @alters N/A
	 * @ensures nothing is changed
	 * @return the enum type of the SourceRecord
	 */
	public RecordType getType();
	
	/**
	 * Description: returns the location offset of the SourceRecord
	 * @requires true
	 * @alters N/A
	 * @ensures nothing is changed
	 * @return the location offset of the SourceRecord
	 */
	public int getLocOffset();
	
	/**
	 * Description: returns a copy of the label field of the SourceRecord
	 * @requires true
	 * @alters N/A
	 * @ensures nothing is changed
	 * @return a copy of the label field of the SourceRecord 
	 */
	public String getLabel();
	
	/**
	 * Description: This method adds the SourceRecord�s label and its LabelValue 
	 * into the SymbolTable labels
	 * @requires true
	 * @alters the SourceRecord label and its LabelValue are added to the 
	 * SymbolTable labels.
	 * @ensures the SourceRecord is unchanged
	 * @param labels the table where label is added
	 * @throws LabelAlreadyDefinedException
	 * @throws LabelNotDefinedException 
	 * @throws InvalidRecordException 
	 */
	public void addLabel( SymbolTable labels) throws LabelAlreadyDefinedException, LabelNotDefinedException, InvalidRecordException;
	
	/**
	 * Description: The Operand field of the SouceRecord is updated. If the Operand has a 
	 * label, this label is looked up in the SymbolTable, labels, and value is set to the 
	 * numeric value of that symbol in the table.
	 * @requires true
	 * @alters the SourceRecords Operand fields value is updated with the value 
	 * corresponding to label in the SymbolTable 
	 * @ensures labels is unchanged
	 * @param labels the table where the label corresponding value is to be looked up
	 * @throws InvalidRecordException
	 * @throws LabelNotDefinedException
	 */
	public void replaceLabels(SymbolTable labels) throws InvalidRecordException, LabelNotDefinedException;
	
	/**
	 * Description: Adds all literals in LiteralPool literals to a SymbolTable
	 * @requires true
	 * @alters the SymbolTable
	 * @ensures labels is unchanged
	 * @param literals literal pool being added to the end of a SymbolTable
	 * @throws InvalidRecordException 
	 */
	public void addLiteral(LiteralPool literals) throws InvalidRecordException;

	/**
	 * Description: Creates a formatted string of the SourceRecord for the executable 
	 * object output file (see users guide)
	 * @requires true
	 * @alters creates a formatted string (see users guide)
	 * @ensures absolute and the SourceRecord are unchanged
	 * @param absolute flag determining if the executable file is absolute or relocatable
	 * @return a formatted string of the SourceRecord for the executable object file 
	 * (see users guide)
	 */
	public String toExecutableString(Boolean absolute);

	/**
	 * Description: Creates a formatted string of the SourceRecord for the listing output 
	 * file (see users guide)
	 * @requires true
	 * @alters creates a formatted string (see users guide)
	 * @ensures absolute, line, and the SourceRecord are unchanged
	 * @param absolute flag determining if the executable file is absolute or relocatable
	 * @param line the line number being printed
	 * @return a formatted string of the SourceRecord  for the listing output file 
	 * (see users guide)
	 */
	public String toListingString(Boolean absolute, int line);
}
