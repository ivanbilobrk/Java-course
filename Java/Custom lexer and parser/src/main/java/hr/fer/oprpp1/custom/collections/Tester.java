package hr.fer.oprpp1.custom.collections;

/**
 * Interface with method test which tests if element satisfies a certain condition
 * @author Ivan Bilobrk
 * @version 1.0
 */
public interface Tester {
	
	/**
	 * Method to test if Object obj satisfies desired conditions.
	 * @param obj - Object which you want to test.
	 * @return true if obj satisfies condition otherwise false.
	 */
	boolean test(Object obj);

}
