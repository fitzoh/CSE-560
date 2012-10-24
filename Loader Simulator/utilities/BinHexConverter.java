package utilities;

/**
 * BinHexConverter can be used to convert boolean values to int, strings of hex
 * to binary, one hex character into binary, binary characters, to boolean, and
 * binary integers into boolean. Also contains a method to convert the decimal
 * value of the BinHexConverter to its corresponding string of hex characters.
 * 
 * @author Dragon Slayer
 */
public class BinHexConverter {

	/**
	 * Description: translates the boolean value into its corresponding integer
	 * value.
	 * 
	 * @requires N/A
	 * @alters: N/A
	 * @ensures: b is unchanged
	 * @param b
	 *            - parameter being translated into its integer equivalent
	 * @return the integer value 1 if the boolean was true, and integer value 0
	 *         if the boolean was false.
	 */
	public static int boolToBin(boolean b) {
		int i = b ? 1 : 0;
		return i;
	}

	/**
	 * Description: if the character passed in is a '1', it returns true, if it
	 * is '0' it will return false. Expected use for translating the char values
	 * of '1' and '0' to their boolean equivalent.
	 * 
	 * @requires char must be either a character '0' or a '1'
	 * @alters N/A
	 * @ensures b is unchanged
	 * @param b
	 *            - character to be translated into boolean
	 * @return If the character passed in is a ‘1’, it returns true, else it
	 *         will return false.
	 */
	public static boolean binToBool(char b) {
		return b == '1';
	}

	/**
	 * Description: if the integer passed in is a '1', it returns true, else it
	 * will return false. Expected use for translating only the integer numbers
	 * 1 and 0 to their boolean equivalent
	 * 
	 * @requires integer value b must be either '0' or '1'
	 * @alters N/A
	 * @ensures b is unchanged
	 * @param b
	 *            - integer to be translated into boolean
	 * @return If the integer passed in is a 1, it returns true, else it will
	 *         return false.
	 */
	public static boolean binToBool(int b) {
		return b == 1;
	}

	/**
	 * Description: translates a string of hex characters to a string of its
	 * binary (0's or 1's) equivalent
	 * 
	 * @requires String is only comprised of hex characters (0 - F)
	 * @alters N/A
	 * @ensures b is unchanged
	 * @param hex
	 *            - string of hex characters being translated into a string of
	 *            binary characters
	 * @return the translated string of hex characters as a string of its binary
	 *         (0's or 1's) equivalent
	 */
	public static String hexToBin(String hex) {
		StringBuffer bin = new StringBuffer();
		for (int i = 0; i < hex.length(); i++) {
			bin.append(oneHexToBin(hex.charAt(i)));
		}
		return bin.toString();
	}

	/**
	 * Description: Takes one hex character and translates it to its binary
	 * representation.
	 * 
	 * @requires char hex must be a character (0 - F)
	 * @alters N/A
	 * @ensures char is unchanged
	 * @param hex
	 *            - character to be translated into its binary representation
	 * @return the hex character's binary representation. If the char pasted in
	 *         is not a hex character (0 - F) , then it is translated to the
	 *         binary representation of hex zero (0000)
	 */
	public static String oneHexToBin(char hex) {
		String result = "0000";
		//select the right binary digits
		switch (hex) {
		case '0': {
			result = "0000";
			break;
		}
		case '1': {
			result = "0001";
			break;
		}
		case '2': {
			result = "0010";
			break;
		}
		case '3': {
			result = "0011";
			break;
		}
		case '4': {
			result = "0100";
			break;
		}
		case '5': {
			result = "0101";
			break;
		}
		case '6': {
			result = "0110";
			break;
		}
		case '7': {
			result = "0111";
			break;
		}
		case '8': {
			result = "1000";
			break;
		}
		case '9': {
			result = "1001";
			break;
		}
		case 'A': {
			result = "1010";
			break;
		}
		case 'B': {
			result = "1011";
			break;
		}
		case 'C': {
			result = "1100";
			break;
		}
		case 'D': {
			result = "1101";
			break;
		}
		case 'E': {
			result = "1110";
			break;
		}
		case 'F': {
			result = "1111";
			break;
		}
		}
		return result;
	}

	/**
	 * Description: creates a new String of hex characters that represents dec.
	 * numBits is the number of bits that will be represented by the hex string.
	 * if needed, '0's are added to the front of the hex string in order to make
	 * the string as long as specified by numBits. The length of the string will
	 * be numBits/4
	 * 
	 * @requires the dec number must be able to be represented in the numBits.
	 * @alters N/A
	 * @ensures dec and numBits and the String are unchanged.
	 * @param dec
	 *            - decimal number to be converted to a hex string
	 * @param numBits
	 *            - the number of bits that will be represented by the hex
	 *            string.
	 * @return the hex string value of dec. with the length of the string being
	 *         numBits/4
	 */
	public static String toHexString(int dec, int numBits) {
		//set up the string in hex
		StringBuffer hexString = new StringBuffer(Integer.toHexString(dec)
				.toUpperCase());
		while (hexString.length() < numBits / 4) {
			hexString.insert(0, '0');
		}
		return hexString.toString();
	}

}
