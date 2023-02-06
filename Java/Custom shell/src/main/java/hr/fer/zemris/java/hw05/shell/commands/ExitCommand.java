package hr.fer.zemris.java.hw05.shell.commands;

import java.util.List;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

/**
 * Implementation of exit command.
 * @author Ivan Bilobrk
 *
 */
public class ExitCommand implements ShellCommand{
	
	/**
	 * Method for terminating shell.
	 * @param env - interface through which a command communicates with a shell
	 * @param arguments - exit command doesn't accept any arguments.
	 * @return ShellStatus
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if(arguments != null && arguments.length() != 0) {
			env.writeln("Exit command doesn't accept any arguments.");
			return ShellStatus.CONTINUE;
		} else {
			return ShellStatus.TERMINATE;
		}
	}

	@Override
	public String getCommandName() {
		return "Exit";
	}

	@Override
	public List<String> getCommandDescription() {
		return List.of("This command exits shell.");
	}

}
