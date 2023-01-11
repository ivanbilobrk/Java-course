package hr.fer.zemris.java.gui.calc;

import javax.swing.JButton;

/**
 * Class representing number buttons on calculator
 * @author Ivan Bilobrk
 *
 */
public class NumberButton extends JButton{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Number which is displayed on button.
	 */
	private int number;
	
	/**
	 * Constructor
	 * @param number
	 */
	public NumberButton(int number) {
		this.number = number;
		this.setText(number+"");
		this.setFont(this.getFont().deriveFont(30f));
	}
	
	/**
	 * Method which adds new number to calculator display.
	 * @param impl - calculator model to which you want to add new number to.
	 */
	public void addNumber(CalcModelImpl impl) {
		impl.insertDigit(number);
	}
	
	
	
}
