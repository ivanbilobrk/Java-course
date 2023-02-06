package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Element representing Integer number.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class ElementConstantInteger extends Element{
	
	/**
	 * Value stored in this element.
	 */
	private int value;

	/**
	 * Constructor
	 * @param value you want to store in this element.
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	
	/**
	 * Method to get current value of this element.
	 * @return value of this element.
	 */
	public int getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return String.valueOf(value);
	}
	
	//default implementacija equals metode nam je dobra
}
