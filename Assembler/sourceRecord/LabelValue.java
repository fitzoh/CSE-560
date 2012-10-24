package sourceRecord;

/**
 * The class has only one method that is a constructor and can create and object 
 * with two values. One value corresponds to the integer value of a label and the 
 * other determines if the symbol is absolute.
 * @author Dragon Slayer
 */
public class LabelValue {
	public int value;
	public boolean absolute;
	
	/**
	 * Description: (Immutable Constructor)  Creates a new LabelValue with the 
	 * internal representation of the LabelValue value set to the  integer value 
	 * of the symbol (value) and the internal representation of the LableValue 
	 * absolute set to the boolean absolute passed into the constructor. 
	 * @requires true
	 * @ensures a new immutable LabelValue is created with the integer value of 
	 * the symbol and with a boolean determining if the symbol is absolute (true 
	 * if absolute) 
	 * @param value the integer value of the symbol
	 * @param absolute relocatable value of the symbol
	 */
	public LabelValue(int value, boolean absolute){
		this.value=value;
		this.absolute=absolute;
	}
	public LabelValue getCopy(){
		return new LabelValue(this.value, this.absolute);
	}
}
