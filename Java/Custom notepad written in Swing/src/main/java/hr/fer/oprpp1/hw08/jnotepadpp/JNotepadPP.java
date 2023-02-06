package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;

import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;

/**
 * Class representing JNotepadd, its GUI components and actions for buttons.
 * @author Ivan Bilobrk
 *
 */
public class JNotepadPP extends JFrame implements MultipleDocumentListener{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Status bar shown on the bottom of text area.
	 */
	private StatusBar statusBar;
	
	/**
	 * Path of current open document.
	 */
	private Path openedFilePath;
	
	/**
	 * LocalizationProvider used for translation.
	 */
	private FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), JNotepadPP.this);
	
	/**
	 * DocumentModel used for manipulating open documents.
	 */
	private DefaultMultipleDocumentModel mul;
	
	/**
	 * Menu items for changing case of letters.
	 */
	private JMenuItem upperCase, lowerCase, invertCase;
	
	/**
	 * Constructor
	 */
	public JNotepadPP() {
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		//dodajemo listenera koji prati zatvaranje prozora te koji povezuje most LocalizationProvider-a sa stvarnim LocalizationProvider-om.
		//kada je prozor zatvoren odspajamo LocalizationProvider-a te zatvarmo prozor i gasimo dretvu zaduženu za sat
		this.addWindowListener(new WindowAdapter() {
			
			 public void windowOpened(WindowEvent e) {
			 }
			 
			 public void windowClosing(WindowEvent e) {
				    exitAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
						private static final long serialVersionUID = 1L;
				    });
			 }
			
			public void windowClosed(WindowEvent e) {
				statusBar.stop();
			}
		});
		setLocation(0, 0);
		setSize(1000, 1000);
		
		this.mul = new DefaultMultipleDocumentModel(flp, this);
		mul.addMultipleDocumentListener(this);
		
		//listener koji prati mijenjanje tabova
		mul.addChangeListener((event)->{
			int index = mul.getSelectedIndex();
			if(index != -1) {
				openedFilePath = mul.getDocument(index).getFilePath();
				
				mul.changeCurrentDoc(mul.getCurrentDocument(), mul.getDocument(index));
				
				if(openedFilePath == null) {
					setTitle("(unnamed) - JNotepad++");
				} else {
					try {
						setTitle(openedFilePath.toRealPath(LinkOption.NOFOLLOW_LINKS).toString()+" - JNotepad++");
					} catch (IOException e) {
						String title = flp.getString("error");
						String description = flp.getString("cantSetTitle");
						String options[] = {"OK"};
						Util.showMessage(title, description, JNotepadPP.this, options);
					}
				}
				//ažuriranje status bara
				statusBar.setValueLength(fileSize(mul.getDocument(index).getTextComponent()));
				mul.getCurrentDocument().getTextComponent().grabFocus();
				updateCarot(mul.getCurrentDocument().getTextComponent());
			} else {
				setTitle("JNotepad++");
			}

		});
		initGUI();
		
		setTitle("JNotepad++");
	}
	
	/*
	 * Private method for initializing GUI of JNotepad
	 */
	private void initGUI() {
		statusBar = new StatusBar(flp);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		panel.add(mul, BorderLayout.CENTER);
		panel.add(statusBar, BorderLayout.PAGE_END);
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panel, BorderLayout.CENTER);
		
		
		createActions();
		createMenus();
		createToolbars();
		
	}
	
	/**
	 * Action for opening new document.
	 */
	private Action openNewDocument = new LocalizableAction(flp, "new") {
		

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			mul.createNewDocument();
		}
	};
	
	/*
	 * Action for opening existing document.
	 */
	private Action openDocumentAction = new LocalizableAction(flp, "open") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle(flp.getString("openFile"));
			if(fc.showOpenDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
				return;
			}
			File fileName = fc.getSelectedFile();
			Path filePath = fileName.toPath();
			
			if(!Files.isReadable(filePath)) {
				String title = flp.getString("error");
				String description = flp.getString("file")+" "+fileName.getAbsolutePath()+" "+flp.getString("notExists");
				String options[] = {"OK"};
				Util.showMessage(title, description, JNotepadPP.this, options);
				return;
			}
			try {
				mul.loadDocument(filePath);
			} catch(Exception ex) {
				String title = flp.getString("error");
				String description = flp.getString("cantLoadFile");
				String options[] = {"OK"};
				Util.showMessage(title, description, JNotepadPP.this, options);
				return;
			}
			
			openedFilePath = filePath;
		}
	};
	
	/**
	 * Action for saving document as.
	 */
	private Action saveAsDocumentAction = new LocalizableAction(flp, "saveAs") {
		
		private static final long serialVersionUID = 1L;
		@Override
		public void actionPerformed(ActionEvent e) {
			//zovemo metodu saveFile sa parametrom true jer želimo odabir mjesta spremanja ako datoteka već postoji
			saveFile(true);
		}
	};
	
	/**
	 * Private method for saving files or saving files as.
	 * @param code - if false user will choose desired place to save a document, otherwise document
	 * 				will be saved to existing path
	 */
	private void saveFile(boolean code) {
		Path old = openedFilePath;
		int index = mul.getSelectedIndex();
		
		if(index == -1) {
			String title = flp.getString("warning");
			String description = flp.getString("noSelection");
			String options[] = {"OK"};
			Util.showMessage(title, description, JNotepadPP.this, options);
			return;
		}
		
		//izbornik odabira mjesta spremanja nudimo samo po potrebi
		if(code) {
			if(!chooseFile()) {
				return;
			};
		} else {
			if(openedFilePath == null) {
				if(!chooseFile()) {
					return;
				};
			}
		}
			
		try {
			//ako odabrano mjesto već postoji pitamo usera želi li nastaviti sa promjenom ili ju odbaciti
			if(code && Files.exists(openedFilePath, LinkOption.NOFOLLOW_LINKS)) {
				String title = flp.getString("documentExists");
				String description = flp.getString("documentExistsLong");
				String[] options = Util.yesNo(flp);
				int x = Util.showMessage(title, description, JNotepadPP.this, options);
				if(x == 1 || x == -1) {
					openedFilePath = old;
					return;
				}
			}
			
			mul.saveDocument(mul.getDocument(index), openedFilePath);
		} catch (Exception e1) {
			String title = flp.getString("error");
			String description = flp.getString("errorWhileWriting")+" "+openedFilePath.toAbsolutePath()+"\n "+flp.getString("errorWhileWriting1");
			String options[] = {"OK"};
			Util.showMessage(title, description, JNotepadPP.this, options);
			mul.getCurrentDocument().setModified(false);
			return;
		}
		String title = flp.getString("success");
		String description = flp.getString("fileSaved");
		String options[] = {"OK"};
		Util.showMessage(title, description, JNotepadPP.this, options);
	}
	
	
	/**
	 * Method which shows user a dialog to choose where to save document.
	 * @return true if user has choosen a place, false otherwise
	 */
	private boolean chooseFile() {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle(flp.getString("save"));
		if(jfc.showSaveDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
			String title = flp.getString("warning");
			String description = flp.getString("nothingSaved");
			String options[] = {"OK"};
			Util.showMessage(title, description, JNotepadPP.this, options);
			return false;
		}
		openedFilePath = jfc.getSelectedFile().toPath();
		return true;
	}
	
	/**
	 * Action for saving document.
	 */
	private Action saveDocumentAction = new LocalizableAction(flp, "save") {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//parametar je false jer ne želimo izbornik odabira mjesta spremanja ako je putanja poznata
			//ako putanja nije poznata korisnik će morati izabrati mjesto
			saveFile(false);
		}
	};
	
	/**
	 * Method used for copying or cutting selected text from current document.
	 * @param code - if true method will cut text, otherwise just copy
	 * @param warning
	 */
	private void copyCut(boolean code, String warning) {
		int index = mul.getSelectedIndex();
		if(index == -1) {
			String title = flp.getString("warning");
			String description = flp.getString("noSelection");
			String options[] = {"OK"};
			Util.showMessage(title, description, JNotepadPP.this, options);
			return;
		} 
		
		SingleDocumentModel temp = mul.getDocument(index);
		JTextArea editor = temp.getTextComponent();
		
		//izračun selekcije
		int len = Math.abs(editor.getCaret().getDot()-editor.getCaret().getMark());
		int offset = Math.min(editor.getCaret().getDot(),editor.getCaret().getMark());
		try {
			Document doc = editor.getDocument();
			String selection = doc.getText(offset, len);
			StringSelection stringSelection = new StringSelection(selection);
			
			//uklanjamo tekst ako je riječ o rezanju
			if(code) {
				doc.remove(offset, len);
			}
			
			//spremamo tekst u clipboard
			Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
			clpbrd.setContents (stringSelection, null);
		} catch (BadLocationException e1) {
			String title = flp.getString("warning");
			String options[] = {"OK"};
			Util.showMessage(title, warning, JNotepadPP.this, options);
			return;
		}
	}
	
	/**
	 * Action for copying selected text
	 */
	private Action copySelection = new LocalizableAction(flp, "copy") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			copyCut(false, flp.getString("cantCopy"));
		}
	};
	
	/**
	 * Action for cutting selected text.
	 */
	private Action cutAction = new LocalizableAction(flp, "cut") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			copyCut(true, flp.getString("cantCut"));
		}
	};
	
	/**
	 * Method which changes casing of selected letters based on given code.
	 * @param code - if code is 0 given this method will return new string with all uppercase letters, 
	 * 				if code is 1 this method will return new string with all lowercase letters and
	 * 				if code is 2 this method will return new string with lowercase letters changed to uppercase
	 */
	private void changeCaseItem(int code) {
		Document doc = mul.getCurrentDocument().getTextComponent().getDocument();
		Caret caret = mul.getCurrentDocument().getTextComponent().getCaret();
		
		int len = Math.abs(caret.getDot()-caret.getMark());
		int offset = 0;
		
		if(len!=0) {
			offset = Math.min(caret.getDot(),caret.getMark());
		} 
		
		try {
			String text = doc.getText(offset, len);
			text = Util.changeCase(text, code);
			
			doc.remove(offset, len);
			doc.insertString(offset, text, null);
		} catch(BadLocationException ex) {
			String title = flp.getString("warning");
			String description = flp.getString("cantChangeCase");
			String options[] = {"OK"};
			Util.showMessage(title, description, JNotepadPP.this, options);
		}
	}
	
	/**
	 * Action for changing selected text to lowercase.
	 */
	private Action toggleLowerCaseAction = new LocalizableAction(flp, "lowerCase") {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			changeCaseItem(1);
		}
	};
	
	/**
	 * Action for inverting casing of selected text.
	 */
	private Action toggleInvertCase = new LocalizableAction(flp, "invertCase") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			changeCaseItem(2);
		}
	};
	
	/**
	 * Action for changing selected text to uppercase.
	 */
	private Action toggleCaseAction = new LocalizableAction(flp, "upperCase") {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			changeCaseItem(0);
		}
	};
	
	/**
	 * Action for removing tab from tabbed pane.
	 */
	private Action removeTabAction = new LocalizableAction(flp, "removeTab") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			mul.closeDocument(mul.getCurrentDocument());
			
			//nakon što smo maknuli tab potrebno je ažurirati sve vrijednosti status bara i onemogućiti promjenu velikih i malih slova 
			//jer nema selekcije teksta
			if(mul.getNumberOfDocuments() == 0) {
				statusBar.setValueLength(0);
				statusBar.setValueLine(0);
				statusBar.setValueColumn(0);
				statusBar.setValueSelection(0);
				statusBar.changeText();
				upperCase.setEnabled(false);
				lowerCase.setEnabled(false);
				invertCase.setEnabled(false);
			}
			
		}
	};
	
	/**
	 * Action for pasting text.
	 */
	private Action pasteAction = new LocalizableAction(flp, "paste") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			SingleDocumentModel model = mul.getCurrentDocument();
			if(model == null) {
				String title = flp.getString("warning");
				String description = flp.getString("noSelection");
				String options[] = {"OK"};
				Util.showMessage(title, description, JNotepadPP.this, options);
				return;
			}
			JTextArea editor = model.getTextComponent();
			int offset = Math.min(editor.getCaret().getDot(),editor.getCaret().getMark());
			try {
				String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
				editor.getDocument().insertString(offset, data, null);
			} catch (HeadlessException | UnsupportedFlavorException | IOException | BadLocationException e1) {
				String title = flp.getString("error");
				String description = flp.getString("cantPaste");
				String options[] = {"OK"};
				Util.showMessage(title, description, JNotepadPP.this, options);
				return;
			} 
			
			
		}
	};
	
	/**
	 * Action for exiting which checks if there are any unsaved documents.
	 */
	private Action exitAction = new LocalizableAction(flp, "exit") {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int count = mul.getComponentCount();
			//prolazimo sve dokumente i provjeravamo jesu li spremljeni
			for(int i = count-1; i >= 0; i--) {
				SingleDocumentModel temp = mul.getDocument(i);
				if(temp.isModified()) {
					String title = flp.getString("warning");
					String description;
					if(temp.getFilePath() == null) {
						description = flp.getString("notSavedMods")+" unnamed";
					} else {
						description = flp.getString("notSavedMods")+" "+temp.getFilePath().getFileName().toString();
					}
					String options[] = {flp.getString("save"), flp.getString("discard"), flp.getString("abort")};
					
					int x = Util.showMessage(title, description, JNotepadPP.this, options);
					
					if(x == 0 && temp.getFilePath() == null) {
					    saveAsDocumentAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
							private static final long serialVersionUID = 1L;
					    });
					    //temp.setModified(false);
					} else if(x == 0 && temp.getFilePath() != null) {
						saveDocumentAction.actionPerformed(new ActionEvent(this,  ActionEvent.ACTION_PERFORMED, null){
							private static final long serialVersionUID = 1L;
						});
						//temp.setModified(false);
					} else if(x == 2 || x == -1) {
						return;
					}
				}
			}
			JNotepadPP.this.dispose();
		}
	};
	
	/**
	 * Method used for sorting or removing unique lines from selected text.
	 * @param code - if given code is 0 selection will be sorted in ascending way and if code is 1 in descending way for current selected language
	 * 				if given code is 2 duplicate lines will be removed from selection
	 */
	private void sortUniqueAction(int code) {
		SingleDocumentModel model = mul.getCurrentDocument();
		if(model == null) {
			String options[] = {"OK"};
			JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("noSelection"),
	                flp.getString("warning"),
	                JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION, null, options, options[0]);
			return;
		}
		
		JTextArea area = model.getTextComponent();
		Document doc = area.getDocument();
		Offset offset = Util.calcOffset(area);
		int offset1 = offset.getOffset1();
		int offset2 = offset.getOffset2();
		
		try {
			String insertionText;
			if(code == 1 || code == 0) {
				insertionText = Util.sort(offset1, offset2, code, flp, mul, JNotepadPP.this);
			} else {
				insertionText = Util.removeDuplicate(offset1, offset2,flp, mul, JNotepadPP.this);
			}
			doc.remove(offset1, offset2-offset1-1);
			doc.insertString(offset1, insertionText, null);
		} catch (BadLocationException e1) {
			String title = flp.getString("warning");
			String description = flp.getString("noSelectionSort");
			String options[] = {"OK"};
			Util.showMessage(title, description, JNotepadPP.this, options);
			return;
		}
	}
	
	/**
	 * Action for removing duplicate lines from selection.
	 */
	private Action uniqueAction = new LocalizableAction(flp, "unique") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			sortUniqueAction(3);
		}
			
	};
	
	/**
	 * Action for sorting selected text in ascending way.
	 */
	private Action ascending = new LocalizableAction(flp, "ascending") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			sortUniqueAction(0);	
		}
	};
	
	/**
	 * Action for sorting selected text in descscending way.
	 */
	private Action descending = new LocalizableAction(flp, "descending") {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			sortUniqueAction(1);
		}
	};
	
	/**
	 * Action for showing statistic of current selected document.
	 */
	private Action stats = new LocalizableAction(flp, "stats") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel model = mul.getCurrentDocument();
			if(model == null) {
				String title = flp.getString("warning");
				String description = flp.getString("noSelection");
				String options[] = {"OK"};
				Util.showMessage(title, description, JNotepadPP.this, options);
				return;
			}
			
			JTextArea editor = model.getTextComponent();
			Document doc = editor.getDocument();
			int lengthAll = doc.getLength();
			int lengthNoSpaces = Util.lengthWithoutSpace(editor.getText());
			int numOfLines = Util.numberOfLines(editor.getText());
			
			String title = flp.getString("stats");
			String description = flp.getString("numCharsAll")+" "+lengthAll+"\n"+flp.getString("numChars")+
					" "+lengthNoSpaces +"\n"+flp.getString("numLines")+" "+numOfLines;
			String options[] = {"OK"};
			Util.showMessage(title, description, JNotepadPP.this, options);
		}
	};
	
	/**
	 * Method for initializing actions.
	 */
	private void createActions() {
		Util.initActions(openDocumentAction, saveDocumentAction, exitAction, saveAsDocumentAction, removeTabAction, copySelection, pasteAction, cutAction, stats);
	}

	/**
	 * Method for creating menus.
	 */
	private void createMenus() {
		//svim menijima i podmenijima možemo pridijeliti LocalizableAction sa praznom metodom actionPerformed
		//na ovaj način nam ne treba klasa nova za menije, a mijenjat će im se prijevod
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu(new LocalizableAction(flp, "file") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveAsDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exitAction));
		fileMenu.add(new JMenuItem(stats));
		
		JMenu editMenu = new JMenu(new LocalizableAction(flp, "edit") {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {	
			}
		});
		menuBar.add(editMenu);
		
		JMenu tools = new JMenu(new LocalizableAction(flp, "tools") {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		menuBar.add(tools);
		
		
		JMenu changeCase = new JMenu(new LocalizableAction(flp, "changeCase") {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		tools.add(changeCase);
		
		upperCase = new JMenuItem(toggleCaseAction);
		upperCase.setEnabled(false);
		
		lowerCase = new JMenuItem(toggleLowerCaseAction);
		lowerCase.setEnabled(false);
		
		invertCase = new JMenuItem(toggleInvertCase);
		invertCase.setEnabled(false);
		
		changeCase.add(upperCase);
		changeCase.add(lowerCase);
		changeCase.add(invertCase);
		
		editMenu.add(new JMenuItem(removeTabAction));
		editMenu.add(new JMenuItem(copySelection));
		editMenu.add(new JMenuItem(pasteAction));
		editMenu.add(new JMenuItem(cutAction));
		
		JMenu sort = new JMenu(new LocalizableAction(flp, "sort") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		sort.add(new JMenuItem(ascending));
		sort.add(new JMenuItem(descending));
		
		tools.add(new JMenuItem(uniqueAction));
		tools.add(sort);
		
		JMenu languageMenu = new JMenu(new LocalizableAction(flp, "language") {

			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
			}
			
		});
		menuBar.add(languageMenu);
		
		JMenuItem mItemHR = new JMenuItem(new LocalizableAction(flp, "croatian") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("hr");
			}
			
		});
		
		JMenuItem mItemDE = new JMenuItem(new LocalizableAction(flp, "german") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("de");
			}
		});
		
		JMenuItem mItemEN = new JMenuItem(new LocalizableAction(flp, "english") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("en");
			}
		});
		
		languageMenu.add(mItemHR);
		languageMenu.add(mItemEN);
		languageMenu.add(mItemDE);
		
		this.setJMenuBar(menuBar);
	}
	
	/**
	 * Method used for creating toolbars
	 */
	private void createToolbars() {
		JToolBar toolBar = new JToolBar("Alati");
		toolBar.setFloatable(true);
		
		toolBar.add(new JButton(openNewDocument));
		toolBar.add(new JButton(openDocumentAction));
		toolBar.add(new JButton(saveDocumentAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(removeTabAction));
		toolBar.add(new JButton(saveAsDocumentAction));
		toolBar.add(new JButton(exitAction));
		toolBar.add(new JButton(copySelection));
		toolBar.add(new JButton(pasteAction));
		toolBar.add(new JButton(cutAction));
		toolBar.add(new JButton(stats));
		
		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new JNotepadPP().setVisible(true);
			}
		});
	}

	@Override
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
		if(previousModel != null) {
			previousModel.removeSingleDocumentListener(listener);
			previousModel.getTextComponent().removeCaretListener(listnerCaret);
		}
		if(currentModel != null) {
			currentModel.addSingleDocumentListener(listener);
			currentModel.getTextComponent().addCaretListener(listnerCaret);
			
		}
		
	}
	
	/**
	 * Method used for calculating size of selected document
	 * @param area - text area which size you want to get
	 * @return size of given text area
	 */
	private int fileSize(JTextArea area) {
		long size = area.getText().getBytes(StandardCharsets.UTF_8).length;
		return (int) size;
	}
	
	/**
	 * Listener for changes made to to document.
	 * It updates GUI elements based on actions.
	 */
	SingleDocumentListener listener = new SingleDocumentListener() {
		
		@Override
		public void documentModifyStatusUpdated(SingleDocumentModel model) {
			
			statusBar.setValueLength(fileSize(model.getTextComponent()));
			statusBar.changeText();
			
		}
		
		@Override
		public void documentFilePathUpdated(SingleDocumentModel model) {
			
			if(model.getFilePath() != null) {
				JNotepadPP.this.setTitle(model.getFilePath().toAbsolutePath().toString());
			} else {
				JNotepadPP.this.setTitle("(unnamed) - JNotepad++");
			}

		}
	};
	
	/**
	 * Method used for updating status bar based on caret position.
	 * @param area
	 */
	private void updateCarot(JTextArea area) {
		SingleDocumentModel model = mul.getCurrentDocument();
		if(model != null) {
			int offset = area.getCaretPosition();
			int pos1 = area.getCaret().getDot();
			int pos2 = area.getCaret().getMark();
			int sel = Math.abs(pos1-pos2);
			Document doc = area.getDocument();
			Element root = doc.getDefaultRootElement();
			
			int line = root.getElementIndex(offset);
			int column = offset - root.getElement(line).getStartOffset();
			statusBar.setValueLine(line+1);
			statusBar.setValueColumn(column+1);
			statusBar.setValueSelection(sel);
			statusBar.changeText();
			
			if(sel > 0) {
				toggleCaseAction.setEnabled(true);
				toggleInvertCase.setEnabled(true);
				toggleLowerCaseAction.setEnabled(true);
			} else {
				toggleCaseAction.setEnabled(false);
				toggleInvertCase.setEnabled(false);
				toggleLowerCaseAction.setEnabled(false);
			}
		}
	}
	
	/**
	 * Caret listener which listens for caret changes and updates GUI
	 */
	private CaretListener listnerCaret = new CaretListener() {
		
		@Override
		public void caretUpdate(CaretEvent e) {
			updateCarot((JTextArea)e.getSource());
		}
	};

	@Override
	public void documentAdded(SingleDocumentModel model) {

		model.getTextComponent().addCaretListener(listnerCaret);
		
	}

	@Override
	public void documentRemoved(SingleDocumentModel model) {
		model.getTextComponent().removeCaretListener(listnerCaret);
	}

}