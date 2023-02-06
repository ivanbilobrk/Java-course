package hr.fer.oprpp1.hw04.db;
import java.util.List;

/**
 * Class implementing IFilter interface by going through internal list of conditions and testing each student record 
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class QueryFilter implements IFilter{
	
	/**
	 * Internal list of conditional expressions which will be used for filtering student records.
	 */
	private List<ConditionalExpression> uvjeti;
	
	/**
	 * Constructor.
	 * @param uvjeti - conditions which will be used for filtering student records.
	 */
	public QueryFilter(List<ConditionalExpression> uvjeti) {
		super();
		this.uvjeti = uvjeti;
	}
	
	/**
	 * Custom implementation of accepts method from interface IFilter which goes through internal list 
	 * of conditions and tests if student record satisfies them.
	 * @return true if student record satisifies all conditions, otherwise false.
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		
		for(var c: uvjeti) {
			if(!(c.getComparisonOperator().satisfied(c.getFieldGetter().get(record), c.getStringLiteral()))) {
				return false;
			}
		}
		
		return true;
	}

}
