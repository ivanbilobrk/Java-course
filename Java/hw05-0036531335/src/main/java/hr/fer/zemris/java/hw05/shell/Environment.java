package hr.fer.zemris.java.hw05.shell;

import java.util.SortedMap;

import hr.fer.zemris.java.hw05.shell.commands.ShellCommand;

/**
 * Interface specifying methods which a shell must implement.
 * @author Ivan Bilobrk
 *
 */
public interface Environment {
	
	 /**
	  * Method which reads single line from console.
	  * @return String line that has been read.
	  * @throws ShellIOException if an error occurs while reading user input.
	  */
	 String readLine() throws ShellIOException;
	 
	 /**
	  * Method for writing given string to shell.
	  * @param text - which you want to print to shell.
	  * @throws ShellIOException if an error occurs while writing given text to shell.
	  */
	 void write(String text) throws ShellIOException;
	 
	 /**
	  * Method for writing given string to shell as a line.
	  * @param text - which you want to print to shell.
	  * @throws ShellIOException if an error occurs while writing given text to shell.
	  */
	 void writeln(String text) throws ShellIOException;
	 
	 /**
	  * Method to get all commands which a shell supports.
	  * @return Unmodifiable SortedMap where key is a string representing command name and value is one ShellCommand object.
	  */
	 SortedMap<String, ShellCommand> commands();
	 
	 /**
	  * Method which gets current multi line symbol for shell.
	  * @return character of current multi line symbol.
	  */
	 Character getMultilineSymbol();
	 
	 /**
	  * Method which sets multi line symbol to a new given character.
	  * @param symbol - character you want to set for new multi line symbol.
	  */
	 void setMultilineSymbol(Character symbol);
	 
	 /**
	  * Method which gets current prompt symbol for shell.
	  * @return character of current prompt symbol.
	  */
	 Character getPromptSymbol();
	 
	 /**
	  * Method which sets prompt symbol to a new given character.
	  * @param symbol - character you want to set for new prompt symbol.
	  */
	 void setPromptSymbol(Character symbol);
	 
	 /**
	  * Method which gets current more lines symbol for shell.
	  * @return character of current more lines symbol.
	  */
	 Character getMorelinesSymbol();
	 
	 /**
	  * Method which sets more lines symbol to a new given character.
	  * @param symbol - character you want to set for new more lines symbol.
	  */
	 void setMorelinesSymbol(Character symbol);
	 
}
