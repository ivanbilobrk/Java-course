package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;

public interface SingleDocumentModel {
	
	public JTextArea getTextComponent();
	public Path getFilePath();
	public void setFilePath(Path path);
	public boolean isModified();
	public void setModified(boolean modified);
	public void addSingleDocumentListener(SingleDocumentListener l);
	public void removeSingleDocumentListener(SingleDocumentListener l);
	
}
