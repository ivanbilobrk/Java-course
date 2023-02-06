package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of ILocalizationProvider. It is abstract because there is no getString and getCurrentLanguage method implementation.
 * @author Ivan Bilobrk
 *
 */

public abstract class AbstractLocalizationProvider implements ILocalizationProvider{
	
	/**
	 * All localization listeners.
	 */
	private List<ILocalizationListener> listeners;
	
	/**
	 * Constructor
	 */
	public AbstractLocalizationProvider() {
		this.listeners = new ArrayList<>();
	}
	
	public void addLocalizationListener(ILocalizationListener listener) {
		listeners.add(listener);
	}
	
	public void removeLocalizationListener(ILocalizationListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Method which alerts all listeners to this localization provider that language has been changed and that they
	 * need to update its text.
	 */
	public void fire() {
		for(var x: listeners) {
			x.localizationChanged();
		}
	}
	
	public abstract String getString(String lan);
}
