package hr.fer.oprpp1.hw04.db;

/**
 * Class with constants representing operators and implementations of IComparisonOperator
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class ComparisonOperators {
	
	/**
	 * Implementation of operator less (<). Compares two strings and returns true if first is 
	 * lexically smaller than second.
	 */
	public static final IComparisonOperator LESS = (s1, s2) -> s1.compareTo(s2) < 0 ? true: false;
	
	/**
	 * Implementation of operator less or equals (<=). Compares two strings and returns true if first is lexically
	 * smaller or equal to the second.
	 */
	public static final IComparisonOperator LESS_OR_EQUALS = (s1, s2) -> s1.compareTo(s2) <= 0 ? true: false;
	
	/**
	 * Implementation of operator greater (>). Compares two strings and returns true if first is 
	 * lexically greater than second.
	 */
	public static final IComparisonOperator GREATER = (s1, s2) -> s1.compareTo(s2) > 0 ? true: false;
	
	/**
	 * Implementation of operator greater or equal (>=). Compares two strings and returns true if first is 
	 * lexically greater or equal to the second.
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS = (s1, s2) -> s1.compareTo(s2) >= 0 ? true: false;
	
	/**
	 * Implementation of operator equals (=). Compares two strings and returns true if first is 
	 * lexically equal to the second.
	 */
	public static final IComparisonOperator EQUALS = (s1, s2) -> s1.compareTo(s2) == 0 ? true: false;
	
	/**
	 * Implementation of operator not equal (!=). Compares two strings and returns true if first is 
	 * lexically different from the second.
	 */
	public static final IComparisonOperator NOT_EQUALS = (s1, s2) -> s1.compareTo(s2) != 0 ? true: false;
	
	/**
	 * Implementation of operator like (LIKE). Compares two strings and returns true if first string
	 * satisfies sql operator LIKE with condition string being written in the other string.
	 */
	public static final IComparisonOperator LIKE = (s1, s2) ->{
		int occurence = 0;
		int indexWildCard = 0;
		int s2Len = s2.length();
		
		//tražimo index wildcarda ako ga ima
		for(int i = 0; i < s2Len; i++) {
			if(s2.charAt(i) == '*') {
				++occurence;
				indexWildCard = i;
			}
		}
		
		if(occurence > 1) throw new IllegalStateException("Više od jednog wildcarda * u LIKE.");
		
		//ako nema wildcarda stringove možemo usporediti sa equals
		if(occurence == 0) {
			return s1.equals(s2);
		}
		
		if(s2Len-1 > s1.length()) return false;
		
		//posebni slučajevi ovisno o poziciji wildcarda
		
		if(indexWildCard == 0) {
			
			return s2.substring(1).equals(s1.substring(s1.length()-s2Len+1));
	
		} else if(indexWildCard == s2Len-1) {
			
			return s2.substring(0, s2Len-1).equals(s1.substring(0, s2Len-1));
			
		} else {
			
			boolean b1 = s2.substring(0, indexWildCard).equals(s1.substring(0, indexWildCard));
			boolean b2 = s2.substring(indexWildCard+1).equals(s1.substring(s1.length() -(s2.substring(indexWildCard+1).length())));
			
			return b1 && b2;
		}

	};
	
	
}
