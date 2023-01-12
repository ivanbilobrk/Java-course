package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.*;

public class DefaultSingleDocumentModel implements SingleDocumentModel {
	
	private List<SingleDocumentListener> listeners;
	private Path path;
	private String text;
	private JTextArea area;
	private boolean modified;
	private static int brojac = 0;
	private int counter;
	
	public DefaultSingleDocumentModel(Path path, String text) {
		super();
		counter = brojac++;
		this.path = path;
		this.text = text;
		modified = false;
		this.listeners = new ArrayList<>();
		area = new JTextArea();
		area.setText(text);
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
	
	private void alertListenersPath() {
		for(var x: listeners) {
			x.documentFilePathUpdated(this);
		}
	}
	
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
