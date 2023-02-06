package hr.fer.zemris.java.gui.layouts;

/**
 * Custom exception thrown in some cases when adding components to CalcLayout.
 * @author Ivan Bilobrk
 *
 */
public class CalcLayoutException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor.
	 */
	public CalcLayoutException() {
		
	}
	
	/**
	 * Constructor.
	 * @param s - message which describes exception.
	 */
	public CalcLayoutException(String s) {
		super(s);
	}
}
