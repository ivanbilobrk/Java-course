package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

/**
 * Class with static helper methods for JNotepad
 * @author Ivan Bilobrk
 *
 */
public class Util {
	
	/**
	 * Method which creates string array with two elements: "yes" and "no" translated to current selected language.
	 * @param flp - LocalizationProvider used for translation
	 * @return String array with two elements
	 */
	public static String[] yesNo(ILocalizationProvider flp) {
		String[] options = {flp.getString("yes"), flp.getString("no")};
		return options;
	}
	
	/**
	 * Method which initializes actions used in JNotepad
	 * @param openDocumentAction
	 * @param saveDocumentAction
	 * @param exitAction
	 * @param saveAsDocumentAction
	 * @param removeTabAction
	 * @param copySelection
	 * @param pasteAction
	 * @param cutAction
	 * @param stats
	 */
	public static void initActions(Action openDocumentAction, Action saveDocumentAction, Action exitAction, 
			Action saveAsDocumentAction, Action removeTabAction, Action copySelection,
			Action pasteAction, Action cutAction, Action stats) {
		
		//dodavanje mnemonika i kratica za tipkovnicu za određene akcije
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
	
	/**
	 * Method which shows JOptionPane dialog window when there is a need for interaction with user or alerting user.
	 * @param title of dialog
	 * @param description of dialog
	 * @param notepadFrame - frame in which dialog is shown
	 * @param options - options user can choose in this dialog
	 * @return
	 */
	public static int showMessage(String title, String description, JNotepadPP notepadFrame, String[] options) {
		return JOptionPane.showOptionDialog(notepadFrame, description, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
	}
	
	/**
	 * Method for changing text from uppercase to lowercase or lowercase to uppercase.
	 * @param text - text you want to change
	 * @param code - if code is 0 given this method will return new string with all uppercase letters, 
	 * 				if code is 1 this method will return new string with all lowercase letters and
	 * 				if code is 2 this method will return new string with lowercase letters changed to uppercase
	 * 				and vice versa
	 * @return modified given string 
	 */
	public static String changeCase(String text, int code) {
		
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
	
	/**
	 * Calculates length of given string without whitespace.
	 * @param s
	 * @return - length of string without whitespace
	 */
	public static int lengthWithoutSpace(String s) {
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
	
	/**
	 * Method which calculates number of lines present in a given string.
	 * @param s
	 * @return number of lines in given string
	 */
	public static int numberOfLines(String s) {
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
	
	/**
	 * Method which removes duplicate lines from selection of current document from given DefaultMultipleDocumentModel
	 * @param start - start offset in current Document
	 * @param end - end offset in current Document
	 * @param flp - LocalizationProvider for translation
	 * @param mul - model to get current Document from
	 * @param notepadFrame - frame in which dialogs will be shown to user
	 * @return - modified string without duplicate lines, only first occurrence of line is present
	 */
	public static String removeDuplicate(int start, int end, ILocalizationProvider flp, DefaultMultipleDocumentModel mul, JNotepadPP notepadFrame) {
		
		JTextArea area = mul.getCurrentDocument().getTextComponent();
		Document doc = area.getDocument();
		String selectedText;
		try {
			selectedText = doc.getText(start, end-start);
			
			String[] lines = selectedText.split("\n");;
			Set<String> newLines = new LinkedHashSet<>();
			
			for(String s : lines) {
				newLines.add(s);
			}
			return newLines.stream().collect(Collectors.joining("\n"));
		} catch (BadLocationException e) {
			String title = flp.getString("warning");
			String description = flp.getString("noSelectionSort");
			String options[] = {"OK"};
			showMessage(title, description, notepadFrame, options);
			return "";
		}
	}
	
	/**
	 * Method which sorts selected lines of current Document of DefaultMultipleDocumentModel.
	 * @param start - start offset in current Document
	 * @param end - end offset in current Document
	 * @param code - if given code is 0 selection will be sorted in ascending way and if code is 1 in descending way for current selected language
	 * @param flp - LocalizationProvider for translation
	 * @param mul - model to get current Document from
	 * @param notepadFrame - frame in which dialogs will be shown to user
	 * @return - sorted string from selection
	 */
	public static String sort(int start, int end, int code, ILocalizationProvider flp, DefaultMultipleDocumentModel mul, JNotepadPP notepadFrame) {
		Locale locale = new Locale(flp.getCurrentLanguage());
		Collator collator = Collator.getInstance(locale);
		JTextArea area = mul.getCurrentDocument().getTextComponent();
		Document doc = area.getDocument();
		String selectedText;
		try {
			selectedText = doc.getText(start, end-start);
			
			//dobivamo sve linije iz odabranog teksta
			String[] lines = selectedText.split("\n");
			List<String> newLines = new ArrayList<>();
			
			//prolazimo sve linije te ih sortiramo ovisno o argumentu code
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
			String title = flp.getString("warning");
			String description = flp.getString("noSelectionSort");
			String options[] = {"OK"};
			showMessage(title, description, notepadFrame, options);
			return "";
		}
	}
	
	/**
	 * Method which calculates current offset based on caret position.
	 * @param area to get caret position from
	 * @return - instance of Offset class which holds beginning of first selected line and end of last selected line
	 */
	public static Offset calcOffset(JTextArea area) {
	
		Document doc = area.getDocument();
		Caret caret = area.getCaret();
		int mark = caret.getMark();
		int dot = caret.getDot();
		Element root = doc.getDefaultRootElement();
		
		int line1 = root.getElementIndex(mark);
		int line2 = root.getElementIndex(dot);
		int offset1 = root.getElement(line1).getStartOffset();
		int offset2 = root.getElement(line2).getEndOffset();
			
		if(line1 > line2) {
			offset1 = root.getElement(line2).getStartOffset();
			offset2 = root.getElement(line1).getEndOffset();
		} 
		
		return new Offset(offset1, offset2);
	}
	
	/**
	 * Method used to load icons for saved and unsaved documents.
	 * @param color - color of save icon
	 * @return ImageIcon corresponding to given color
	 */
	public static ImageIcon getSave(String color, ILocalizationProvider flp, JNotepadPP notepad) {
		InputStream is = notepad.getClass().getResourceAsStream("icons/"+color+"Save.png");
		if(is==null) {
			String title = flp.getString("warning");
			String description = flp.getString("picFail");
			String[] options = {"OK"};
			Util.showMessage(title, description, notepad, options);
			return null;
		}
		byte[] bytes;
		try {
			bytes = is.readAllBytes();
			is.close();
		} catch (IOException e) {
			String title = flp.getString("warning");
			String description = flp.getString("picFail");
			String[] options = {"OK"};
			Util.showMessage(title, description, notepad, options);
			return null;
		}
		
		Image scaled = new ImageIcon(bytes).getImage().getScaledInstance(25, 20, WindowConstants.DO_NOTHING_ON_CLOSE);
		
		return new ImageIcon(scaled);
	}
}
