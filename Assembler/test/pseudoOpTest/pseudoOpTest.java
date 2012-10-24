package test.pseudoOpTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import utilities.regexes;

public class pseudoOpTest {

	/**
	 * ORI test with label
	 */
	@Test
	public void oriTest1() {
		Pattern p = Pattern.compile(regexes.oriRecord);
		Matcher m = p.matcher("Prog     ORI 1");
		m.matches();
		assertEquals("The label field doesn't match.", "Prog  ", m.group(1));
		assertEquals("The op field doesn't match.", "ORI", m.group(2));
		assertEquals("The operand field doesn't match.", "1", m.group(3));
		assertEquals("The operand field doesn't match.", null, m.group(4));
		assertEquals("The comment field doesn't match.", null, m.group(5));
	}

	/**
	 * ORI test with label and comment
	 */
	@Test
	public void oriTest2() {
		Pattern p = Pattern.compile(regexes.oriRecord);
		Matcher m = p.matcher("Prog     ORI 1 ;comment");
		m.matches();
		assertEquals("The label field doesn't match.", "Prog  ", m.group(1));
		assertEquals("The op field doesn't match.", "ORI", m.group(2));
		assertEquals("The operand field doesn't match.", "1", m.group(3));
		assertEquals("The comment field doesn't match.", ";comment", m.group(5));
	}

	/**
	 * ORI test with label but no operand
	 */
	@Test
	public void oriTest3() {
		Pattern p = Pattern.compile(regexes.oriRecord);
		Matcher m = p.matcher("Prog     ORI ");
		m.matches();
		assertEquals("The label field doesn't match.", "Prog  ", m.group(1));
		assertEquals("The op field doesn't match.", "ORI", m.group(2));

	}

	/**
	 * END test with symbol operand
	 */
	@Test
	public void endTest1() {
		Pattern p = Pattern.compile(regexes.endRecord);
		Matcher m = p.matcher("         END Begin");
		m.matches();
		assertEquals("The label field doesn't match.", "      ", m.group(1));
		assertEquals("The operand field doesn't match.", "Begin", m.group(3));
		assertEquals("The comment field doesn't match.", null, m.group(5));
	}

	/**
	 * END test
	 */
	@Test
	public void endTest2() {
		Pattern p = Pattern.compile(regexes.endRecord);
		Matcher m = p.matcher("         END ");
		m.matches();
		assertEquals("The label field doesn't match.", "      ", m.group(1));
		assertEquals("The operand field doesn't match.", "", m.group(3));
		assertEquals("The comment field doesn't match.", "", m.group(5));
	}

	/**
	 * EQU test with label, operand and comment
	 */
	@Test
	public void equTest1() {
		Pattern p = Pattern.compile(regexes.equRecord);
		Matcher m = p.matcher("Reg      EQU 2 ;comment");
		m.matches();
		assertEquals("The label field doesn't match.", "Reg   ", m.group(1));
		assertEquals("The operand field doesn't match.", "2", m.group(3));
		assertEquals("The comment field doesn't match.", ";comment", m.group(7));
	}

	/**
	 * EQU test with operand only
	 */
	@Test
	public void equTest2() {
		Pattern p = Pattern.compile(regexes.equRecord);
		Matcher m = p.matcher("         EQU 2 ");
		m.matches();
		assertEquals("The label field doesn't match.", "      ", m.group(1));
		assertEquals("The operand field doesn't match.", "2", m.group(3));
		assertEquals("The comment field doesn't match.", null, m.group(7));
	}

	/**
	 * NMD test with label, operand and comment
	 */
	@Test
	public void nmdTest1() {
		Pattern p = Pattern.compile(regexes.nmdRecord);
		Matcher m = p.matcher("Reg      NMD 2 ;comment");
		m.matches();
		assertEquals("The label field doesn't match.", "Reg   ", m.group(1));
		assertEquals("The operand field doesn't match.", "2", m.group(3));
		assertEquals("The comment field doesn't match.", ";comment", m.group(5));
	}

	/**
	 * NMD test with operand only
	 */
	@Test
	public void nmdTest2() {
		Pattern p = Pattern.compile(regexes.nmdRecord);
		Matcher m = p.matcher("         NMD 222 ");
		m.matches();
		assertEquals("The label field doesn't match.", "      ", m.group(1));
		assertEquals("The operand field doesn't match.", "222", m.group(3));
		assertEquals("The comment field doesn't match.", null, m.group(5));
	}

