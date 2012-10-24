package objectLine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utilities.regexes;

/**
 * This class is used to create ObjectLines. Based on the format of the String line 
 * passed in, the appropriate type of ObjectLine is created. The types of ObjectLines 
 * that can be created from this class are: AbsoluteLine, EntryLine, ExternalLine, 
 * HeaderLine, and RelocatableLine. Regular expressions are used to check the format 
 * of record. See 
 * @author Dragon Slayer
 */
public class ObjectLineFactory {

	/**
	 * Description: Makes the appropriate method call based on the format of the 
	 * String record to create a SourceRecord of the correct dynamic type. 
	 * @requires true
	 * @ensures line is unchanged
	 * @throws N/A
	 * @param line the string value of a line from a object-input file.
	 * @return a copy of the ObjectLine with the appropriate dynamic type
	 */
	public static ObjectLine getObjectLine(String line){
		ObjectLine resultLine = null;//return null if no matches found
		if(line.matches(regexes.absolute)){
			resultLine= getAbsoluteLine(line);
		}else if(line.matches(regexes.entry)){
			resultLine= getEntryLine(line);
		}else if(line.matches(regexes.external)){
			resultLine= getExternalLine(line);
		} else if(line.matches(regexes.header)){
			resultLine= getHeaderLine(line);
		} else if (line.matches(regexes.relocatable)){
			resultLine = getRelocatableLine(line);
		}
		return resultLine;
	}

	/**
	 * Description: Creates an ObjectLine with dynamic type RelocatableLine. The 
	 * RelocatableLine parameters (address, content, sField) are set based on the 
	 * divisions specified by regexes.java. (See regexes)
	 * @requires true
	 * @alters N/A
	 * @ensures line is unchanged
	 * @param line the string value of a line from an object-input file.
	 * @return a copy of the ObjectLine with dynamic type RelocatableLine
	 */
	private static ObjectLine getRelocatableLine(String line) {
		Pattern p = Pattern.compile(regexes.relocatable);
		Matcher m = p.matcher(line);
		m.matches();
		String address = m.group(1);
		String content = m.group(2);
		String sField = m.group(3);
		return new RelocatableLine(address, content, sField);
	}

	/**
	 * Description: Creates an ObjectLine with dynamic type HeaderLine. The HeaderLine
	 * parameters (execAddress, segName, loadAddress, segLength) are set based on the 
	 * divisions specified by regexes.java. (See regexes)
	 * @requires true
	 * @alters N/A
	 * @ensures line is unchanged
	 * @param line the string value of a line from an object-input file.
	 * @return a copy of the ObjectLine with dynamic type HeaderLine
	 */
	private static ObjectLine getHeaderLine(String line) {
		Pattern p = Pattern.compile(regexes.header);
		Matcher m = p.matcher(line);
		m.matches();
		String execAddress = m.group(1);
		String segName = m.group(2);
		String loadAddress = m.group(3);
		String segLength = m.group(4);
		return new HeaderLine(execAddress, segName, loadAddress, segLength);
	}

	/**
	 * Description: Creates an ObjectLine with dynamic type ExternalLine. The 
	 * ExternalLine parameters (address, content, extSymb) are set based on the 
	 * divisions specified by regexes.java. (See regexes)
	 * @requires true
	 * @alters N/A
	 * @ensures: line is unchanged
	 * @param line the string value of a line from an object-input file.
	 * @return a copy of the ObjectLine with dynamic type ExternalLine
	 */
	private static ObjectLine getExternalLine(String line) {
		Pattern p = Pattern.compile(regexes.external);
		Matcher m = p.matcher(line);
		m.matches();
		String address = m.group(1);
		String content = m.group(2);
		String extSymb = m.group(3);
		return new ExternalLine(address, content, extSymb);
	}

	/**
	 * Description: Creates an ObjectLine with dynamic type EntryLine. The EntryLine 
	 * parameters (address, content) are set based on the divisions specified by 
	 * regexes.java. (See regexes)
	 * @requires true
	 * @alters N/A
	 * @ensures line is unchanged
	 * @param line the string value of a line from an object-input file.
	 * @return a copy of the ObjectLine with dynamic type EntryLine
	 */
	private static ObjectLine getEntryLine(String line) {
		Pattern p = Pattern.compile(regexes.entry);
		Matcher m = p.matcher(line);
		m.matches();
		String label = m.group(1);
		String value = m.group(2);
		return new EntryLine(label, value);
	}

	/**
	 * Description: Creates an ObjectLine with dynamic type AbsoluteLine. The 
	 * AbsoluteLine parameters (address, content) are set based on the divisions 
	 * specified by regexes.java. (See regexes)
	 * @requires true
	 * @alters N/A
	 * @ensures line is unchanged
	 * @param line the string value of a line from an object-input file.
	 * @return  a copy of the ObjectLine with dynamic type AbsoluteLine
	 */
	private static ObjectLine getAbsoluteLine(String line) {
		Pattern p = Pattern.compile(regexes.absolute);
		Matcher m = p.matcher(line);
		m.matches();
		String address = m.group(1);
		String content = m.group(2);
		return new AbsoluteLine(address,content);
	}
}
