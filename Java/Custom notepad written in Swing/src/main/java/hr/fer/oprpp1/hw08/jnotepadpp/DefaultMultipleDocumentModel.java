package hr.fer.oprpp1.hw08.jnotepadpp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;

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
	 * LocalizationProvider used for translation.
	 */
	private FormLocalizationProvider flp;
	
	/**
	 * Notepad frame for showing messages.
	 */
	private JNotepadPP notepad;
	
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
	public DefaultMultipleDocumentModel(FormLocalizationProvider flp, JNotepadPP notepad) {
		this.notepad = notepad;
		this.flp = flp;
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
	
	SingleDocumentListener listener = new SingleDocumentListener() {
		
		@Override
		public void documentModifyStatusUpdated(SingleDocumentModel model) {
			int index = getIndexOfDocument(model);
			
			if(model.isModified()) {
				setIconAt(index, Util.getSave("red", flp, notepad));
			} else {
				setIconAt(index, Util.getSave("green", flp, notepad));
			}
			
		}
		
		@Override
		public void documentFilePathUpdated(SingleDocumentModel model) {
			int index = getIndexOfDocument(model);
			setTitleAt(index, model.getFilePath().getFileName().toString());	
			if(model.getFilePath() != null) {
				setTooltipText(model);
			}
		}
	};
	
	/**
	 * Method used for setting tool tip when path of document has been updated or when document has been added.
	 * @param model - document fow which you want to set tooltip for
	 */
	private void setTooltipText(SingleDocumentModel model) {
		try {
			setToolTipTextAt(getIndexOfDocument(model), model.getFilePath().toRealPath(LinkOption.NOFOLLOW_LINKS).toString());
		} catch (IOException e) {
			String title = flp.getString("warning");
			String description = flp.getString("toolTipFail");
			String options[] = {"OK"};
			Util.showMessage(title, description, notepad, options);
		}
	}
	

	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel previous = currentDoc;
		DefaultSingleDocumentModel model = new DefaultSingleDocumentModel(null, "");
		docs.add(model);
		
		ImageIcon icon = Util.getSave("red", flp, notepad);
		model.setModified(true);
		addTab("unnamed",icon ,new JScrollPane(model.getTextComponent()));
			
		model.addSingleDocumentListener(listener);
		
		setToolTipTextAt(getIndexOfDocument(model), "(unnamed)");
		
		setSelectedIndex(getComponentCount()-1);
		
		for(var x: listeners) {
			x.documentAdded(model);
		}
		
		currentDoc = model;
		changeCurrentDoc(previous, currentDoc);
		return model;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDoc;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		SingleDocumentModel previous = currentDoc;
		if(path == null) {
			throw new IllegalArgumentException("Kriva putanja.");
		} else {
			SingleDocumentModel model = findForPath(path);
			if(model != null) {
				int index = getIndexOfDocument(model);
				model.getTextComponent().grabFocus();
				setSelectedIndex(index);
				return null;
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
			
			DefaultSingleDocumentModel model = new DefaultSingleDocumentModel(path, tekst);
			docs.add(model);
			
			ImageIcon icon = Util.getSave("green", flp, notepad);
			addTab(model.getFilePath().getFileName().toString(), icon, new JScrollPane(model.getTextComponent()));
			
			model.addSingleDocumentListener(listener);
			
			setTooltipText(model);
			setSelectedIndex(getComponentCount()-1);
			
			for(var x: listeners) {
				x.documentAdded(model);
			}
			
			currentDoc = model;
			changeCurrentDoc(previous, currentDoc);
			
			return model;
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
		int index = getSelectedIndex();
		
		if(index != -1) {
			removeTabAt(index);
			int count = getComponentCount();

			if(count != 0) {
				setSelectedIndex(count-1);
				changeCurrentDoc(model, getDocument(count-1));
			} else {
				changeCurrentDoc(model, null);
			}
			model.removeSingleDocumentListener(listener);
			
			for(var x: listeners) {
				x.documentRemoved(model);
			}
		} else {
			String title = flp.getString("error");
			String description = flp.getString("notChoosen");
			String options[] = {"OK"};
			Util.showMessage(title, description, notepad, options);
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
