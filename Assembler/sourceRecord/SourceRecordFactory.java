package sourceRecord;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utilities.regexes;

/**
 * This class is used to create SourceRecords. Based on the format of the string record passed in, the 
 * appropriate type of SourceRecord is created. The types of SourceRecords which can be created from 
 * this class are: machineRec, oriRec, endRec, equRec, nmdRec, ccdRec, resRec, retRec, gtcRec, ptcRec 
 * or a CommentLine. (Regular expressions are used to check the format of record. See regexes.java)
 * @author Dragon Slayer
 */
public class SourceRecordFactory {

	/**
	 * Description: Makes the appropriate method call based on the format of the String record to 
	 * create a SourceRecord of the correct dynamic type. 
	 * @requires true
	 * @alters N/A
	 * @ensures record and location are unchanged
	 * @param record the string value of the assembler instruction
	 * @param location the location counter value associated with the SourceRecord 
	 * @return a copy of the SourceRecord with the appropriate dynamic type
	 * @throws InvalidRecordException
	 */
	public static SourceRecord getSourceRecord(String record, int location)
			throws InvalidRecordException {
		SourceRecord result = null;
		//Match the source code with one of the following format
		if (record.matches(regexes.machineRecord)) {
			result = machineRec(record, location);
		} else if (record.matches(regexes.oriRecord)) {
			result = oriRec(record, location);
		} else if (record.matches(regexes.endRecord)) {
			result = endRec(record, location);
		} else if (record.matches(regexes.equRecord)) {
			result = equRec(record, location);
		} else if (record.matches(regexes.nmdRecord)) {
			result = nmdRec(record, location);
		} else if (record.matches(regexes.ccdRecord)) {
			result = ccdRec(record, location);
		} else if (record.matches(regexes.resRecord)) {
			result = resRec(record, location);
		} else if (record.matches(regexes.retRecord)) {
			result = retRec(record, location);
		} else if (record.matches(regexes.gtcRecord)) {
			result = gtcRec(record, location);
		} else if (record.matches(regexes.ptcRecord)) {
			result = ptcRec(record, location);
		} else if (record.matches(regexes.commentRecord)){
			result = new CommentLine(record, location);
		} else if (record.matches(regexes.entRecord)){
			result = entRec(record, location);
		} else if (record.matches(regexes.extRecord)){
			result = extRec(record, location);
		} 
		//Invalid record
		if(result==null){
			throw new InvalidRecordException("Error_Code 304: No matches found");
		}
		return result;
	}

	/**
	 * Description: Creates a SourceRecord with dynamic type extRec. The extRec 
	 * parameters (op1, op2, op3, op4) are set based on the divisions specified 
	 * by regexes.java. Record and location are passed into the extRec parameters 
	 * record and location. If the method is unable to create an extRec, an 
	 * InvalidRecordException is thrown. 
	 * @requires true
	 * @alters N/A
	 * @ensures record and location are unchanged
	 * @param record the string value of the assembler instruction
	 * @param location the location counter value associated with the SourceRecord 
	 * @return a copy of a SourceRecord with dynamic type extRec.
	 * @throws InvalidRecordException
	 */
	private static SourceRecord extRec(String record, int location) throws InvalidRecordException {
		Pattern p = Pattern.compile(regexes.extRecord);
		Matcher m = p.matcher(record);
		m.matches();
		String op1 = m.group(1);
		String op2 = m.group(3);
		String op3 = m.group(5);
		String op4 = m.group(7);
		return new extRec(op1, op2, op3, op4, record, location);
	}

	/**
	 * Description:  Creates a SourceRecord with dynamic type entRec. The entRec parameters 
	 * (op1, op2, op3, op4) are set based on the divisions specified by regexes.java. Record 
	 * and location are passed into the entRec parameters record and location. If the method 
	 * is unable to create an entRec, an InvalidRecordException is thrown. 
	 * @requires true
	 * @alers N/A
	 * @ensures record and location are unchanged
	 * @param record the string value of the assembler instruction
	 * @param location the location counter value associated with the SourceRecord 
	 * @return a copy of a SourceRecord with dynamic type entRec.
	 * @throws InvalidRecordException
	 */
	private static SourceRecord entRec(String record, int location) throws InvalidRecordException {
		Pattern p = Pattern.compile(regexes.entRecord);
		Matcher m = p.matcher(record);
		m.matches();
		String op1 = m.group(1);
		String op2 = m.group(3);
		String op3 = m.group(5);
		String op4 = m.group(7);
		return new entRec(op1, op2, op3, op4, record, location);
	}

