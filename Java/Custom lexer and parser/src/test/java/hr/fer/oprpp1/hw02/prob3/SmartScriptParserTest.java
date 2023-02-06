package hr.fer.oprpp1.hw02.prob3;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantDouble;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantInteger;
import hr.fer.oprpp1.custom.scripting.elems.ElementFunction;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

/**
 * Class with testing methods for SmartScriptParser.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class SmartScriptParserTest {
	
	
	@Test
	public void testExample1() {
		String text = readExample(1);
		
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode doc = parser.getDocumentNode();
		
		SmartScriptParser parser2 = new SmartScriptParser(doc.toString());
		DocumentNode doc2 = parser2.getDocumentNode();
		
		assertEquals(TextNode.class, doc.getChild(0).getClass());
		assertEquals(1, doc.numberOfChildren());	
		assertEquals(doc, doc2);
	}
	
	
	@Test
	public void testExample2() {
		String text = readExample(2);
		
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode doc = parser.getDocumentNode();
		
		SmartScriptParser parser2 = new SmartScriptParser(doc.toString());
		DocumentNode doc2 = parser2.getDocumentNode();
		
		assertEquals(TextNode.class, doc.getChild(0).getClass());
		assertEquals(1, doc.numberOfChildren());
		assertEquals(doc, doc2);
	}
	
	@Test
	public void testExample3() {
		String text = readExample(3);
		
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode doc = parser.getDocumentNode();
		
		SmartScriptParser parser2 = new SmartScriptParser(doc.toString());
		DocumentNode doc2 = parser2.getDocumentNode();
		
		assertEquals(TextNode.class, doc.getChild(0).getClass());
		assertEquals(1, doc.numberOfChildren());
		assertEquals(doc, doc2);
	}
	
	@Test
	public void testExample4() {
		String text = readExample(4);
		
		assertThrows(SmartScriptParserException.class, ()->{
			SmartScriptParser parser = new SmartScriptParser(text);
		});
	}
	
	@Test
	public void testExample5() {
		String text = readExample(5);
		
		assertThrows(SmartScriptParserException.class, ()->{
			SmartScriptParser parser = new SmartScriptParser(text);
		});
	}
	
	@Test
	public void testExample6() {
		String text = readExample(6);
		
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode doc = parser.getDocumentNode();
		
		SmartScriptParser parser2 = new SmartScriptParser(doc.toString());
		DocumentNode doc2 = parser2.getDocumentNode();
		
		assertEquals(2, doc.numberOfChildren());
		assertEquals(TextNode.class, doc.getChild(0).getClass());
		assertEquals(EchoNode.class, doc.getChild(1).getClass());
		
		Element[] echoNodeArray = ((EchoNode)(doc.getChild(1))).getElements();
		
		assertEquals(2, echoNodeArray.length);
		assertEquals(ElementString.class, echoNodeArray[1].getClass());
		assertEquals(doc, doc2);
	}
	
	@Test
	public void testExample7() {
		String text = readExample(7);
		
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode doc = parser.getDocumentNode();
		
		SmartScriptParser parser2 = new SmartScriptParser(doc.toString());
		DocumentNode doc2 = parser2.getDocumentNode();
		
		assertEquals(2, doc.numberOfChildren());
		assertEquals(TextNode.class, doc.getChild(0).getClass());
		assertEquals(EchoNode.class, doc.getChild(1).getClass());
		
		Element[] echoNodeArray = ((EchoNode)(doc.getChild(1))).getElements();
		
		assertEquals(2, echoNodeArray.length);
		assertEquals(doc, doc2);
		assertEquals(ElementString.class, echoNodeArray[1].getClass());
	}
	
	
	@Test
	public void testExample8() {
		String text = readExample(8);
		
		assertThrows(SmartScriptParserException.class, ()->{
			SmartScriptParser parser = new SmartScriptParser(text);
		});
	}
	
	@Test
	public void testExample9() {
		String text = readExample(9);
		
		assertThrows(SmartScriptParserException.class, ()->{
			SmartScriptParser parser = new SmartScriptParser(text);
		});
	}
	
	@Test
	public void testExample10() {
		String text = readExample(10);
		
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode doc = parser.getDocumentNode();
		
		SmartScriptParser parser2 = new SmartScriptParser(doc.toString());
		DocumentNode doc2 = parser2.getDocumentNode();
		
		assertEquals(2, doc.numberOfChildren());
		assertEquals(TextNode.class, doc.getChild(0).getClass());
		assertEquals(ForLoopNode.class, doc.getChild(1).getClass());
		assertEquals(ForLoopNode.class, doc.getChild(1).getChild(0).getClass());
		assertEquals(doc, doc2);
	}
	
	@Test
	public void testExample11() {
		String text = readExample(11);
		
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode doc = parser.getDocumentNode();
		
		SmartScriptParser parser2 = new SmartScriptParser(doc.toString());
		DocumentNode doc2 = parser2.getDocumentNode();
		
		assertEquals(1, doc.numberOfChildren());
		assertEquals(ForLoopNode.class, doc.getChild(0).getClass());
		
		Element first = ((ForLoopNode)doc.getChild(0)).getVariable();
		Element second = ((ForLoopNode)doc.getChild(0)).getStartExpression();
		Element third = ((ForLoopNode)doc.getChild(0)).getStartExpression();
		Element fourth = ((ForLoopNode)doc.getChild(0)).getStepExpression();
		
		assertEquals(ElementVariable.class, first.getClass());
		assertEquals(ElementString.class, second.getClass());
		assertEquals(ElementString.class, third.getClass());
		
		assertThrows(NullPointerException.class, ()->{
			fourth.getClass();
		});
		
		assertEquals(doc, doc2);
	}
	
	@Test
	public void testExample12() {
		String text = readExample(12);
		
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode doc = parser.getDocumentNode();
		
		SmartScriptParser parser2 = new SmartScriptParser(doc.toString());
		DocumentNode doc2 = parser2.getDocumentNode();
		
		assertEquals(1, doc.numberOfChildren());
		assertEquals(TextNode.class, doc.getChild(0).getClass());
		assertEquals("Testiranje escape \\\\ \\{$for.", ((TextNode)doc.getChild(0)).getText());
		assertEquals(doc, doc2);
	}
	
	@Test
	public void testExample13() {
		String text = readExample(13);
		
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode doc = parser.getDocumentNode();
		
		SmartScriptParser parser2 = new SmartScriptParser(doc.toString());
		DocumentNode doc2 = parser2.getDocumentNode();
		
		Element[] elements = ((EchoNode)doc.getChild(0)).getElements();
		
		assertEquals(ElementVariable.class, elements[0].getClass());
		assertEquals(ElementConstantDouble.class, elements[1].getClass());
		assertEquals(ElementString.class, elements[2].getClass());
		assertEquals(ElementConstantInteger.class, elements[3].getClass());
		assertEquals(ElementFunction.class, elements[4].getClass());
		assertEquals(5, elements.length);
		assertEquals(doc, doc2);
	}
	
	
	
	private String readExample(int n) {
		  try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer"+n+".txt")) {
		    if(is==null) throw new RuntimeException("Datoteka extra/primjer"+n+".txt je nedostupna.");
		    byte[] data = is.readAllBytes();
		    String text = new String(data, StandardCharsets.UTF_8);
		    return text;
		  } catch(IOException ex) {
		    throw new RuntimeException("Greška pri čitanju datoteke.", ex);
		  }
		}
}
