package hr.fer.zemris.java.hw05.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;
import hr.fer.zemris.java.hw05.shell.Util;

/**
 * Implementation of copy command.
 * @author Ivan Bilobrk
 *
 */
public class CopyCommand implements ShellCommand{

	/**
	 * Method for copying content of a file to another file.
	 * @param env - interface through which a command communicates with a shell
	 * @param arguments for copy command. User can give two arguments which represent paths of a destination file and source file. 
	 * If destination file already exists user will be asked for permission to overwrite existing file. If destination file doesn't exist
	 * it will be created.
	 * @return ShellStatus
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments == null || arguments.length() == 0) {
			env.writeln("Copy command must be given two arguments.");
			return ShellStatus.CONTINUE;
		}
		
		try {
			String[] tempArray = Util.getArguments(arguments);
			String first = tempArray[0];
			String second = tempArray[1];
			
			//provjera argumenata
			if(second.length() == 0) {
				env.writeln("Copy command must be given two arguments.");
				return ShellStatus.CONTINUE;
			}
			
			Path p1 = Util.parsePath(first);
			
			if(!Files.exists(p1)) {
				env.writeln("File you are trying to copy doesn't exist.");
				return ShellStatus.CONTINUE;
			}
			
			if(Files.isDirectory(p1)) {
				env.writeln("Can't copy directory.");
				return ShellStatus.CONTINUE;
			}
			
			Path p2 = Util.parsePath(second);
			
			if(Files.isDirectory(p2)) {
				p2 = p2.resolve(p1.getFileName());
			}
			
			if(!Files.isDirectory(p2) && Files.exists(p2)) {
				env.writeln("Destination file already exists. Do you want to overwrite it? Write \"yes\" or \"no\"");
				String result;
				
				do {
					env.write(env.getPromptSymbol()+" ");
					result = env.readLine();
					
				}while(!result.toLowerCase().equals("yes") && !result.toLowerCase().equals("no"));
				
				if(result.toLowerCase().equals("no")) {
					return ShellStatus.CONTINUE;
				}
			}
			
			//tokovi za čitanje i pisanje
			BufferedInputStream is = new BufferedInputStream(new FileInputStream(p1.toFile()));
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(p2.toFile()));
			
			int read;
			byte[] byteArray = new byte[4096];
			
			while((read = is.read(byteArray, 0, 4096)) != -1) {
				os.write(byteArray, 0, read);
			}
			os.close();
			is.close();
			return ShellStatus.CONTINUE;
			
		} catch(Exception e) {
			env.writeln(e.getMessage() + e.getClass());
			return ShellStatus.CONTINUE;
		}
	}

	@Override
	public String getCommandName() {
		return "Copy";
	}

	@Override
	public List<String> getCommandDescription() {
		return List.of("Copies file which path is specified in the first argument to the second second file specified in the secon argument", 
				"If second argument specifies a file that already exists user will be asked for permission to overwrite the file.",
				"If second argument specifies a path to directory new file with the same name as original file will be created in that directory.");
	}

}
