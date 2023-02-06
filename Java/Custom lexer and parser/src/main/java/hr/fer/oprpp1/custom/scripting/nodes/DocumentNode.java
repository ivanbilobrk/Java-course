package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * Represents root node of syntax tree.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class DocumentNode extends Node{
	
	
	@Override
	public boolean equals(Object obj) { //dva DocumentNode-a su ista ako su im isti svi elementi, tj. djeca
		
		if(obj == null)
			return false;
		if(obj.getClass() != this.getClass())
			return false;
		
		DocumentNode other = (DocumentNode) obj;
		
		if(other.numberOfChildren() != this.numberOfChildren()) {
			return false;
		}
			
		
		int childrenNumber = this.numberOfChildren();
		for(int i = 0; i < childrenNumber; i++) {
			if(!(this.getChild(i).equals(other.getChild(i)))) {
				System.out.println(this.getChild(i));
				return false;
			}
		}
		
		return true;
	}
}
