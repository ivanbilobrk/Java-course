package hr.fer.oprpp1.custom.scripting.lexer;

import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

/**
 * Lexer class is used for tokenization of text.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class Lexer {
		
		/**
		 * Char array which stores all characters from source text.
		 */
		private char[] data; // ulazni tekst
		
		/**
		 * Value of last token.
		 */
		private Token token; // trenutni token
		
		/**
		 * Index of first unread character.
		 */
		private int currentIndex; // indeks prvog neobrađenog znaka
		
		/**
		 * Used to store current LexerState
		 */
		private LexerState state;
		
		/**
		 * Constructor of Lexer.
		 * @param text - text you want to tokenize.
		 */
		public Lexer(String text) {
			data = text.toCharArray();		//pretvaramo dobiveni String u character polje
			currentIndex = 0;				//postavljamo index na početak ulaznog niza
			this.state = LexerState.BASIC;	//Lexer započinje u stanju BASIC
		}
		
		
		/**
		 * Method gets next token from source text and assigns it to the current token value.
		 * @return - next Token from text.
		 * @throws SmartScriptParserException if there is a problem with tokenization.
		 */
		public Token nextToken() { 
			extractNextToken();		//ažuriramo vrijednost trenutnog tokena
			return token;
		}
		
		/**
		 * Method to get current token. Can be called multiple times.
		 * @return - current Token value.
		 */
		public Token getToken() {
			return token;
		}
		
		/**
		 * Method which skips all white spaces starting from current position of Lexer.
		 */
		private void skipBlanks() {
			
			while(currentIndex<data.length) {
				char c = data[currentIndex];
			
				if(c==' ' || c=='\t' || c=='\r' || c=='\n') {	// ako je riječ o praznini samo preskačemo te uvećavamo currentIndex
					currentIndex++;
					continue;
				}
				
				break;
			}
		}
		
		/**
		 * Method which updates current token value
		 * @throws SmartScriptParserException if there is no next token, token cannot be recognized or any other mistake which 
		 * can occur during this phase.
		 */
		private void extractNextToken () {
			
			//provjera jesmo li došli do kraja teksta
			if(token != null && token.getType() == TokenType.EOF) throw new SmartScriptParserException("No more tokens available in text.");
			
			//preskačemo sve praznine
			
			skipBlanks();
			
			//moguće je da nakon preskakanja svih praznina smo došli do kraja teksta
			
			if(currentIndex >= data.length) {
				
				token = new Token(TokenType.EOF, null);
				return;
			}
			
			/*
			 * Provjera jesmo li došli do oznake početka ili kraj taga i ako jesmo generiramo token tipa TAGOPEN odnosno
			 * TAGCLOSE kako bi Parser mogao promjeniti stanje. Ovo radimo neovisno o stanju u kojem se nalazimo
			 */
			
			if(data[currentIndex] == '{') {
				
				if(currentIndex < data.length && data[currentIndex+1] == '$') {
					
					token = new Token(TokenType.TAGOPEN, "{$");
					currentIndex += 2;
					
					return;
				} else {
					
					throw new SmartScriptParserException("Invalid tag declaration.");
				}
			}
			
			if(data[currentIndex] == '$') {
				
				if(currentIndex < data.length && data[currentIndex+1] == '}') {
					
					token = new Token(TokenType.TAGCLOSE, "$}");
					currentIndex += 2;
					
					return;
				} else {
					
					throw new SmartScriptParserException("Invalid tag declaration.");
				}
			}
			
			
			//provjera jesmo li u stanju TAG
			if(state == LexerState.TAG) {
				
				//ako je prvi znak slovo moguće je da se radi o varijabli
				if(Character.isLetter(data[currentIndex])) {
					
					int startIndex = currentIndex;
					++currentIndex;
					
					//dokle god je zadovoljen uvjet da se radi o varijabli uvećavamo currentIndex
					while(Character.isLetter(data[currentIndex]) || data[currentIndex] == '_' || Character.isDigit(data[currentIndex]))
						++currentIndex;
					
					token = new Token(TokenType.VARIABLE, new String(data, startIndex, currentIndex - startIndex));
					
					return;
				}
				
				if(data[currentIndex] == '-') {
					
					//kada uočimo minus treba provjeriti je li riječ o negativnom broju te ako je pozivamo metodu koja služi za 
					//određivanje o kojem se broju radi te koje su mu granice u nizu
					if(currentIndex + 1 < data.length && Character.isDigit(data[currentIndex+1])) {
						
						++currentIndex;
						extractNumber(true);
						
						return;
					} else {
						
						//ako nije broj, radi se o simbolu
						token = new Token(TokenType.SYMBOL, String.valueOf(data[currentIndex++]));
						
						return;
					}
				}
				
				//ako uočimo da je prvi znak broj pozivamo metodu za izlučivanje broja
				if(Character.isDigit(data[currentIndex])) {
					
					extractNumber(false);
					
					return;
				}
				
				//riječ je o imenu funkcije
				if(data[currentIndex] == '@') {
					
					//provjera je li ispravno ime funkcije
					if(currentIndex + 1 < data.length && Character.isLetter(data[currentIndex+1])) {
						
						++currentIndex;
						int startIndex = currentIndex;
						//uvećavamo currentIndex dokle kod je valjano ime funkcije
						while(currentIndex < data.length && (Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex]) || data[currentIndex] == '_'))
							++currentIndex;
						
						token = new Token(TokenType.FUNCTION, new String(data, startIndex, currentIndex - startIndex));
						
						return;
					} else {
						//ako je prvo bio znak @ te nakon njega neispravno ime funkcije bacamo iznimku
						throw new SmartScriptParserException("Invalid function name.");
					}
				}
				
				//provjera jesmo li naišli na token tipa symbol odnosno je li riječ o operatoru
				//ovdje nema provjere za minus jer smo njega provjerili u gornjem if-u zajedno sa provjerom negativnih brojeva
				if(data[currentIndex] == '+' || data[currentIndex] == '*' || data[currentIndex] == '/' || data[currentIndex] == '^') {
					
					token = new Token(TokenType.SYMBOL, String.valueOf(data[currentIndex++]));
					
					return;
				}
				
				//znak jednakosti svrstavam u tokene tipa varijable zbog načina na koji radim provjere za echonode
				if(data[currentIndex] == '=') {
					
					token = new Token(TokenType.VARIABLE, String.valueOf(data[currentIndex++]));
					
					return;
				}
				
				//provjera je li riječ o stringu unutar taga
				if(data[currentIndex] == '\"') {
					
					++currentIndex;
					int startIndex = 0;
					//zbog escape mogućnosti ovdje ćemo pojedinačne znakove stavljati u polje koje mora biti 
					//dovoljne veličine da primi ostatak ulaznog niza
					char[] chars = new char[data.length - currentIndex]; 
																		
					
					//provjera za dvije mogućnosti escape-a u stringu
					while(currentIndex < data.length && data[currentIndex] != '\"') {
						
						if(data[currentIndex] == '\\') {
							
							if(currentIndex+1 < data.length && (data[currentIndex+1] == '\\' || data[currentIndex+1] == '\"'
									|| data[currentIndex+1] == 'n' || data[currentIndex+1] == 'r' || data[currentIndex+1] == 't')) {
								chars[startIndex++] = data[currentIndex];
								chars[startIndex++] = data[currentIndex+1];
								currentIndex += 2;
							} else {
								//ako nakon znaka "\" ne slijedi nijedan nama poznati znak bacamo iznimku
								throw new SmartScriptParserException("Invalid escape in String in a tag.");
							}
						} else {
							chars[startIndex++] = data[currentIndex++];
						}
					}
						
					//provjera je li string ispravno zatvoren znakon "
					if(currentIndex < data.length && data[currentIndex] != '\"')
						throw new SmartScriptParserException("Invalid String declaration.");
					
					token = new Token(TokenType.STRING, new String(chars, 0, startIndex));
					++currentIndex;
					
					return;
				}
				
				//naišli smo na neki nepoznati znak unutar taga te bacamo iznimku
				throw new SmartScriptParserException("Invalid tag declaration.");
				
			} else {
				//nalazimo se u stanju BASIC za text node
				int startIndex = 0;
				//opet koristimo polje jer u text node-u je isto tako moguć escape
				char[] chars = new char[data.length-currentIndex];
				
				while(currentIndex < data.length) {
					
					if(data[currentIndex] == '\\') {
						if(currentIndex+1 < data.length && (data[currentIndex+1] == '{' || data[currentIndex+1] == '\\')) {
							chars[startIndex++] = data[currentIndex];
							chars[startIndex++] = data[currentIndex+1];
							currentIndex += 2;
						} else {
							//ako nakon znaka "\" ne slijedi nijedan nama poznati znak bacamo iznimku
							throw new SmartScriptParserException("Invalid escape in Text tag.");
						}
						
					}   //provjera jesmo li došli do znaka za otvaranje taga
						else if(currentIndex + 1 < data.length && data[currentIndex] == '{' && data[currentIndex+1] == '$') {
						token = new Token(TokenType.TEXTNODE, new String(chars, 0, startIndex));
						
						return;
					} else {
						chars[startIndex++] = data[currentIndex++];
					}
				}
				token = new Token(TokenType.TEXTNODE, new String(chars, 0, startIndex));
				
				return;
			}
			
		}
		
		/**
		 * Private method used for extracting numbers in tags.
		 * @param isNegative - used to indicate if we want to extract a negative or postive number
		 */
		private void extractNumber(boolean isNegative) {
			
			int startIndex = currentIndex;
			boolean decimal = false;
			
			//prelazimo cjelobrojni dio
			while(Character.isDigit(data[currentIndex]) && currentIndex < data.length)
				++currentIndex;
			
			if(currentIndex < data.length && data[currentIndex] == '.') {
				decimal = true;
				++currentIndex;
				
				//prelazimo decimalni dio
				while(Character.isDigit(data[currentIndex]) && currentIndex < data.length)
					++currentIndex;
				
			}
			
			String result = new String(data, startIndex, currentIndex - startIndex);
			
			if(decimal) {
				if(isNegative)
					token = new Token(TokenType.DOUBLE, -Double.valueOf(result));
				else 
					token = new Token(TokenType.DOUBLE, Double.valueOf(result));
			} else {
				if(isNegative)
					token = new Token(TokenType.INTEGER, -Integer.valueOf(result));
				else 
					token = new Token(TokenType.INTEGER, Integer.valueOf(result));
			}
			
			return;
			
		}
		
		/**
		 * Method to change state of Lexer
		 * @param state - represents a state you want to put Lexer in.
		 * @throws SmartScriptParserException - if given state is null.
		 */
		public void setState(LexerState state) {
			if(state == null) throw new SmartScriptParserException("Cannot have null state.");
			
			this.state = state;
		}
		
		/**
		 * Method to get current Lexer State
		 * @return BASIC or TAG Lexer State.
		 */
		public LexerState getState() {
			return this.state;
		}
}
