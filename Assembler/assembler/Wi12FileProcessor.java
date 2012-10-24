package assembler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import sourceRecord.EndRec;
import sourceRecord.InvalidRecordException;
import sourceRecord.LabelAlreadyDefinedException;
import sourceRecord.OriRec;
import sourceRecord.SymbolTable;
import sourceRecord.LabelNotDefinedException;
import sourceRecord.LiteralPool;
import sourceRecord.RecordType;
import sourceRecord.SourceRecord;
import sourceRecord.SourceRecordFactory;

/**
 * Implements the interface, FileProcessor. This class processes the files. It 
 * creates and fills a SymbolTable, Literal Pool and a List of SourceRecords. It 
 * processes each record from the assembly-input file and prints to the object-output 
 * file and listing-output file (See Users Guide for formatting).
 * @author Dragon Slayer
 */
public class Wi12FileProcessor implements FileProcessor {

	SymbolTable labels;
	LiteralPool literals;
	List<SourceRecord> recordList;
	int location;
	private boolean absolute;
	File assembly, object, listing;

	/**
	 * Description: Initializes a SymbolTable, Literal Pool and a List of 
	 * SourceRecords. Sets the files as the assembly-input, object-output, 
	 * listing-output. Sets the location counter value to 0. Initializes the 
	 * flag as the assembly-input being absolute. 
	 * @requires true
	 * @ensures (see description)
	 * @param assembly file used as assembly-input file
	 * @param object file used as object-output file
	 * @param listing file used as listing-output file
	 */
	public Wi12FileProcessor(File assembly, File object, File listing) {
		labels = new SymbolTable();
		literals = new LiteralPool();
		recordList = new ArrayList<SourceRecord>();
		location = 0;
		absolute = true;
		this.assembly=assembly;
		this.object=object;
		this.listing=listing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see loader.FileProcessor#processFiles(java.io.File, java.io.File,
	 * java.io.File)
	 */
	@Override
	public void processFiles() throws IOException, InvalidRecordException,
			LabelAlreadyDefinedException, LabelNotDefinedException {
		int lineCount=0;
		try{
		int execStart=0;
		boolean execDefined=false;
		BufferedReader input = new BufferedReader(new FileReader(assembly));
		// read in first line from the assembly file
		String nextRecordString = input.readLine();
		if(nextRecordString == null){
			throw new InvalidRecordException("Error_Code 105: No ORI found");
		}
		lineCount++;
		// create a SourceRecord
		SourceRecord nextRecord = SourceRecordFactory.getSourceRecord(nextRecordString, location);
		//Cycle through any comments that may exist before ORI
		while(nextRecord.getType()==RecordType.COMMENT){
			recordList.add(nextRecord);
			nextRecordString = input.readLine();
			if(nextRecordString == null){
				throw new InvalidRecordException("Error_Code 105: No ORI found");
			}
			lineCount++;
			nextRecord = SourceRecordFactory.getSourceRecord(nextRecordString, location);
		}
		// If the first record isn't ORI, throw an exception
		if (nextRecord.getType() != RecordType.ORI) {
			throw new InvalidRecordException("Error_Code 101: First non-comment record must be ORI");
		}
		OriRec ORI = (OriRec) nextRecord;
		absolute = ORI.isAbsolute();
		location+=ORI.getLocOffset();
		// Add label values to the SourceRecord & add record to the record list
		nextRecord.addLabel(labels);
		recordList.add(nextRecord);
		while (nextRecord.getType() != RecordType.END) {// Read records until END
			nextRecordString = input.readLine();
			if(nextRecordString == null){
				throw new InvalidRecordException("Error_Code 106: No END found");
			}
			lineCount++;
			nextRecord = processRecord(nextRecordString);
			// find the execution start
			if(!execDefined && nextRecord.getType()==RecordType.EXEC){
				execDefined=true;
				execStart = location-1; 
			}
			
		}
		nextRecordString = input.readLine();
		if(!(nextRecordString == null || nextRecordString== "")){
			throw new InvalidRecordException("Error_Code 107: END must be followed by new line or end of File");
		}
		nextRecordString = input.readLine();
		if(nextRecordString!=null){
			throw new InvalidRecordException("Error_Code 107: END must be followed by new line or end of file");
		}
		// add literals to the list and replace labels
		EndRec END = (EndRec) nextRecord;
		int current = recordList.size();
		recordList.addAll(literals.getRecordList(location));
		labels.addLiterals(literals, location);
		// replace all labels in SourceRecords
		location += recordList.size()-current;
		lineCount=1;
		for (SourceRecord s : recordList) {
			s.replaceLabels(labels);
			lineCount++;
		}
		// set the execution start and segment length of the ORI
		if (END.getExecStart()!=-1){
			execStart=END.getExecStart();
		}
		ORI.setExecStart(execStart);
		ORI.setSegmentLength(location);
		printFiles(absolute); // print files
		}
		catch (Exception e){
			throw new InvalidRecordException("Line "+lineCount+"(+/-1): "+e.getMessage());
		}
	}

