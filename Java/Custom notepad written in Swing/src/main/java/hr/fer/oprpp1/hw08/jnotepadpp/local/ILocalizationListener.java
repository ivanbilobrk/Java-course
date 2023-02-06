package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Interface specifying methods for one localization listener.
 * @author Ivan Bilobrk
 *
 */
public interface ILocalizationListener {
	
	/**
	 * Method which is invoked when there has been a localization change and listener can detect it and make 
	 * desirable actions.
	 */
	public void localizationChanged();
	
}
