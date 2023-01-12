package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentListener;

import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

public class StatusBar extends JPanel{

	private static final long serialVersionUID = 1L;
	private ILocalizationProvider lp;
	private int length, line, column, selection;
	private JLabel first, second, third;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	private volatile String vrijeme;
	private volatile boolean stopRequested;
	
	public StatusBar(ILocalizationProvider lp){
		this.lp = lp;
		length = line = column = selection = 0;
		this.lp.addLocalizationListener(()->{
			changeText();
		});
		this.vrijeme = formatter.format(LocalTime.now());
		initGUI();
	}
	
	private void initGUI(){
		this.setLayout(new GridLayout(1,2));
		
		JPanel panel1 = new JPanel(new BorderLayout());
		JPanel panel2 = new JPanel(new BorderLayout());
		
		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		
		first = new JLabel(lp.getString("length")+" : 0 " );
		second = new JLabel(lp.getString("line")+": 0 "+lp.getString("column")+" : 0 "+lp.getString("selection")+" : 0");
		third = new JLabel(vrijeme);
		
		panel1.add(first, BorderLayout.LINE_START);
		panel1.add(separator, BorderLayout.LINE_END);
		panel2.add(second, BorderLayout.LINE_START);
		panel2.add(third, BorderLayout.LINE_END);
		
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
		
		this.add(panel1);
		this.add(panel2);
	}
	
	private void changeTime() {
		vrijeme = formatter.format(LocalTime.now());
		third.setText(vrijeme);
	}
	
	public void stop() {
		stopRequested = true;
	}
	
	public void changeText() {
		first.setText(lp.getString("length")+" : "+length);
		second.setText(lp.getString("line")+": "+line+" "+lp.getString("column")+" : "+column+" "+lp.getString("selection")+" : "+selection+" ");
	}
	
	public void setValueLength(int number) {
		this.length = number;
	}
	
	public void setValueLine(int number) {
		this.line = number;
	}
	
	public void setValueColumn(int number) {
		this.column = number;
	}
	
	public void setValueSelection(int number) {
		this.selection = number;
	}
}
