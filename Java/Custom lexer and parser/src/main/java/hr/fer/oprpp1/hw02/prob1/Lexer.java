package hr.fer.oprpp1.hw02.prob1;

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
		 * @throws LexerException if there is a problem with tokenization
		 * @throws NullPointerException if given text is null.
		 */
		public Lexer(String text) {
			
			if(text == null) throw new NullPointerException("Cannot accept null String as text."); //ako je text null bacimo iznimku
			
			data = text.toCharArray();		//pretvaramo dobiveni String u character polje
			currentIndex = 0;				//postavljamo index na početak ulaznog niza
			this.state = LexerState.BASIC;	//Lexer započinje u stanju BASIC
		}
		
		
		/**
		 * Method gets next token from source text and assigns it to the current token value.
		 * @return - next Token from text.
		 * @throws LexerException if there is a problem with tokenization.
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
		 * @throws LexerException if you ask for another token but there are no more elements in source string
		 * or there is an illegal escape in source text or number in source text cannot be represented as long.
		 */
		private void extractNextToken () {
			
			//provjera jesmo li došli do kraja teksta
			if(token != null && token.getType() == TokenType.EOF) throw new LexerException("No more tokens available in text.");
			
			//preskačemo sve praznine
			
			skipBlanks();
			
			//moguće je da nakon preskakanja svih praznina smo došli do kraja teksta
			
			if(currentIndex >= data.length) {
				token = new Token(TokenType.EOF, null);
				return;
			}
			
			//provjera jesmo li u stanju EXTENDED
			if(state == LexerState.EXTENDED) {
				
				if(data[currentIndex] == '#') {									//ako Lexer trenutno pokazuje # moramo token ažurirati kao da je tipa SYMBOL
					token = new Token(TokenType.SYMBOL, data[currentIndex++]);
					return;
				}
				
				//ako ne čitamo trenutno # token može biti jedino tipa WORD
				int startIndex = currentIndex;
				
				//uvećavamo index sve dok ne naiđemo na neku prazninu ili #
				while(data[currentIndex] != ' ' && 
					  data[currentIndex] != '\t' &&
					  data[currentIndex] != '\r' &&
					  data[currentIndex] != '\n' && 
					  data[currentIndex] != '#') ++currentIndex;
				
				String word = new String(data, startIndex, currentIndex - startIndex);
				token = new Token(TokenType.WORD, word);		//stvaramo novi token
				return;
			}
			
			//ovdje se nalazimo u stanju BASIC 
			
			//prvo provjeravamo je li index trenutno pokazuje na slovo ili escape znak
			if(Character.isLetter(data[currentIndex]) || data[currentIndex] ==  '\\') {
				 
				char[] newToken = new char[data.length-currentIndex];	//character polje koje koristimo za spremanje pojedinih znakova iz ulaznog niza
				int index = 0;
				
				boolean flagEscape = false;
				
				if(data[currentIndex] == '\\') {	//ako odmah na početku novog tokena se nalazi escape znak ažuriramo zastavicu i uvećamo trenutni index
					flagEscape = true;
					++currentIndex;
				}
				
				//provjera nalazi li se escape znak na kraju niza što bi značilo da se escape neispravno koristi
				if(currentIndex >= data.length) throw new LexerException("Invalid escape."); 
				
				while(currentIndex < data.length) {
					if(Character.isLetter(data[currentIndex]) || flagEscape) {
						if(!(data[currentIndex] == '\\' || Character.isDigit(data[currentIndex])) && flagEscape) //provjera je li se iza escape znaka nalazi isključivo drugi escape znak ili brojka
							throw new LexerException("Invalid escape");
						
						flagEscape = false;
						newToken[index++] = data[currentIndex];	//kopiramo znak na poziciji trenutnog indexa u polje 
						++currentIndex;
					} else if(data[currentIndex] == '\\') { //ako je riječ o escape znaku ne kopiramo znak u polje već ga samo preskočimo i ažuriramo zastavicu
						flagEscape = true;
						++currentIndex;
					} else {
						break;
					}
				}
				
				String word = new String(newToken, 0, index);
				
				token = new Token(TokenType.WORD, word);	//ažuriramo trenutni token
				return;
			}
			
			if(Character.isDigit(data[currentIndex])) {		//ako je riječ o brojci uvećavamo trenutni index sve dok čitamo brojke sa ulaza
				int startIndex = currentIndex;
				
				while(currentIndex < data.length && Character.isDigit(data[currentIndex])) ++currentIndex;
				
				String numberString = new String(data, startIndex, currentIndex - startIndex);
				Long number;
				
				try {
					number = Long.parseLong(numberString);	//provjerimo može li se broj prikazati kao long, ako ne baciti će iznimku koju hvatamo i dalje proslijeđujemo svoju iznimku
				} catch (Exception e) {
					throw new LexerException("Cannot parse this number as long.");
				}
				
				token = new Token(TokenType.NUMBER, number);	//ažuriramo trenutni token
				return;
			}
			
			//ako nijedan od gornjih slučajeva nije zadovoljen riječ je o simbolu
			
			token = new Token(TokenType.SYMBOL, data[currentIndex++]);
		}
		
		/**
		 * Method to change state of Lexer
		 * @param state - represents a state you want to put Lexer in.
		 * @throws NullPointerException - if given state is null.
		 */
		public void setState(LexerState state) {
			if(state == null) throw new NullPointerException("Cannot have null state.");
			
			this.state = state;
		}
}
