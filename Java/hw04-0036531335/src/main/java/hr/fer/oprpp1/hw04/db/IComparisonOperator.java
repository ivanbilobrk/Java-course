package hr.fer.oprpp1.hw04.db;

/**
 * Interface specifying one method which compares two strings and tests if two strings satisfy desired condition.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public interface IComparisonOperator {
	
	/**
	 * Abstract method used for testing if two strings satisfy desired condition.
	 * @param value1 - String which is compared to value2 using custom operator
	 * @param value2 - String which value1 is compared to using custom operator.
	 * @return true if custom operator determines that two strings satisfy desired condition, otherwise false.
	 */
	public boolean satisfied(String value1, String value2);

}
