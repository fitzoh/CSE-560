package objectLine;

import java.util.Map;

import objectFile.InvalidObjectFileException;

/**
 * Implements ObjectLine with the ability to process the header record.
 * A HeaderLine has 5 fields: an int field for the load address, an int field 
 * for the segment length, and an int field for the execution address. It also 
 * has string field for the segment name, and segment label (which are the same). 
 * @author Dragon Slayer
 */
public class HeaderLine implements ObjectLine {
	int execAddress;
	String segLabel;
	String segName;
	int loadAddress;
	int segLength;

	/**
	 * Description: Immutable constructor that sets the HeaderLine fields of execAddress, 
	 * loadAddress, and segLength to the corresponding parameters. It sets the HeaderLine 
	 * field of segLabel to the name of the segment and the segName field to the segName 
	 * parameter without the trailing spaces.
	 * @requires true
	 * @ensures The fields of the HeaderLine are set. The parameters are unchanged.
	 * @param execAddress the execution start address from the header record
	 * @param segName the name of the segment.
	 * @param loadAddress the load address from the header record
	 * @param segLength the segment length from the header record
	 */
	public HeaderLine(String execAddress, String segName, String loadAddress,
			String segLength) {
		this.execAddress = Integer.parseInt(execAddress, 16);
		this.segLabel=segName;
		this.segName = (segName+' ').substring(0,(segName+' ').indexOf(' '));
		this.loadAddress = Integer.parseInt(loadAddress, 16);
		this.segLength = Integer.parseInt(segLength,16);
	}

	/**
	 * Description: Returns the integer value of the loadAddress of the HeaderLine
	 * @requires true
	 * @alters Nothing
	 * @ensures the HeaderLine is unchanged.
	 * @return The integer value of the loadAddress of the HeaderLine
	 */
	public int getLoadAdd() {
		return loadAddress;
	}

	/**
	 * Description: Returns the integer value of the segment length of the HeaderLine
	 * @requires true
	 * @alters Nothing
	 * @ensures the HeaderLine is unchanged.
	 * @return The integer value of the segment length of the HeaderLine
	 */
	public int getSegLength() {
		return segLength;
	}

	/**
	 * Description: Returns the integer value of the execution address of the HeaderLine
	 * @requires true
	 * @alters Nothing
	 * @ensures the HeaderLine is unchanged.
	 * @return The integer value of the execution address of the HeaderLine
	 */
	public int getExecAdd() {
		return execAddress;
	}

	/**
	 * Description: Returns true if the header records segment name was main. 
	 * False otherwise.
	 * @requires true
	 * @alters Nothing
	 * @ensures the HeaderLine is unchanged.
	 * @return True if the header records segment name was main. False otherwise.
	 */
	public boolean isMain() {
		return segName.toUpperCase().equals("MAIN");
	}
	
	/**
	 * Description: sets the value of the segment length to the parameter newLength
	 * @requires true
	 * @alters Nothing
	 * @ensures newLength is unchanged.
	 * @param newLength the new segment length value for the header record.
	 */
	public void setLength(int newLength){
		segLength = newLength;
	}

	@Override
	public LineType getType() {
		return LineType.Header;
	}

	@Override
	public void addOffset(int offset) {//both load and execution address must be incremented
		loadAddress+=offset;
		execAddress+=offset;
	}

	@Override
	public void addEntry(Map<String, Integer> entries) throws InvalidObjectFileException {
		if(entries.containsKey(segName)){//check for duplicate symbols
			throw new InvalidObjectFileException("Error_Code 201: Duplicate entry detected");
		}
		entries.put(segName, loadAddress);//and add symbol to table
		
	}

	@Override
	public void replaceExt(Map<String, Integer> fullMap, int offset) {
		//do nothing
		
	}
	
	/**
	 * Description: Uses the fields of execAddress, segLabel, loadAddress, and segLegnth 
	 * to return a formatted string that is a header record.
	 * @requires true
	 * @alters N/A
	 * @ensures The HeaderLine is unchanged.
	 * @return A formatted string that is a header record.
	 */
	public String toString(){
    //They said I could name my executable file whatever I wanted
    //So I named it after my inspiration, Kai Li
		return 'H'+String.format("%02X", execAddress)+"HaiKai"+String.format("%02X", loadAddress)+String.format("%02X", segLength)+'\n';
	}

}