	/**
	 * Description: Creates two Writers: one to the object-output file and one 
	 * two the listing output file. Prints to the listing and object files 
	 * simultaneously line by line. 
	 * @requires true
	 * @alters listing and object files
	 * @ensures memory is unchanged.
	 * @param abs flags if the files will be printed out as absolute or relative
	 */
	private void printFiles(boolean abs) {
		Writer objectStream = null;
		Writer listingStream = null;
		try {
			objectStream = new BufferedWriter(new FileWriter(object));
			listingStream = new BufferedWriter(new FileWriter(listing));
			listingStream.write("LC Cntnt R     REC Label    Opn Operand/Comments\n");

			for (int i = 0; i < recordList.size(); i++) {
				objectStream.write(recordList.get(i).toExecutableString(
						abs));
				listingStream.write(recordList.get(i).toListingString(abs,
						i));
			}
		} catch (IOException e) {//fatal error and should not occur
			System.err.println("Error_Code 108: WHAT DID YOU DO?");
		} finally{
			closeQuietly(objectStream);
			closeQuietly(listingStream);
		}
	}

	/**
	 * Description: Creates a SourceRecord from the string recordString. If it was 
	 * the 200th SourceRecord to be created or if the SourceRecord is an ORI, an 
	 * InvalidRecordExeption is thrown. If the SourceRecord type is a RES or EQU 
	 * then the numeric values of the SourceRecords are replaced right away. For 
	 * any other type of SourceRecord(Other than ORI), labels are added to the 
	 * SymbolTable, literals are added to the LiteralTable, the SourceRecord is 
	 * added to a SourceRecord list, and the location counter is set based on the 
	 * location field of the SourceRecord
	 * @requires true
	 * @alters creates a SourceRecord, alters the SourceRecord list, may alter the 
	 * SymbolTable or LiteralTable
	 * @ensures recordString is unchanged
	 * @param recordString line from the assembly-input file which is used to create 
	 * a SourceRecord
	 * @return a copy of the created SourceRecord.
	 * @throws InvalidRecordException
	 * @throws LabelAlreadyDefinedException
	 * @throws LabelNotDefinedException
	 */
	private SourceRecord processRecord(String recordString)
			throws InvalidRecordException, LabelAlreadyDefinedException,
			LabelNotDefinedException {
		SourceRecord Record = SourceRecordFactory.getSourceRecord(recordString,
				location);
		if (recordList.size() == 200) {
			throw new InvalidRecordException("Error_Code 102: Max of 200 entries allowed");
		}
		if (Record.getType() == RecordType.ORI) {
			throw new InvalidRecordException("Error_Code 103:  Only 1 ORI entry allowed");
		}
		if (Record.getType() == RecordType.RES
				|| Record.getType() == RecordType.EQU) {
			Record.replaceLabels(labels);
		}
		// update tables, list, and location counter
		Record.addLabel(labels);
		Record.addLiteral(literals);
		location += Record.getLocOffset();
		if(location>256){
			throw new InvalidRecordException("Error_Code 104:  Memory limit exceeded");
		}
		recordList.add(Record);
		return Record;
	}
	
	/**
	 * Description: Closes the Writer stream
	 * @requires true
	 * @alters closes the Writer stream
	 * @ensures Writer stream is closed.
	 * @param write Writer stream being closed
	 */
	private void closeQuietly (Writer write){
		try{
			if(write!=null){
			write.close();
			}
		}catch (IOException e){
			
		}
	}

}
