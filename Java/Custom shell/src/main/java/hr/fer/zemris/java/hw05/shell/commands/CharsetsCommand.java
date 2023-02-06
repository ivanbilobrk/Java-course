package hr.fer.zemris.java.hw05.shell.commands;

import java.nio.charset.Charset;
import java.util.List;
import java.util.SortedMap;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

/**
 * Implementation of charset command.
 * @author Ivan Bilobrk
 *
 */
public class CharsetsCommand implements ShellCommand{
	
	/**
	 * Method for printing all available charsets that shell supports.
	 * @param env - interface through which a command communicates with a shell
	 * @param arguments - charset command doesn't accept any arguments.
	 * @return ShellStatus
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if(arguments != null && arguments.length() != 0) {
			env.writeln("Charsets commnad doesn't accept any arguments.");
			return ShellStatus.CONTINUE;
		} 
		
		SortedMap<String, Charset> charsets = Charset.availableCharsets();
		charsets.forEach((k, v)-> env.writeln(k));
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "Charsets";
	}

	@Override
	public List<String> getCommandDescription() {
		return List.of("Charsets command lists all available charsets on this machine.");
	}

}
