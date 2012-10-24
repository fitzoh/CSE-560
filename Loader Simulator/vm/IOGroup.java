package vm;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.Reader;

/**
 * Wraps the input reader and output writer into one class. Instead of using
 * both a Reader and Writer, an IOGroup can be used. It contains three fields, a
 * Reader, a Writer, and an integer which is the next character from input.
 * 
 * @author Dragon Slayer
 */
public class IOGroup {
	private Reader in;
	private BufferedOutputStream out;
	private int nextChar;

	/**
	 * Description: Sets the private fields of IOGroup. Sets in as the Reader,
	 * out as the Writer, and the next character is set to the next character
	 * from input. If this is unsuccessful, an IOException is thrown.
	 * 
	 * @requires true
	 * @alters IOGroup's reader and writer are set if possible, if not, an
	 *         IOExeption is thrown.
	 * @ensures immutablity of IOGroup
	 * @param in
	 *            - character stream reader being set for the IOGroup
	 * @param out2
	 *            - character stream writer being set for the IOGroup
	 * @throws IOException
	 */
	public IOGroup(Reader in, BufferedOutputStream out2) throws IOException {
		this.in = in;
		this.out = out2;
		nextChar = in.read();
	}

	/**
	 * Description: Returns the next character as an integer
	 * 
	 * @requires true
	 * @alters N/A
	 * @ensures true
	 * @return the next character as an integer
	 */
	public int nextChar() {
		return nextChar;
	}

	/**
	 * Description: Read a single character. This method will block until a
	 * character is available, an I/O error occurs, or the end of the stream is
	 * reached.
	 * 
	 * @requires input stream is open
	 * @alters N/A
	 * @ensures true
	 * @return The character read, as an integer
	 * @throws IOException
	 */
	public int read() throws IOException {
		nextChar = in.read();
		return nextChar;
	}

	/**
	 * Description: Write a single character.
	 * 
	 * @requires output stream is open
	 * @alters N/A
	 * @ensures true
	 * @param i
	 *            - integer specifying character to be written
	 * @throws IOException
	 */
	public void write(int i) throws IOException {
		write(Integer.toString(i));

	}

	/**
	 * Description: Write a string.
	 * 
	 * @requires output stream is open
	 * @alters N/A
	 * @ensures true
	 * @param str
	 *            - string to be written
	 * @throws IOException
	 */
	public void write(String str) throws IOException {
		out.write(str.getBytes());

	}

	/**
	 * Description: Writes the specified byte to the buffered output stream.
	 * 
	 * @requires output stream is open
	 * @alters N/A
	 * @ensures one byte is sent to the output stream
	 * @param i
	 *            - the byte to be written
	 * @throws IOException
	 */
	public void writeByte(int i) throws IOException {
		out.write(i);
	}

	/**
	 * Description: Closes the input and output streams. Catches an IOException
	 * if there was one.
	 * 
	 * @requires input and output streams are open
	 * @alters closes streams
	 * @ensures input and output streams are closed
	 */
	public void cleanUp() {
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			// nothing, close quietly
		}
	}
}
