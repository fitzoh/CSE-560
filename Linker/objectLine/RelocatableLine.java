package objectLine;

import java.util.Map;

/**
 * Implements ObjectLine with the ability to process a text record that is 
 * marked as relocatable. A RelocatableLine has 3 fields: an int field for 
 * the address of the relocatable record and an int field for the s field of 
 * the record. It also has a string field which contains the rest of the 
 * line(without the address or sfield).
 * @author Dragon Slayer
 */
public class RelocatableLine implements ObjectLine {

	int address;
	String content;
	int sField;
	
	/**
	 * Description: Immutable constructor that sets the address field to the address 
	 * passed in, the content field to the content parameter passed in, and the sField 
	 * to the sField passed in.
	 * @requires true
	 * @ensures The fields of the RelocatableLine are set. The parameters address, content, 
	 * and sField are unchanged.
	 * @param address the address of the RelocatableLine from the object-input file.
	 * @param content the contents of the relocatable line from the object-input file 
	 * (without the sfield or address).
	 * @param sField the s field of the object-input relocatable line
	 */
	public RelocatableLine(String address, String content, String sField) {
		this.address = Integer.parseInt(address, 16);
		this.content = content;
		this.sField = Integer.parseInt(sField, 16);
	}

	@Override
	public LineType getType() {
		return LineType.Relocatable;
	}

	@Override
	public void addOffset(int offset) {
		address+=offset;
		sField+=offset;
		
	}

	@Override
	public void addEntry(Map<String, Integer> entries) {
		// do nothing
		
	}

	@Override
	public void replaceExt(Map<String, Integer> fullMap, int offset) {
		// do nothing
		
	}
	
	/**
	 * Description: Uses address, content, and sField to create a formatted string 
	 * which is a text record of an relocatable line. This string is returned. 
	 * @requires true
	 * @alters N/A
	 * @ensures the ExternalLine is unchanged.
	 * @return A text record formatted string of the relocatable line.
	 */
	public String toString(){

		return 'T'+String.format("%02X", address)+content+String.format("%02X", sField)+'\n';
	}

}
