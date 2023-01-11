package hr.fer.zemris.java.gui.calc;

import java.util.function.DoubleBinaryOperator;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Class which represents one button which task is to implement one binary operation of a calculator.
 * @author Ivan Bilobrk
 *
 */
public class BinaryOperationButton extends JButton{
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * Binary operator of this button.
	 */
	private DoubleBinaryOperator operator;
	
	/**
	 * Constructor
	 * @param symbol - text for button
	 * @param operator 
	 * @param impl - calculator model where this button is used
	 * @param frame - frame where this button is displayed, used for showing warning messages.
	 */
	public BinaryOperationButton(String symbol, DoubleBinaryOperator operator, CalcModelImpl impl, Calculator frame) {
		super();
		this.setText(symbol);
		this.operator = operator;
		
		//dodavanje akcije gumbu
		this.addActionListener((event)->{
			//ako postoji zamrznuta vrijednost bacamo iznimku
			if(impl.getFreezeValue() != null) {
				JOptionPane.showMessageDialog(frame, "There is already freeze value set, cannot use this operation.");
				impl.setValue(Double.parseDouble(impl.getFreezeValue()));
				return;
			}
			
			//slučaj ako smo gumb kliknuli, a već postoji operand koji je postavljen
			//događa se kada zaredom pritišćemo gumb +,-,*,/
			if(impl.isActiveOperandSet()) {
				double result = impl.getPendingBinaryOperation().applyAsDouble(impl.getActiveOperand(), impl.getValue());
				impl.setValue(result);
			}
				
			//postavljamo novu vrijednost operanda te novu operaciju i "čistimo" kalkulator
			impl.setActiveOperand(impl.getValue());
			impl.setPendingBinaryOperation(this.operator);
			impl.clear();
		});
	}
	
	/**
	 * Setter method for internal DoubleBinaryOperator.
	 * @param other - DoubleBinaryOperator you want to set this button to use it.
	 */
	public void setOperator(DoubleBinaryOperator other) {
		this.operator = other;
	}

}
