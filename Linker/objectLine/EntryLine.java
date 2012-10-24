package objectLine;

import java.util.Map;

import objectFile.InvalidObjectFileException;

/**
 * Implements ObjectLine with the ability to process an entry line. An entry line is a 
 * line from the object-input file that defines the value of a global symbol. An EntryLine 
 * has 2 fields: an int field that is the numeric value of the entry label and a string that 
 * is the name of the label. 
 * @author Dragon Slayer
 */
public class EntryLine implements ObjectLine {
	String label;
	int value;

	/**
	 * Description: Immutable constructor that sets the label field of an EntryLine to 
	 * the label passed in and the value field of the EntryLine to the numeric value 
	 * associated with the label. 
	 * @requires true
	 * @ensures The field of the EntryLine are set. The parameters label and value are 
	 * unchanged. 
	 * @param label the name of the external symbol
	 * @param value the corresponding value of the label.
	 */
	public EntryLine(String label, String value) {
		this.label = (label+' ').substring(0,(label+' ').indexOf(' '));//get rid of spaces
		this.value = Integer.parseInt(value, 16);
	}

	@Override
	public LineType getType() {
		return LineType.Entry;
	}

	@Override
	public void addOffset(int offset) {
		value+=offset;//need to increment as all entries are relative
		
	}

	@Override
	public void addEntry(Map<String, Integer> entries) throws InvalidObjectFileException {
		if(entries.containsKey(label)){//check for duplicate symbols
			throw new InvalidObjectFileException("Error_Code 201: Duplicate entry detected");
		}
		entries.put(label, value);//and add symbol to table
	}

	@Override
	public void replaceExt(Map<String, Integer> fullMap, int offset) {
		//do nothing
	}
	
	/**
	 * Description: This method prints nothing. Entry lines do not get printed to the 
	 * executable-output file.
	 * @requires true
	 * @alters N/A
	 * @ensures the EntryLine is unchanged.
	 * @return: An empty String.
	 */
	public String toString(){//entries are not printed, return empty string
		return "";
	}

}
