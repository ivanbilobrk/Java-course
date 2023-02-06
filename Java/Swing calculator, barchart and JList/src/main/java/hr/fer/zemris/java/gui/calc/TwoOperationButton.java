package hr.fer.zemris.java.gui.calc;

import java.awt.event.ActionListener;
import java.util.function.DoubleUnaryOperator;
import javax.swing.JButton;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

/**
 * Class representing one button of calculator which has two operations.
 * @author Ivan Bilobrk
 *
 */
public class TwoOperationButton extends JButton{
	

	private static final long serialVersionUID = 1L;
	
	/**
	 * Flag telling current operation that is used for calculating.
	 */
	private boolean version;
	
	/**
	 * Texts which are shown on button depending on current version of button.
	 */
	private String firstText, secondText;

	/**
	 * First and second action of a button.
	 */
	private ActionListener firstAction, secondAction;
	
	/**
	 * Constrctor
	 * @param first - first operator of a button
	 * @param second - first operator of a button
	 * @param version - initial version of a button, true uses first operator for calculation and false second
	 * @param firstText - text displayed when version is set to true
	 * @param secondText - text displayed when version is set to false
	 * @param model
	 */
	public TwoOperationButton(DoubleUnaryOperator first, DoubleUnaryOperator second, boolean version, String firstText,
			String secondText, CalcModel model) {
		super();
		this.version = version;
		this.firstText = firstText;
		this.secondText = secondText;
		this.firstAction = (event)->model.setValue(first.applyAsDouble(model.getValue()));
		this.secondAction = (event)->model.setValue(second.applyAsDouble(model.getValue()));
		
		this.setText(firstText);
		this.addActionListener(firstAction);
	}
	
	/**
	 * Method used to change operation for this button.
	 */
	public void changeOperationAndText() {
		version = !version;
		if(version) {
			this.setText(firstText);
			this.removeActionListener(secondAction);
			this.addActionListener(firstAction);
		} else {
			this.setText(secondText);
			this.removeActionListener(firstAction);
			this.addActionListener(secondAction);
		}
		
	}
	
	
}
