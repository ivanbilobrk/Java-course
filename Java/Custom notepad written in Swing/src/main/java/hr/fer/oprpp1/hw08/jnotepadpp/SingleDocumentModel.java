package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Class representing one SingleDocumentModel
 * @author Ivan Bilobrk
 *
 */
public interface SingleDocumentModel {
	
	/**
	 * Method which gets JTextArea component of this SingleDocumentModel.
	 * @return JTextArea
	 */
	public JTextArea getTextComponent();
	
	/**
	 * Method which gets path of this SingleDocumentModel.
	 * @return path 
	 */
	public Path getFilePath();
	
	/**
	 * Method which sets path of this SingleDocumentModel.
	 * @param path - new path of this SingleDocumentModel
	 */
	public void setFilePath(Path path);
	
	/**
	 * Method which checks if this SingleDocumentModel has been modified.
	 * @return true if modified, false otherwise
	 */
	public boolean isModified();
	
	/**
	 * Method which changes modification flag of this SingleDocumentModel.
	 * @param modified
	 */
	public void setModified(boolean modified);
	
	/**
	 * Method which adds new listener to this SingleDocumentModel.
	 * @param l
	 */
	public void addSingleDocumentListener(SingleDocumentListener l);
	
	/**
	 * Method which removes listener from this SingleDocumentModel.
	 * @param l
	 */
	public void removeSingleDocumentListener(SingleDocumentListener l);
	
}
