package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

/**
 * Class which represents position of element in custom layout CalcLayout.
 * @author Ivan Bilobrk
 *
 */
public class RCPosition {
	
	/**
	 * Represents row of a component.
	 */
	private int row;
	
	/**
	 * Represents column of a component.
	 */
	private int column;
	
	/**
	 * Constructor
	 * @param row - desired row of a component
	 * @param column - desired column of a component.
	 */
	public RCPosition(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Getter for row.
	 * @return row of a component.
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Getter for column.
	 * @return column of a component.
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * Method to parse given String and create new RCPosition object from it.
	 * @param text - String which holds desired row and column of a component you want to add to CalcLayout.
	 * 				 Row and column are separated using a column. Example; argument "1,5" will create a new 
	 * 				 RCPosition object with row 1 and column 5.  
	 * @return new RCPosition object.
	 */
	public static RCPosition parse(String text) {
		
		try {
			//razdvajamo row i column po zarezu te stvaramo novi RCPosition objekt
			String[] tempArray = text.split(",");
			int row = Integer.parseInt(tempArray[0]);
			int column = Integer.parseInt(tempArray[1]);
			return new RCPosition(row, column);
		} catch(NumberFormatException e) {
			System.out.println("Illegal row or column.");
			throw e;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RCPosition other = (RCPosition) obj;
		return column == other.column && row == other.row;
	}
	
	
	
}
