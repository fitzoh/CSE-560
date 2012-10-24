package vm;

import utilities.BinHexConverter;
import utilities.MachineInfo;

/**
 * Can be used to represent a field of bits. A BitField is represented by two
 * parts, an integer for length (length), and an array of boolean for the
 * representation of the bits (rep).This class has many BitField constructors
 * which take the length of the BitField to be created and either a string of
 * hexadecimal characters, a decimal integer, or a BitField as the values to be
 * placed in the BitField. There is also a default constructor that just takes
 * the length and sets all the bits to false. It also has methods for converting
 * the BitFields to strings of binary or hex. It also has methods for
 * incrementing the BitField, for converting a BitField to a decimal integer,
 * for accessing a specific bit from a BitField, and accessing a substring of a
 * BitField.
 * 
 * @author Dragon Slayer
 */
public class BitField {
	private final int length;
	private static final int BITS_IN_WORD = MachineInfo.BITS_IN_WORD;
	private static final int BITS_IN_ADDRESS = MachineInfo.BITS_IN_ADDRESS;
	protected Boolean[] rep;

	/**
	 * Description: Sets the BitField to the length of the int length parameter.
	 * Initializes the BitField to all false.
	 * 
	 * @requires true
	 * @alters creates a BitField
	 * @ensures Sets the BitField to the length of the int length parameter.
	 *          Initializes the BitField to all false.
	 * @param length
	 *            - length of the BitField
	 */
	public BitField(int length) {
		this.length = length;
		this.rep = new Boolean[length];
		for (int i = 0; i < length; i++) {// default to all zero
			this.rep[i] = false;
		}
	}

	/**
	 * Description: Sets the BitField to the length of the int length parameter.
	 * Converts the hex string into its binary representation, and then converts
	 * the binary representation into the correct boolean values (See class
	 * BinHexConverter)
	 * 
	 * @requires hex be a String of Hex digits (0-F)
	 * @alters creates a BitField
	 * @ensures Sets the BitField to the length of the int length parameter.
	 *          Converts the hex string into its binary representation, and then
	 *          converts the binary representation into the correct boolean
	 *          values (See class BinHexConverter)
	 * @param hex
	 *            - string of hex characters to be converted to its boolean
	 *            representation
	 * @param length
	 *            - length of the BitField
	 */
	public BitField(String hex, int length) {
		this.length = length;
		this.rep = new Boolean[length];
		String bin = BinHexConverter.hexToBin(hex);// convert to binary string
		for (int i = 0; i < length; i++) {// and change rep bit by bit
			this.rep[length - (i + 1)] = BinHexConverter.binToBool(bin
					.charAt(i));
		}
	}

	/**
	 * Description: Sets the BitField to the length of the int length parameter.
	 * Converts the decimal based integer into its boolean representation.
	 * 
	 * @requires true
	 * @alters creates a BitField
	 * @ensures Sets the BitField to the length of the int length parameter.
	 *          Converts the decimal based integer into its boolean
	 *          representation.
	 * @param dec
	 *            - decimal number to be converted to its boolean representation
	 * @param length
	 *            - length of the BitField
	 */
	public BitField(int dec, int length) {
		this(BinHexConverter.toHexString(dec, length), length);
	}

	/**
	 * Description: Sets the BitField to the length of the int length parameter.
	 * Converts (copies) BitField b into its boolean representation.
	 * 
	 * @requires true
	 * @alters creates a BitField
	 * @ensures Sets the BitField to the length of the int length parameter.
	 *          Converts (copies) BitField b into its boolean representation to.
	 * @param b
	 *            - BitField to be converted(copied) to its boolean
	 *            representation
	 * @param length
	 *            - length of the BitField
	 */
	public BitField(BitField b, int length) {
		this.length = length;
		this.rep = new Boolean[length];
		for (int i = 0; i < length; i++) {
			this.rep[i] = b.rep[i];
		}
	}

	/**
	 * Description: Converts (copies) BitField b into its boolean
	 * representation.
	 * 
	 * @requires true
	 * @alters creates a BitField
	 * @ensures Converts (copies) BitField b into its boolean representation.
	 * @param b
	 *            - BitField to be converted(copied) to its boolean
	 *            representation
	 */
	public BitField(BitField b) {
		this(b, b.length);
	}

	/**
	 * Description: Creates and returns a copy of the BitField.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures original is unchanged
	 * @return a copy of the BitField
	 */
	public BitField get() {
		return new BitField(this);
	}

