package hr.fer.zemris.java.hw05.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Custom SimpleFileVisitor implementation for tree command.
 * @author Ivan Bilobrk
 *
 */
public class MyFileVisitor extends SimpleFileVisitor<Path>{
	
	/**
	 * Current depth of directory.
	 */
	int level = 0;
	/**
	 * String which represents directory structure.
	 */
	String result = "";
	
	/**
	 * Method which appends directory name to the result and increases current level by 2;
	 * @param p - Path of a directory
	 * @param attrs - Attributes of a directory.
	 * @return FileVisitResult
	 */
	public FileVisitResult preVisitDirectory(Path p, BasicFileAttributes attrs) {
		result += " ".repeat(level) + p.getFileName().toString()+"\n";
		level += 2;
		return FileVisitResult.CONTINUE;
	}
	
	/**
	 * Method which appends file name to the result and increases current level by 2;
	 * @param p - Path of a file
	 * @param attrs - Attributes of a file.
	 * @return FileVisitResult
	 */
	public FileVisitResult visitFile(Path p, BasicFileAttributes attrs) {
		result += " ".repeat(level) + p.getFileName().toString()+"\n";
		return FileVisitResult.CONTINUE;
	}
	
	/**
	 * Invoked for a file that could not be visited. 
	 * @param p - Path of a file
	 * @param exc - the I/O exception that prevented the file from being visited.
	 * @return FileVisitResult
	 */
	public FileVisitResult visitFileFailed(Path p, IOException exc) {
		result += " ".repeat(level) + p.getFileName().toString() +" couldn't be visited.";
		return FileVisitResult.CONTINUE;
	}
	
	/**
	 * Invoked for a directory after entries in the directory, and all of their descendants, have been visited. 
	 * @param p - Path of a file
	 * @param exc - null if the iteration of the directory completes without an error; 
	 * otherwise the I/O exception that caused the iteration of the directory to complete prematurely
	 */
	public FileVisitResult postVisitDirectory(Path p, IOException exc) {
		level -= 2;
		return FileVisitResult.CONTINUE;
	}
	
	/**
	 * Method to get result of file visitor.
	 * @return String representation of result from going through directory structure.
	 */
	public String getResult() {
		return result;
	}
	
}
