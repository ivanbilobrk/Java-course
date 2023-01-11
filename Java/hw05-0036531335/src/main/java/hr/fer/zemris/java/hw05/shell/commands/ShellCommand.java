package hr.fer.zemris.java.hw05.shell.commands;

import java.util.List;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

/**
 * Interface which specifies methods that a shell command must implement.
 * @author Ivan Bilobrk
 *
 */
public interface ShellCommand {
	
	/**
	 * Method which executes a command.
	 * @param env - interface for communication with shell
	 * @param arguments of a command.
	 * @return ShellStatus
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	
	/**
	 * Method to get command name.
	 * @return command name.
	 */
	String getCommandName();
	
	/**
	 * Method to get descriptions of a command as List of String.
	 * @return List of command descriptions.
	 */
	List<String> getCommandDescription();
	
}
