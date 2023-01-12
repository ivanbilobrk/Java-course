package hr.fer.oprpp1.hw08.jnotepadpp;

public interface MultipleDocumentListener {
	
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);
	public void documentAdded(SingleDocumentModel model);
	public void documentRemoved(SingleDocumentModel model);
}
