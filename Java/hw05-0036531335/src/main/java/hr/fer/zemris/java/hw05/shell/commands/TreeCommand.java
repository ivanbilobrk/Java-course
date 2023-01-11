package hr.fer.zemris.java.hw05.shell.commands;

import java.nio.file.Files;

import java.nio.file.Path;
import java.util.List;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;
import hr.fer.zemris.java.hw05.shell.Util;

/**
 * Implementation of tree command.
 * @author Ivan Bilobrk
 *
 */
public class TreeCommand implements ShellCommand{
	
	/**
	 * Method for printing contents of a directory recursively.
	 * @param env - interface through which a command communicates with a shell
	 * @param arguments for tree command. This command accepts one argument and it has to be a path to the directory. 
	 * @return ShellStatus
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if(arguments == null || arguments.length() == 0) {
			env.writeln("Tree command must be given at least one argument.");
			return ShellStatus.CONTINUE;
		}
	
		//tree naredba implementirana sa simple file visitorom 
		try {
			String[] tempArray = Util.getArguments(arguments);
			String first = tempArray[0];
			if(tempArray[1].length() != 0) {
				env.writeln("Tree doesn't accept more than one argument.");
				return ShellStatus.CONTINUE;
			}
			Path p = Util.parsePath(first);
			MyFileVisitor visitor = new MyFileVisitor();
			Files.walkFileTree(p, visitor);
			String result = visitor.getResult();
			result = result.substring(0, result.length()-1);
			env.writeln(result);
			return ShellStatus.CONTINUE;
		} catch(Exception e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
	}

	@Override
	public String getCommandName() {
		return "tree";
	}

	@Override
	public List<String> getCommandDescription() {
		return List.of("Tree command lists all files and directories recursively in one directory.");
	}

}
