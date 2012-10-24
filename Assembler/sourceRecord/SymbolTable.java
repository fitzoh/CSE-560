package sourceRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a map with the name(String) of the symbol as its key value and
 * LabelValue as the depend value in the map. It contains methods to add a
 * symbol to the map, to retrieve an integer value corresponding to a symbol
 * from the table, and to determine if a symbol is absolute. It also contains a
 * method to add all the literals in a literal pool to the symbol table.
 * 
 * @author Dragon Slayer
 */
public class SymbolTable {
	private Map<String, LabelValue> rep;

	/**
	 * Description: (Constructor) Creates an immutable SymbolTable, with a
	 * string key value corresponding to the symbol name and a LabelValue as the
	 * dependent.
	 * @requires true
	 * @ensures a new SymbolTable (map) is created. Immutable
	 */
	public SymbolTable() {
		rep = new HashMap<String, LabelValue>();
	}

	/**
	 * Description: If label is not already defined in the table, label is added
	 * to the symbol table with its corresponding value (the integer value
	 * corresponding to the symbol and whether it is absolute or relative). If
	 * label is already defined, a LableAlreadyDefinedExecption is thrown
	 * 
	 * @requires true
	 * @alters label and value are added to the symbol table.
	 * @ensures label and value are unchanged.
	 * @param label
	 *            symbol to be added to the symbol table
	 * @param value
	 *            integer value corresponding to the symbol and whether it is
	 *            absolute or relative.
	 * @throws LabelAlreadyDefinedException
	 * @throws InvalidRecordException 
	 */
	public void addLabel(String label, LabelValue value)
			throws LabelAlreadyDefinedException, InvalidRecordException {
		if (rep.containsKey(label)) {
			throw new LabelAlreadyDefinedException("Error_Code 305: Label "+label+" has already been defined");
		}
		//There must not be more than 100 symbols in the source code
		if(rep.size()>=100){
			throw new InvalidRecordException("Error_Code 306: 100 labels exceeded");
		}
		//Empty label won't be added into the symbol table
		if (label.length()>0)
		{
			rep.put(label, value.getCopy());
		}
	}

	/**
	 * Description: If the label is in the symbol table map, it returns the
	 * integer value corresponding to label. If label is not defined, a
	 * LabelNotDefinedException is thrown
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures the symbol table and label are unchanged.
	 * @param label
	 *            the name of the symbol. It is the key value of the map used to
	 *            find its corresponding integer value.
	 * @return the integer value corresponding to label
	 * @throws LabelNotDefinedException
	 */
	public int getLabel(String label) throws LabelNotDefinedException {
		if (!rep.containsKey(label)) {
			throw new LabelNotDefinedException("Error_Code 307: label "+ label+ " has not been defined");
		}
		return rep.get(label).value;
	}

	/**
	 * Description: returns true if the label is absolute, else returns false.
	 * 
	 * @requires label is defined in the symbol table map
	 * @alters N/A
	 * @ensures label and the symbol table map are unchanged
	 * @param label
	 *            label being checked to determine if it is absolute
	 * @return true iff the label is absolute.
	 */
	public boolean isAbsolute(String label) {
		return rep.get(label).absolute;

	}

	/**
	 * Description: Adds all literals in pool to the symbol table map. The names
	 * of the literals are the names of the symbols. The integer value
	 * corresponding with each literal (now symbol) is the value of start + the
	 * address value of the literal from the literal pool. All the literals are
	 * added to the symbol table map as absolute.
	 * 
	 * @requires true
	 * @alters all literals are added to the symbol table
	 * @ensures pool and start are unchanged.
	 * @param pool
	 *            literal pool being added to the symbol table
	 * @param start
	 *            value of location counter
	 */
	public void addLiterals(LiteralPool pool, int start) {
		Map<String, Integer> poolCopy = pool.getCopy();
		for (String s : poolCopy.keySet()) {
			rep.put(s, new LabelValue(start + poolCopy.get(s), false));
			// store at mem start+offset
		}
	}
}
