package hr.fer.oprpp1.hw02.prob1;

/**
 * Custom exception thrown when there has been an error during lexical analysis.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class LexerException extends RuntimeException{
	
	public LexerException(String message) {
		super(message);
	}
		
}
