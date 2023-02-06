package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * BarChart implementation.
 * @author Ivan Bilobrk
 *
 */
public class BarChart {
	
	/**
	 * List holding all XYValues shown on chart.
	 */
	private List<XYValue> list;
	
	/**
	 * Descriptions shown on X-axis and Y-axis.
	 */
	private String xDesc, Ydesc;
	
	/**
	 * Minimum and maximum Y values shown on chart and space between different Y values.
	 */
	private int yMin, yMax, space;
	
	/**
	 * Constructor
	 * @param list
	 * @param xDesc
	 * @param ydesc
	 * @param yMin
	 * @param yMax
	 * @param space
	 */
	public BarChart(List<XYValue> list, String xDesc, String ydesc, int yMin, int yMax, int space) {
		super();
		
		//provjere parametara
		if(yMin < 0) {
			throw new IllegalArgumentException("Min Y value cannot be negative.");
		}
		
		if(!(yMax > yMin)) {
			throw new IllegalArgumentException("Max Y value must be > min Y value.");
		}
		
		for(var x: list) {
			if(!(x.getY() >= yMin)) {
				throw new IllegalArgumentException("Cannot have XYValue with Y smaller than yMin.");
			}
		}
		
		this.list = list;
		this.xDesc = xDesc;
		Ydesc = ydesc;
		this.yMin = yMin;
		this.yMax = yMax;
		
		//ukoliko razlika minimalnog i maksimalnog y nije dijeljiva sa razmakom, moramo ga korigirati
		if((yMax - yMin)%space != 0) {
			space = yMin+1;
		}
		
		this.space = space;
	}
	
	/**
	 * Getter for list of XYValues shown on chart.
	 * @return
	 */
	public List<XYValue> getList() {
		return list;
	}
	
	/**
	 * Getter for description of X-axis.
	 * @return
	 */
	public String getxDesc() {
		return xDesc;
	}
	
	/**
	 * Getter for description of Y-axis.
	 * @return
	 */
	public String getYdesc() {
		return Ydesc;
	}
	
	/**
	 * Getter for minimum Y value on chart.
	 * @return
	 */
	public int getyMin() {
		return yMin;
	}
	
	/**
	 * Getter for maximum Y value on chart.
	 * @return
	 */
	public int getyMax() {
		return yMax;
	}
	
	/**
	 * Getter for space between Y values on chart.
	 * @return
	 */
	public int getSpace() {
		return space;
	}
}
