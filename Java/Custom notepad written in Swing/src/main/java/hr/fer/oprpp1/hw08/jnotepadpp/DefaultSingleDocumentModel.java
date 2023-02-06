package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.*;

/**
 * Implementation of SingleDocumentModel
 * @author Ivan Bilobrk
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
	
	/**
	 * All listeners subscribed to this model.
	 */
	private List<SingleDocumentListener> listeners;
	
	/**
	 * Path of this model.
	 */
	private Path path;
	
	/**
	 * Current content of this model.
	 */
	private String text;
	
	/**
	 * GUI component of this model.
	 */
	private JTextArea area;
	
	/**
	 * Flag telling if model has been modified.
	 */
	private boolean modified;
	
	/**
	 * Counter used for enumerating created models. Used for equals and hashcode method.
	 */
	private static int brojac = 0;
	
	/**
	 * Index of this model.
	 */
	private int counter;
	
	/**
	 * Constructor
	 * @param path
	 * @param text
	 */
	public DefaultSingleDocumentModel(Path path, String text) {
		super();
		counter = brojac++;
		this.path = path;
		this.text = text;
		modified = false;
		this.listeners = new ArrayList<>();
		area = new JTextArea();
		area.setText(text);
		
		//dodajemo listenera na dokument vezan uz JTextArea koji postavlja zastavicu koja označava da se dokument promjenio
		area.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				setModified(true);
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				setModified(true);
				
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				setModified(true);
				
			}
		});
		
	}

	@Override
	public JTextArea getTextComponent() {
		return area;
	}

	@Override
	public Path getFilePath() {
		return path;
	}

	@Override
	public void setFilePath(Path path) {
		if(path == null) {
			throw new IllegalArgumentException("Path ne smije bit null.");
		}
		
		this.path = path;
		alertListenersPath();
		
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
		alertListenersModified();
		
	}
	
	/**
	 * Private method used for alerting listeners if there has been a change in path.
	 */
	private void alertListenersPath() {
		for(var x: listeners) {
			x.documentFilePathUpdated(this);
		}
	}
	
	/**
	 * Private method used for alerting listeners if there has been a change in document.
	 */
	private void alertListenersModified() {
		for(var x: listeners) {
			x.documentModifyStatusUpdated(this);
		}
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(l);
		
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
		
	}

	@Override
	public int hashCode() {
		return Objects.hash(counter);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultSingleDocumentModel other = (DefaultSingleDocumentModel) obj;
		return counter == other.counter;
	}
}
