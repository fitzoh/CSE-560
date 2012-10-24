package utilities;

/**
 * This class does not have any methods. It contains variables that 
 * represent the regular expressions for the different 
 * commands of the assembly-source file
 * @author Dragon Slayer
 */
public class regexes {
	
	
	//substrings of main regexes
	public static final String labelField = "( {6}|\\p{Alpha} {5}|\\p{Alpha}\\p{Alnum} {4}|\\p{Alpha}\\p{Alnum}{2} {3}|\\p{Alpha}\\p{Alnum}{3} {2}|\\p{Alpha}\\p{Alnum}{4} {1}|\\p{Alpha}\\p{Alnum}{5})";
	public static final String symbol = "(\\p{Alpha}|\\p{Alpha}\\p{Alnum}{1,5})";//one alpha char followed by 0-5 alphanumeric chars
	public static final String literal = "=(-?\\d+)";//= sign, optional negative sign, series of digits
	public static final String machineOps = "(LD |LDI|ST |ADD|SUB|MUL|DIV|OR |AND|SHL|SHR|IO |BR |BRZ|BRN|BRS)";
	public static final String integer = "(-?\\d*)";
	public static final String symbolOrInt = "("+symbol+"|"+integer+")";
	public static final String symbolAddressOrLit = "("+integer+"|"+symbol+"|"+literal+")";
	public static final String machineOpOperands = ""+symbolOrInt+","+symbolAddressOrLit+"(\\("+ symbolOrInt+"\\))?";
	public static final String endOfLineComments = "( | (.*))?";
	
	//regexes for all valid record entries
	public static final String commentRecord = ";(.*)";	
	public static final String machineRecord = labelField + "   " + machineOps+" " + machineOpOperands+endOfLineComments;
	public static final String oriRecord = labelField + "   " + "(ORI)" + " " + integer+"?"+endOfLineComments;
	public static final String endRecord = labelField + "   " + "(END)" + " " + symbolOrInt +"?"+endOfLineComments;
	public static final String equRecord = labelField + "   " + "(EQU)" + " " + symbolOrInt + endOfLineComments;
	public static final String nmdRecord = labelField + "   " + "(NMD)" + " " + integer+endOfLineComments;
	public static final String ccdRecord = labelField + "   " + "(CCD)" + " " + "(..)"+endOfLineComments;
	public static final String resRecord = labelField + "   " + "(RES)" + " " + symbolOrInt + endOfLineComments;
	public static final String retRecord = labelField + "   " + "(RET)" + " " + symbolOrInt + endOfLineComments;
	public static final String gtcRecord = labelField + "   " + "(GTC)" + " " + symbolOrInt + endOfLineComments;
	public static final String ptcRecord = labelField + "   " + "(PTC)" + " " + symbolOrInt + endOfLineComments;
	
	//new for lab 4
	public static final String entRecord = "         " + "ENT" + " " + symbol + "(,"+symbol + ")?"+"(,"+symbol + ")?"+"(,"+symbol + ")?"+endOfLineComments;
	public static final String extRecord = "         " + "EXT" + " " + symbol + "(,"+symbol + ")?"+"(,"+symbol + ")?"+"(,"+symbol + ")?"+endOfLineComments;


}