	/**
	 * Description: returns a copy (for immutability) of the bit field that is
	 * incremented. (has one added to the original value.)
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures (see returns) the BitField that this method is called on remains
	 *          the same.
	 * @return an incremented copy of the BitField.
	 */
	public BitField increment() {
		BitField result = new BitField(this);
		boolean carry = true;// the initial +1
		for (int i = 0; i < this.length; i++) {
			if (!carry && !this.rep[i]) {// 0 + 0 = 0
				result.rep[i] = false;
				carry = false;
			} else if (carry && this.rep[i]) {// 1 + 1 = 10
				result.rep[i] = false;
				carry = true;
			} else {// 1 + 0 = 1
				result.rep[i] = true;
				carry = false;
			}
		}
		return result;
	}

	/**
	 * Description: This method returns the decimal value of the BitField that
	 * it is called on
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures BitField is unchanged.
	 * @return the decimal value of the BitField
	 */
	public int toDecInt() {
		return Integer.parseInt(this.toBinString(), 2);
	}

	/**
	 * Description: converts the specified part of the BitField into a string.
	 * low is the lower bound, high is the upper bound of the part to be
	 * converted.
	 * 
	 * @requires true alters: N/A
	 * @ensures: BitField is unchanged.
	 * @param low
	 *            - the lower bound of BitField to be converted to a string
	 * @param high
	 *            - the upper bound of the BitField to be converted to a string
	 * @return a string of binary characters from the BitField specified by low
	 *         and high
	 */
	public String binSubstring(int low, int high) {
		StringBuffer binString = new StringBuffer();
		for (int i = 0; i < high - low; i++) {
			binString.append(BinHexConverter
					.boolToBin(this.rep[high - (i + 1)]));
		}
		return binString.toString();
	}

	/**
	 * Description: returns the BitField rep as a string of binary characters
	 * (0's and 1's)
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures BitField is unchanged
	 * @return a string of binary characters (0's and 1's) that correspond to
	 *         the BitField rep
	 */
	public String toBinString() {
		return this.binSubstring(0, this.length);
	}

