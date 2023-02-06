package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests for LinkedListIndexedCollection class
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class LinkedListIndexedCollectionTest {
	
	/**
	 * Checks functionality of constructor if other collection which you want to copy to new collection is null.
	 */
	@Test
	public void testConstructor() {
		assertThrows(NullPointerException.class, () -> {
			LinkedListIndexedCollection l = new LinkedListIndexedCollection(null);	//očekivamo da će ovakav poziv instruktora izazvati NullPointerException
		});
	}
	
	/**
	 * Checks if you can add null element to the collection.
	 */
	@Test
	public void testAddNull() {
		assertThrows(NullPointerException.class, () -> {
			LinkedListIndexedCollection l = new LinkedListIndexedCollection();	//dodavanjem null elementa očekujemo NullPointerException
			l.add(null);
		});
	}
	
	/**
	 * Checks if toArray method works as intended. Also if toArray method works that means that add also wokrs.
	 */
	@Test
	public void testToArrayAndAdd() {
		Object[] expected = new Object[] {20, "string", 2.33};
		
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();	//metodu toArray testiramo tako da polje expected usporedimo s poljem
		l.add(20);															//kojeg vrati metoda toArray
		l.add("string");													//testiranjem metode toArray smo provjerili i metodu add jer smo elemente prvo
		l.add(2.33);														//morali dodati u kolekciju
		
		Object[] result = l.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if get method throws IndexOutOfBoundsException if given argument is too small.
	 */
	@Test
	public void testGetTooSmallIndex() {
		assertThrows(IndexOutOfBoundsException.class, () -> {
			LinkedListIndexedCollection l = new LinkedListIndexedCollection();
			l.add(20);
			l.add("string");
			l.add(2.33);
			l.get(-1);	//očekujemo da će ovakav poziv izazvati IndexOutOfBoundsException
		});
	}
	
	/**
	 * Checks if get method throws IndexOutOfBoundsException if given argument is too big.
	 */
	@Test
	public void testGetTooBigIndex() {
		assertThrows(IndexOutOfBoundsException.class, () -> {
			LinkedListIndexedCollection l = new LinkedListIndexedCollection();
			l.add(20);
			l.add("string");
			l.add(2.33);
			l.get(3);			//očekujemo da će ovakav poziv izazvati IndexOutOfBoundsException
		});
	}
	
	/**
	 * Checks if method get which accepts integer works as intended.
	 */
	@Test 
	public void testGet() {
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();	//testiramo get metodu tako da prvo dodamo elemente u kolekciju 
		l.add(20);															//te ih onda probamo dobiti sa nama poznatih pozicija
		l.add("string");
		l.add(2.33);
		
		Object[] expected = new Object[] {20, "string", 2.33};
		Object[] result = new Object[3];
		
		result[0] = l.get(0);
		result[1] = l.get(1);
		result[2] = l.get(2);
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method forEach works by going through all elements and adding them in new array then compares new array with the expected result.
	 */
	@Test
	public void testForEach() {
		Object[] expected = new Object[] {20, "string", 2.33};
		Object[] result = new Object[3];
		
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();	//metodu forEach testiramo tako da napravimo TestProcessor klasu koja 
		l.add(20);															//nasljeđuje klasu Processor te koja u svojoj metodi process svaki element
		l.add("string");													//kojeg primi kao argument sprema u novo polje
		l.add(2.33);														//novo polje na kraju možemo usporediti s poljem expected
		
		class TestProcessor extends Processor{
			int index = 0;
			public void process(Object value) {
				result[index++] = value;
			}
		}
		
		l.forEach(new TestProcessor());
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if size is adjusted when using clear method and if all elements are removed.
	 */
	@Test
	public void testClear() {
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();
		
		l.add(20);
		l.add("string");			//brisanje svih elemenata kolekcije testiramo tako što provjerimo je li duljina polja kojeg vrati
		l.add(2.33);				//metoda toArray nula, ovdje se možemo osloniti na metodu toArray jer je ona već testirana
		l.clear();
		
		Object[] result = l.toArray();
	
		assertEquals(0, result.length);
	}
	
	/**
	 * Checks if method remove which accepts an object removes element from the beginning correctly.
	 */
	@Test
	public void testRemoveValueBeginning() {
		Object[] expected = new Object[] {"string", 2.33};
		
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();
		l.add(20);
		l.add("string");										//provjeravamo metodu remove koja prima objekt je li ispravno briše element sa početka
		l.add(2.33);											//tako da se oslonimo na metodu toArray i usporedimo polje expected i result
		l.remove(Integer.valueOf(20));
		
		Object[] result = l.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method remove which accepts an object removes element from the end correctly.
	 */
	@Test
	public void testRemoveValueEnd() {
		Object[] expected = new Object[] {20, "string"};
		
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();
		l.add(20);							//provjeravamo metodu remove koja prima objekt je li ispravno briše element sa kraja 
		l.add("string");					//tako da se oslonimo na metodu toArray i usporedimo polje expected i result
		l.add(2.33);	
		l.remove(2.33);
		
		Object[] result = l.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method remove which accepts an object removes element from the middle.
	 */
	@Test
	public void testRemoveValueMiddle() {
		Object[] expected = new Object[] {20, 2.33};
		
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();
		l.add(20);
		l.add("string");						//provjeravamo metodu remove koja prima objekt je li ispravno briše element iz sredine
		l.add(2.33);							//tako da se oslonimo na metodu toArray i usporedimo polje expected i result
		l.remove("string");
		
		Object[] result = l.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method remove which accepts integer index removes element from the beginning.
	 */
	@Test
	public void testRemoveIndexBeginning() {
		Object[] expected = new Object[] {"string", 2.33};
		
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();
		l.add(20);
		l.add("string");							//provjeravamo metodu remove koja prima index je li ispravno briše element sa početka
		l.add(2.33);								//tako da se oslonimo na metodu toArray i usporedimo polje expected i result
		l.remove(0);
		
		Object[] result = l.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method remove which accepts integer index removes element from the end.
	 */
	@Test
	public void testRemoveIndexEnd() {
		Object[] expected = new Object[] {20, "string"};
		
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();
		l.add(20);							//provjeravamo metodu remove koja prima index je li ispravno briše element sa kraja
		l.add("string");					//tako da se oslonimo na metodu toArray i usporedimo polje expected i result
		l.add(2.33);
		l.remove(2);
		
		Object[] result = l.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method remove which accepts integer index removes element from the middle.
	 */
	@Test
	public void testRemoveIndexMiddle() {
		Object[] expected = new Object[] {20, 2.33};
		
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();
		l.add(20);					//provjeravamo metodu remove koja prima index je li ispravno briše element iz sredine
		l.add("string");			//tako da se oslonimo na metodu toArray i usporedimo polje expected i result
		l.add(2.33);
		l.remove(1);
		
		Object[] result = l.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks method insert if it throws NullPointerException when given null value as an argument.
	 */
	@Test
	public void insertNull() {
		assertThrows(NullPointerException.class, () -> {
			LinkedListIndexedCollection l = new LinkedListIndexedCollection();
			l.insert(null, 0);						//metodu insert testiramo baci li NullPointerException ako probamo ubaciti null vrijednost u kolekciju
		});
	}
	
	/**
	 * Checks method insert if it throws IndexOutOfBoundsException when given wrong index as an argument.
	 */
	@Test
	public void insertAtWrongIndex() {
		assertThrows(IndexOutOfBoundsException.class, () -> {
			LinkedListIndexedCollection l = new LinkedListIndexedCollection();
			l.add(1);
			l.insert(23, 2);				//testiramo baca li metoda insert IndexOutOfBoundsException ako probamo dodati element na krivu poziciju u kolekciji
		});
	}
	
	/**
	 * Checks if method insert which accepts integer index and object inserts element correctly at the beginning.
	 */
	@Test
	public void testInsertAtBeginning() {
		Object[] expected = new Object[] {20, "string",2.33};
		
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();
		l.add("string");						//insert na početak kolekcije testiramo oslanjajući se na metodu toArray te 
		l.add(2.33);							//uspoređujemo polje expected sa poljem koje vrati metoda toArray
		l.insert(20, 0);
		
		Object[] result = l.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method insert which accepts integer index and object inserts element correctly at the end.
	 */
	@Test
	public void testInsertAtEnd() {
		Object[] expected = new Object[] {20, "string",2.33};
		
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();
		l.add(20);
		l.add("string");								//insert na kraj kolekcije testiramo oslanjajući se na metodu toArray te 
		l.insert(2.33, 2);								//uspoređujemo polje expected sa poljem koje vrati metoda toArray
		
		Object[] result = l.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method insert which accepts integer index and object inserts element correctly in the middle.
	 */
	@Test
	public void testInsertAtMiddle() {
		Object[] expected = new Object[] {20, "string",2.33};
		
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();
		l.add(20);
		l.add(2.33);						//insert u sredinu kolekcije testiramo oslanjajući se na metodu toArray te 
		l.insert("string", 1);				//uspoređujemo polje expected sa poljem koje vrati metoda toArray
		
		Object[] result = l.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method indexOf returns correct integer index for desired elements in collection.
	 */
	@Test
	public void testIndexOf() {
		int[] expected = new int[] {0,1,1,0};
		int[] result = new int[4];
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();
		
		l.add(20);
		result[0] = l.indexOf(Integer.valueOf(20));	//metodu indexOf testiramo tako da ju nekoliko puta pozovemo te sve rezultate spremimo
													//u polje, a zatim to polje usporedimo sa poljem expected za koje znamo da ima 
		l.add("string");							//ispravne vrijednosti indexa
		result[1] = l.indexOf("string");
		
		l.insert(2.33, 1);
		result[2] = l.indexOf(2.33);
		
		result[3] = l.indexOf(20);
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method size works correctly after add, insert, clear and remove.
	 */
	@Test
	public void testSize() {
		int[] expected = new int[] {1,3,2,1,2,0};
		int[] result = new int[6];
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();
		l.add(20);
		result[0] = l.size();						//metodu size testiramo u nekoliko slučajeva te rezultate spremamo u polje koje na kraju
		l.add("string");							//usporedimo sa poljem expected za koje znamo da ima ispravne vrijednosti size
		l.add(2.33);
		result[1] = l.size();
		l.remove(0);
		result[2] = l.size();
		l.remove("string");
		result[3] = l.size();
		l.insert(30, 1);
		result[4] = l.size();
		l.clear();
		result[5] = l.size();
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if contains method works correctly after add, insert, remove and for some values that not exist in collection.
	 */
	@Test
	public void testContains() {
		boolean[] expected = new boolean[] {true, true, false, false};
		boolean[] result = new boolean[4];
		
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();
		l.add(20);												//metodu contains testiramo tako da prvo dodamo neke elemente, zatim obrišemo
		l.add(2.33);											//te nakon toga pozovemo contains za neke vrijednosti 
		l.insert("string", 1);									//za neke očekujemo da postoje u kolekciji, a neke ne 
																//sve rezultate spremamo u polje result koje na kraju usporedimo sa poljem result
		result[0] = l.contains("string");	
		result[1] = l.contains(Integer.valueOf(20));
		result[2] = l.contains("not here");
		l.remove(1);
		result[3] = l.contains("string");
	}
	
	/**
	 * Checks if method removes which accepts index throws IndexOutOfBoundsException if given wrong value of index.
	 */
	@Test 
	public void removeWrongIndex() {
		LinkedListIndexedCollection l = new LinkedListIndexedCollection();
		l.add(20);
		l.add(2.33);											//testiramo baca li metoda remove iznimku IndexOutOfBoundsException
		l.insert("string", 1);									//ako predamo krivi index
		
		assertThrows(IndexOutOfBoundsException.class, () -> {
			l.remove(3);
		});
	}
	
}