	/**
	 * Description:  Creates a SourceRecord with dynamic type machineRec. The machineRec parameters
	 * (label, OP, R, S, and X) are set based on the divisions specified by regexes.java. Record and 
	 * location are passed into the machineRec parameters record and location. If the method is unable 
	 * to create a machineRec, an InvalidRecordException is thrown. 
	 * @requires true
	 * @alters N/A
	 * @ensures record and location are unchanged
	 * @param record the string value of the assembler instruction
	 * @param location the location counter value associated with the SourceRecord 
	 * @return a copy of a SourceRecord with dynamic type machineRec.
	 * @throws InvalidRecordException
	 */
	private static SourceRecord machineRec(String record, int location) throws InvalidRecordException {
		Pattern p = Pattern.compile(regexes.machineRecord);
		Matcher m = p.matcher(record);
		m.matches();
		String label = m.group(1);
		String OP = m.group(2);
		String R = m.group(3);
		String S = m.group(6);
		String X = m.group(11);
		//Commenting out all comment groups. We are not currently using them, but they are being left in as they could be useful in a future version
		return new MachineRec(label, OP, R, S, X, record, location);
	}

	/**
	 * Description:  Creates a SourceRecord with dynamic type oriRec. The oriRec parameters 
	 * (label and operand) are set based on the divisions specified by regexes.java. Record 
	 * and location are passed into the oriRec parameters record and location. If the method 
	 * is unable to create an oriRec, an InvalidRecordException is thrown. 
	 * @requires true
	 * @alters N/A
	 * @ensures record and location are unchanged
	 * @param record the string value of the assembler instruction
	 * @param location the location counter value associated with the SourceRecord 
	 * @return a copy of a SourceRecord with dynamic type oriRec.
	 * @throws InvalidRecordException
	 */
	private static SourceRecord oriRec(String record, int location)
			throws InvalidRecordException {
		Pattern p = Pattern.compile(regexes.oriRecord);
		Matcher m = p.matcher(record);
		m.matches();
		String label = m.group(1);
		String operand = m.group(3);
		if(operand.length()==0){
			operand = null;
		}
		return new OriRec(label, operand, record);
	}

	/**
	 * Description:  Creates a SourceRecord with dynamic type endRec. The endRec parameters 
	 * (label and operand) are set based on the divisions specified by regexes.java. Record 
	 * and location are passed into the endRec parameters record and location. If the method 
	 * is unable to create an endRec, an InvalidRecordException is thrown. 
	 * @requires true
	 * @alters N/A
	 * @ensures record and location are unchanged
	 * @param record the string value of the assembler instruction
	 * @param location the location counter value associated with the SourceRecord 
	 * @return a copy of a SourceRecord with dynamic type endRec.
	 * @throws InvalidRecordException
	 */
	private static SourceRecord endRec(String record, int location) throws InvalidRecordException {
		Pattern p = Pattern.compile(regexes.endRecord);
		Matcher m = p.matcher(record);
		m.matches();
		String label = m.group(1);
		String operand = m.group(3);
		if(operand.length()==0){
			operand=null;
		}
		return new EndRec(label, operand, record, location);
	}

	/**
	 * Description:  Creates a SourceRecord with dynamic type equRec. The equRec parameters 
	 * (label and operand) are set based on the divisions specified by regexes.java. Record 
	 * and location are passed into the equRec parameters record and location. If the method 
	 * is unable to create an equRec, an InvalidRecordException is thrown. 
	 * @requires true
	 * @alters N/A
	 * @ensures record and location are unchanged
	 * @param record the string value of the assembler instruction
	 * @param location the location counter value associated with the SourceRecord 
	 * @return a copy of a SourceRecord with dynamic type equRec.
	 * @throws InvalidRecordException
	 */
	private static SourceRecord equRec(String record, int location) throws InvalidRecordException {
		Pattern p = Pattern.compile(regexes.equRecord);
		Matcher m = p.matcher(record);
		m.matches();
		String label = m.group(1);
		String operand = m.group(3);
		return new EquRec(label, operand, record, location);
	}

	/**
	 * Description:  Creates a SourceRecord with dynamic type nmdRec. The nmdRec parameters 
	 * (label and operand) are set based on the divisions specified by regexes.java. Record 
	 * and location are passed into the nmdRec parameters record and location. If the method 
	 * is unable to create an nmdRec, an InvalidRecordException is thrown. 
	 * @requires true
	 * @alters N/A
	 * @ensures record and location are unchanged
	 * @param record the string value of the assembler instruction
	 * @param location the location counter value associated with the SourceRecord 
	 * @return a copy of a SourceRecord with dynamic type nmdRec.
	 * @throws InvalidRecordException
	 */
	private static SourceRecord nmdRec(String record, int location) throws InvalidRecordException {
		Pattern p = Pattern.compile(regexes.nmdRecord);
		Matcher m = p.matcher(record);
		m.matches();
		String label = m.group(1);
		String operand = m.group(3);
		return new nmdRec(label, operand, record, location);
	}

