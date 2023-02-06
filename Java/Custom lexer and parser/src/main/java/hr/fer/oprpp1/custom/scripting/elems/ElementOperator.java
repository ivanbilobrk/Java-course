package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Element representing operator element.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class ElementOperator extends Element{
	
	/**
	 * Value stored in this element.
	 */
	private String symbol;
	
	/**
	 * Constructor
	 * @param value you want to store in this element.
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * Method to get current value of this element.
	 * @return value of this element.
	 */
	public String getValue() {
		return symbol;
	}
	
	@Override
	public String asText() {
		return symbol;
	}
	//default implementacija equals metode nam je dobra
}