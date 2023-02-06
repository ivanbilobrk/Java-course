package hr.fer.oprpp1.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

/**
 * Base Node class for syntax tree.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class Node {
	
	/**
	 * Internal collection used for storage of child nodes.
	 */
	private ArrayIndexedCollection col;
	
	/**
	 * Method to add child in a node.
	 * @param child - node which you want to add.
	 */
	public void addChildNode(Node child) {
		
		if(child == null) throw new SmartScriptParserException("Cannot add null child.");
		
		if(col == null) col = new ArrayIndexedCollection();
		
		col.add(child);
	}
	
	/**
	 * Method to get number of node children.
	 * @return number of children of this node.
	 */
	public int numberOfChildren() {
		if(col == null) return 0;
		
		return col.size();
	}
	
	/**
	 * Method to get child of node from desired index.
	 * @param index - position from where you want to get child of this node.
	 * @return Node at index position.
	 */
	public Node getChild(int index) {
		if(numberOfChildren() == 0) throw new SmartScriptParserException("Node has no children.");
		
		try {
			return (Node)col.get(index);
		} catch(Exception e){
			throw new SmartScriptParserException("Cannot get child from that position.");
		}
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < numberOfChildren(); i++) {
			sb.append(getChild(i));
		}
		return sb.toString();
	}


	@Override
	public boolean equals(Object obj) {			//default implementacija equals metode
		if(this == obj)
			return true;
		if(!(obj instanceof Node))
			return false;
		
		Node other = (Node) obj;

		if(!(other.toString().equals(this.toString()))) {
			return false;
		}
		
		
			
		
		return true;
	}
	
	
}
