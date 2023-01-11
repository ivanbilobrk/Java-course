package hr.fer.zemris.java.hw05.shell;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Util class with static helper methods for shell.
 * @author Ivan Bilobrk
 *
 */
public class Util {
	
	/**
	 * Method which extracts path from given string. This method uses escaping mechanism
	 * ('\' followed by '"' represents '"' and '\' followed by '\' represents '\').
	 * @param s - string which you want to convert to path object.
	 * @return Path representation of given string.
	 */
	public static Path parsePath(String s) {
		s = s.strip();
		
		if(s.charAt(0) != '\"') {
			return Paths.get(s);
		}
		
		char[] sChar = s.toCharArray();
		int len = s.length();
		String result = "";
		
		int i;
		for(i = 1; i < len && sChar[i] != '\"'; i++) {
			if(sChar[i] == '\\' && i+1 < len && sChar[i+1] == '\\') {
				result += sChar[i];
				i += 1;
			} else if(sChar[i] == '\\' && i+1 < len && sChar[i+1] == '\"') {
				result += sChar[i+1];
				i += 1;
			} else {
				result += sChar[i];
			}
		}
		
		
		return Paths.get(result);
		
	}
	
	/**
	 * Method for extracting two arguments from input. Used for shell commands which accept two arguments.
	 * @param arguments - string from which you want to extract two arguments.
	 * @return String array with size of 2 containing 2 arguments extracted from given string argument.
	 */
	public static String[] getArguments(String arguments) {
		String[] res = new String[2];
		
		String first = "";
		String second = "";
		arguments = arguments.strip();
		
		char[] argChars = arguments.toCharArray();
		int len = argChars.length;
		if(argChars[0] == '\"') {
			first += '\"';
			int i;
			
			for(i = 1; i < len && argChars[i] != '\"'; i++) {
				first += argChars[i];
			}
			if(i == len) {
				throw new IllegalArgumentException("Invalid number of double quotes.");
			}
			
			if(i+1 < len-1 && argChars[i+1] != ' ') {
				throw new IllegalArgumentException("Cannot have anything after double quotes that is not a space.");
			}
			
			first += '\"';
			second = arguments.substring(first.length());
			
		} else {
			int i = 0;
			while(i < len && argChars[i] != ' ') {
				i++;
			}
			
			first = new String(argChars).substring(0, i).strip();
			second = new String(argChars).substring(i).strip();
		}
		res[0] = first; 
		res[1] = second;
		return res;
	}
	
}
