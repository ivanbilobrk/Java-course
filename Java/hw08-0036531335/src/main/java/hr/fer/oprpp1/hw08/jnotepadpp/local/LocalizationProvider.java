package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider{
	
	private String language;
	private ResourceBundle bundle;
	private static LocalizationProvider instance = new LocalizationProvider();
	
	private LocalizationProvider() {
		this.setLanguage("en");
	}
	
	public static LocalizationProvider getInstance() {
		return instance;
	}
	
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
