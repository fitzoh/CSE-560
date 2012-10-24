package utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//checking regex behavior
		Pattern p = Pattern.compile(regexes.entRecord);
		Matcher m = p.matcher("         ENT a,b,c stuff");
		System.out.println(m.matches());
		System.out.println(m.groupCount());
		for(int i=0;i<=9;i++){
			System.out.println("group "+i+": "+m.group(i));
		}
	}

}
