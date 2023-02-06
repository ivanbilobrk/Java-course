package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Class representing one LocalizableAction. This class can be used to make buttons or jmenus and jmenu items to
 * change their text dynamically when localization has been changed.
 * @author Ivan Bilobrk
 *
 */
public abstract class LocalizableAction extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Listener for localization changes.
	 */
	private ILocalizationListener listener;
	
	/**
	 * Localization provider with methods to get translations.
	 */
	private ILocalizationProvider prov;
	
	/**
	 * Key of text for which you want to get translation for based on current selected language.
	 */
	private String key;

	/**
	 * Constructor
	 * @param prov
	 * @param key
	 */
	public LocalizableAction(ILocalizationProvider prov, String key) {
		super();
		this.prov = prov;
		this.key = key;
		makeAction();
		
		this.listener = ()->{
			makeAction();
		};
		
		prov.addLocalizationListener(listener);
	}
	
	/**
	 * Method which changes name of this action when localization has been changed. If we change the name of 
	 * action all buttons, jmenus, jmenu items will change their text shown on GUI.
	 */
	private void makeAction() {
		this.putValue(Action.NAME, prov.getString(key));
	}

}
