package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Element representing variable element.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class ElementVariable extends Element{
	
	/**
	 * Value stored in this element.
	 */
	private String name;
	
	/**
	 * Constructor
	 * @param value you want to store in this element.
	 */
	public ElementVariable(String name) {
		this.name = name;
	}
	
	/**
	 * Method to get current value of this element.
	 * @return value of this element.
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String asText() {
		return name;
	}
	//default implementacija equals metode nam je dobra
}
