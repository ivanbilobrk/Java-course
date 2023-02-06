package hr.fer.oprpp1.hw02.prob1;

/**
 * A class which represents one token in source text.
 * @author Ivan Bilobrk
 * @version 1.0
 */

public class Token {
	
	/**
	 * Enumeration which represents a type of token.
	 */
	private TokenType type;
	
	/**
	 *  Value of token.
	 */
	private Object value;
	
	/**
	 * Constructor which creates Token class instance.
	 * @param type - type of Token 
	 * @param value - value of Token.
	 * @throws IllegalArgumentException if TokenType is null.
	 */
	public Token(TokenType type, Object value) {
		super();
		
		 if(type == null) throw new IllegalArgumentException("Token type can not be null."); //token mora biti nekog tipa
		 this.type = type;
		 this.value = value;
	}
	
	/**
	 * Method to return the value of token.
	 * @return - value of token, null if token of this type doesn't have assigned value.
	 */
	public Object getValue() {
		return this.value;
	}
	
	/**
	 * Method to return the type of token.
	 * @return Token type.
	 */
	public TokenType getType() {
		return this.type;
	}
	
	/**
	 * Implementation of toString method used for testing.
	 */
	@Override
	public String toString() {
			return "(" + type.toString() + ", " + (value == null ? "null" : value.toString()) + ")";
	}

}