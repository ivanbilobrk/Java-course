package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Element representing string element.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class ElementString extends Element{
	
	/**
	 * Value stored in this element.
	 */
	private String value;
	
	/**
	 * Constructor
	 * @param value you want to store in this element.
	 */
	public ElementString(String value) {
		this.value = value;
	}
	
	/**
	 * Method to get current value of this element.
	 * @return value of this element.
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return "\"" + value + "\"";
	}
	
	@Override
	public String toString() {		//nova implementacija toString metode zbog mogućnosti escape-a unutar stringa
		int len = value.length();
		char[] ValueArray = new char[len];
		
		int i = 0;
		int index = 0;
		while(i < len) {
			if(value.charAt(i) == '\\' && i+1 < len) {
				if(value.charAt(i+1) == '\\' || value.charAt(i+1) == '\"') {
					ValueArray[index++] = value.charAt(i+1);
				} else if(value.charAt(i+1) == 'n') {
					ValueArray[index++] = '\n';
				}else if(value.charAt(i+1) == 'r') {
					ValueArray[index++] = '\r';
				} else if(value.charAt(i+1) == 't') {
					ValueArray[index++] = '\t';
				}
				i += 2;
			} else {
				ValueArray[index++] = value.charAt(i);
				i++;
			}
		}
		
		return new String(ValueArray, 0, index);
	}
	
	//default implementacija equals metode nam je dobra
}
