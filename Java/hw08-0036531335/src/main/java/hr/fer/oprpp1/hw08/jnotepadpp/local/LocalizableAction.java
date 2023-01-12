package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.AbstractAction;
import javax.swing.Action;

public abstract class LocalizableAction extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	
	private ILocalizationListener listener;
	private ILocalizationProvider prov;
	private String key;

	public LocalizableAction(ILocalizationProvider prov, String key) {
		super();
		this.prov = prov;
		this.key = key;
		makeAction();
		
		prov.addLocalizationListener(()->{
			makeAction();
		});
	}

	private void makeAction() {
		this.putValue(Action.NAME, prov.getString(key));
	}

}
