package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Class for initializing GUI of two lists showing prime numbers. 
 * @author Ivan Bilobrk
 *
 */
public class PrimDemo extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor.
	 */
	public PrimDemo() {
		setLocation(20, 50);
		setSize(300, 200);
		setTitle("Moj prozor!");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		initGUI();
	}	
	
	/**
	 * Method for initializing GUI of a component. Component shows two lists of prime numbers.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		PrimListModel model = new PrimListModel();
		//početno se u modelu nalazi samo broj 1
		model.next();
		
		JList<Integer> list1 = new JList<>(model);
		JList<Integer> list2 = new JList<>(model);
		
		//raspoređivanje elemenata
		JPanel bottomPanel = new JPanel(new GridLayout(1, 1));

		JButton dodaj = new JButton("Dodaj");
		bottomPanel.add(dodaj);
		
		dodaj.addActionListener(e -> {
			model.next();
		});


		JPanel central = new JPanel(new GridLayout(1, 0));
		central.add(new JScrollPane(list1));
		central.add(new JScrollPane(list2));
		
		cp.add(central, BorderLayout.CENTER);
		cp.add(bottomPanel, BorderLayout.PAGE_END);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new PrimDemo();
			frame.pack();
			frame.setVisible(true);
		});
	}
}
