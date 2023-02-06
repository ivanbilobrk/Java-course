package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing custom query parser.
 * @author Ivan Bilobrk
 * @version 1.0
 *
 */
public class QueryParser {
	
	/**
	 * String representing one query.
	 */
	private String query;
	
	/**
	 * String which is only initialized if query is direct and stores jmbag from direct query.
	 */
	private String queriedJMBAG;
	
	/**
	 * Constructor
	 * @param query - String which represents query.
	 */
	public QueryParser(String query) {
		super();
		this.query = query.strip();
	}
	
	/**
	 * Method which checks if query is direct (has only one condition that jmbag equals to some value)
	 * @return true if query is direct, otherwise false.
	 * @throws IllegalStateException if an error occurs when parsing query.
	 */
	public boolean isDirectQuery() {
		List<ConditionalExpression> uvjeti = getQuery();
		
		if(uvjeti.size() != 1 || uvjeti.get(0).getComparisonOperator() != ComparisonOperators.EQUALS ||
				uvjeti.get(0).getFieldGetter() != FieldValueGetters.JMBAG) return false;
		queriedJMBAG = uvjeti.get(0).getStringLiteral();
		return true;
		
	}
	
	/**
	 * Method which gets jmbag from query if it is direct.
	 * @return queried jmbag.
	 * @throws IllegalStateException if query is not direct.
	 */
	public String getQueriedJMBAG() {
		
		if(isDirectQuery()) return queriedJMBAG;
		else
			throw new IllegalStateException("Upit nije direktan.");
	}
	
	/**
	 * Method which gets all conditions from query.
	 * @return list of conditions found in query.
	 * @throws IllegalStateException if an error occurs when parsing query.
	 */
	public List<ConditionalExpression> getQuery(){
		List<ConditionalExpression> expressions = new ArrayList<>();
		
		//razdvajamo uvjete prema "and" neovisno o velikim i malim slovima
		String[] conditions = query.split("(?i)and");
		
		if(conditions.length == 1 && conditions[0].length()==0) {
			return expressions;
		}
		
		for(String s : conditions) {
			s = s.strip();
			
			if(s.indexOf("jmbag") == 0) {
				ConditionalExpression expr = getCondition(FieldValueGetters.JMBAG, s, 5);
				
				for(int i = 0; i < expr.getStringLiteral().length(); i++) {
					if(!Character.isDigit(expr.getStringLiteral().charAt(i))) throw new IllegalStateException("Krivi uvjet");
				}
				
				expressions.add(expr);
			} else if(s.indexOf("firstName") == 0) {
				ConditionalExpression expr = getCondition(FieldValueGetters.FIRST_NAME, s, 9);
				
				expressions.add(expr);
			} else if(s.indexOf("lastName") == 0) {
				ConditionalExpression expr = getCondition(FieldValueGetters.LAST_NAME, s, 8);
				
				expressions.add(expr);
			} else {
				throw new IllegalStateException("Krivi uvjet");
			}
			
		}
		return expressions;
	}
	
	/**
	 * Private method used for getting operator and string literal from query.
	 * @param getter - implementation of IFieldValueGetter which is used for getting desired student record attribute
	 * @param condition - entire condition
	 * @param startPosition - position from which String condition is analyzed.
	 * @return ConditionalExpression extracted from condition.
	 * @throws IllegalStateException if an error occurs when parsing condition.
	 */
	private ConditionalExpression getCondition(IFieldValueGetter getter, String condition, int startPosition) {
		int index = startPosition;
		IComparisonOperator operator;
		
		//parsiranje stringa u upitu koji slijedi nakon ključne riječi operatora
		while(condition.charAt(index) == ' ' || condition.charAt(index) == '\t') ++index;
		
		if(condition.charAt(index) == 'L') {
			if(!(condition.substring(index, index+4)).equals("LIKE")) {
				throw new IllegalStateException("Krivi uvjet");
			} else {
				index += 4;
				operator = ComparisonOperators.LIKE;
			}
		} else if(condition.charAt(index) == '!') {
			if(!(condition.substring(index, index+2)).equals("!=")) {
				throw new IllegalStateException("Krivi uvjet");
			} else {
				index += 2;
				operator = ComparisonOperators.NOT_EQUALS;
			}
		} else if(condition.charAt(index) == '>') {
			if(condition.charAt(index+1) == '=') {
				operator = ComparisonOperators.GREATER_OR_EQUALS;
				index += 2;
			} else {
				operator = ComparisonOperators.GREATER;
				++index;
			}
		} else if(condition.charAt(index) == '<') {
			if(condition.charAt(index+1) == '=') {
				operator = ComparisonOperators.LESS_OR_EQUALS;
				index += 2;
			} else {
				operator = ComparisonOperators.LESS;
				++index;
			}
		} else if(condition.charAt(index) == '=') {
			operator = ComparisonOperators.EQUALS;
			index += 1;
		} else {
			throw new IllegalStateException("Krivi uvjet");
		}
		
		//sada smo dosli nakon operatora te mora slijedit ili white space ili navodnik
		
		while(condition.charAt(index) == ' ' || condition.charAt(index) == '\t') ++index;
		
		if(condition.charAt(index) != '\"') throw new IllegalStateException("Krivi uvjet");
		
		int startLiteral = ++index;
	
		
		while(condition.charAt(index) != '\"') {
			
			++index;
			if(index == condition.length()) throw new IllegalStateException("Krivi uvjet");
		}
		
		if(index != condition.length()-1)
			throw new IllegalStateException("Krivi uvjet");
		
		
		String stringLiteral = condition.substring(startLiteral, index);
		
		return new ConditionalExpression(getter, stringLiteral, operator);
		
	}

	
	
}
