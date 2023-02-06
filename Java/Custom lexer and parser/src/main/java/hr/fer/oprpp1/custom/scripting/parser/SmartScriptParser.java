package hr.fer.oprpp1.custom.scripting.parser;


import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.Processor;
import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantDouble;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantInteger;
import hr.fer.oprpp1.custom.scripting.elems.ElementFunction;
import hr.fer.oprpp1.custom.scripting.elems.ElementOperator;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;
import hr.fer.oprpp1.custom.scripting.lexer.Lexer;
import hr.fer.oprpp1.custom.scripting.lexer.LexerState;
import hr.fer.oprpp1.custom.scripting.lexer.Token;
import hr.fer.oprpp1.custom.scripting.lexer.TokenType;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp1.custom.scripting.nodes.Node;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;

/**
 * Parser class which evaluates if source text satisfies desired rules.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class SmartScriptParser {
	
	/**
	 * Source text we want to evaluate.
	 */
	private String docBody;
	
	/**
	 * The root node of syntax tree.
	 */
	private DocumentNode docNode;
	
	/**
	 * Stack used internally for parsing.
	 */
	private ObjectStack stack;
	
	/**
	 * Constructor which parses given text.
	 * @param docBody - text to be parsed.
	 * @throws SmartScriptParserException if docBody is null.
	 */
	public SmartScriptParser(String docBody) {
		
		if(docBody == null) throw new SmartScriptParserException("Cannot parse null String.");
		
		this.docBody = docBody;
		stack = new ObjectStack();
		docNode = new DocumentNode();
		parse();
	}
	
	/**
	 * Method to get the main Node of syntax tree.
	 * @return Root of syntax tree.
	 */
	public DocumentNode getDocumentNode() {
		return docNode;
	}
	
	/**
	 * Private method first called in constructor which parses given text.
	 * @throws SmartScriptParserException if any error occurs during parsing.
	 */
	private void parse() {
		
		//na stog odmah stavljamo glavni node sintaksnog stabla
		try {
			stack.push(docNode);
		} catch (Exception e) {
			throw new SmartScriptParserException("Cannot push element to stack.");
		}
		
		Lexer lexer = new Lexer(docBody);
		Token token = lexer.nextToken();
		
		while(true) {
			//provjera jesmo li došli do kraja teksta
			if(token.getType() == TokenType.EOF) {
				break;
			}
			//ovisno u kojem smo stanju vrijede različita pravila
			if(lexer.getState() == LexerState.BASIC) {
				
				//ako nismo naišli na token otvaranja taga radi se o text node
				if(!(token.getType() == TokenType.TAGOPEN)) {
	
					TextNode node = new TextNode((String)token.getValue());
					
					Node currentTop = null;
					try {
						currentTop = (Node)stack.peek();
					} catch(Exception e) {
						throw new SmartScriptParserException("Cannot peek object from stack.");
					}
					
					
					currentTop.addChildNode(node);
					token = lexer.nextToken();
					
				} else {
					lexer.setState(LexerState.TAG); //u slučaju da se otvorio tag moramo promjeniti stanje lexera
				}
			} else {
				//ušli smo u stanje TAG
				
				//tražimo sljedeći token
				token = lexer.nextToken();
				
				//provjera jesmo li došli do oznake end kojom zatvaramo tag
				if(token.getType() != TokenType.INTEGER && token.getType() != TokenType.DOUBLE &&
						((String)token.getValue()).toLowerCase().equals("end")) {
					
					token = lexer.nextToken();
					//nakon "end" mora doći oznaka kraja taga
					if(token.getType() != TokenType.TAGCLOSE)
						throw new SmartScriptParserException("Invalid use of END tag");
					
					try {
						stack.pop();
					} catch (Exception e) {
						throw new SmartScriptParserException("Cannot pop element from stack.");
					}
					
					//ako nakon uklanjanja vrijednosti sa stoga stog ostane prazan došlo je do pogreške
					if(stack.size() == 0)
						throw new SmartScriptParserException("Wrong number of END tags.");
					
					token = lexer.nextToken();
					//mijenjamo stanje u Basic
					lexer.setState(LexerState.BASIC);
					
				}else if(token.getType() != TokenType.INTEGER && token.getType() != TokenType.DOUBLE &&
						((String)token.getValue()).toLowerCase().equals("for")) {
					//riječ je o for tagu
					
					//kako for tag može imati najviše 4 elementa stvaramo polje tipa Element veličine 4
					Element[] elements = new Element[4];
					int index = 0;
					
					//prvo mora ići varijabla
					
					token = lexer.nextToken();
					
					if(token.getType() != TokenType.VARIABLE)
						throw new SmartScriptParserException("First element in for loop must be variable.");
					
					//ako je varijabla dodamo ju u polje
					
					ElementVariable variableFirst = new ElementVariable((String)token.getValue());
					elements[index++] = variableFirst;
					
					Element el = null;
					
					//provjera sljedeća dva elementa koji su nužni u for tagu
					for(int i = 0; i < 2; i++) {
						
						token = lexer.nextToken();
						
						if(token.getType() == TokenType.VARIABLE)
							el = new ElementVariable((String)token.getValue());
						else if(token.getType() == TokenType.INTEGER)
							el = new ElementConstantInteger((int)token.getValue());
						else if(token.getType() == TokenType.DOUBLE)
							el = new ElementConstantDouble((double)token.getValue());
						else if(token.getType() == TokenType.STRING)
							el  = new ElementString((String)token.getValue());
						else 
							throw new SmartScriptParserException("Cannot have anything in for loop that is not a varable, a number or string.");
						
						elements[index++] = el;
					}
					
					//sad kad smo dobili dva elementa koje for loop mora imati provjerimo ima li možda i četvrtog
					//ako ne stvaramo novi ForLoopNode te stavljamo da je zadnji element null
					
					token = lexer.nextToken();
					ForLoopNode node;
					
					if(token.getType() == TokenType.TAGCLOSE) {
						node = new ForLoopNode((ElementVariable)elements[0], elements[1], elements[2], null);
					} else {
						if(token.getType() == TokenType.VARIABLE)
							el = new ElementVariable((String)token.getValue());
						else if(token.getType() == TokenType.INTEGER)
							el = new ElementConstantInteger((int)token.getValue());
						else if(token.getType() == TokenType.DOUBLE)
							el = new ElementConstantDouble((double)token.getValue());
						else if(token.getType() == TokenType.STRING)
							el  = new ElementString((String)token.getValue());
						else 
							throw new SmartScriptParserException("Cannot have anything in for loop that is not a varable, a number or string");
						
						token = lexer.nextToken();
						
						if(token.getType() != TokenType.TAGCLOSE)
							throw new SmartScriptParserException("Invalid for loop.");
						
						elements[index++] = el;
						node = new ForLoopNode((ElementVariable)elements[0], elements[1], elements[2], elements[3]);
					}
					
					//ForLoopNode postaje dijete trenutnog Node-a na vrhu stoga te onda i sam ForLoopNode dodajemo na vrh stoga
					Node currentTop = null;
					try {
						currentTop = (Node)stack.peek();
					} catch(Exception e) {
						throw new SmartScriptParserException("Cannot peek element from stack.");
					}
					
					currentTop.addChildNode(node);
					
					try {
						stack.push(node);
					} catch (Exception e) {
						throw new SmartScriptParserException("Cannot push element to stack.");
					}
					
					lexer.setState(LexerState.BASIC);
					token = lexer.nextToken();
				}	else  { //riječ je o ECHONODE
					
					//kolekcija u koju stavljamo elemente taga
					ArrayIndexedCollection elements = new ArrayIndexedCollection();
					
					Element first = null;
					
					//prvo mora ići varijabla
					if(token.getType() != TokenType.VARIABLE) {
						throw new SmartScriptParserException("Invalid tag name.");
					}
					
					first = new ElementVariable((String)token.getValue());
	
					elements.add(first);
					
					token = lexer.nextToken();
					
					//vrtimo petlju dokle god lexer vraća odgovarajuće tipove
					while(true) {
						
						Element el = null;
						if(token.getType() == TokenType.DOUBLE)
							el = new ElementConstantDouble((double)token.getValue());
						else if(token.getType() == TokenType.INTEGER)
							el = new ElementConstantInteger((int)token.getValue());
						else if(token.getType() == TokenType.FUNCTION)
							el = new ElementFunction((String)token.getValue());
						else if(token.getType() == TokenType.SYMBOL)
							el = new ElementOperator((String)token.getValue());
						else if(token.getType() == TokenType.STRING)
							el = new ElementString((String)token.getValue());
						else if(token.getType() == TokenType.VARIABLE)
							el = new ElementVariable((String)token.getValue());
						else 
							break;
							
						elements.add(el);
						token = lexer.nextToken();	
					}
					
					//došli smo do kraja ECHONODE te radimo provjeru je li ispravno zatvoren
					if(token.getType() != TokenType.TAGCLOSE) {
						throw new SmartScriptParserException("Invalid ECHONODE.");
					}
					
					Element[] resultArray = new Element[elements.size()];
					
					//prebacujemo sve elemente iz ArrayIndexedCollection u polje
					elements.forEach(new Processor() {
						
						int index = 0;
						@Override
						public void process(Object value) {
							resultArray[index++] = (Element) value;
						}
					});
					
					//novi echonode moramo staviti kao dijete trenutnom node-u koji je na vrhu stoga
					EchoNode node = new EchoNode(resultArray);
					
					Node currentTop = null;
					try {
						currentTop = (Node)stack.peek();
					} catch(Exception e) {
						throw new SmartScriptParserException("Cannot peek element from stack.");
					}
					
					
					currentTop.addChildNode(node);
					lexer.setState(LexerState.BASIC);
					token = lexer.nextToken();
				} 		
			}
		}
		//nakon što smo prošli cijeli ulazni niz na stogu se smije nalaziti samo root node, tj. DocumentNode
		if(stack.size() != 1) 
			throw new SmartScriptParserException("Wrong number of END tags.");
		
	}
}
