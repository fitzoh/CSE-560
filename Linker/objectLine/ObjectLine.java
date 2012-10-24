package objectLine;

import java.util.Map;

import objectFile.InvalidObjectFileException;

/**
 * Interface implemented AbsoluteLine, EntryLine, ExternalLine, HeaderLine and RelocatableLine. 
 * An ObjectLine holds all of the information relevant to the type of record from the object-input 
 * file. See the individual dynamic types for more information on each. 
 * @author Dragon Slayer
 */
public interface ObjectLine {

	/**
	 * Description: returns the enum type of the ObjectLine
	 * @requires true
	 * @alters N/A
	 * @ensures nothing is changed
	 * @return the enum type of the ObjectLine.
	 */
	LineType getType();

	/**
	 * Description: adds the value of the offset to the value of the address 
	 * of the ObjectLine.
	 * @requires int must be nonnegative
	 * @alters adds offset to the address of the Object line.
	 * @ensures offset remains the same
	 * @param offset value being added to the address value
	 */
	void addOffset(int offset);

	/**
	 * Description: Adds an entry symbol (key) and its numeric value to a 
	 * map for external symbols. If the symbol has already been defined, an 
	 * error is thrown.
	 * @requires True
	 * @alters entries receives a new pair.
	 * @ensures entries receives the new symbol and its value.
	 * @param entries map that the symbol and numeric value of the symbol are being added to
	 * @throws InvalidObjectFileException
	 */
	void addEntry(Map<String, Integer> entries) throws InvalidObjectFileException;

	/**
	 * Description: Updates the Object line by replacing the external symbol with its numeric 
	 * value. It gets the numeric value from fullMap.
	 * @requires true
	 * @alters The ObjectLine’s s field so that it contains the numeric value of the symbol
	 * @ensures fullMap and offset are unchanged.
	 * @param fullMap the map of external symbols. Used to look up the numeric value of the symbol to be replaced
	 * @param offset the offset of the line from the load address
	 * @throws InvalidObjectFileException
	 */
	void replaceExt(Map<String, Integer> fullMap, int offset) throws InvalidObjectFileException;

}