	/**
	 * NMD test with operand only
	 */
	@Test
	public void nmdTest3() {
		Pattern p = Pattern.compile(regexes.nmdRecord);
		Matcher m = p.matcher("         NMD  ");
		m.matches();
		assertEquals("The label field doesn't match.", "      ", m.group(1));
		assertFalse("The operand field doesn't match.", false);
		assertEquals("The comment field doesn't match.", null, m.group(5));
	}

	/**
	 * CCD test with label, operand and comment
	 */
	@Test
	public void ccdTest1() {
		Pattern p = Pattern.compile(regexes.ccdRecord);
		Matcher m = p.matcher("char     CCD HI ;comment");
		m.matches();
		assertEquals("The label field doesn't match.", "char  ", m.group(1));
		assertEquals("The operand field doesn't match.", "HI", m.group(3));
		assertEquals("The comment field doesn't match.", ";comment", m.group(5));
	}

	/**
	 * CCD test with only operand
	 */
	@Test
	public void ccdTest2() {
		Pattern p = Pattern.compile(regexes.ccdRecord);
		Matcher m = p.matcher("         CCD HI ");
		m.matches();
		assertEquals("The label field doesn't match.", "      ", m.group(1));
		assertEquals("The operand field doesn't match.", "HI", m.group(3));
		assertEquals("The comment field doesn't match.", null, m.group(5));
	}

	/**
	 * RES test with label, operand and comment
	 */
	@Test
	public void resTest1() {
		Pattern p = Pattern.compile(regexes.resRecord);
		Matcher m = p.matcher("NAME     RES 1 ;comment");
		m.matches();
		assertEquals("The label field doesn't match.", "NAME  ", m.group(1));
		assertEquals("The operand field doesn't match.", "1", m.group(3));
		assertEquals("The comment field doesn't match.", ";comment", m.group(7));
	}

	/**
	 * RES test with operand
	 */
	@Test
	public void resTest2() {
		Pattern p = Pattern.compile(regexes.resRecord);
		Matcher m = p.matcher("         RES 1 ");
		m.matches();
		assertEquals("The label field doesn't match.", "      ", m.group(1));
		assertEquals("The operand field doesn't match.", "1", m.group(3));
		assertEquals("The comment field doesn't match.", null, m.group(7));
	}

	/**
	 * RET test with label, operand and comment
	 */
	@Test
	public void retTest1() {
		Pattern p = Pattern.compile(regexes.retRecord);
		Matcher m = p.matcher("NAME     RET 1 ;comment");
		m.matches();
		assertEquals("The label field doesn't match.", "NAME  ", m.group(1));
		assertEquals("The operand field doesn't match.", "1", m.group(3));
		assertEquals("The comment field doesn't match.", ";comment", m.group(7));
	}

	/**
	 * RET test with label symbol operand
	 */
	@Test
	public void retTest2() {
		Pattern p = Pattern.compile(regexes.retRecord);
		Matcher m = p.matcher("         RET abc ");
		m.matches();
		assertEquals("The label field doesn't match.", "      ", m.group(1));
		assertEquals("The operand field doesn't match.", "abc", m.group(3));
		assertEquals("The comment field doesn't match.", null, m.group(7));
	}

	/**
	 * GTC test with label, operand and comment with a lot of spaces
	 */
	@Test
	public void gtcTest1() {
		Pattern p = Pattern.compile(regexes.gtcRecord);
		Matcher m = p.matcher("x        GTC 3       ;Input char to R3");
		m.matches();
		assertEquals("The label field doesn't match.", "x     ", m.group(1));
		assertEquals("The operand field doesn't match.", "3", m.group(3));
		assertEquals("The comment field doesn't match.",
				"      ;Input char to R3", m.group(7));
	}

	/**
	 * GTC test with operand
	 */
	@Test
	public void gtcTest2() {
		Pattern p = Pattern.compile(regexes.gtcRecord);
		Matcher m = p.matcher("         GTC 1 ");
		m.matches();
		assertEquals("The label field doesn't match.", "      ", m.group(1));
		assertEquals("The operand field doesn't match.", "1", m.group(3));
		assertEquals("The comment field doesn't match.", null, m.group(7));
	}

	/**
	 * PTC test with label, operand and comment with a lot of spaces
	 */
	@Test
	public void ptcTest1() {
		Pattern p = Pattern.compile(regexes.ptcRecord);
		Matcher m = p.matcher("X        PTC 3    ;print 'n'");
		m.matches();
		assertEquals("The label field doesn't match.", "X     ", m.group(1));
		assertEquals("The operand field doesn't match.", "3", m.group(3));
		assertEquals("The comment field doesn't match.", "   ;print 'n'",
				m.group(7));
	}

