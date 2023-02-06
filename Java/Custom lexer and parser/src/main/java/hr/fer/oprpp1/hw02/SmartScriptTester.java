package hr.fer.oprpp1.hw02;

import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

/**
 * Class with main method used for testing SmartScriptParser
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class SmartScriptTester {

	public static void main(String[] args) throws IOException {
		
		String docBody = new String(
				 Files.readAllBytes(Paths.get(args[0])),
				 StandardCharsets.UTF_8
		);

		
		SmartScriptParser parser = null;
		
		
		try {
		 parser = new SmartScriptParser(docBody);
		} catch(SmartScriptParserException e) {
		 System.out.println("Unable to parse document!");
		 System.exit(-1);
		} catch(Exception e) {
		 System.out.println("If this line ever executes, you have failed this class!");
		 System.exit(-1);
		}
		
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = document.toString();
		//System.out.println(originalDocumentBody);
		
		
		
		
		
		SmartScriptParser parser2 = null;
		try {
			parser2 = new SmartScriptParser(originalDocumentBody);
		} catch(SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.exit(1);
		} catch(Exception e){
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}
				
		DocumentNode document2 = parser2.getDocumentNode();
		// now document and document2 should be structurally identical trees
		//System.out.println(document2);
		boolean same = document.equals(document2);
		System.out.println(same);
	}

}
