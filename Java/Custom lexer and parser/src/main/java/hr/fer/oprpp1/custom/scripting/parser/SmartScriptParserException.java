package hr.fer.oprpp1.custom.scripting.parser;

/**
 * Custom exception for Parser.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class SmartScriptParserException extends RuntimeException{
	
	/**
	 * Constructor.
	 * @param message to describe exception.
	 */
	public SmartScriptParserException(String message){
		super(message);
	}
	
}
