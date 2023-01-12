package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JComponent;

public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	
	public JComponent getVisualComponent();
	public SingleDocumentModel createNewDocument();
	public SingleDocumentModel getCurrentDocument();
	public SingleDocumentModel loadDocument(Path path);
	public void saveDocument(SingleDocumentModel model, Path newPath);
	public void closeDocument(SingleDocumentModel model);
	public void addMultipleDocumentListener(MultipleDocumentListener l);
	public void removeMultipleDocumentListener(MultipleDocumentListener l);
	public int getNumberOfDocuments();
	public SingleDocumentModel getDocument(int index);
	public SingleDocumentModel findForPath(Path path); //null, if no such model exists
	public int getIndexOfDocument(SingleDocumentModel doc); //-1 if not present
	
}