	/**
	 * Description:  Creates a SourceRecord with dynamic type ccdRec. The ccdRec parameters 
	 * (label and operand) are set based on the divisions specified by regexes.java. Record 
	 * and location are passed into the ccdRec parameters record and location. If the method 
	 * is unable to create an ccdRec, an InvalidRecordException is thrown. 
	 * @requires true
	 * @alters N/A
	 * @ensures record and location are unchanged
	 * @param record the string value of the assembler instruction
	 * @param location the location counter value associated with the SourceRecord 
	 * @return a copy of a SourceRecord with dynamic type ccdRec.
	 * @throws InvalidRecordException
	 */
	private static SourceRecord ccdRec(String record, int location)
			throws InvalidRecordException {
		Pattern p = Pattern.compile(regexes.ccdRecord);
		Matcher m = p.matcher(record);
		m.matches();
		String label = m.group(1);
		String operand = m.group(3);
		return new ccdRec(label, operand, record, location);
	}

	/**
	 * Description:  Creates a SourceRecord with dynamic type resRec. The resRec parameters 
	 * (label and operand) are set based on the divisions specified by regexes.java. Record 
	 * and location are passed into the resRec parameters record and location. If the method 
	 * is unable to create an resRec, an InvalidRecordException is thrown. 
	 * @requires true
	 * @alters N/A
	 * @ensures record and location are unchanged
	 * @param record the string value of the assembler instruction
	 * @param location the location counter value associated with the SourceRecord 
	 * @return a copy of a SourceRecord with dynamic type resRec.
	 * @throws InvalidRecordException
	 */
	private static SourceRecord resRec(String record, int location) throws InvalidRecordException {
		Pattern p = Pattern.compile(regexes.resRecord);
		Matcher m = p.matcher(record);
		m.matches();
		String label = m.group(1);
		String operand = m.group(3);
		return new ResRec(label, operand, record, location);
	}

	/**
	 * Description:  Creates a SourceRecord with dynamic type retRec. The retRec parameters 
	 * (label and operand) are set based on the divisions specified by regexes.java. Record 
	 * and location are passed into the oriRec parameters record and location. If the method 
	 * is unable to create an retRec, an InvalidRecordException is thrown. 
	 * @requires true
	 * @alters N/A
	 * @ensures record and location are unchanged
	 * @param record the string value of the assembler instruction
	 * @param location the location counter value associated with the SourceRecord 
	 * @return a copy of a SourceRecord with dynamic type retRec.
	 * @throws InvalidRecordException
	 */
	private static SourceRecord retRec(String record, int location) throws InvalidRecordException {
		Pattern p = Pattern.compile(regexes.retRecord);
		Matcher m = p.matcher(record);
		m.matches();
		String label = m.group(1);
		String operand = m.group(3);
		return new RetRec(label, operand, record, location);
	}

	/**
	 * Description:  Creates a SourceRecord with dynamic type gtcRec. The gtcRec parameters 
	 * (label and operand) are set based on the divisions specified by regexes.java. Record 
	 * and location are passed into the gtcRec parameters record and location. If the method 
	 * is unable to create an gtcRec, an InvalidRecordException is thrown. 
	 * @requires true
	 * @alters N/A
	 * @ensures record and location are unchanged
	 * @param record the string value of the assembler instruction
	 * @param location the location counter value associated with the SourceRecord 
	 * @return a copy of a SourceRecord with dynamic type gtcRec.
	 * @throws InvalidRecordException
	 */
	private static SourceRecord gtcRec(String record, int location) throws InvalidRecordException {
		Pattern p = Pattern.compile(regexes.gtcRecord);
		Matcher m = p.matcher(record);
		m.matches();
		String label = m.group(1);
		String operand = m.group(3);
		return new GtcRec(label, operand, record, location);
	}

	/**
	 * Description:  Creates a SourceRecord with dynamic type ptcRec. The ptcRec parameters 
	 * (label and operand) are set based on the divisions specified by regexes.java. Record 
	 * and location are passed into the ptcRec parameters record and location. If the method 
	 * is unable to create an ptcRec, an InvalidRecordException is thrown. 
	 * @requires true
	 * @alters N/A
	 * @ensures record and location are unchanged
	 * @param record the string value of the assembler instruction
	 * @param location the location counter value associated with the SourceRecord 
	 * @return a copy of a SourceRecord with dynamic type ptcRec.
	 * @throws InvalidRecordException
	 */
	private static SourceRecord ptcRec(String record, int location) throws InvalidRecordException {
		Pattern p = Pattern.compile(regexes.ptcRecord);
		Matcher m = p.matcher(record);
		m.matches();
		String label = m.group(1);
		String operand = m.group(3);
		return new PtcRec(label, operand, record, location);
	}

}
