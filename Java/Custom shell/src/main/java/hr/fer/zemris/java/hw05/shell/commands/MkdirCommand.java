package hr.fer.zemris.java.hw05.shell.commands;

import java.io.File;
import java.util.List;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;
import hr.fer.zemris.java.hw05.shell.Util;

/**
 * Implementation of mkdir command.
 * @author Ivan Bilobrk
 *
 */
public class MkdirCommand implements ShellCommand{
	
	/**
	 * Method for creating desired directory structure.
	 * @param env - interface through which a command communicates with a shell
	 * @param arguments for mkdir command. This command accepts one argument and that is a path of directory structure you want to create.
	 * @return ShellStatus
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments == null || arguments.length() == 0) {
			env.writeln("Mkdir command must be given at least one argument.");
			return ShellStatus.CONTINUE;
		}
	
		
		try {
			//provjera argumenata
			String[] tempArray = Util.getArguments(arguments);
			String first = tempArray[0];
			if(tempArray[1].length() != 0) {
				env.writeln("Mkdir doesn't accept more than one argument.");
				return ShellStatus.CONTINUE;
			}
			File f = Util.parsePath(first).toFile();
			if(f.mkdirs()) {
				env.writeln("Directory was created.");
			} else {
				env.writeln("Couldn't create directories.");
			}
			return ShellStatus.CONTINUE;
		} catch(Exception e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
	}

	@Override
	public String getCommandName() {
		return "Mkdir";
	}

	@Override
	public List<String> getCommandDescription() {
		return List.of("Mkdir command creates a desired directory structure.");
	}

}
