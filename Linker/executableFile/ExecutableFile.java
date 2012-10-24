package executableFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import objectFile.InvalidObjectFileException;
import objectFile.ObjectFile;

/**
 * This class is used to process the object-input files to create an executable-output 
 * file. It contains methods to create an ObjectFile for each object-input file, get the 
 * load address from the user for the executable-output file. It also contains a method 
 * which makes calls to create an external symbol table and update all the ObjectFiles 
 * with the external symbol values and the offset of the load address. An ExecutableFile 
 * has 5 private fields: an ObjectFile for the main object-input file, a List of ObjectFiles 
 * for the rest of the object-input files, an integer for the executable-output load address, 
 * an integer for the total length of the executable-output file, and a Map<String, Integer> 
 * as an external symbol table. 
 * @author Dragon Slayer
 */
public class ExecutableFile {

	private ObjectFile main;
	private List<ObjectFile> objectList;
	private int loadAddress;
	private int totalLength;
	private Map<String, Integer> symbolTable;

	/**
	 * Description: Initializes main to null. Creates an ObjectFile for each object-input 
	 * file. Makes calls to populate main, objectList, and symbolTable. 
	 * @requires true
	 * @ensures main, objectList, symbolTable, loadAddress and totalLength are all set
	 * (by other method calls in the class).
	 * @param objectFiles - array of object-input files to be processed.
	 * @throws IOException
	 * @throws InvalidObjectFileException
	 * @throws InvalidExecutableException
	 */
	public ExecutableFile(File[] objectFiles) throws IOException,
			InvalidObjectFileException, InvalidExecutableException {
		main = null;
		objectList = new LinkedList<ObjectFile>();
		symbolTable = new HashMap<String, Integer>();
		for (File f : objectFiles) {
			createObjectFile(f);//one ObjectFile created for each input file
		}
		if (totalLength > 255) {
			throw new InvalidExecutableException("Error_Code 008: Executable length > 255");
		}
		if(main == null){
			throw new InvalidExecutableException("Error_Code 009: No main segment found.");
		}
	}

	/**
	 * Description: This method gets the load address and uses its value as the 
	 * offset for the address of all the ObjectLines. It sets the addresses of 
	 * all the ObjecLines in all the ObjectFiles. (Main will be the first ObjectFile 
	 * of the executable –output file). Then, this method populates the external symbol 
	 * table by going through main and the objectList and adding all external symbols 
	 * and their address values to the table. It then goes through main and the 
	 * objectList again, replacing the external symbols with their addresses from 
	 * the external symbol table. The header record ObjectLine in the main ObjectFile 
	 * is then updated with segment load address and execution start (which are the 
	 * same). Then, main is printed to the executable-output file, followed by each 
	 * objectFile in the objectList.
	 * @requires true
	 * @ensures symbolTable, main, loadAddress, loadAddress, objectList are all set.
	 * @alters symbolTable, main, loadAddress, loadAddress, objectList
	 * @param outputFile the resulting executable-output file
	 * @throws InvalidObjectFileException
	 * @throws IOException
	 */
	public void processAndGenOutput(File outputFile) throws InvalidObjectFileException, IOException{
		main.setOffset(loadAddress);//main will be the first segment of the result
		int counter = main.getLength() ;//maintain counter of offset from load address
		for(ObjectFile o:objectList){
			o.setOffset(loadAddress+counter);
			counter+=o.getLength();
		}
		main.getEntries(symbolTable);//populate symbol table
		for(ObjectFile o:objectList){
			o.getEntries(symbolTable);
		}
		main.replaceExt(symbolTable);//and replace all external symbols
		for(ObjectFile o:objectList){
			o.replaceExt(symbolTable);
		}
		main.setLength(totalLength);//update header in Main segment
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(outputFile));
			out.write(main.toString());//print main first
			for(ObjectFile f: objectList){//then the rest of the ObjectFiles
				out.write(f.toString());
			}
		} catch (IOException e) {
			throw new IOException ("Error_Code 666: Fatal IO error");
		} finally{
			if(out!=null){
				out.close();
			}
		}
	}
	
	/**
	 * Description: Reads in an object-input file. As object-input files are read in, 
	 * the ExecutableFile field totalLength contains the total length of all the 
	 * object-input files read in so far. The main object-input file is put into its 
	 * own ObjectFile(ExecutableFile field main). The rest of the object-input files 
	 * are added to a list of ObjectFiles (ExecutableFile field objectList).
	 * @requires true
	 * @ensures totalLength, main, and objectList are set.
	 * @param f object-input file being read from
	 * @throws IOException
	 * @throws InvalidObjectFileException
	 * @throws InvalidExecutableException
	 */
	private void createObjectFile(File f) throws IOException,
			InvalidObjectFileException, InvalidExecutableException {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(f));
			ObjectFile currentFile = new ObjectFile(in);
			totalLength += currentFile.getLength();
			if (currentFile.isMain()) {
				if(main!=null){//if main has already been assigned there are 2 mains. this is bad
					throw new InvalidExecutableException("Error_Code 108: Multiple main segments found");
				}
				main = currentFile;//store main segment in own variable
			} else {
				objectList.add(currentFile);//if not main add to list
			}
		} catch (IOException e) {
			throw new IOException("Error_Code 666: Fatal IO error");
		}
	}

	/**
	 * Description: Outputs the total segment length to System out and prompts the 
	 * user to enter a load address. If the user doesn’t input a valid address, the 
	 * method continues to prompt for a load address until a valid one is given.
	 * @requires true
	 * @ensures load address for the executable file header is set. (ExecutableFile 
	 * field loadAddress)
	 * @throws IOException
	 */
	public void getLoadAddress() throws IOException {
		System.out.println("Segment length is " + totalLength
				+ "; Please enter a load address:");
		BufferedReader console = null;
		boolean valid = false;
		int value;
		try {
			console = new BufferedReader(new InputStreamReader(System.in));
			while (!valid) {//repeatedly prompt for input until good input is received
				String userInput = console.readLine();
				if (userInput.matches("[0-9]+")) {//regex to make sure it's an integer
					value = Integer.parseInt(userInput);
					if(value+totalLength>255){
						System.out.println("Load address + program length must be < 255, please try again");
					} else{
						loadAddress = value;
						valid = true;
					}
				} else{
					System.out.println("Invalid input, please enter an integer");
				}
			}
		} catch (IOException e) {
			throw new IOException("Error_Code 666: Fatal IO error");
		} finally{
			if(console!=null){
				console.close();
			}
		}
	}
}
