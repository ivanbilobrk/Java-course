package hr.fer.oprpp1.hw08.jnotepadpp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import java.util.*;

/**
 * Class representing implementation of MultipleDocumentModel.
 * This class is responsible for GUI of tabbed pane in JNotepad
 * @author Ivan Bilobrk
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * All documents shown in tab.
	 */
	private List<SingleDocumentModel> docs;
	
	/**
	 * Current selected document.
	 */
	private SingleDocumentModel currentDoc;
	
	/**
	 * All listeners subscribed to this model.
	 */
	private List<MultipleDocumentListener> listeners;
	
	/**
	 * Constructor
	 */
	public DefaultMultipleDocumentModel() {
		this.listeners = new ArrayList<>();
		this.docs = new ArrayList<>();
	}
	
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return docs.iterator();
	}

	@Override
	public JComponent getVisualComponent() {
		return this;
	}
	

	@Override
	public SingleDocumentModel createNewDocument() {
		DefaultSingleDocumentModel temp = new DefaultSingleDocumentModel(null, "");
		docs.add(temp);
		for(var x: listeners) {
			x.documentAdded(temp);
		}
		return temp;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDoc;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		if(path == null) {
			throw new IllegalArgumentException("Kriva putanja.");
		} else {
			if(findForPath(path) != null) {
				throw new IllegalArgumentException("Dokument je već otvoren.");
			}
		}
		
		try {
			byte[] okteti;
			try {
				okteti = Files.readAllBytes(path);
			} catch(Exception ex) {
				throw ex;
			}
			String tekst = new String(okteti, StandardCharsets.UTF_8);
			
			DefaultSingleDocumentModel temp = new DefaultSingleDocumentModel(path, tekst);
			docs.add(temp);
			for(var x: listeners) {
				x.documentAdded(temp);
			}
			
			return temp;
		} catch (IOException e) {
			throw new IllegalStateException("Ne mogu otvoriti datoteku.");
		}
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		if(newPath != null) {
			int len = docs.size();
			for(int i = 0; i < len; i++) {
				if(i != getIndexOfDocument(model) && docs.get(i).getFilePath() != null && docs.get(i).getFilePath().equals(newPath)) {
					throw new IllegalArgumentException("Već postoji datoteka te putanje. Zatvori ju i probaj ponovno.");
				}
			}
		} else {
			newPath = model.getFilePath();
		}
		
		byte[] podatci = model.getTextComponent().getText().getBytes(StandardCharsets.UTF_8);
		try {
			Files.write(newPath, podatci);
		} catch (IOException e) {
			throw new IllegalStateException("Ne mogu spremiti datoteku.");
		}
		
		model.setFilePath(newPath);
		model.setModified(false);
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		docs.remove(model);
		for(var x: listeners) {
			x.documentRemoved(model);
		}
		
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);
		
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
		
	}

	@Override
	public int getNumberOfDocuments() {
		return docs.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		if(index >= 0 && index < docs.size()) {
			return docs.get(index);
		}
		throw new IllegalArgumentException("Index prevelik.");
		
	}

	@Override
	public SingleDocumentModel findForPath(Path path) {
		if(path == null) {
			throw new IllegalArgumentException("Kriva putanja.");
		}
		
		for(var x: docs) {
			if(x.getFilePath() != null && x.getFilePath().equals(path)) {
				return x;
			}
		}
		return null;
	}

	@Override
	public int getIndexOfDocument(SingleDocumentModel doc) {
		return docs.indexOf(doc);
	}
	
	public void changeCurrentDoc(SingleDocumentModel previous, SingleDocumentModel current) {
		this.currentDoc = current;
		for(var x: listeners) {
			x.currentDocumentChanged(previous, current);
		}
	}

}
