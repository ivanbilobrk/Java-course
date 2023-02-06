package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

/**
 * Class representing ForLoopNode.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class ForLoopNode extends Node{
	
	/**
	 * First element of for loop.
	 */
	private ElementVariable variable;
	/**
	 * Second element of for loop.
	 */
	private Element startExpression;
	/**
	 * Third element of for loop.
	 */
	private Element endExpression;
	/**
	 * Fourth element of for loop.
	 */
	private Element stepExpression;
	
	/**
	 * Constructor.
	 * @param variable - first element of for loop.
	 * @param startExpression - second element of for loop.
	 * @param endExpression - third element of for loop.
	 * @param stepExpression - fourth element of for loop.
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}
	
	/**
	 * Method to get first element of for loop.
	 * @return first element of for loop.
	 */
	public ElementVariable getVariable() {
		return variable;
	}
	
	/**
	 * Method to get second element of for loop.
	 * @return second element of for loop.
	 */
	public Element getStartExpression() {
		return startExpression;
	}
	
	/**
	 * Method to get third element of for loop.
	 * @return third element of for loop.
	 */
	public Element getEndExpression() {
		return endExpression;
	}
	
	/**
	 * Method to get fourth element of for loop.
	 * @return fourth element of for loop.
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{$FOR ");
		sb.append(variable.asText() + " ");
		sb.append(startExpression.asText() + " ");
		sb.append(endExpression.asText() + " ");
		
		if(stepExpression != null)
			sb.append(stepExpression.asText() + "$}");
		else 
			sb.append("$}");
		
		sb.append(super.toString());
		sb.append("{$END$}");
		
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {		//2 ForLoopNode-a su ista ako su im isti elementi u tagu i ako imaju svu istu djecu
		if(obj == null)
			return false;
		if(obj.getClass() != this.getClass())
			return false;
		
		ForLoopNode node2 = (ForLoopNode) obj;
		
		if(!(this.getVariable().equals(node2.getVariable()))) 
			return false;
		if(this.getStartExpression() == null && node2.getStartExpression() != null)
			return false;
		if(this.getStartExpression()!= null && node2.getStartExpression() == null)
			return false;
		if(this.getStartExpression() != null && node2.getStartExpression() != null && !(this.getStartExpression().equals(node2.getStartExpression())))
			return false;
		if(!(this.getStartExpression().equals(node2.getStartExpression())))
			return false;
		if(!(this.getEndExpression().equals(node2.getEndExpression())))
			return false;
		
		
		if(this.numberOfChildren() != node2.numberOfChildren())
			return false;
		
		int childrenNumber = this.numberOfChildren();
		for(int i = 0; i < childrenNumber; i++) {
			if(!(this.getChild(i).equals(node2.getChild(i)))) {
				return false;
			}
		}
		return true;	
	}
}
