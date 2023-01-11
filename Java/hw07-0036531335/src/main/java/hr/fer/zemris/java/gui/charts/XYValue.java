package hr.fer.zemris.java.gui.charts;

/**
 * Class representing x and y values of object shown on chart.
 * @author Ivan Bilobrk
 *
 */
public class XYValue {
	
	/**
	 * Variables holding x and y values of object.
	 */
	private int x, y;

	/**
	 * Constructor
	 * @param x
	 * @param y
	 */
	public XYValue(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter for X value.
	 * @return - X value of object.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Getter for Y value.
	 * @return - Y value of object.
	 */
	public int getY() {
		return y;
	}
	
}
