package hr.fer.oprpp1.hw08.jnotepadpp;

/**
 * Interface with method of one SingleDocumentListener
 * @author Ivan Bilobrk
 *
 */
public interface SingleDocumentListener {
	
	/**
	 * Method invoked when SingleDocumentModel has been modified.
	 * @param model
	 */
	public void documentModifyStatusUpdated(SingleDocumentModel model);
	
	/**
	 * Method invoked when SingleDocumentModel path has been updated.
	 * @param model
	 */
	public void documentFilePathUpdated(SingleDocumentModel model);

}
