package sourceRecord;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is an object used to represent the Literal Pool for the Assembler. 
 * It is a map with the name of the literal being the key value (string). The 
 * dependent is an integer that is the offset of the address of the literal. 
 * This class is immutable. It contains methods to add literals to the pool, 
 * get a copy of the LiteralPool, and a method to create a list of SourceRecords 
 * out of the LiteralPool. 
 * 
 * @author Dragon Slayer
 */
public class LiteralPool {
	private Map<String, Integer> rep;
	private int offset;

	/**
	 * Description: (Constructor) Creates an immutable LiteralPool that is a map, 
	 * with a string key as the literals name and an integer, the offset of the 
	 * address  of the literal, as the dependent. The offset is set to one.
	 * @requires true
	 * @ensures a new LiteralPool (map) is created. Immutable 
	 */
	public LiteralPool() {
		rep = new HashMap<String, Integer>();
		offset=0;
	}

	/**
	 * Description: Adds string as the key value of the literal with the dependent 
	 * integer of the literal being the one greater than the size of the LiteralPool 
	 * (e.g. if there were 2 literals in the pool, the next literal added will have 
	 * its dependent value set to 3) If string already exists in the LiteralPool, 
	 * nothing is done.  
	 * @requires true
	 * @alters a new Literal (pair) is added to the LiteralPool with string being the 
	 * key value of the map, and offset being one greater than the size of the LiteralPool
	 * @ensures string is unchanged
	 * @param string literal name to be added to the LiteralPool
	 * @throws InvalidRecordException 
	 */
	public void addLiteral(String string) throws InvalidRecordException {
		if(!rep.containsKey(string)){
			//There should be less than 50 literals in source code
			if(rep.size()>=50){
				throw new InvalidRecordException("Error_Code 300: 50 literals exceeded");
			}
			rep.put(string, offset); //Put the literal into the literal pool
			offset++; //Increase the LC by 1
		}
	}
	
	/**
	 * Description: returns a copy of the whole LiteralPool.
	 * @requires true
	 * @alters N/A
	 * @ensures the LiteralPool is unchanged. 
	 * @return a copy of every literal pair in the LiteralPool.
	 */
	public Map<String, Integer> getCopy(){
		Map<String,Integer> copy = new HashMap<String, Integer>();
		for(String s:rep.keySet()){
			copy.put(s, rep.get(s));
		}
		return copy;
	}
	
	/**
	 * Description: Creates a SourceRecord (dynamic type LiteralRecord) for each 
	 * literal in the literal pool and places them in a list. The key value of the 
	 * literal is used to set the string value of the SourceRecord and the dependent 
	 * integer is the address offset of the literal
	 * @requires true
	 * @alters creates a list of SourceRecords (LiteralRecords), out of the literals 
	 * in the LiteralPool
	 * @ensures the LiteralPool is unchanged.
	 * @return a SourceRecord (dynamic type LiteralRecord) for each literal in the 
	 * literal pool and places them in a list.
	 * @throws InvalidRecordException 
	 */
	public List<SourceRecord> getRecordList(int location) throws InvalidRecordException{
		List<SourceRecord> recordList = new ArrayList<SourceRecord>();
		for(String value : rep.keySet()){
			recordList.add(new LiteralRecord(value, location+rep.get(value)));
			//Address out of bounds
			if(location+rep.get(value)>255){
				throw new InvalidRecordException("Error_Code 312: Memory address > 255 detected");
			}
		}
		return recordList;
	}
}
