package hr.fer.oprpp1.custom.collections;

/**
 * Interface with method test which tests if element satisfies a certain condition
 * @author Ivan Bilobrk
 * @version 1.0
 * @param <T> - Desired type of Tester.
 */
public interface Tester<T> {
	
	/**
	 * Method to test if Object obj satisfies desired conditions.
	 * @param obj - Object which you want to test.
	 * @return true if obj satisfies condition otherwise false.
	 */
	boolean test(T obj);

}
