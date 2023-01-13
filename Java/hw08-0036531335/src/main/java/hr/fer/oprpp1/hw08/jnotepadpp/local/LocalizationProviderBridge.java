package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Class representing a bridge (proxy) for communicating with LocalizationProvider so original LocalizationProvider doesn't hold reference 
 * to all GUI elements which are listeners for language change events.
 * @author Ivan Bilobrk
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider{
	
	/**
	 * Flag indicating if LocalizationProviderBridge is connected to LocalizationProvider.
	 */
	private boolean connected = false;
	
	/**
	 * LocalizationProvider with methods for getting translations for given keys.
	 */
	private ILocalizationProvider parent;
	
	/**
	 * LocalizationListener which listens for language changes from LocalizationProvider and alerts all GUI elements
	 * subscribed to this bridge.
	 */
	private ILocalizationListener listener;
	
	/**
	 * Current language.
	 */
	private String language;
	
	/**
	 * Constructor
	 * @param parent
	 */
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = parent;
		this.language = parent.getCurrentLanguage();
	}
	
	/**
	 * Method which disconnects this bridge from LocalizationProvider
	 */
	public void disconnect() {
		this.parent.removeLocalizationListener(listener);
		connected = false;
		language = this.getCurrentLanguage();
	}
	
	/**
	 * Method which connects this bridge from LocalizationProvider
	 */
	public void connect() {
		if(!connected && !parent.getCurrentLanguage().equals(language)) {
			this.language = parent.getCurrentLanguage();
			this.fire();
		}
		
		//stvaramo listenerea koji će za svaku promjenu jezika obavijestiti sve GUI komponente
		this.listener = new ILocalizationListener() {
			
			@Override
			public void localizationChanged() {
				fire();
			}
		};
		this.parent.addLocalizationListener(listener);
		connected = true;
	}
	
	
	@Override
	public String getString(String lan) {
		return parent.getString(lan);
	}

	@Override
	public String getCurrentLanguage() {
		return language;
	}

}
