package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Class which represents one FormLocalizationProvider.
 * @author Ivan Bilobrk
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge{
	
	/**
	 * Frame you want to add localization provider to.
	 */
	private JFrame frame;

	/**
	 * Constructor
	 * @param parent
	 * @param frame
	 */
	public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
		super(parent);
		this.frame = frame;
		
		frame.addWindowListener(new WindowAdapter() {
			
			 public void windowOpened(WindowEvent e) {
				 connect();
			 }
			 
			 public void windowClosing(WindowEvent e) {
			 }
			
			public void windowClosed(WindowEvent e) {
				disconnect();
			}
		});
		
	}

}
