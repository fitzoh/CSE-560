package utilities;

/**
 * This class does not have any methods. It contains variables that 
 * represent the regular expressions for the different 
 * records of the object-input files
 * @author Dragon Slayer
 */
public class regexes {
	
	private static final String hexChar ="[0-9A-F]";//one hex characters
	private static final String address = '('+hexChar+hexChar+')';//two hex characters in a group
	private static final String symbol = "(\\p{Alpha}|\\p{Alpha}\\p{Alnum}{1,5})";//one alpha char followed by 0-5 alphanumeric chars
	// one alpha char followed by 0-5 alpha-numeric chars, filled to 6 characters with spaces		
	private static final String label = "(\\p{Alpha} {5}|\\p{Alpha}\\p{Alnum} {4}|\\p{Alpha}\\p{Alnum}{2} {3}|\\p{Alpha}\\p{Alnum}{3} {2}|\\p{Alpha}\\p{Alnum}{4} {1}|\\p{Alpha}\\p{Alnum}{5})";
		
	public static final String header = 'H'+address+label+address+address+'M';
	public static final String entry = 'E'+label+address;
	public static final String absolute = 'T'+address + '('+hexChar + "{5})";
	public static final String relocatable = 'T' + address +'(' + hexChar + "{3})"+address+'M';
	public static final String external = 'T' + address +'('+ hexChar + "{3})00"+'X'+symbol;
	
			}