	/**
	 * Description: returns the BitField rep as a string of upper case hex
	 * characters
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures BitField is unchanged
	 * @return the BitField rep as a string of upper case hex characters
	 */
	public String toHexString() {
		return BinHexConverter.toHexString(this.toDecInt(), this.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.toHexString();
	}

	/**
	 * Description: returns the boolean value at the specified index
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures BitField is unchanged
	 * @return the boolean value at the specified index
	 * @param index
	 *            - position in BitField
	 */
	public boolean getBit(int index) {
		return this.rep[index];
	}

	/**
	 * Description: Extends the BitField address to be the length of a BitField
	 * word. The new word has the same value as the address. (An address has 8
	 * bits and a Word has 20. The last 8 bits of the word are the bits from the
	 * address, the first twelve are false.) This BitField word is returned.
	 * 
	 * @requires true
	 * @alters initializes a word to have the value of the address it was called
	 *         on.
	 * @ensures The address this method is called on is not changed.
	 * @return a BitField word that has the same numerical value as the address
	 *         it was called on.
	 */
	public BitField extendToWord() {
		BitField b = new BitField(BITS_IN_WORD);
		for (int i = 0; i < this.length; i++) {
			b.rep[i] = this.rep[i];
		}
		return b;
	}

	/**
	 * Description: creates a BitField out of the bits 0 - 7 of the BitField it
	 * is called on, and returns it.
	 * 
	 * @requires true
	 * @alters creates a new BitField whose value is the bits 0 - 7 of the
	 *         #BitField
	 * @ensures original BitField remains the same.
	 * @return A new BitField whose value is the bits 0 - 7 of the #BitField
	 */
	public BitField parseAddress() {
		// Word is 5 hex chars, we want the last 2 to make an address
		String hexAdd = this.toString().substring(3);
														
		return new BitField(hexAdd, BITS_IN_ADDRESS);
	}

	/**
	 * Description: adds w to the BitField the method is called on and returns
	 * the result as a new BitField.
	 * 
	 * @requires w must be a valid BitField
	 * @alters new word is created
	 * @ensures w and the BitField the method is called on remain the same
	 * @return a new Word resulting from the boolean addition of w1 & w2.
	 * @param w
	 *            - BitField to be added to the BitField the method was called
	 *            on.
	 */
	public BitField add(BitField w) {
		BitField result = new BitField(this.length);
		boolean carry = false;
		for (int i = 0; i < this.length; i++) {
			// addition is done bitwise with propagating carry
			int numTrue = BinHexConverter.boolToBin(this.rep[i])
					+ BinHexConverter.boolToBin(w.rep[i])
					+ BinHexConverter.boolToBin(carry);
			result.rep[i] = (numTrue % 2 == 1);
			carry = numTrue > 1;
		}
		return result;
	}

	/**
	 * Description: returns an inverted copy of the BitField. Each bit of the
	 * BitField is inverted, (false bits becomes true & true bits become false)
	 * 
	 * @requires true
	 * @alters creates a new inverted BitField.
	 * @ensures immutability of the BitField
	 * @return an inverted copy of the BitField.
	 */
	public BitField invert() {
		// used as helper for 2's complement calculation

		BitField result = new BitField(this.length);
		for (int i = 0; i < this.length; i++) {
			result.rep[i] = !this.rep[i];
		}
		return result;
	}

	/**
	 * Description: returns the 2's complement value of the Bitfield w passed
	 * in.
	 * 
	 * @requires true
	 * @alters creates a new 2's complement BitField
	 * @ensures immutability of the BitField
	 * @return a new BitField which is the 2's complement value of the BitField
	 *         the method was called on.
	 */
	public BitField negate() {
		// invert and add one for 2's complement
		BitField result = this.invert();
		result = result.increment();
		return result;
	}

	/**
	 * Description: Subtracts w from the BitField the method was called on and
	 * returns a new BitField with this value. (Boolean subtraction of the
	 * BitField) The subtraction is done by negating w and then performing
	 * boolean addition with the BitField and the negated value of w.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures immutability of the BitField
	 * @return a new BitField which is the result of subtracting w from the
	 *         BitField the method was called on.
	 */
	public BitField subtract(BitField w) {
		// to subtract just negate and add
		BitField result = new BitField(this.length);
		BitField minus = w.negate();
		result = this.add(minus);
		return result;
	}

	/**
	 * Description: Shifts the boolean values of the BitField times times to the
	 * left. The bits that are shifted past the end of the left are discarded.
	 * The bits in the right are filled in with false. If times is larger than
	 * one less than the length of the BitField or is a negative number, an
	 * IllegalArgumentException is thrown.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures w and times are unchanged
	 * @returns: a new BitField if times is a valid value, in which the bits of
	 *           w are shifted to the left times times is returned, else an
	 *           IllegalArgumentException is thrown.
	 * @throws IllegalArgumentException
	 */
	public BitField shiftLeft(int times) throws IllegalArgumentException {
		if (times > this.length - 1 || times < 0) {
			// shift amount must be between zero and word size
			throw new IllegalArgumentException("invalid shift amount");
		}
		BitField result = new BitField(this.length);
		for (int i = 0; i < times; i++) {
			result.rep[i] = false;
		}
		for (int i = 0; i < this.length - times; i++) {
			result.rep[i + times] = this.rep[i];
		}
		return result;
	}

	/**
	 * Description: Shifts the boolean values of the BitField times times to the
	 * right. The bits that are shifted past the end of the right are discarded.
	 * The bits in the right are filled in with same value as the original left
	 * most bit. (Signed shift right) If times is larger than one less than the
	 * length of the BitField or is a negative number, an
	 * IllegalArgumentException is thrown.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures w and times are unchanged
	 * @return a new BitField if times is a valid value, in which the bits of w
	 *         are shifted to the right times times is returned, with the
	 *         leading bits extending the sign or the boolean value of the
	 *         original Word, else an IllegalArgumentException is thrown.
	 * @param times
	 *            - amount of bit shifts to shift the BitField to the right.
	 * @return
	 */
	public BitField shiftRight(int times) throws IllegalArgumentException {
		if (times > this.length - 1 || times < 0) {
			// shift amount must be between zero and word size
			throw new IllegalArgumentException("Invalid shift amount");
		}
		BitField result = new BitField(this.length);
		for (int i = 0; i < times; i++) {
			result.rep[this.length - (i + 1)] = this.rep[this.length - 1];
		}
		for (int i = times; i < this.length; i++) {
			result.rep[i - times] = this.rep[i];
		}
		return result;
	}

	/**
	 * Description: multiplies the values of w with the BitField the method was
	 * called on and returns this value in a new BitField.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures w is unchanged and immutably of the BitField
	 * @param w
	 *            - value that the BitField the method was called on will be
	 *            multiplied by.
	 * @return a new BitField that is the result of multiplying the BitField the
	 *         method was called on with w.
	 */
	public BitField multiply(BitField w) {
		
		// binary multiplication is just repeated addition
		BitField result = new BitField(this.length);
		for (int i = 0; i < this.length; i++) {
			if (w.rep[i]) {
				// shift left, add if current bit is positive
				result = result.add(this.shiftLeft(i));
														 
			}
		}
		return result;
	}

	/**
	 * Description: returns true if the BitField represents a positive number.
	 * (if the left most bit of the word is false), else returns false
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures BitField remains the same.
	 * @return true if BitField represents a positive number. (if the left most
	 *         bit of the word is false, else returns false
	 */
	public boolean isPositive() {
		// check last bit for 2's complement negative value
		return !this.rep[this.length - 1];
	}

	/**
	 * Description: returns true iff the BitField represents the number zero.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures BitField is unchanged
	 * @return true iff the BitField represents the number zero.
	 */
	public boolean isZero() {
		// returns true if all bits are zero
		boolean result = false;
		for (int i = 0; i < this.length; i++) {
			result |= this.rep[i];
		}
		return !result;
	}

	/**
	 * Description: Divides the BitField the method was called on by the
	 * BitField w using boolean division. Let n, d, q and r, be the numerator,
	 * the denominator, the quotient and the remainder respectively. Then we
	 * define division by n/d = q, where n = q x d + r and 0 <= r < d (Uses long
	 * division)
	 * 
	 * @requires true
	 * @alters new BitField which is the result of the division
	 * @ensures w and the BitField the method was called on are unchanged.
	 * @return a new BitField which is the result of the BitField the method was
	 *         called on being divided by w.
	 * @param w
	 *            - denominator
	 */
	public BitField divideBy(BitField w) {
		if (w.isZero()) {
			throw new IllegalArgumentException("Dividing by zero");
		}
		BitField result = new BitField(this.length);
		BitField dividend = new BitField(this);
		BitField divisor = new BitField(w);
		
		// store current signs for later
		boolean dividendIsPos = this.isPositive();
		boolean divisorIsPos = divisor.isPositive();
		
		// make positive for division
		if (!dividendIsPos) {
			dividend = dividend.negate();
		}
		// make positive for division
		if (!divisorIsPos) {
			divisor = divisor.negate();
		}
		// keep track of current index of dividend
		int index = 0;
		
		// shift left until first 1 of divisor is left aligned
		while (divisor.isPositive()) {							
			divisor = divisor.shiftLeft(1);
			index++;
		}
		// and shift back 1 to make it non-negative
		divisor = divisor.shiftRight(1);
					
		divisor.rep[this.length - 1] = false;
		index--;
		
		// standard long division with binary numbers
		while (!divisor.isZero() && index >= 0) {
			BitField divide = dividend.subtract(divisor);
			if (divide.isPositive()) {
				result.rep[index] = true;
				dividend = divide;
			}
			divisor = divisor.shiftRight(1);
			index--;
		}
		
		// if they have opposite signs, the result is negative
		if (divisorIsPos != dividendIsPos) {
			result = result.negate();
		}
		return result;
	}

	/**
	 * Description: returns a new BitField that is the bit wise or of the values
	 * of w and the BitField the method was called on according to boolean
	 * algebra.
	 * 
	 * @requires true
	 * @alters new BitField which is the result of the bitwise or
	 * @ensures w and the BitField the method was called on are unchanged.
	 * @return new BitField that is the bit wise or of the values of w and the
	 *         BitField the method was called on according to boolean algebra.
	 * @param w
	 *            - BitField to be �or�ed with the BitField the method was
	 *            called on
	 */
	public BitField or(BitField w) {
		
		// simple bitwise or
		BitField result = new BitField(this.length);
		for (int i = 0; i < this.length; i++) {
			result.rep[i] = this.rep[i] || w.rep[i];
		}
		return result;
	}

	/**
	 * Description: returns a new BitField that is the bit wise and of the
	 * values of w and the BitField the method was called on according to
	 * boolean algebra.
	 * 
	 * @requires true
	 * @alters new BitField which is the result of the bitwise and
	 * @ensures w and the BitField the method was called on are unchanged.
	 * @return new BitField that is the bit wise and of the values of w and the
	 *         BitField the method was called on according to boolean algebra.
	 * @param w
	 *            - BitField to be �and�ed with the BitField the method was
	 *            called on
	 */
	public BitField and(BitField w) {
		
		// simple bitwise and
		BitField result = new BitField(this.length);
		for (int i = 0; i < this.length; i++) {
			result.rep[i] = this.rep[i] && w.rep[i];
		}
		return result;
	}

	/**
	 * Description: determines is the int value (decimal integer value.) can be
	 * represented as a 2's complement BitField with the specified number of
	 * bits (numBits). Returns true if value can be represented as a 2's
	 * complement BitField of length numBits, false otherwise.
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures: value is unchanged
	 * @param value
	 *            the value being checked
	 * @param numBits
	 *            the number of bits to try to represent value in
	 * @return true if value can be represented as a 2's complement BitField of
	 *         length numBits, false otherwise.
	 */
	public static boolean isValid2sComp(int value, int numBits) {
		return (value >= (Math.pow(2, numBits - 1) * -1) && value <= Math.pow(
				2, numBits - 1) - 1);
	}

	/**
	 * Description: returns the decimal integer value of the 2's complement
	 * BitField it is called on.
	 * 
	 * @requires the Word it is called on is in 2's complement form
	 * @alters N/A
	 * @ensures the BitField is not changed.
	 * @return the decimal integer value of the 2's complement BitField it is
	 *         called on.
	 */
	public int toDecInt2sComp() {
		boolean neg = !this.isPositive();
		int val;
		if (neg) {
			val = this.negate().toDecInt() * -1;
		} else {
			val = this.toDecInt();
		}
		return val;
	}

}
