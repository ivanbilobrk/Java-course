package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

/**
 * Class representing GUI status bas for JNotepad
 * @author Ivan Bilobrk
 *
 */
public class StatusBar extends JPanel{

	private static final long serialVersionUID = 1L;
	
	/**
	 * LocalizationProvider for translation
	 */
	private ILocalizationProvider lp;
	
	/**
	 * Current length of opened file, line, column and selection size.
	 */
	private int length, line, column, selection;
	
	/**
	 * Labels for showing info to user.
	 */
	private JLabel first, second, third;
	
	/**
	 * Custom DateTimeFormatter for clock
	 */
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").withZone(ZoneId.systemDefault());
	
	/**
	 * String holding current time
	 */
	private volatile String vrijeme;
	
	/**
	 * Flag which indicates thread responsible for updating time that it can stop working.
	 */
	private volatile boolean stopRequested;
	
	/**
	 * Constructor
	 * @param lp
	 */
	public StatusBar(ILocalizationProvider lp){
		this.lp = lp;
		length = line = column = selection = 0;
		this.lp.addLocalizationListener(()->{
			changeText();
		});
		Instant instant = Instant.now();
		vrijeme = formatter.format(instant);
		initGUI();
	}
	
	/**
	 * Method which initializes GUI for this status bar
	 */
	private void initGUI(){
		this.setLayout(new GridLayout(1,2));
		
		JPanel panel1 = new JPanel(new BorderLayout());
		JPanel panel2 = new JPanel(new BorderLayout());
		
		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		
		//inicijalne vrijednosti 
		first = new JLabel(lp.getString("length")+" : 0 " );
		second = new JLabel(lp.getString("line")+": 0 "+lp.getString("column")+" : 0 "+lp.getString("selection")+" : 0");
		third = new JLabel(vrijeme);
		
		panel1.add(first, BorderLayout.LINE_START);
		panel1.add(separator, BorderLayout.LINE_END);
		panel2.add(second, BorderLayout.LINE_START);
		panel2.add(third, BorderLayout.LINE_END);
		
		//posebna dretva zadužena za ažuriranje vremena
		Thread t = new Thread(()->{
			while(true) {
				try {
					Thread.sleep(500);
				} catch(Exception ex) {}
				if(stopRequested) break;
				SwingUtilities.invokeLater(()->{
					changeTime();
				});
			}
		});
		t.setDaemon(true);
		t.start();
		
		this.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY));
		this.add(panel1);
		this.add(panel2);
	}
	
	/**
	 * Private method which changes label responsible for showing time.
	 */
	private void changeTime() {
		Instant instant = Instant.now();
		vrijeme = formatter.format(instant);
		third.setText(vrijeme);
	}
	
	/**
	 * Method which stops thread responsible for updating time.
	 */
	public void stop() {
		stopRequested = true;
	}
	
	/**
	 * Method which updates text which shows length, line, column and selction.
	 */
	public void changeText() {
		first.setText(lp.getString("length")+" : "+length);
		second.setText(lp.getString("line")+": "+line+" "+lp.getString("column")+" : "+column+" "+lp.getString("selection")+" : "+selection+" ");
	}
	
	/**
	 * Setter for length.
	 * @param number
	 */
	public void setValueLength(int number) {
		this.length = number;
	}
	
	/**
	 * Setter for line.
	 * @param number
	 */
	public void setValueLine(int number) {
		this.line = number;
	}
	
	/**
	 * Setter for column.
	 * @param number
	 */
	public void setValueColumn(int number) {
		this.column = number;
	}
	
	/**
	 * Setter for selection length.
	 * @param number
	 */
	public void setValueSelection(int number) {
		this.selection = number;
	}
}
