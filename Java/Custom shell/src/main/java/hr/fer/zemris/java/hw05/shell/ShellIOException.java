package hr.fer.zemris.java.hw05.shell;

/**
 * Custom ShellIOException that is used to indicate that shell can not read or write to the console.
 * @author Ivan Bilobrk
 *
 */
public class ShellIOException extends RuntimeException{
	
	/**
	 * Constructor
	 * @param s - message for exception.
	 */
	public ShellIOException(String s) {
		super(s);
	}
}
