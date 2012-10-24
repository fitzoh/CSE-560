package objectFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import objectLine.HeaderLine;
import objectLine.LineType;
import objectLine.ObjectLine;
import objectLine.ObjectLineFactory;

/**
 * Holds important values from an object-input file such as the load address, 
 * the segment length, the execution start address and the offset. Keeps track 
 * of all the entries in the object-input file, the header and all the objectlines.  
 * Also keeps track of whether the object file was the main. It contains methods to 
 * set all of its fields, populate the external symbol table, and to create a string 
 * of all the ObjectLines for output.
 * @author Dragon Slayer
 */
public class ObjectFile {

	boolean main;
	ObjectLine header;
	List<ObjectLine> rep;
	int offset;
	int loadAddress;
	int segmentLength;
	int executionAddress;
	Map<String,Integer> entries;
	
	/**
	 * Description: Immutable constructor which initializes entries as an empty map, 
	 * header as the HeaderLine, and populates rep with the ObjectLines from an object 
	 * input file. 
	 * @requires true
	 * @ensures header, entries, and rep are set.
	 * @param input reader to read the object-input files.
	 * @throws IOException
	 * @throws InvalidObjectFileException
	 */
	public ObjectFile(BufferedReader input) throws IOException, InvalidObjectFileException{
		rep = new LinkedList<ObjectLine>();
		entries = new HashMap<String, Integer>();
		header = getHeader(input);//parse first line for header
		populateRep(input);//parse the rest of the lines
	}
	
	/**
	 * Description: Adds the offset (load address value) to the addresses of all the 
	 * ObjectLines. Sets the ObjectFile field offset to the value passed in.
	 * @requires offset is nonnegative
	 * @alters The addresses of all the ObjectLines and the ObjectFile field offset.
	 * @ensures The addresses of all the ObjectLines are their values, plus the offset. 
	 * Sets the ObjectFile field offset to the value passed in.
	 * @param offset the value being added to the address of all the ObjectLines.
	 */
	public void setOffset(int offset){//add load address to each ObjectLine
		this.offset= offset;
		header.addOffset(offset);
		for(ObjectLine o:rep){
			o.addOffset(offset);
		}
	}
	
	/**
	 * Description: Adds the symbols to entries map (external symbol table).
	 * @requires true
	 * @alters entries to contain the symbols in the ObjectFile
	 * @ensures adds all symbols that are in the ObjectFile to the external symbol table.
	 * @param entries the map that the symbols and their values are added to.
	 * @throws InvalidObjectFileException
	 */
	public void getEntries(Map<String, Integer> entries) throws InvalidObjectFileException{
		header.addEntry(entries);
		for(ObjectLine o:rep){//add all symbols to symbol table
			o.addEntry(entries);
		}
	}
	
	/**
	 * Description: Iterates through the ObjectFile replacing any external symbols with 
	 * their address values. 
	 * @requires true
	 * @alters Any ObjectLine that contains an external symbol, its s field is replaced with the 
	 * external symbol�s address.
	 * @ensures fullMap is not changed.
	 * @param fullMap the external symbol table that is used to look up the symbol�s value using 
	 * the symbol�s name.
	 * @throws InvalidObjectFileException
	 */
	public void replaceExt(Map<String, Integer> fullMap) throws InvalidObjectFileException{
		for(ObjectLine o: rep){//replace all external entries
			o.replaceExt(fullMap, offset);
		}
	}
	
	/**
	 * Description: Returns the HeaderLine of the ObjectFile
	 * @requires true
	 * @alters Nothing
	 * @ensures Nothing is changed.
	 * @return The HeaderLine of the Object File.
	 */
	public HeaderLine getHeader(){
		return (HeaderLine) header;
	}
	
	/**
	 * Description: Returns the length of the segment of the ObjectFile (from the 
	 * HeaderLine) and sets the ObjectFile field segmentLength to that same value.
	 * @requires true
	 * @alters Sets the segmentLength field 
	 * @ensures Nothing is changed.
	 * @return The length of the segment represented by the ObjectFile.
	 */
	public int getLength(){
		segmentLength = ((HeaderLine)header).getSegLength();
		return segmentLength;
	}
	
	/**
	 * Description: returns true iff the ObjectFile is the main object-input file.
	 * @requires true
	 * @alters Nothing
	 * @ensures Nothing is changed 
	 * @return true iff the ObjectFile is the main object-input file. 
	 */
	public boolean isMain(){
		return main;
	}
	
