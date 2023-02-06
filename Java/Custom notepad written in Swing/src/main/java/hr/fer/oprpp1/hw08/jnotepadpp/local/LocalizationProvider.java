package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class representing one LocalizationProvider which extends abstract class AbstractLocalizationProvider and implements 
 * missing methods getCurrentLanguage and getString. This class can implement these methods because it has a reference to 
 * ResourceBundle which supplies LocalizationProvider with translations for given keys.
 * and implements 
 * @author Ivan Bilobrk
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider{
	
	/**
	 * Current language of LocalizationProvider
	 */
	private String language;
	
	/**
	 * ResourceBundle with desired translations
	 */
	private ResourceBundle bundle;
	
	/**
	 * Reference to LocalizationProvider. Since this class is a singleton this is the only instance of this class
	 * available to user.
	 */
	private static LocalizationProvider instance = new LocalizationProvider();
	
	/**
	 * Private constructor for this singleton which sets current language to english.
	 */
	private LocalizationProvider() {
		this.setLanguage("en");
	}
	
	/**
	 * Getter for the only instance of this class.
	 * @return
	 */
	public static LocalizationProvider getInstance() {
		return instance;
	}
	
	/**
	 * Method which changes current language of LocalizationProvider.
	 * @param lan
	 */
	public void setLanguage(String lan) {
		this.language = lan;
		this.bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.local.prijevodi", Locale.forLanguageTag(language));
		this.fire();
	}

	@Override
	public String getString(String key) {
		return this.bundle.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return language;
	}

}
