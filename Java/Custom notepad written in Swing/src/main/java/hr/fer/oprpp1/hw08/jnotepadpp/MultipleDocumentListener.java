package hr.fer.oprpp1.hw08.jnotepadpp;

/**
 * Interface with methods of one MultipleDocumentListener
 * @author Ivan Bilobrk
 *
 */
public interface MultipleDocumentListener {
	
	/**
	 * Method invoked when current document of MultipleDocumentModel is changed.
	 * @param previousModel
	 * @param currentModel
	 */
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);
	
	/**
	 * Method invoked when new document has been added to MultipleDocumentModel.
	 * @param model
	 */
	public void documentAdded(SingleDocumentModel model);
	
	/**
	 * Method invoked when document has been removed from MultipleDocumentModel.
	 * @param model
	 */
	public void documentRemoved(SingleDocumentModel model);
}
