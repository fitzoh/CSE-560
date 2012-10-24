package utilities;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to determine if an ASCII encoded character is an ASCII
 * encoded decimal digit, an ASCII encoded printable character, an ASCII encoded
 * hex character, an ASCII encoded valid character or an ASCII encoded separator
 * character
 * 
 * @author Dragon Slayer
 */
public class CharChecker {

	public static final int MACNEWLINE = 10;// Mac new Line
	public static final int ASCIIZERO = 48;// ASCII value of zero
	public static final int ASCIININE = 57;// ASCII value of nine
	public static final int ASCIITAB = 9;// ASCII value of tab
	public static final int ASCIIMIN = 32;// Minimum printable ASCII value
											// (other than tab)
	public static final int ASCIIMAX = 126;// Maximum printable ASCII value
	public static final int ASCIISEPMIN1 = 0;// Minimum value of first set of
												// separator characters
	public static final int ASCIISEPMAX1 = 32;// Maximum value of first set of
												// separator characters
	public static final int ASCIISEPMIN2 = 127;// Minimum value of second set of
												// separator characters
	public static final int ASCIISEPMAX2 = 160;// Maximum value of second set of
												// separator characters

	private static Set<Integer> ASCIIDigits;
	private static Set<Integer> ASCIIPrintable;
	private static Set<Integer> ASCIISeparators;
	private static Set<Character> hexChars;
	private static Set<Character> validChars;

	/**
	 * Description: Initializes an integer set of the ASCII digit values (0 - 9)
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures initializes an integer set of the ASCII digit values (0-9)
	 */
	private static void initializeASCIIDigits() {
		ASCIIDigits = new HashSet<Integer>();
		for (int i = ASCIIZERO; i <= ASCIININE; i++) {
			ASCIIDigits.add(i);
		}

	}

	/**
	 * Description: Initializes an integer set of the ASCII printable character
	 * values (32 - 126) and (9)
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures initializes an integer set of the ASCII printable character
	 *          values
	 */
	private static void initializeASCIIPrintable() {

		ASCIIPrintable = new HashSet<Integer>();
		ASCIIPrintable.add(ASCIITAB);
		for (int i = ASCIIMIN; i <= ASCIIMAX; i++) {
			ASCIIPrintable.add(i);
		}
	}

	/**
	 * Description: Initializes an integer set of the ASCII separator characters
	 * values (0 - 32)
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures an integer set of the ASCII separator characters.
	 */
	private static void initializeASCIISeparators() {
		ASCIISeparators = new HashSet<Integer>();
		for (int i = ASCIISEPMIN1; i <= ASCIISEPMAX1; i++) {
			ASCIISeparators.add(i);
		}
		for (int i = ASCIISEPMIN2; i <= ASCIISEPMAX2; i++) {
			ASCIISeparators.add(i);
		}
	}

	/**
	 * Description: initializes a character set of the hex characters (0 - F)
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures a character set of hex characters (0 - F) is initialized
	 */
	private static void initializeHexChars() {
		hexChars = new HashSet<Character>();
		hexChars.add('0');
		hexChars.add('1');
		hexChars.add('2');
		hexChars.add('3');
		hexChars.add('4');
		hexChars.add('5');
		hexChars.add('6');
		hexChars.add('7');
		hexChars.add('8');
		hexChars.add('9');
		hexChars.add('A');
		hexChars.add('B');
		hexChars.add('C');
		hexChars.add('D');
		hexChars.add('E');
		hexChars.add('F');

	}

	/**
	 * Description: initializes a valid set of characters based on their ASCII
	 * representation (48-57) & (65-90) & (97-122) & (32)
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures a set of valid characters is initialized
	 */
	private static void initializeValidChars() {
		validChars = new HashSet<Character>();
		validChars.add((char) 32);
		for (int i = 48; i <= 57; i++) {
			validChars.add((char) i);
		}
		for (int i = 65; i <= 90; i++) {
			validChars.add((char) i);
		}
		for (int i = 97; i <= 122; i++) {
			validChars.add((char) i);
		}
	}

	/**
	 * Description: initializes the set vaildChars if it hasn't already been
	 * initialized, and then determines if the character is in the set of
	 * validChars
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures validChars is initialized
	 * @return true if c is a valid character, false if c is not a valid
	 *         character.
	 */
	public static boolean isValidChar(char c) {
		if (validChars == null) {
			initializeValidChars();
		}
		return validChars.contains(c);
	}

	/**
	 * Description: initializes the set hexChars if it hasn't already been
	 * initialized, and then determines if the character is in the set of
	 * hexChars
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures hexChars is initialized
	 * @param c
	 *            - character being checked
	 * @return true if c is a hex character, false if c is not a hex character.
	 */
	public static boolean isHexChar(char c) {
		if (hexChars == null) {
			initializeHexChars();
		}
		return hexChars.contains(c);
	}

	/**
	 * Description: if ASCIIDigits has not been initialized, it is initialized
	 * and then it checks if the integer passed in is an ASCII encoded decimal
	 * digit
	 * 
	 * @param i
	 *            integer being checked
	 * @requires true
	 * @alters N/A
	 * @ensures ASCIIDigits is initialized
	 * @return true iff i is an ASCII encoded decimal digit.
	 */
	public static boolean isASCIIDigit(int i) {
		if (ASCIIDigits == null) {
			initializeASCIIDigits();
		}
		return ASCIIDigits.contains(i);
	}

	/**
	 * Description: initializes ASCIIPrintable if it hasn't been already, then
	 * checks if the integer passed in is an ASCII encoded printable character
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures ASCIIPrintable is initialized
	 * @param i
	 *            - integer being checked
	 * @return true iff int i is an ASCII encoded printable character
	 */
	public static boolean isASCIIPrintable(int i) {
		if (ASCIIPrintable == null) {
			initializeASCIIPrintable();
		}
		return ASCIIPrintable.contains(i);
	}

	/**
	 * Description: Determines if the integer passed in is an ASCII encoded
	 * non-zero digit.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures i is unchanged.
	 * @return true iff the integer passed in is a nonzero ASCII encoded digit.
	 * @param i
	 *            - integer to be checked
	 * @return true iff int i is an ASCII character but not ASCIIZERO
	 */
	public static boolean isASCIINonZeroDigit(int i) {
		return isASCIIDigit(i) && (i != ASCIIZERO);
	}

	/**
	 * Description: returns true if nextChar is a separator character, false
	 * otherwise
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures nextChar unchanged
	 * @param nextChar
	 *            - integer to be checked
	 * @return true iff the integer passed in is a separator ASCII encoded digit
	 */
	public static boolean isSeparator(int nextChar) {
		if (ASCIISeparators == null) {
			initializeASCIISeparators();
		}
		return ASCIISeparators.contains(nextChar);
	}
}