	/**
	 * Description: If input has a valid header record an ObjectLine of dynamic type 
	 * HeaderLine is returned. This method also sets the boolean field main if the 
	 * HeaderLine is the main header of the object-input files. If input is not valid, 
	 * an IOException is thrown. If there is no header line, the segment length is greater 
	 * than 255, the load address is invalid, or the execution address is invalid, an 
	 * InvalidObjetFileException is thrown.
	 * @requires true
	 * @alters main is set to true if the ObjectFile is the main object-input file.
	 * @ensures (see return)
	 * @param input input from the object-input file.
	 * @return The HeaderLine ObjectLine if input has a valid header record.
	 * @throws IOException
	 * @throws InvalidObjectFileException
	 */
	private ObjectLine getHeader(BufferedReader input) throws IOException, InvalidObjectFileException {
		String headerStr = input.readLine();
		if(headerStr==null){//if first line is empty the file is invalid
			throw new InvalidObjectFileException("Error_Code 101: No header found");
		}
		ObjectLine headerObj = ObjectLineFactory.getObjectLine(headerStr);
		if(headerObj == null){
			throw new InvalidObjectFileException("Error_Code 102: Invalid header");
		}
		if(headerObj.getType()!=LineType.Header){//first line must be header
			throw new InvalidObjectFileException("Error_Code 101: No header found");
		}
		loadAddress = ((HeaderLine) headerObj).getLoadAdd();
		if(loadAddress != 0){
			throw new InvalidObjectFileException("Error_Code 103: loadAddress must be 0");
		}
		segmentLength = ((HeaderLine) headerObj).getSegLength();
		if(segmentLength>255){
			throw new InvalidObjectFileException("Error_Code 104: segment length must be < 255");
		}
		executionAddress = ((HeaderLine) headerObj).getExecAdd();
		if(executionAddress<0 || executionAddress>segmentLength){
			throw new InvalidObjectFileException("Error_Code 105: execution address must be between load address and load address + segment length");
		}
		main = ((HeaderLine) headerObj).isMain();
		return headerObj;
	}

	/**
	 * Description: Reads in all the lines from the object-input file and creates the 
	 * correct dynamic type of ObjectLine for each line. If more than one header line 
	 * is in the object-input file or an invalid line is found, an exception is thrown.
	 * @requires object-input file must exist and be readable.
	 * @alters rep contains a list of all the ObjectLines from the object-input file.
	 * @ensures input is unchanged. If no exception is thrown, ObjectLines are created 
	 * for each line of the input file and are sequentially added to a list of Objectlines.
	 * @param input object-input file
	 * @throws IOException
	 * @throws InvalidObjectFileException
	 */
	private void populateRep(BufferedReader input) throws IOException, InvalidObjectFileException {
		String currentLine = input.readLine();
		while(currentLine!=null){//read until end of file
			ObjectLine currentObjLine = ObjectLineFactory.getObjectLine(currentLine);
			if(currentObjLine==null){//this will be null if a valid line is not found
				throw new InvalidObjectFileException("Error_Code 106:  invalid line found");
			}
			if(currentObjLine.getType()==LineType.Header){//multiple headers == bad
				throw new InvalidObjectFileException("Error_Code 107: Multiple headers found");
			}
			rep.add(currentObjLine);//add ObjectLine to the list 
			currentLine = input.readLine();//and read next line
		}
	}
	
	/**
	 * Description: If the ObjectFile contains the information from the main object 
	 * input file, then the header record is added to the string. After that, all the 
	 * rest of the ObjectLines are appended to the String. This string is returned.
	 * @requires true
	 * @alters Nothing
	 * @ensures (see returns)
	 * @return a string of all the ObjectLines in the Object file if the ObjectFile 
	 * is the main object file. If it is not the main, the header record is not printed.
	 */
	public String toString(){
		StringBuilder result = new StringBuilder();
		if(main){//only print header for main segment
			result.append(header.toString());
		}
		for(ObjectLine o:rep){//and print every other line
			result.append(o.toString());
		}
		return result.toString();
		
	}
	
	/**
	 * Description: Sets the segment length of the Object file to the value 
	 * of newLength  
	 * @requires true
	 * @alters segmentLength to the value of of newLength
	 * @ensures newLength is unchanged.
	 * @param newLength the value that the segment length of the ObjectFile 
	 * will be set to.
	 */
	public void setLength(int newLength){//need to update length of the main header
		((HeaderLine) header).setLength(newLength);
	}
}
