package objectLine;

import java.util.Map;

/**
 * Implements ObjectLine to specifically handle an absolute record line from the object-input 
 * file. An absolute line is a text record that has no external symbols and is not marked for 
 * relocation. An AbsoluteLine has 2 fields: an int field that contains the address of the 
 * absolute record and a string that has the value of the line from the object-input file 
 * of everything after the address. In addition to the methods in ObjectLine, Absolute Line 
 * implements a toString method.
 * @author Dragon Slayer
 */
public class AbsoluteLine implements ObjectLine {

	int address;
	String content;
	
	/**
	 * Description: Immutable constructor that sets the address field of an AbsoluteLine 
	 * to the address passed in and the content field of the AbsoluteLine to content 
	 * (the whole line) passed in.
	 * @requires true
	 * @ensures The field of the AbsoluteLine are set. The parameters address and content 
	 * are unchanged. 
	 * @param address address of the absolute line from the object input file
	 * @param content the complete absolute line from the object input file.
	 */
	public AbsoluteLine(String address, String content) {
		this.address = Integer.parseInt(address,16);
		this.content = content;//non-relocatable file, this is all the information after the address
	}

	@Override
	public LineType getType() {
		return LineType.Absolute;
	}

	@Override
	public void addOffset(int offset) {
		address+=offset;
	}

	@Override
	public void addEntry(Map<String, Integer> entries) {
		//do nothing
	}

	@Override
	public void replaceExt(Map<String, Integer> fullMap, int offset) {
		//do nothing
	}

	/**
	 * Description: This method uses the address and content fields to create a 
	 * formatted string, which is a text record for the executable-output file. 
	 * This string is returned.
	 * @requires true
	 * @alters N/A
	 * @ensures the AbsoluteLine is unchanged.
	 * @return address and content as a formatted text record string.
	 */
	public String toString(){
		return 'T'+String.format("%02X", address)+content+'\n';
	}

}
