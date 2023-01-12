package hr.fer.oprpp1.hw08.jnotepadpp;

public interface SingleDocumentListener {
	
	public void documentModifyStatusUpdated(SingleDocumentModel model);
	public void documentFilePathUpdated(SingleDocumentModel model);

}
