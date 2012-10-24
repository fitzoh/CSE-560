package utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Pattern p = Pattern.compile(regexes.external);
		Matcher m = p.matcher("T0012300Xlab");
		System.out.println(m.matches());
		System.out.println(m.groupCount());
		for(int i=0;i<=m.groupCount();i++){
			System.out.println("group "+i+": "+m.group(i));
		}
	}

}
