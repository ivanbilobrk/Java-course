package hr.fer.oprpp1.custom.scripting.elems;


/**
 * Base Element class.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class Element {
	
	public String asText() {
		return "";
	}
	
	@Override
	public boolean equals(Object obj) {		//default implementacija equals metode
		if(obj == null) 
			return false;
		if(!(obj instanceof Element))
			return false;
		
		Element el2 = (Element) obj;
		
		if(!(this.asText().equals(el2.asText()))) {
			return false;
		}
		
		return true;
	}
	
}
