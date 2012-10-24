package test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import objectFile.InvalidObjectFileException;
import objectLine.AbsoluteLine;
import objectLine.ExternalLine;
import objectLine.LineType;
import objectLine.ObjectLine;
import objectLine.ObjectLineFactory;

import org.junit.Before;
import org.junit.Test;

public class ObjectLineTest {

	Map<String, Integer> fullMap;
	ObjectLine objl;
	AbsoluteLine absl;
	ExternalLine extl;
	String source;

	@Before
	public void initialize() {
		this.fullMap = new HashMap<String, Integer>();
		this.fullMap.put("SYMBOL", 1);
	}

	/**
	 * Tests for AbsoluteLine class
	 */
	// Constructor test
	@Test
	public void absTest1() {
		this.absl = new AbsoluteLine("FF", "C3C00");
		assertEquals("Constructor error!", "TFFC3C00\n", this.absl.toString());
		assertEquals("Wrong type is returned.", LineType.Absolute,
				this.absl.getType());
	}

	// addOffsetTest
	@Test
	public void absTest2() {
		this.absl = new AbsoluteLine("FE", "C3C00");
		this.absl.addOffset(1);
		assertEquals("Constructor error!", "TFFC3C00\n", this.absl.toString());
	}

	/**
	 * Tests for ExternalLine class
	 */
	// Constructor test
	@Test
	public void extTest1() {
		this.extl = new ExternalLine("00", "B20", "SYMBOL");
		assertEquals("Constructor error!", "T00B2000\n", this.extl.toString());
		assertEquals("Wrong type is returned.", LineType.External,
				this.extl.getType());
	}

	// replaceExt test
	@Test
	public void extTest2() throws InvalidObjectFileException {
		this.extl = new ExternalLine("00", "B20", "SYMBOL");
		this.extl.replaceExt(this.fullMap, 0);
		assertEquals("Constructor error!", "T00B2001\n", this.extl.toString());
	}

	/**
	 * Tests for ObjectLineFactory class
	 */
	// getRelocatableLine test
	@Test
	public void objFacTest1() {
		this.source = "T00F3000M";
		this.objl = ObjectLineFactory.getObjectLine(this.source);
		assertEquals("The returned class is incorrect!",
				"objectLine.RelocatableLine", this.objl.getClass().getName());
	}

	// TODO
	// getHeaderLine test
	@Test
	public void objFacTest2() {
		this.source = "H00Main  0000M";
		this.objl = ObjectLineFactory.getObjectLine(this.source);
		assertEquals("The returned class is incorrect!",
				"objectLine.HeaderLine", this.objl.getClass().getName());
	}

	// getExternalLine test
	@Test
	public void objFacTest3() {
		this.source = "T00F3000XP";
		this.objl = ObjectLineFactory.getObjectLine(this.source);
		assertEquals("The returned class is incorrect!",
				"objectLine.ExternalLine", this.objl.getClass().getName());
	}

	// getEntryLine test
	@Test
	public void objFacTest4() {
		this.source = "EPnum  00";
		this.objl = ObjectLineFactory.getObjectLine(this.source);
		assertEquals("The returned class is incorrect!",
				"objectLine.EntryLine", this.objl.getClass().getName());
	}

	// getAbsoluteLine test
	@Test
	public void objFacTest5() {
		this.source = "T00F3000";
		this.objl = ObjectLineFactory.getObjectLine(this.source);
		assertEquals("The returned class is incorrect!",
				"objectLine.AbsoluteLine", this.objl.getClass().getName());
	}
}
