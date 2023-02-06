package hr.fer.zemris.java.gui.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

/**
 * Custom calculator model implementation.
 * @author Ivan Bilobrk
 *
 */
public class CalcModelImpl implements CalcModel {
	
	/**
	 * Flag telling if calculator is editable.
	 */
	private boolean editable = true;
	/**
	 * Flag telling if current calculator value is positive or false.
	 */
	private boolean positive = true;
	/**
	 * String holding current input of calculator.
	 */
	private String input = "";
	/**
	 * Current input value of calculator as double.
	 */
	private double inputValue = 0;
	/**
	 * Current freeze value of calculator. Used for showing frozen value of calculator on display when using binary operations.
	 */
	private String freezeValue = null;
	/**
	 * Current active operand of a calculator.
	 */
	private double activeOperand = 0;
	/**
	 * Flag for telling if there is currently active operand set.
	 */
	private boolean isActiveOperandSet = false;
	/**
	 * Pending double binary operator.
	 */
	private DoubleBinaryOperator op;
	/**
	 * All listeners of this calculator model.
	 */
	private List<CalcValueListener> listeners = new ArrayList<>();
	
	@Override
	public void addCalcValueListener(CalcValueListener l) {
		listeners.add(l);
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(l);
	}

	@Override
	public double getValue() {
		//dodavanje predznaka ako je nužno
		if(!positive && inputValue >= 0) {
			return -inputValue;
		} 
		return inputValue;
	}
	
	/**
	 * Method used for alerting listeners when there has been a change in current calculator value.
	 */
	private void alertListeners() {
		for(var l: listeners) {
			l.valueChanged(this);
		}
	}

	@Override
	public void setValue(double value) {
		//promjena predznaka 
		if(value < 0) {
			positive = false;
		} else {
			positive = true;
		}
		
		//ažuriranje vrijednosti
		inputValue = value;
		input = value +"";
		editable = false;
		//obavještavanje svih listenera
		alertListeners();
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void clear() {
		input = "0";
		inputValue = 0;
		editable = true;
		positive = true;
		//obavještavanje svih listenera
		alertListeners();
	}

	@Override
	public void clearAll() {
		freezeValue = null;
		clear();
		clearActiveOperand();
		op = null;
		//obavještavanje svih listenera
		alertListeners();
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if(!editable) {
			throw new CalculatorInputException("Kalkulator nije editabilan.");
		} else {
			positive = !positive;
			freezeValue = null;
			//obavještavanje svih listenera
			alertListeners();
		}
		
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if(!editable) {
			throw new CalculatorInputException("Model nije editabilan.");
		}
		
		if(input.contains(".")) {
			throw new CalculatorInputException("Točka već postoji.");
		} 
		
		if(input.length() == 0) {
			throw new CalculatorInputException("Ne mogu dodati točku, fali cijeli broj.");
		}
		freezeValue = null;
		input += ".";
		//obavještavanje svih listenera
		alertListeners();
	}
	
	/**
	 * Method for getting current freeze value of calculator model.
	 * @return current freeze value.
	 */
	public String getFreezeValue() {
		return freezeValue;
	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if(!isEditable()) {
			throw new CalculatorInputException("Ne možeš koristiti insertDigit ako kalkulator nije editabilan.");
		} else {
			input += ""+digit;
			
			int currentLen = input.length();
			
			if(currentLen >= 309) {
				throw new CalculatorInputException("Previše brojeva dodano.");
			}
	
			
			try {
				double tempValue = Double.parseDouble(input);
				inputValue = tempValue;
				if(!input.contains(".")) {
					boolean hasAnotherValue = false;
					int index = 0;
					
					//provjera ima li trenutni string koji predstavlja vrijednost neki broj različit od 0, ovo je provjera kada
					//imamo više uzastopnih nula na početku te ih ne želimo prikazivati
					for(int i = 0; i < input.length(); i++) {
						if(input.charAt(i) != '0') {
							index = i;
							hasAnotherValue = true;
							break;
						}
					}
					
					if(!hasAnotherValue) {
						//ako je input string koji ima samo 0 dovoljno je da ima jednu 0
						input = "0";
					} else {
						//mičemo sve nule koje su se eventualno našle na početku inputa
						input = input.substring(index);
					}
				} 
				freezeValue = null;
				//obavještavanje svih listenera
				alertListeners();
			} catch(NumberFormatException e) {
				throw new CalculatorInputException("Ne mogu parsirati novi broj s ulaza.");
			}
		}
		
	}

	@Override
	public boolean isActiveOperandSet() {
		return isActiveOperandSet;
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if(!isActiveOperandSet) {
			throw new IllegalStateException("Operand nije postavljen.");
		}
		return activeOperand;
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
		isActiveOperandSet = true;
		
	}

	@Override
	public void clearActiveOperand() {
		activeOperand = 0;
		isActiveOperandSet = false;
		
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return op;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		this.op = op;
		
		//postavljanje freeze value
		if(op != null) {
			if(!positive && !input.contains("-")) {
				//dodavanje minusa po potrebi
				this.freezeValue = "-"+input;
			} else {
				this.freezeValue = input;
			}
		} else {
			freezeValue = null;
		}
		
	}
	
	@Override
	public String toString() {
		if(freezeValue != null) {
			//ako je freeze value postavljen njega vraćamo
			return freezeValue;
		} else if(input.length() == 0){
			//ako je string koji pamti trenutni input prazan vraćamo 0 ili -0 ovisno o predznaku
			return positive ? "0" : "-0";
			//promjena predznaka po potrebi i vraćanje inputa kao string
		} else if(!positive && input.charAt(0) != '-') {
			return "-"+input;
		} else {
			return input;
		}
	}
}
