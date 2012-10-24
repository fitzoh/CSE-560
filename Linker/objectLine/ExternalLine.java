package objectLine;

import java.util.Map;

import objectFile.InvalidObjectFileException;

/**
 * Implements ObjectLine with the ability to process a record that contains an 
 * external symbol. An external line is a record from the object-input file that 
 * is marked as having an external symbol. An ExternalLine has 5 fields: an int 
 * field for the address of the line containing an external symbol, an int field 
 * for the offset of the line for when it is combined to create the executable 
 * output, and an int field for the value of the s field of the record. Also 
 * contains a string field for the contents of the record from the input file, 
 * not including the address, sField, or external symbol. It also has a string 
 * for the name of the external symbol in the record.
 * @author Dragon Slayer
 */
public class ExternalLine implements ObjectLine {
	int address;
	int sField;
	String content;
	String external;

	/**
	 * Description: Immutable constructor that sets the address field to the address 
	 * passed in, the content field to the content parameter passed in, and the external 
	 * field to the extSymb passed in. The ExternalLine sField is set to 0.
	 * @requires true
	 * @ensures The field of the ExternalLine are set. The parameters address, content, 
	 * and extSymb are unchanged.
	 * @param address the address of the ExternalLine from the object-input file.
	 * @param content the contents of the whole ExternalLine from the object-input file.
	 * @param extSymb the external symbol used in the line
	 */
	public ExternalLine(String address, String content, String extSymb) {
		this.address = Integer.parseInt(address, 16);
		this.content = content;
		sField = 0;
		external = (extSymb+' ').substring(0,(extSymb+' ').indexOf(' '));//remove trailing spaces
	}

	@Override
	public LineType getType() {
		return LineType.External;
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
	public void replaceExt(Map<String, Integer> fullMap, int offset) throws InvalidObjectFileException {
		if(!fullMap.containsKey(external)){//check for undefined symbols
			throw new InvalidObjectFileException("Error_Code 200: Undefined external value: "+ external);
		}
		sField = fullMap.get(external);//and replace symbol
	}
	
	/**
	 * Description: Uses address, content, and sField to create a formatted string which is a 
	 * text record of an external line. This string is returned. 
	 * @requires true
	 * @alters N/A
	 * @ensures the ExternalLine is unchanged.
	 * @return A text record formatted string of the external line.
	 */
	public String toString(){

		return 'T'+String.format("%02X", address)+content+String.format("%02X", sField)+"\n";
	}

}
