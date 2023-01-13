package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Interface which tells all methods one localization provider should have.
 * @author Ivan Bilobrk
 *
 */
public interface ILocalizationProvider {
	
	/**
	 * Method which adds new localiztion listener to this localization provider.
	 * @param listener
	 */
	public void addLocalizationListener(ILocalizationListener listener);
	
	/**
	 * Method which removes localiztion listener from this localization provider.
	 * @param listener
	 */
	public void removeLocalizationListener(ILocalizationListener listener);
	
	/**
	 * Method which gets string for given key in current language. 
	 * @param lan - key for desired word
	 * @return translation for given key
	 */
	public String getString(String lan);
	
	/**
	 * Method which gets current language.
	 * @return - current language
	 */
	public String getCurrentLanguage();
}