	/**
	 * PTC test with operand
	 */
	@Test
	public void ptcTest2() {
		Pattern p = Pattern.compile(regexes.ptcRecord);
		Matcher m = p.matcher("         PTC 1 ");
		m.matches();
		assertEquals("The label field doesn't match.", "      ", m.group(1));
		assertEquals("The operand field doesn't match.", "1", m.group(3));
		assertEquals("The comment field doesn't match.", null, m.group(7));
	}

	/**
	 * EXT test: no symbol in operand field
	 */
	@Test
	public void extTest1() {
		Pattern p = Pattern.compile(regexes.extRecord);
		Matcher m = p.matcher("         EXT   ");
		m.matches();
		try {
			m.group(1);
			fail("It should be an illegal ext command!");

		} catch (IllegalStateException e) {
			// Test passed
		}
	}

	/**
	 * EXT test: normal input with one symbol in operand field
	 */
	@Test
	public void extTest2() {
		Pattern p = Pattern.compile(regexes.extRecord);
		Matcher m = p.matcher("         EXT Symbol comment");
		m.matches();
		assertEquals("The operand field doesn't match.", "Symbol", m.group(1));
		assertEquals("The operand field doesn't match.", null, m.group(3));
		assertEquals("The operand field doesn't match.", null, m.group(5));
		assertEquals("The operand field doesn't match.", null, m.group(6));
	}

	/**
	 * EXT test: normal input with several symbol in operand field
	 */
	@Test
	public void extTest3() {
		Pattern p = Pattern.compile(regexes.extRecord);
		Matcher m = p
				.matcher("         EXT Symbo1,Symbo2,Symbo3,Symbo4 comment");
		m.matches();
		assertEquals("The operand field doesn't match.", "Symbo1", m.group(1));
		assertEquals("The operand field doesn't match.", "Symbo2", m.group(3));
		assertEquals("The operand field doesn't match.", "Symbo3", m.group(5));
		assertEquals("The operand field doesn't match.", "Symbo4", m.group(7));
	}

	/**
	 * EXT test: error when EXT has a label
	 */
	@Test
	public void extTest4() {
		Pattern p = Pattern.compile(regexes.extRecord);
		Matcher m = p
				.matcher("sample   EXT Symbo1,Symbo2,Symbo3,Symbo4 comment");
		m.matches();
		try {
			m.group(1);
			fail("It should be an illegal ext command!");

		} catch (IllegalStateException e) {
			// Test passed
		}
	}

	/**
	 * EMT test: no symbol in operand field
	 */
	@Test
	public void entTest1() {
		Pattern p = Pattern.compile(regexes.entRecord);
		Matcher m = p.matcher("         ENT   ");
		m.matches();
		try {
			m.group(1);
			fail("It should be an illegal ent command!");

		} catch (IllegalStateException e) {
			// Test passed
		}
	}

	/**
	 * ENT test: normal input with one symbol in operand field
	 */
	@Test
	public void entTest2() {
		Pattern p = Pattern.compile(regexes.entRecord);
		Matcher m = p.matcher("         ENT Symbol comment");
		m.matches();
		assertEquals("The operand field doesn't match.", "Symbol", m.group(1));
		assertEquals("The operand field doesn't match.", null, m.group(3));
		assertEquals("The operand field doesn't match.", null, m.group(5));
		assertEquals("The operand field doesn't match.", null, m.group(6));
	}

	/**
	 * ENT test: normal input with several symbol in operand field
	 */
	@Test
	public void entTest3() {
		Pattern p = Pattern.compile(regexes.entRecord);
		Matcher m = p
				.matcher("         ENT Symbo1,Symbo2,Symbo3,Symbo4 comment");
		m.matches();
		assertEquals("The operand field doesn't match.", "Symbo1", m.group(1));
		assertEquals("The operand field doesn't match.", "Symbo2", m.group(3));
		assertEquals("The operand field doesn't match.", "Symbo3", m.group(5));
		assertEquals("The operand field doesn't match.", "Symbo4", m.group(7));
	}

	/**
	 * ENT test: error when ENT has a label
	 */
	@Test
	public void entTest4() {
		Pattern p = Pattern.compile(regexes.entRecord);
		Matcher m = p
				.matcher("sample   ENT Symbo1,Symbo2,Symbo3,Symbo4 comment");
		m.matches();
		try {
			m.group(1);
			fail("It should be an illegal ent command!");

		} catch (IllegalStateException e) {
			// Test passed
		}
	}

}
