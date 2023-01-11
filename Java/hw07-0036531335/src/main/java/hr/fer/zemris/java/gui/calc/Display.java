package hr.fer.zemris.java.gui.calc;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;

/**
 * Class representing Display of a calculator
 * @author Ivan Bilobrk
 *
 */
public class Display extends JLabel implements CalcValueListener{

	private static final long serialVersionUID = 1L;
	
	/**
	 * String representing current value of a display.
	 */
	private String value;
	
	/**
	 * Constructor
	 * @param value - initial value of a display.
	 */
	public Display(String value) {
		setValue(value);
		this.setFont(this.getFont().deriveFont(30f));
		this.setHorizontalAlignment(SwingConstants.RIGHT);
		this.setBackground(Color.YELLOW);
		this.setOpaque(true);
	}

	@Override
	public void valueChanged(CalcModel model) {
		//svaki put kad model javi da je došlo do promjene vrijednosti moramo ažurirati prikaz
		String input = model.toString();
		if(input.contains(".")) {
			//micanje eventualne 0 nakon decimalne točke ako je ona jedina koja slijedi nakon točke
			String[] tempArray = input.split("\\.");
			if(tempArray.length == 2 && tempArray[1].equals("0")) {
				input = input.substring(0, input.indexOf("."));
			}
		}
		this.setValue(input);
	}
	
	/**
	 * Getter for current value of display.
	 * @return - current value of display.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Setter for current value of display.
	 * @param value which will be displayed on display.
	 */
	public void setValue(String value) {
		this.value = value;
		this.setText(value);
	}

}
