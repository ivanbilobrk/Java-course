package hr.fer.oprpp1.hw04.db;

/**
 * Interface used for checking if student record satisfies sutom conditions.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public interface IFilter {
	
	/**
	 * Method used to check if StudenRecord satisfies custom condition.
	 * @param record - StudentRecord which you want to test.
	 * @return true if record satisfies custom condition, otherwise false.
	 */
	public boolean accepts(StudentRecord record);
}
