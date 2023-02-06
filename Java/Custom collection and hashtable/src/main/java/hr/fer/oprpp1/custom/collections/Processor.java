package hr.fer.oprpp1.custom.collections;

/**
 * Functional interface with method process which performs desired action on one Object element.
 * @author Ivan Bilobrk
 * @version 1.0
 * @param <T> - Desired type of Processor.
 */
public interface Processor<T>{
	
	/**
	 * Method which makes desired action with given parameter.
	 * @param value - object with which action is performed.
	 */
	public void process(T value);
}