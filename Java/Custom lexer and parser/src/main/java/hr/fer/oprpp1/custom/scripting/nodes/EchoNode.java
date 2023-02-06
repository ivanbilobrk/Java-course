package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

/**
 * Class which represents one EchoNode in source text.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class EchoNode extends Node{
	
	/**
	 * Array which contains all elements inside of this node.
	 */
	private Element[] elements;

	/**
	 * Constructor which creates an instance of EchoNode with given elements.
	 * @param elements which are inside EchoNode.
	 */
	public EchoNode(Element[] elements) {
		this.elements = elements;
	}
	
	/**
	 * Method to get elements of EchoNode
	 * @return elements inside EchoNode.
	 */
	public Element[] getElements() {
		return elements;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{$ ");
		for(int i = 0; i < elements.length; i++) {
			sb.append(elements[i].asText() + " ");
		}
		sb.append("$}");
		
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {		//dva EchoNode-a su ista ako su im isti unutarnji elementi
		if(obj == null)
			return false;
		if(obj.getClass() != this.getClass())
			return false;
		
		EchoNode node2 = (EchoNode) obj;
		
		Element[] elements1 = this.getElements();
		Element[] elements2 = node2.getElements();
		
		if(elements1.length != elements2.length)
			return false;
		
		for(int i = 0; i < elements1.length; i++) {
			if(!(elements1[i].equals(elements2[i])))
				return false;
		}
		
		return true;
	}
	
}
