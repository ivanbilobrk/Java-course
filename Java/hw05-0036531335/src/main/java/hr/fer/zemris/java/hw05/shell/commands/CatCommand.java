package hr.fer.zemris.java.hw05.shell.commands;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;
import hr.fer.zemris.java.hw05.shell.Util;

/**
 * Implementation of cat command.
 * @author Ivan Bilobrk
 *
 */
public class CatCommand implements ShellCommand{
	
	/**
	 * Method for printing content of a file to the console.
	 * @param env - interface through which a command communicates with a shell
	 * @param arguments for cat command. User can give one argument which represents a path of a file or two where the first is 
	 * also a path and the second is a desired charset for reading a file. If user does not specify charset than a default one will be used.
	 * @return ShellStatus
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments == null || arguments.length() == 0) {
			env.writeln("Cat command must be given at least one argument.");
			return ShellStatus.CONTINUE;
		}
		
		try {
			//dohvaćanje argumenata naredbe
			String[] tempArray = Util.getArguments(arguments);
			String path = tempArray[0];
			String charset = tempArray[1];
			Path p = Util.parsePath(path);
			
			//provjera argumenata
			if(Files.isDirectory(p)) {
				env.writeln("Cannot use cat command on directory.");
				return ShellStatus.CONTINUE;
			}
			
			if(charset.length() == 0) {
				Files.lines(p).forEach(line->env.writeln(line));
				return ShellStatus.CONTINUE;
			} else {
				Files.lines(p, Charset.forName(charset)).forEach(line->env.writeln(line));
				return ShellStatus.CONTINUE;
			}
			
		} catch(Exception e) {
			env.writeln(e.getMessage() + e.getClass());
			return ShellStatus.CONTINUE;
		}
		
		
	}

	@Override
	public String getCommandName() {
		return "Cat";
	}

	@Override
	public List<String> getCommandDescription() {
		return List.of("Cat command that accepts one argument writes content of file to the console.", "Cat command that accepts two arguments "
				+ "writes content of file to the console using desired charset.");
	}

}
