package sourceRecord;


/**
 * This class is used to hold the information for an operand. An Operand has 
 * five fields: label, value, lowVal, highVal, and hasLabel. They are set 
 * according to the constructor. This class contains methods to get the label 
 * of the Operand and the value of the Operand. It also contains methods to set 
 * the value of an Operand and to look up the label of an Operand in a SymbolTable 
 * and set the value of the label to the value of the label in the SymbolTable.
 * @author Dragon Slayer
 */
public class Operand {
	private String label;
	private int value;
	private int lowVal;
	private int highVal;
	private boolean hasLabel; //True iff the operand has label
	private boolean isAbsolute; //True iff the label is absolute

	/**
	 * Description: Immutable Operand constructor. It sets the Operand fields 
	 * lowVal and highVal to the parameters low and high respectively. low and 
	 * high are the lowest and highest possible operand decimal, integer values 
	 * (respectively) allowed by the operand. label is set to the value of 
	 * valOrLabel, if valOrLabel is a label . If the operand is not a label, 
	 * value is set to the decimal integer value of the operand. The boolean 
	 * hasLabel is set to true if the Operand is a label. It throws an 
	 * InvalidRecordExeption if its  (value < lowVal) or (value > highVal)
	 * @requires true
	 * @ensures  valOrlabel, low, and high are unchanged. The fields of
	 * Operation are set.
	 * @param valOrLabel the Operand which is either a value or a label
	 * @param low the lowest decimal value of the operand allowed by 
	 * the operation
	 * @param high the highest decimal value of the operand allowed by 
	 * the operation
	 * @throws InvalidRecordException
	 */
	public Operand(String valOrLabel, int low, int high)
			throws InvalidRecordException {
		this.lowVal = low;
		this.highVal = high;
		isAbsolute=true;
		if (valOrLabel.matches("-?\\d+")) {
			value = Integer.parseInt(valOrLabel);
			if (value < lowVal || value > highVal) {
				throw new InvalidRecordException("Error_Code 301: Operand value must be between "+lowVal+" and "+highVal);
			}
			hasLabel = false;
		} else { //If the operand has label
			label = (valOrLabel+' ').substring(0,(valOrLabel+' ').indexOf(' '));
			hasLabel = true;
		}
	}

	/**
	 * Description: if the Operand has a label, this label is looked up in 
	 * the SymbolTable, labels, and value is set to the numeric value of that 
	 * symbol in the table.
	 * @requires true
	 * @alters if label is in the SymbolTable, value is set to the numeric value of 
	 * that symbol.
	 * @ensures labels and label are unchanged
	 * @param labels the table in which the label will be looked up to find its corresponding value.
	 * @throws InvalidRecordException
	 * @throws LabelNotDefinedException
	 */
	public void replaceLabel(SymbolTable labels) throws InvalidRecordException,
			LabelNotDefinedException {
		if (hasLabel) {
			setValue(labels.getLabel(label));
			isAbsolute= labels.isAbsolute(label);
		}
	}

	/**
	 * Description: returns the label of the Operand as a string
	 * @requires true
	 * @alters N/A
	 * @ensures label is unchanged.
	 * @return the label of the Operand as a string
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Description: returns true iff the Operand has a label
	 * @requires true
	 * @alters N/A
	 * @ensures label is unchanged.
	 * @return true iff the Operand has a label
	 */
	public boolean hasLabel(){
		return hasLabel;
	}
	/**
	 * Description:  sets the value of the Operand to labelValue unless 
	 * (value < lowVal) or (value > highVal), in which case an 
	 * InvalidRecordException is thrown.
	 * @requires true
	 * @alters value
	 * @ensures value is set to labelValue
	 * @param labelValue value is set to this
	 * @throws InvalidRecordException
	 */
	public void setValue(int labelValue) throws InvalidRecordException {
		value = labelValue;
		if (value < lowVal || value > highVal) {
			throw new InvalidRecordException("Error_Code 301: Operand value must be " +
					"between "+lowVal+" and "+highVal);
		}
	}

	/**
	 * Description: returns the numeric value of the Operand 
	 * @requires true
	 * @alters N/A
	 * @ensures value is unchanged.
	 * @return the numeric value of the Operand
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Description: returns true iff the Symbol is absolute 
	 * @requires true
	 * @alters N/A
	 * @ensures Nothing is changed.
	 * @return true iff the Symbol is absolute
	 */
	public boolean isAbsolute(){
		return isAbsolute;
		
	}
}
