package hr.fer.oprpp1.hw08.jnotepadpp;

/**
 * Class holding current offset for a selection.
 * @author Ivan Bilobrk
 *
 */
public class Offset {
	
	/**
	 * Offset of line start of first selected line.
	 */
	private int offset1;
	
	/**
	 * Offset of line end of last selected line.
	 */
	private int offset2;
	
	/**
	 * Constructor
	 * @param offset1
	 * @param offset2
	 */
	public Offset(int offset1, int offset2) {
		super();
		this.offset1 = offset1;
		this.offset2 = offset2;
	}
	
	/**
	 * Getter for offset1.
	 * @return
	 */
	public int getOffset1() {
		return offset1;
	}
	
	/**
	 * Getter for offset2.
	 * @return
	 */
	public int getOffset2() {
		return offset2;
	}
	
	

}
