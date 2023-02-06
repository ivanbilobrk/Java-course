package hr.fer.oprpp1.hw04.db;

/**
 * Class representing one conditional expression in query. It has reference to the operator which is used,
 * field representing one attribute of a student and a string literal which is compared to that attribute using
 * the operator.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class ConditionalExpression {
	
	/**
	 * Reference to IFieldValueGetter used for getting one attribute of a student record.
	 */
	private IFieldValueGetter fieldGetter;
	
	/**
	 * String which is compared to attribute returned by fieldGetter using custom operator.
	 */
	private String stringLiteral;
	
	/**
	 * Custom operator used to compare stringLiteral and attribute of a student record returned by fieldGetter.
	 */
	private IComparisonOperator comparisonOperator;
	
	/**
	 * Constructor
	 * @param fieldGetter
	 * @param stringLiteral
	 * @param comparisonOperator
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral, IComparisonOperator comparisonOperator) {
		super();
		this.fieldGetter = fieldGetter;
		this.stringLiteral = stringLiteral;
		this.comparisonOperator = comparisonOperator;
	}
	
	/**
	 * Getter for IFieldValueGetter.
	 * @return IFieldValueGetter reference from this class.
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}
	
	/**
	 * Getter for string literal.
	 * @return String literal of this class.
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}
	
	/**
	 * Getter for IComparisonOperator.
	 * @return Custom IComparisonOperator of this class.
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}
	
}
