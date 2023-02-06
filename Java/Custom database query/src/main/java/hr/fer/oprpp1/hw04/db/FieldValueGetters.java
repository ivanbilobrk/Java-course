package hr.fer.oprpp1.hw04.db;

/**
 * Class storing references to IFieldValueGetter implementations.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class FieldValueGetters {
	
	/**
	 * Implementation of IFieldValueGetter returning students first name.
	 */
	public static final IFieldValueGetter FIRST_NAME = (record) -> record.getFirstName();
	
	/**
	 * Implementation of IFieldValueGetter returning students last name.
	 */
	public static final IFieldValueGetter LAST_NAME = (record) -> record.getLastName();
	
	/**
	 * Implementation of IFieldValueGetter returning students jmbag.
	 */
	public static final IFieldValueGetter JMBAG = (record) -> record.getJmbag();
}
