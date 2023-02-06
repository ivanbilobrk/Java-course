package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * Class representing one TextNode.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class TextNode extends Node{
	/**
	 * Value of text node.
	 */
	private String text;
	
	/**
	 * Constructor
	 * @param text - value of text node.
	 */
	public TextNode(String text) {
		this.text = text;
	}
	
	/**
	 * Method to get string value of text node.
	 * @return string value of text node.
	 */
	public String getText() {
		return text;
	}
	
	@Override	
	public String toString() {
		return text;
	}
	
	//ovdje nismo napravili novu implementaciju equals metode jer nam je dobra default koja se nudi u klasi Node
}
