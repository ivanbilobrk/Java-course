package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class FormLocalizationProvider extends LocalizationProviderBridge{
	
	private JFrame frame;

	public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
		super(parent);
		this.frame = frame;
		
	}

}
