package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
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
import javax.swing.KeyStroke;
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

public class JNotepadPP extends JFrame implements MultipleDocumentListener{

	private static final long serialVersionUID = 1L;
	private StatusBar statusBar;
	private JTextArea editor;
	private Path openedFilePath;
	private FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
	private DefaultMultipleDocumentModel mul;
	private JMenuItem upperCase, lowerCase, invertCase;
	
	public JNotepadPP() {
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			
			 public void windowOpened(WindowEvent e) {
				 flp.connect();
			 }
			 
			 public void windowClosing(WindowEvent e) {
				    exitAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
						private static final long serialVersionUID = 1L;
				    });
			 }
			
			public void windowClosed(WindowEvent e) {
				flp.disconnect();
				statusBar.stop();
			}
		});
		setLocation(0, 0);
		setSize(700, 700);
		this.mul = new DefaultMultipleDocumentModel();
		mul.addMultipleDocumentListener(this);
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
						String options[] = {"OK"};
						JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("cantSetTitle"),
				                flp.getString("error"),
				                JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION, null, options, options[0]);
					}
				}
				statusBar.setValueLength(fileSize(mul.getDocument(index).getTextComponent()));
				mul.getCurrentDocument().getTextComponent().grabFocus();
				updateCarot(mul.getCurrentDocument().getTextComponent());
			}

		});
		initGUI();
		setTitle("JNotepad++");
	}
	
	private String[] yesNo() {
		String[] options = {flp.getString("yes"), flp.getString("no")};
		return options;
	}
	
	private void initGUI() {
		statusBar = new StatusBar(flp);
		editor = new JTextArea();
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
	
	private Action openNewDocument = new LocalizableAction(flp, "new") {
		

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			mul.createNewDocument();
		}
	};

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
				String options[] = {"OK"};
				JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("file")+" "+fileName.getAbsolutePath()+" "+flp.getString("notExists"),
		                flp.getString("error"),
		                JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION, null, options, options[0]);
				return;
			}
			try {
				mul.loadDocument(filePath);
			} catch(Exception ex) {
				String options[] = {"OK"};
				JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("cantLoadFile"),
		                flp.getString("error"),
		                JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION, null, options, options[0]);
				return;
			}
			
			openedFilePath = filePath;
		}
	};
	
	private Action saveAsDocumentAction = new LocalizableAction(flp, "saveAs") {
		
		private static final long serialVersionUID = 1L;
		@Override
		public void actionPerformed(ActionEvent e) {
			saveFile(true);
		}
	};
	
	private void saveFile(boolean code) {
		Path old = openedFilePath;
		int index = mul.getSelectedIndex();
		
		if(index == -1) {
			String options[] = {"OK"};
			JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("noSelection"),
	                flp.getString("warning"),
	                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
			return;
		}
		
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
			if(code && Files.exists(openedFilePath, LinkOption.NOFOLLOW_LINKS)) {
				 String[] options = yesNo();
				int x = JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("documentExistsLong"),
		                flp.getString("documentExists"),
		                JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION, null, options, options[1]);
				if(x == 1 || x == -1) {
					openedFilePath = old;
					return;
				}
			}
			
			mul.saveDocument(mul.getDocument(index), openedFilePath);
		} catch (Exception e1) {
			String options[] = {"OK"};
			JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("errorWhileWriting")+" "+openedFilePath.toAbsolutePath()+"\n "+flp.getString("errorWhileWriting1"),
	                flp.getString("error"),
	                JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
			return;
		}
		String options[] = {"OK"};
		JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("fileSaved"),
                flp.getString("success"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
	}
	
	
	private boolean chooseFile() {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle(flp.getString("save"));
		if(jfc.showSaveDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
			String options[] = {"OK"};
			JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("nothingSaved"),
	                flp.getString("warning"),
	                JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION, null, options, options[0]);
			return false;
		}
		openedFilePath = jfc.getSelectedFile().toPath();
		return true;
	}
	
	
	private Action saveDocumentAction = new LocalizableAction(flp, "save") {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			saveFile(false);
		}
	};
	
	private void copyCut(boolean code, String warning) {
		int index = mul.getSelectedIndex();
		if(index == -1) {
			String options[] = {"OK"};
			JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("noSelection"),
	                flp.getString("warning"),
	                JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION, null, options, options[0]);
			return;
		} 
		
		SingleDocumentModel temp = mul.getDocument(index);
		JTextArea editor = temp.getTextComponent();
		int len = Math.abs(editor.getCaret().getDot()-editor.getCaret().getMark());
		int offset = Math.min(editor.getCaret().getDot(),editor.getCaret().getMark());
		try {
			Document doc = editor.getDocument();
			String selection = doc.getText(offset, len);
			StringSelection stringSelection = new StringSelection(selection);
			if(code) {
				doc.remove(offset, len);
			}
			Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
			clpbrd.setContents (stringSelection, null);
		} catch (BadLocationException e1) {
			String options[] = {"OK"};
			JOptionPane.showOptionDialog(JNotepadPP.this, warning,
	                flp.getString("warning"),
	                JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION, null, options, options[0]);
			return;
		}
	}
	
	private Action copySelection = new LocalizableAction(flp, "copy") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			copyCut(false, flp.getString("cantCopy"));
		}
	};
	
	private Action cutAction = new LocalizableAction(flp, "cut") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			copyCut(true, flp.getString("cantCut"));
		}
	};
	
	private Action deleteSelectedPartAction = new AbstractAction() {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = editor.getDocument();
			int len = Math.abs(editor.getCaret().getDot()-editor.getCaret().getMark());
			if(len==0) return;
			int offset = Math.min(editor.getCaret().getDot(),editor.getCaret().getMark());
			try {
				doc.remove(offset, len);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
	};
	
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
			text = changeCase(text, code);
			doc.remove(offset, len);
			doc.insertString(offset, text, null);
		} catch(BadLocationException ex) {
			ex.printStackTrace();
		}
	}
	

	private String changeCase(String text, int code) {
		char[] znakovi = text.toCharArray();
		for(int i = 0; i < znakovi.length; i++) {
			char c = znakovi[i];
			if(Character.isLowerCase(c) && (code == 0 || code == 2)) {
				znakovi[i] = Character.toUpperCase(c);
			} else if(Character.isUpperCase(c) && (code == 1 || code == 2)) {
				znakovi[i] = Character.toLowerCase(c);
			}
		}
		return new String(znakovi);
	}
	
	private Action toggleLowerCaseAction = new LocalizableAction(flp, "lowerCase") {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			changeCaseItem(1);
		}
	};
	
	private Action toggleInvertCase = new LocalizableAction(flp, "invertCase") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			changeCaseItem(2);
		}
	};
	
	private Action toggleCaseAction = new LocalizableAction(flp, "upperCase") {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			changeCaseItem(0);
		}
	};
	
	private Action removeTabAction = new LocalizableAction(flp, "removeTab") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			int index = mul.getSelectedIndex();
			
			if(index != -1) {
				mul.removeTabAt(index);
				int count = mul.getComponentCount();

				if(count != 0) {
					mul.setSelectedIndex(count-1);
					mul.changeCurrentDoc(mul.getDocument(index), mul.getDocument(count-1));
				} else {
					mul.changeCurrentDoc(mul.getDocument(index), null);
				}
				mul.closeDocument(mul.getDocument(index));
			} else {
				String options[] = {"OK"};
				JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("notChoosen"),
		                flp.getString("error"),
		                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			}
			
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
	
	private Action pasteAction = new LocalizableAction(flp, "paste") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			int index = mul.getSelectedIndex();
			if(index == -1) {
				String options[] = {"OK"};
				JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("noSelection"),
		                flp.getString("warning"),
		                JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION, null, options, options[0]);
				return;
			}
			SingleDocumentModel temp = mul.getDocument(index);
			JTextArea editor = temp.getTextComponent();
			int offset = Math.min(editor.getCaret().getDot(),editor.getCaret().getMark());
			try {
				String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
				editor.getDocument().insertString(offset, data, null);
			} catch (HeadlessException | UnsupportedFlavorException | IOException | BadLocationException e1) {
				String options[] = {"OK"};
				JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("cantPaste"),
		                flp.getString("error"),
		                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				return;
			} 
			
			
		}
	};
	
	private Action exitAction = new LocalizableAction(flp, "exit") {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int count = mul.getComponentCount();
			for(int i = 0; i < count; i++) {
				SingleDocumentModel temp = mul.getDocument(i);
				if(temp.isModified()) {
					String options[] = {flp.getString("save"), flp.getString("discard"), flp.getString("abort")};
					int x = JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("notSavedMods")+" "+mul.getTitleAt(i),
			                flp.getString("warning"),
			                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
					
					if(x == 0) {
					    saveDocumentAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
							private static final long serialVersionUID = 1L;
					    });
					} else if(x == 2 || x == -1) {
						return;
					} else {
						if(temp.getFilePath() != null) {
							mul.setIconAt(i, getSave("green"));
						}
					}
				}
			}
			JNotepadPP.this.dispose();
		}
	};
	
	private int lengthWithoutSpace(String s) {
		char[] tempArray = s.toCharArray();
		int len = tempArray.length;
		int counter = 0;
		
		for(int i = 0; i < len; i++) {
			char c = tempArray[i];
			if (!(c == ' ' || c == '\t' || c == '\n' || c == '\r')) {
				++counter;
			}
		}
		return counter;
	}
	
	private int numberOfLines(String s) {
		char[] tempArray = s.toCharArray();
		int len = tempArray.length;
		int counter = 0;
		
		for(int i = 0; i < len; i++) {
			char c = tempArray[i];
			if (c == '\n') {
				++counter;
			}
		}
		return counter+1;
	}
	
	private String sort(int start, int end, int code) {
		Locale locale = new Locale(flp.getCurrentLanguage());
		Collator collator = Collator.getInstance(locale);
		JTextArea area = mul.getCurrentDocument().getTextComponent();
		Document doc = area.getDocument();
		String selectedText;
		try {
			selectedText = doc.getText(start, end-start);
			
			String[] lines = selectedText.split("\n");;
			List<String> newLines = new ArrayList<>();
			
			for(String s : lines) {
				String[] tempArray = s.split(" ");
				String line;
				if(code == 0) {
					line = Stream.of(tempArray).sorted((s1, s2)-> collator.compare(s1, s2)).collect(Collectors.joining(" "));
				} else {
					line = Stream.of(tempArray).sorted((s1, s2)-> -collator.compare(s1, s2)).collect(Collectors.joining(" "));
				}
				
				newLines.add(line);
			}
			
			return newLines.stream().collect(Collectors.joining("\n"));
		} catch (BadLocationException e) {
			System.out.println(e.getMessage());
			String options[] = {"OK"};
			JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("noSelectionSort"),
	                flp.getString("warning"),
	                JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION, null, options, options[0]);
			return "";
		}
	}
	
	private void sortAction(int code) {
		int index = mul.getSelectedIndex();
		if(index == -1) {
			String options[] = {"OK"};
			JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("noSelection"),
	                flp.getString("warning"),
	                JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION, null, options, options[0]);
			return;
		}
		
		JTextArea area = mul.getCurrentDocument().getTextComponent();
		Document doc = area.getDocument();
		Caret caret = area.getCaret();
		int mark = caret.getMark();
		int dot = caret.getDot();
		Element root = doc.getDefaultRootElement();
		
		try {
			int line1 = root.getElementIndex(mark);
			int line2 = root.getElementIndex(dot);
			int offset1 = root.getElement(line1).getStartOffset();
			int offset2 = root.getElement(line2).getEndOffset();
			
			 if(line1 > line2) {
				 offset1 = root.getElement(line2).getStartOffset();
				 offset2 = root.getElement(line1).getEndOffset();
			} 
			String sortedText = sort(offset1, offset2, code);
			System.out.println(offset1+" "+offset2);
			doc.remove(offset1, offset2-offset1-1);
			doc.insertString(offset1, sortedText, null);
			
		} catch (BadLocationException e1) {
			System.out.println(e1.getMessage());
			String options[] = {"OK"};
			JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("noSelectionSort"),
	                flp.getString("warning"),
	                JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION, null, options, options[0]);
			return;
		}
	}
	
	private Action ascending = new LocalizableAction(flp, "ascending") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			sortAction(0);	
		}
	};
	
	private Action descending = new LocalizableAction(flp, "descending") {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			sortAction(1);
		}
	};
	
	private Action stats = new LocalizableAction(flp, "stats") {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int index = mul.getSelectedIndex();
			if(index == -1) {
				String options[] = {"OK"};
				JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("noSelection"),
		                flp.getString("warning"),
		                JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION, null, options, options[0]);
				return;
			}
			
			JTextArea editor =mul.getDocument(index).getTextComponent();
			Document doc = editor.getDocument();
			int lengthAll = doc.getLength();
			int lengthNoSpaces = lengthWithoutSpace(editor.getText());
			int numOfLines = numberOfLines(editor.getText());
			
			String options[] = {"OK"};
			JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("numCharsAll")+" "+lengthAll+"\n"+flp.getString("numChars")+
					" "+lengthNoSpaces +"\n"+flp.getString("numLines")+" "+numOfLines,
	                flp.getString("stats"),
	                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			
		}
	};

	private void createActions() {
		
		openDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control O")); 
		openDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_O); 

		
		saveDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control S")); 
		saveDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_S); 

		
		deleteSelectedPartAction.putValue(
				Action.NAME, 
				"Delete selected text");
		deleteSelectedPartAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("F2")); 
		deleteSelectedPartAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_D); 
		
		exitAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control X"));
		exitAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_X); 

		
		saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F12"));
		saveAsDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
		
		removeTabAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
		removeTabAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
		
		copySelection.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copySelection.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		
		pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
		
		cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control B"));
		cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		
		stats.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
		stats.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
	}

	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveAsDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exitAction));
		fileMenu.add(new JMenuItem(stats));
		
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		
		JMenu tools = new JMenu(new LocalizableAction(flp, "tools") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		menuBar.add(tools);
		
		
		JMenu changeCase = new JMenu(new LocalizableAction(flp, "changeCase") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
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
		
		editMenu.add(new JMenuItem(deleteSelectedPartAction));
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
			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("de");
			}
		});
		
		JMenuItem mItemEN = new JMenuItem(new LocalizableAction(flp, "english") {
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

	private void createToolbars() {
		JToolBar toolBar = new JToolBar("Alati");
		toolBar.setFloatable(true);
		
		toolBar.add(new JButton(openNewDocument));
		toolBar.add(new JButton(openDocumentAction));
		toolBar.add(new JButton(saveDocumentAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(deleteSelectedPartAction));
		toolBar.add(new JButton(toggleCaseAction));
		toolBar.add(new JButton(removeTabAction));
		toolBar.add(new JButton(saveAsDocumentAction));
		toolBar.add(new JButton(exitAction));
		toolBar.add(new JButton(copySelection));
		toolBar.add(new JButton(pasteAction));
		toolBar.add(new JButton(cutAction));
		toolBar.add(new JButton(stats));
		
		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}
	
	private ImageIcon getSave(String color) {
		InputStream is = this.getClass().getResourceAsStream("icons/"+color+"Save.png");
		if(is==null) {
			throw new IllegalStateException("Ne mogu učitati sliku.");
		}
		byte[] bytes;
		try {
			bytes = is.readAllBytes();
			is.close();
		} catch (IOException e) {
			throw new IllegalStateException("Ne mogu učitati sliku.");
		}
		
		Image scaled = new ImageIcon(bytes).getImage().getScaledInstance(25, 20, DO_NOTHING_ON_CLOSE);
		
		return new ImageIcon(scaled);
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
	
	private int fileSize(JTextArea area) {
		long size = area.getText().getBytes(StandardCharsets.UTF_8).length;
		return (int) size;
	}
	
	
	SingleDocumentListener listener = new SingleDocumentListener() {
		
		@Override
		public void documentModifyStatusUpdated(SingleDocumentModel model) {
			int index = mul.getIndexOfDocument(model);
			
			statusBar.setValueLength(fileSize(model.getTextComponent()));
			statusBar.changeText();
			
			if(model.isModified()) {
				mul.setIconAt(index, getSave("red"));
			} else {
				mul.setIconAt(index, getSave("green"));
			}
			
		}
		
		@Override
		public void documentFilePathUpdated(SingleDocumentModel model) {
			int index = mul.getIndexOfDocument(model);
			mul.setTitleAt(index, model.getFilePath().getFileName().toString());	
			if(model.getFilePath() != null) {
				setTooltipText(model);
			}
		}
	};
	
	private void setTooltipText(SingleDocumentModel model) {
		try {
			mul.setToolTipTextAt(mul.getIndexOfDocument(model), model.getFilePath().toRealPath(LinkOption.NOFOLLOW_LINKS).toString());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					JNotepadPP.this, 
					"Ne mogu postaviti tooltip", 
					"Pogreška", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void updateCarot(JTextArea area) {
		int index = mul.getSelectedIndex();
		if(index != -1) {
			int offset = area.getCaretPosition();
			int pos1 = area.getCaret().getDot();
			int pos2 = area.getCaret().getMark();
			int sel = Math.abs(pos1-pos2);
			try {
				int line = area.getLineOfOffset(offset);
				int column = offset - area.getLineStartOffset(line);
				statusBar.setValueLine(line+1);
				statusBar.setValueColumn(column+1);
				statusBar.setValueSelection(sel);
				statusBar.changeText();
				if(sel > 0) {
					upperCase.setEnabled(true);
					lowerCase.setEnabled(true);
					invertCase.setEnabled(true);
				}
			} catch (BadLocationException e1) {
				String options[] = {"OK"};
				JOptionPane.showOptionDialog(JNotepadPP.this, flp.getString("positionFail"),
		                flp.getString("warning"),
		                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			}
			
		}
	}
	
	
	private CaretListener listnerCaret = new CaretListener() {
		
		@Override
		public void caretUpdate(CaretEvent e) {
			updateCarot((JTextArea)e.getSource());
		}
	};

	@Override
	public void documentAdded(SingleDocumentModel model) {
		ImageIcon icon;
		if(model.getFilePath() == null) {
			icon = getSave("red");
			model.setModified(true);
			mul.addTab("unnamed",icon ,model.getTextComponent());
		} else {
			icon = getSave("green");
			mul.addTab(model.getFilePath().getFileName().toString(), icon, model.getTextComponent());
		}
		model.addSingleDocumentListener(listener);
		
		if(model.getFilePath() != null) {
			setTooltipText(model);
		} else {
			mul.setToolTipTextAt(mul.getIndexOfDocument(model), "(unnamed)");
		}
		model.getTextComponent().addCaretListener(listnerCaret);
		mul.setSelectedIndex(mul.getComponentCount()-1);
		
	}

	@Override
	public void documentRemoved(SingleDocumentModel model) {
		model.removeSingleDocumentListener(listener);
		model.getTextComponent().removeCaretListener(listnerCaret);
	}

}