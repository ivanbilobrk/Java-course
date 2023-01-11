package hr.fer.zemris.java.hw05.shell.commands;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellStatus;
import hr.fer.zemris.java.hw05.shell.Util;

/**
 * Implementation of ls command.
 * @author Ivan Bilobrk
 *
 */
public class LsCommand implements ShellCommand{
	
	/**
	 * Method for printing contents of a directory (not recursively).
	 * @param env - interface through which a command communicates with a shell
	 * @param arguments for ls command. This command accepts one argument and it has to be a path to the directory. 
	 * @return ShellStatus
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if(arguments == null || arguments.length() == 0) {
			env.writeln("Ls command must be given at least one argument.");
			return ShellStatus.CONTINUE;
		}
		
		
		try {
			//provjera argumenata
			String[] tempArray = Util.getArguments(arguments);
			String first = tempArray[0];
			if(tempArray[1].length() != 0) {
				env.writeln("Ls doesn't accept more than one argument.");
				return ShellStatus.CONTINUE;
			}
			Path p = Util.parsePath(first);
			
			if(!Files.isDirectory(p)) {
				env.writeln("Cannot use ls command on file.");
				return ShellStatus.CONTINUE;
			}
			
			File f = p.toFile();
			
			//prolaz kroz sve što se nalazi u direktoriju te formatiranje ispisa prema atributima
			for(File x: f.listFiles()) {
					Path temp = x.toPath();
				
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					BasicFileAttributeView faView = Files.getFileAttributeView(temp, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
					BasicFileAttributes attributes = faView.readAttributes();
					
					FileTime fileTime = attributes.creationTime();
					String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
					
					long size = attributes.size();
					String formattedSize = String.format("%10d", size);
					
					String fileName = temp.getFileName().toString();
					
					boolean isDirectory = attributes.isDirectory();
					boolean isReadable = Files.isReadable(p);
					boolean isWritable = Files.isWritable(p);
					boolean isExecutable = Files.isExecutable(p);
					
					String result = "";
					
					if(isDirectory) {
						result+="d";
					} else {
						result += "-";
					}
					
					if(isReadable) {
						result += "r";
					} else {
						result += "-";
					}
					
					if(isWritable) {
						result += "w";
					} else {
						result += "-";
					}
					
					if(isExecutable) {
						result += "x";
					} else {
						result += "-";
					}
					
					result += " "+ formattedSize+" "+formattedDateTime+" "+fileName;
					
					env.writeln(result);
			}
			return ShellStatus.CONTINUE;
			
		} catch(Exception e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
	}

	@Override
	public String getCommandName() {
		return "Ls";
	}

	@Override
	public List<String> getCommandDescription() {
		return List.of("Ls command is used to get file or directory info.");
	}

}
