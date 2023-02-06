package hr.fer.zemris.java.gui.calc;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * Class implementing GUI for calculator.
 * @author Ivan Bilobrk
 *
 */
public class Calculator extends JFrame {
	
	private static final long serialVersionUID = 8694571683528436301L;
		
		/**
		 * Constructor.
		 */
		public Calculator() {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			//setSize(500, 500);
			initGUI();
			pack();
		}
		
		/**
		 * Private method used to initialize GUI for calculator.
		 */
		private void initGUI() {
			//interni stog koji se koristi za gumbe push i pop
			Stack<Double> stack = new Stack<>();
			Container cp = getContentPane();
			//postavljanje razmještaja sa razmakom 3
			cp.setLayout(new CalcLayout(3));
			
			CalcModelImpl impl = new CalcModelImpl();
			
			Display display = new Display("");
			
			//dodavanje listenera u model
			impl.addCalcValueListener(display);
			cp.add(display ,"1,1");
			
			//pozicije gumba koji predstavljaju brojeve
			List<RCPosition> positions = new ArrayList<>(List.of(new RCPosition(5,3), new RCPosition(4,3), new RCPosition(4,4), new RCPosition(4,5),
					new RCPosition(3,3), new RCPosition(3,4), new RCPosition(3,5), new RCPosition(2,3), new RCPosition(2,4), new RCPosition(2,5)));
			
			//dodavanje brojeva i funkcionalnosti gumba broja
			for(int i = 0; i <= 9; i++) {
				NumberButton button = new NumberButton(i);
				button.addActionListener((event)->{
					try {
						button.addNumber(impl);
					}catch(Exception e) {
						JOptionPane.showMessageDialog(this, e.getMessage());
					}
				});
				cp.add(button, positions.get(i));
			}
			
			//dodavanje gumba jednakosti i njegove funkcionalnosti
			JButton equality = new JButton("=");
			equality.addActionListener((event)->{
				if(impl.getFreezeValue()!= null) {
					JOptionPane.showMessageDialog(this, "Nema trenutnog operanda.");
				} else {
					try {
						double result = impl.getPendingBinaryOperation().applyAsDouble(impl.getActiveOperand(), impl.getValue());
						impl.setValue(result);
						impl.clearActiveOperand();
						impl.setPendingBinaryOperation(null);
					}catch(Exception e) {
						JOptionPane.showMessageDialog(this, e.getMessage());
					}
				}
			});
			
			//dodavanje gumba za +,-,*,/
			BinaryOperationButton plus = new BinaryOperationButton("+", (v1, v2)->v1+v2, impl, this);
			BinaryOperationButton minus = new BinaryOperationButton("-", (v1, v2)->v1-v2, impl, this);
			BinaryOperationButton multiplication = new BinaryOperationButton("*", (v1, v2)->v1*v2, impl, this);
			BinaryOperationButton division = new BinaryOperationButton("/", (v1, v2)->v1/v2, impl, this);
			
			//gumb clr
			JButton clear = new JButton("clr");
			clear.addActionListener((event)->{
				impl.clear();
			});
			
			//gumb reset
			JButton reset = new JButton("reset");
			reset.addActionListener((event)->{
				impl.clearAll();
			});
			
			//gumb promjene predznaka
			JButton swapSign = new JButton("+/-");
			swapSign.addActionListener((event)->{
				try {
					impl.swapSign();
				}catch(Exception e){
					JOptionPane.showMessageDialog(this, e.getMessage());
				}
			});
			
			///gumb za dodavanje decimalne točke
			JButton addDecimal = new JButton(".");
			addDecimal.addActionListener((event)->{
				try {
					impl.insertDecimalPoint();
				}catch(Exception e) {
					JOptionPane.showMessageDialog(this, e.getMessage());
				}
			});
			
			//dodavanje gumba koji imaju dvije funkcionalnosti
			List<TwoOperationButton> buttons = new ArrayList<>();
			
			buttons.add(new TwoOperationButton((v)->Math.sin(v), (v)->Math.asin(v), true, "sin", "arcsin", impl));
			buttons.add(new TwoOperationButton((v)->Math.cos(v), (v)->Math.acos(v), true, "cos", "arccos", impl));
			buttons.add(new TwoOperationButton((v)->Math.tan(v), (v)->Math.atan(v), true, "tan", "arctan", impl));
			buttons.add(new TwoOperationButton((v)->1.0/Math.tan(v), (v)->1.0/Math.atan(v), true, "ctg", "arcctg", impl));
			buttons.add(new TwoOperationButton((v)->Math.log10(v), (v)->Math.pow(10, v), true, "log", "10^x", impl));
			buttons.add(new TwoOperationButton((v)->Math.log(v), (v)->Math.pow(Math.E, v), true, "ln", "e^x", impl));
			
			positions.clear();
			//stvaranje pozicija za gumbe sa dvije funkcionalnosti
			positions.addAll(List.of(new RCPosition(2, 2), new RCPosition(3, 2), new RCPosition(4, 2), new RCPosition(5, 2), new RCPosition(3, 1), new RCPosition(4, 1)));
			
			for(int i = 0; i < 6; i++) {
				cp.add(buttons.get(i), positions.get(i));
			}
			
			//gumba za 1/x
			JButton oneSlashX = new JButton("1/x");
			oneSlashX.addActionListener((event)->{
				impl.setValue(1.0/Double.parseDouble(impl.toString()));
			});
			
			//gumb koji ima dvije funkcionalnosti, ali je modeliran ssa gumbom koji predstavlja binarnu operaciju kojem 
			//promjenimo operator nakon što se klikne gumb inv na kalkulatoru
			BinaryOperationButton xToN = new BinaryOperationButton("x^n", (v1, v2)-> Math.pow(v1, v2), impl, this);
			
			//gumb za promjenu funckionalnosti nekih drugih gumbova
			JCheckBox invert = new JCheckBox("Inv");
			invert.addActionListener((event)->{
				for(var x: buttons) {
					x.changeOperationAndText();
				}
				xToN.setText("x^(1/n)");
				xToN.setOperator((v1, v2)->Math.pow(v1, 1.0/v2));
			});
			
			//gumbovi pop i push za igranje sa stogom
			JButton push = new JButton("push");
			push.addActionListener((event)->{
				stack.push(impl.getValue());
			});
			
			JButton pop = new JButton("pop");
			pop.addActionListener((event)->{
				if(stack.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Nema elemenata na stogu");
				} else {
					impl.setValue(stack.pop());
				}
			});
			
			//dodavanje ostatka gumbova u frame
			cp.add(push, "3,7");
			cp.add(pop, "4,7");
			cp.add(xToN, "5,1");
			cp.add(oneSlashX, "2,1");
			cp.add(invert, "5,7");
			cp.add(addDecimal, "5,5");
			cp.add(swapSign, "5,4");
			cp.add(reset, "2,7");
			cp.add(clear, "1,7");
			cp.add(division, "2,6");
			cp.add(multiplication, "3,6");
			cp.add(plus, "5,6");
			cp.add(minus, "4,6");
			cp.add(equality, "1,6");
		}
		
		
		public static void main(String[] args) {
			SwingUtilities.invokeLater(()->{
				new Calculator().setVisible(true);
			});
		}
}
