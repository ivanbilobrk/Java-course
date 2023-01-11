package hr.fer.zemris.java.hw05.shell.commands;

import java.util.List;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

/**
 * Implementation of help command.
 * @author Ivan Bilobrk
 *
 */
public class HelpCommand implements ShellCommand{

	/**
	 * Method for getting info for available commands.
	 * @param env - interface through which a command communicates with a shell
	 * @param arguments for help command. If no argument is present this command will print all available commands for one shell. 
	 * If there is an arguments than it must represent a name for a command for which a user wants to get info. 
	 * @return ShellStatus
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if(arguments == null || arguments.length() == 0) {
			env.commands().forEach((a, b)-> env.writeln(a));
			return ShellStatus.CONTINUE;
		}
		
		ShellCommand command = env.commands().get(arguments);
		
		if(command == null) {
			env.writeln("There is no command of this name.");
			return ShellStatus.CONTINUE;
		}
		
		env.writeln(command.getCommandName());
		command.getCommandDescription().forEach(desc-> env.writeln(desc));
		return ShellStatus.CONTINUE;
		
	}

	@Override
	public String getCommandName() {
		return "Help";
	}

	@Override
	public List<String> getCommandDescription() {
		return List.of("If stated with no arguments, help command lists names of all supported commands.", 
				"If started with single argument, help command must print name and the description of selected command (or print"
				+ " appropriate error message if no such command exists).");
	}
	
}
