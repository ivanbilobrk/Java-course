package hr.fer.oprpp1.custom.scripting.parser;

/**
 * Custom unchecked exception class used for notifying that stack is empty.
 * @author Ivan Bilobrk
 * @version 1.0
 */

/*
 * Vlastitu unchecked iznimku radimo tako da klasa naslijedi klasu RunTimeException te u konstruktoru proslijedi poruku
 * konstruktoru nadređene klase.  
 */

public class EmptyStackException extends RuntimeException{
	
	/**
	 * Constructor which calls constructor of super class (in this case RunTimeException) and gives it a custom message.
	 * @param message - you want to give to the custom exception.
	 */
	
	public EmptyStackException(String message) {
		super(message);
	}
	
}
