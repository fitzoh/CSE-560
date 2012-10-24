package vm;

/**
 * If an illegal memory address is attempted, this is thrown. An illegal memory
 * address would be outside the range of (0 – 255)
 * 
 * @author Dragon Slayer
 */
public class IllegalMemoryAddressException extends Exception {

	private static final long serialVersionUID = 4112684946493703809L;

}
