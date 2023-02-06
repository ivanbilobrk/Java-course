package hr.fer.zemris.java.hw05.shell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw05.shell.commands.CatCommand;
import hr.fer.zemris.java.hw05.shell.commands.CharsetsCommand;
import hr.fer.zemris.java.hw05.shell.commands.CopyCommand;
import hr.fer.zemris.java.hw05.shell.commands.ExitCommand;
import hr.fer.zemris.java.hw05.shell.commands.HelpCommand;
import hr.fer.zemris.java.hw05.shell.commands.HexdumpCommand;
import hr.fer.zemris.java.hw05.shell.commands.LsCommand;
import hr.fer.zemris.java.hw05.shell.commands.MkdirCommand;
import hr.fer.zemris.java.hw05.shell.commands.ShellCommand;
import hr.fer.zemris.java.hw05.shell.commands.SymbolCommand;
import hr.fer.zemris.java.hw05.shell.commands.TreeCommand;

public class MyShell implements Environment{
	
	/**
	 * Current prompt symbol.
	 */
	private char promptSymbol;
	
	/**
	 * Current more lines symbol.
	 */
	private char moreLinesSymbol;
	
	/**
	 * Current multi line symbol.
	 */
	private char multiLineSymbol;
	
	/**
	 * Scanner for reading user input.
	 */
	private Scanner sc;
	
	/**
	 * Method holding all supported commands for this shell.
	 */
	private SortedMap<String, ShellCommand> commands;
	
	/**
	 * Current status of a shell.
	 */
	private ShellStatus status;

	/**
	 * Constructor.
	 */
	public MyShell() {
		super();
	}

	
	/**
	 * Method to start a shell which initializes all commands and settings.
	 */
	private void start() {
		setPromptSymbol('>');
		setMorelinesSymbol('\\');
		setMultilineSymbol('|');
		sc = new Scanner(System.in);
		commands = new TreeMap<>();
		commands.put("charsets", new CharsetsCommand());
		commands.put("cat", new CatCommand());
		commands.put("ls", new LsCommand());
		commands.put("tree", new TreeCommand());
		commands.put("copy", new CopyCommand());
		commands.put("mkdir", new MkdirCommand());
		commands.put("hexdump", new HexdumpCommand());
		commands.put("symbol", new SymbolCommand());
		commands.put("exit", new ExitCommand());
		commands.put("help", new HelpCommand());
		writeln("Welcome to MyShell v 1.0");
		status = ShellStatus.CONTINUE;
	}

	@Override
	public String readLine() throws ShellIOException {
		try {
			return sc.nextLine();
		} catch(Exception e) {
			throw new ShellIOException(e.getMessage());
		}
	}

	@Override
	public void write(String text) throws ShellIOException {
		try {
			System.out.print(text);
		} catch(Exception e){
			throw new ShellIOException(e.getMessage());
		}
		
	}

	@Override
	public void writeln(String text) throws ShellIOException {
		try {
			System.out.println(text);
		} catch(Exception e){
			throw new ShellIOException(e.getMessage());
		}
		
	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		return Collections.unmodifiableSortedMap(commands);
	}

	@Override
	public Character getMultilineSymbol() {
		return multiLineSymbol;
	}

	@Override
	public void setMultilineSymbol(Character symbol) {
		this.multiLineSymbol = symbol;
	}

	@Override
	public Character getPromptSymbol() {
		return promptSymbol;
	}

	@Override
	public void setPromptSymbol(Character symbol) {
		this.promptSymbol = symbol;
		
	}

	@Override
	public Character getMorelinesSymbol() {
		return moreLinesSymbol;
	}

	@Override
	public void setMorelinesSymbol(Character symbol) {
		this.moreLinesSymbol = symbol;
	}
	
	public static void main(String[] args) {
		MyShell shell = new MyShell();
		shell.start();
		
		do {
			shell.write(shell.promptSymbol+" ");
			String line = shell.readLine();
			String[] pieces = line.split(" ");
			int len = pieces.length;
			
			if(pieces[len-1].length() == 1 && pieces[len-1].charAt(0) == shell.moreLinesSymbol) {
				List<String> newPieces = new ArrayList<>();
				
				for(int i = 0; i < len-1; i++) {
					newPieces.add(pieces[i]);
				}
				//čitamo nove retke koji si dio iste komande sve dok nemamo simbol more line na kraju
				while(true) {
					shell.write(shell.multiLineSymbol+" ");
					line = shell.readLine();
					pieces = line.split(" ");
					len = pieces.length;
					if(!(pieces[len-1].length() == 1 && pieces[len-1].charAt(0) == shell.moreLinesSymbol)) break;
					
					for(int i = 0; i < len-1; i++) {
						newPieces.add(pieces[i]);
					}
				}
				//jos zadnju liniju moramo dodat u listu
				for(int i = 0; i < pieces.length; i++) {
					newPieces.add(pieces[i]);
				}
				
				pieces = newPieces.toArray(new String[] {});
				len = pieces.length;
				
			}
				//nemamo more lines symbol
				String piecesWithoutCommand = "";
				for(int i = 1; i < len; i++) {
					if(i != len-1)
						piecesWithoutCommand += pieces[i]+" ";
					else 
						piecesWithoutCommand += pieces[i];
				}
				
				if(pieces[0].equals("symbol")) {
					shell.status = shell.commands.get("symbol").executeCommand(shell, piecesWithoutCommand);
				} else if(pieces[0].equals("exit")) {
					shell.status = shell.commands.get("exit").executeCommand(shell, piecesWithoutCommand);
				} else if(pieces[0].equals("charsets")) {
					shell.status = shell.commands.get("charsets").executeCommand(shell, piecesWithoutCommand);
				} else if(pieces[0].equals("cat")) {
					shell.status = shell.commands.get("cat").executeCommand(shell, piecesWithoutCommand);
				} else if(pieces[0].equals("ls")) {
					shell.status = shell.commands.get("ls").executeCommand(shell, piecesWithoutCommand);
				} else if(pieces[0].equals("tree")) {
					shell.status = shell.commands.get("tree").executeCommand(shell, piecesWithoutCommand);
				} else if(pieces[0].equals("copy")) {
					shell.status = shell.commands.get("copy").executeCommand(shell, piecesWithoutCommand);
				} else if(pieces[0].equals("mkdir")) {
					shell.status = shell.commands.get("mkdir").executeCommand(shell, piecesWithoutCommand);
				} else if(pieces[0].equals("hexdump")) {
					shell.status = shell.commands.get("hexdump").executeCommand(shell, piecesWithoutCommand);
				} else if(pieces[0].equals("help")) {
					shell.status = shell.commands.get("help").executeCommand(shell, piecesWithoutCommand);
				} else {
					shell.writeln("Command doesn't exist.");
				}
				
			
			
		} while(shell.status != ShellStatus.TERMINATE);
		shell.sc.close();
	}
	
}
