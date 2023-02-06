package hr.fer.zemris.java.hw05.shell.commands;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

/**
 * Implementation of symbol command.
 * @author Ivan Bilobrk
 *
 */
public class SymbolCommand implements ShellCommand{
	
	/**
	 * Method for getting info about shell symbols or changing them.
	 * @param env - interface through which a command communicates with a shell
	 * @param arguments for symbol command. This method can print current symbol for prompt, more lines or multi line if given one argument. 
	 * If this method receives two arguments it will change current symbol for desired action.
	 * @return ShellStatus
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		String[] args = arguments.split(" ");
		//postoji ukupno 6 slučajeva kod narede symbol, imamo ukupno 3 simbola te za svaki postoji mogućnost samo ispisa trenutnog ili promjene 
		if(args[0].equals("PROMPT")) {
			if(args.length == 1) {
				seeCurrent(env, (environment)-> environment.getPromptSymbol(), "PROMPT");
				return ShellStatus.CONTINUE;
			} else if(args.length == 2 && args[1].length() == 1) {
				changeCurrent(env, (environment)-> environment.getPromptSymbol(), "PROMPT", (environment)->environment.setPromptSymbol(args[1].charAt(0)));
				return ShellStatus.CONTINUE;
			} else {
				env.writeln("Inavlid arguments for command symbol.");
				return ShellStatus.CONTINUE;
			}
		} else if(args[0].equals("MORELINES")) {
			if(args.length == 1) {
				seeCurrent(env, (environment)-> environment.getMorelinesSymbol(), "MORELINES");
				return ShellStatus.CONTINUE;
			} else if(args.length == 2 && args[1].length() == 1) {
				changeCurrent(env, (environment)-> environment.getMorelinesSymbol(), "MORELINES", (environment)->environment.setMorelinesSymbol(args[1].charAt(0)));
				return ShellStatus.CONTINUE;
			} else {
				env.writeln("Inavlid arguments for command symbol.");
				return ShellStatus.CONTINUE;
			}
		} else if(args[0].equals("MULTILINE")) {
			if(args.length == 1) {
				seeCurrent(env, (environment)-> environment.getMultilineSymbol(), "MULTILINE");
				return ShellStatus.CONTINUE;
			} else if(args.length == 2 && args[1].length() == 1) {
				changeCurrent(env, (environment)-> environment.getMultilineSymbol(), "MULTILINE", (environment)->environment.setMultilineSymbol(args[1].charAt(0)));
				return ShellStatus.CONTINUE;
			} else {
				env.writeln("Inavlid arguments for command symbol.");
				return ShellStatus.CONTINUE;
			}
		} else {
			env.writeln("Inavlid arguments for command symbol.");
			return ShellStatus.CONTINUE;
		}
	}
	
	private void seeCurrent(Environment env, Function<Environment, Character> getSymbol, String name) {
		env.writeln("Symbol for "+ name +" is '"+getSymbol.apply(env)+"'");
	}
	
	private void changeCurrent(Environment env, Function<Environment, Character> getSymbol, String name, Consumer<Environment> changeSymbol) {
		char old = getSymbol.apply(env);
		changeSymbol.accept(env);
		env.writeln("Symbol for "+ name +" changed from '"+old+"' to '"+getSymbol.apply(env)+"'");
	}

	@Override
	public String getCommandName() {
		return "symbol";
	}

	@Override
	public List<String> getCommandDescription() {
		return List.of("Command 'symbol PROMPT' writes out current prompt symbol.", "Command 'symbol PROMPT' followed by a single character "
				+ "changes current prompt symbol.", "Command 'symbol MORELINES' writes out current morelines symbol.", 
				"Command 'symbol MORELINES' followed by a single character changes current morelines symbol.", 
				"Command 'symbol MULTILINE' writes out current multiline symbol.", 
				"Command 'symbol MULTILINE' followed by a single character changes current multiline symbol.");
	}

}
