package hr.fer.oprpp1.hw04.db;

/**
 * Interface used for getting one attribute of student record.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public interface IFieldValueGetter {
	
	/**
	 * Method which gets one attribute from student record.
	 * @param record - StudentRecord for which you want to get one attribute.
	 * @return attribute of one StudentRecord.
	 */
	public String get(StudentRecord record);
	
}
