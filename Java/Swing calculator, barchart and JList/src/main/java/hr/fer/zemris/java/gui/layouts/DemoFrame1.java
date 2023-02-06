package hr.fer.zemris.java.gui.layouts;

import java.awt.Color;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Demo class used for testing CalcLayout.
 * @author Ivan Bilobrk
 *
 */
public class DemoFrame1 extends JFrame {
	
	private static final long serialVersionUID = 8694571683528436301L;

		public DemoFrame1() {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			//setSize(500, 500);
			initGUI();
			pack();
		}
		
		private void initGUI() {
			Container cp = getContentPane();
			cp.setLayout(new CalcLayout(3));
			cp.add(l("tekst 1"), "1,1");
			cp.add(l("tekst 2"), new RCPosition(1,6));
			cp.add(l("tekst stvarno najdulji"), new RCPosition(1,7));
			cp.add(l("tekst srednji"), new RCPosition(2,1));
			cp.add(l("tekst srednji"), new RCPosition(2,2));
			cp.add(l("tekst srednji"), new RCPosition(2,3));
			cp.add(l("tekst srednji"), new RCPosition(2,4));
			cp.add(l("tekst srednji"), new RCPosition(2,5));
			cp.add(l("tekst srednji"), new RCPosition(2,6));
			cp.add(l("tekst srednji"), new RCPosition(2,7));
			cp.add(l("tekst srednji"), new RCPosition(3,1));
			cp.add(l("tekst srednji"), new RCPosition(3,2));
			cp.add(l("tekst srednji"), new RCPosition(3,3));
			cp.add(l("tekst srednji"), new RCPosition(3,4));
			cp.add(l("tekst srednji"), new RCPosition(3,5));
			cp.add(l("tekst srednji"), new RCPosition(3,6));
			cp.add(l("tekst srednji"), new RCPosition(3,7));
			cp.add(l("tekst kraći"), new RCPosition(4,1));
			cp.add(l("tekst kraći"), new RCPosition(4,2));
			cp.add(l("tekst kraći"), new RCPosition(4,3));
			cp.add(l("tekst kraći"), new RCPosition(4,4));
			cp.add(l("tekst srednji"), new RCPosition(4,5));
			cp.add(l("tekst kraći"), new RCPosition(4,6));
			cp.add(l("tekst"), new RCPosition(4,7));
			cp.add(l("tekst"), new RCPosition(5,1));
			cp.add(l("tekst"), new RCPosition(5,2));
			cp.add(l("tekst"), new RCPosition(5,3));
			cp.add(l("tekst"), new RCPosition(5,4));
			cp.add(l("tekst"), new RCPosition(5,5));
			cp.add(l("tekst"), new RCPosition(5,6));
			cp.add(l("tekst"), new RCPosition(5,7));
		}
		
		private JLabel l(String text) {
			JLabel l = new JLabel(text);
			l.setBackground(Color.YELLOW);
			l.setOpaque(true);
			return l;
		}
		
		public static void main(String[] args) {
			SwingUtilities.invokeLater(()->{
				new DemoFrame1().setVisible(true);
			});
		}
}
