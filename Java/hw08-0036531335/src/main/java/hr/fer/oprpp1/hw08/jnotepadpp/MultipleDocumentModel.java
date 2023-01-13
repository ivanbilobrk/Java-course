package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JComponent;

/**
 * Interface with methods of one MultipleDocumentModel.
 * @author Ivan Bilobrk
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	
	/**
	 * Method which gets GUI component for this model.
	 * @return
	 */
	public JComponent getVisualComponent();
	
	/**
	 * Method which is used for creating new SingleDocumentModel and adding it to this MultipleDocumentModel.
	 * Alerts all subscribed listeners.
	 * @return new SingleDocumentModel
	 */
	public SingleDocumentModel createNewDocument();
	
	/**
	 * Method used to get current SingleDocumentModel for this MultipleDocumentModel.
	 * @return current SingleDocumentModel.
	 */
	public SingleDocumentModel getCurrentDocument();
	
	/**
	 * Method which creates new SingleDocumentModel based on given path and adds it to this MultipleDocumentModel.
	 * @param path - desired path for new SingleDocumentModel which will be added to this MultipleDocumentModel.
	 * Alerts all subscribed listeners.
	 * @return new SingleDocumentModel.
	 */
	public SingleDocumentModel loadDocument(Path path);
	
	/**
	 * Method used to save changes made to one SingleDocumentModel to given path.
	 * @param model - SingleDocumentModel you want to save
	 * @param newPath - path you want to save given SingleDocumentModel to.
	 */
	public void saveDocument(SingleDocumentModel model, Path newPath);
	
	/**
	 * Method used for closing desired document of this MultipleDocumentModel.
	 * Alerts all subscribed listeners.
	 * @param model - SingleDocumentModel you want to close.
	 */
	public void closeDocument(SingleDocumentModel model);
	
	/**
	 * Method used to add new listeners to this model.
	 * @param l 
	 */
	public void addMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Method used to remove desired listener from this model.
	 * @param l
	 */
	public void removeMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Method which gets number of documents in this MultipleDocumentModel.
	 * @return - number of listeners.
	 */
	public int getNumberOfDocuments();
	
	/**
	 * Method which gets SingleDocumentModel at desired index.
	 * @param index for which you want to get SingleDocumentModel.
	 * @return - SingleDocumentModel at given index of this MultipleDocumentModel.
	 */
	public SingleDocumentModel getDocument(int index);
	
	/**
	 * Method which finds SingleDocumentModel of this MultipleDocumentModel based on given path.
	 * @param path for which you want to get SingleDocumentModel.
	 * @return SingleDocumentModel at given path
	 */
	public SingleDocumentModel findForPath(Path path); //null, if no such model exists
	
	/**
	 * Method which gets index of given SingleDocumentModel in this MultipleDocumentModel
	 * @param doc - SingleDocumentModel for which you want to get index in this MultipleDocumentModel
	 * @return index of SingleDocumentModel in this MultipleDocumentModel
	 */
	public int getIndexOfDocument(SingleDocumentModel doc); //-1 if not present
	
}