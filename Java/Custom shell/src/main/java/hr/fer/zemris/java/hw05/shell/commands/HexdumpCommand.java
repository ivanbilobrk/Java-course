package hr.fer.zemris.java.hw05.shell.commands;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;
import hr.fer.zemris.java.hw05.shell.Util;

/**
 * Implementation of hexdump command.
 * @author Ivan Bilobrk
 *
 */
public class HexdumpCommand implements ShellCommand{

	/**
	 * Method for printing hex dump of a file to the console.
	 * @param env - interface through which a command communicates with a shell
	 * @param arguments for hexdump command. This command accepts one argument and it has to be a path to a file for which user
	 * wants to get hexdump. 
	 * @return ShellStatus
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments == null || arguments.length() == 0) {
			env.writeln("Hexdump command must be given at least one argument.");
			return ShellStatus.CONTINUE;
		}
		
		try {
			//provjera argumenata
			String[] tempArray = Util.getArguments(arguments);
			String first = tempArray[0];
			if(tempArray[1].length() != 0) {
				env.writeln("Hexdump doesn't accept more than one argument.");
				return ShellStatus.CONTINUE;
			}
			Path p = Util.parsePath(first);
			
			if(Files.isDirectory(p)) {
				env.writeln("Cannot use hexdump command on directory.");
				return ShellStatus.CONTINUE;
			}
			
			String result = "";
			
			InputStream is = Files.newInputStream(p);
			
			byte[] byteArray = new byte[16];
			int read;
			int position = 0;
			//bufferirano čitanje po 16 byteova
			while((read = is.read(byteArray, 0, 16)) != -1) {
				result += String.format("%08d", position) +": ";
				
				//formatiranje ispisa
				for(int i = 0; i < 16; i++) {
					String piece = "  ";
					if(i < read) {
						 piece = String.format("%02X", byteArray[i]);
					}
					result += piece;
					if(i == 7) {
						result += "|";
					} else if(i == 15) {
						result += " | ";
					} else {
						result += " ";
					}
					
				}
				
				for(int i = 0; i < read; i++) {
					if(byteArray[i] < 32 || byteArray[i] > 127) {
						result += ".";
					} else {
						result += (char)byteArray[i];
					}
				}
				
				position += 10;
				result += "\n";
			}
			
			env.writeln(result.substring(0, result.length()-1));
			return ShellStatus.CONTINUE;
			
			
		} catch(Exception e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
	}

	@Override
	public String getCommandName() {
		return "Hexdump";
	}

	@Override
	public List<String> getCommandDescription() {
		return List.of("Hexdump command prints hex dump of a file to the console.");
	}

}
