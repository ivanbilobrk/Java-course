package hr.fer.oprpp1.hw08.jnotepadpp.local;

public class LocalizationProviderBridge extends AbstractLocalizationProvider{
	
	private boolean connected = false;
	private ILocalizationProvider parent;
	private ILocalizationListener listener;
	private String language;
	
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = parent;
		this.language = parent.getCurrentLanguage();
	}
	
	public void disconnect() {
		this.parent.removeLocalizationListener(listener);
		connected = false;
		language = this.getCurrentLanguage();
	}
	
	public void connect() {
		if(!connected && !parent.getCurrentLanguage().equals(language)) {
			this.language = parent.getCurrentLanguage();
			this.fire();
		}
		
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
