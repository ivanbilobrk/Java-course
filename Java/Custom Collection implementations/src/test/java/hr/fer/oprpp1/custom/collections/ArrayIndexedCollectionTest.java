package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
/**
 * Tests for ArrayIndexedCollection class
 * @author Ivan Bilobrk
 * @version 1.0
 */

public class ArrayIndexedCollectionTest{
	
	/**
	 * Checks functionality of constructor if initial capacity of new collection is 0.
	 */
	@Test
	public void testConstructorWithIllegalArgumentSize() {
		assertThrows(IllegalArgumentException.class, () -> {
			ArrayIndexedCollection a = new ArrayIndexedCollection(0);	//očekivamo da će ovakav poziv instruktora izazvati IllegalArgumentException
		});
	}
	
	/**
	 * Checks functionality of constructor if other collection which you want to copy to new collection is null.
	 */
	@Test
	public void testConstructorWithNullPointer() {
		assertThrows(NullPointerException.class, () -> {
			ArrayIndexedCollection a = new ArrayIndexedCollection(null);  //očekivamo da će ovakav poziv instruktora izazvati NullPointerException
		});
	}
	
	/**
	 * Checks functionality of constructor if other collection which you want to copy to new collection is null.
	 */
	@Test
	public void testConstructorWithNullPointer2() {
		assertThrows(NullPointerException.class, () -> {
			ArrayIndexedCollection a = new ArrayIndexedCollection(null, 0); //očekivamo da će ovakav poziv instruktora izazvati NullPointerException
		});
	}
	
	/**
	 * Checks if you can add null element to the collection.
	 */
	@Test
	public void testAddNull() {
		assertThrows(NullPointerException.class, () -> {
			ArrayIndexedCollection a = new ArrayIndexedCollection();	//dodavanjem null elementa očekujemo NullPointerException
			a.add(null);
		});
	}
	
	/**
	 * Checks if toArray method works as intended. Also if toArray method works that means that add also wokrs.
	 */
	@Test
	public void testToArrayAndAdd() {
		Object[] expected = new Object[] {20, "string", 2.33};
		
		ArrayIndexedCollection a = new ArrayIndexedCollection();	//metodu toArray testiramo tako da polje expected usporedimo s poljem 
		a.add(20);													//kojeg vrati metoda toArray
		a.add("string");											//testiranjem metode toArray smo provjerili i metodu add jer smo elemente prvo
		a.add(2.33);												//morali dodati u kolekciju
		
		Object[] result = a.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if get method throws IndexOutOfBoundsException if given argument is too small.
	 */
	@Test
	public void testGetTooSmallIndex() {
		assertThrows(IndexOutOfBoundsException.class, () -> {
			ArrayIndexedCollection a = new ArrayIndexedCollection();
			a.add(20);
			a.add("string");
			a.add(2.33);
			a.get(-1);			//očekujemo da će ovakav poziv izazvati IndexOutOfBoundsException
		});
	}
	
	/**
	 * Checks if get method throws IndexOutOfBoundsException if given argument is too big.
	 */
	@Test 
	public void testGetTooBigIndex() {
		assertThrows(IndexOutOfBoundsException.class, () -> {
			ArrayIndexedCollection a = new ArrayIndexedCollection();
			a.add(20);
			a.get(1);		//očekujemo da će ovakav poziv izazvati IndexOutOfBoundsException
		});
	}
	
	/**
	 * Checks if method get which accepts integer works as intended.
	 */
	@Test 
	public void testGet() {
		ArrayIndexedCollection a = new ArrayIndexedCollection();		//testiramo get metodu tako da prvo dodamo elemente u kolekciju 
		a.add(20);														//te ih onda probamo dobiti sa nama poznatih pozicija
		a.add("string");
		a.add(2.33);
		
		Object[] expected = new Object[] {20, "string", 2.33};
		Object[] result = new Object[3];
		
		result[0] = a.get(0);
		result[1] = a.get(1);
		result[2] = a.get(2);
		
		assertArrayEquals(expected, result);
	}
	
	
	/**
	 * Checks if method forEach works by going through all elements and adding them in new array then compares new array with the expected result.
	 */
	@Test
	public void testForEach() {
		Object[] expected = new Object[] {20, "string", 2.33};
		Object[] result = new Object[3];
		
		ArrayIndexedCollection a = new ArrayIndexedCollection();		//metodu forEach testiramo tako da napravimo TestProcessor klasu koja  
		a.add(20);														//nasljeđuje klasu Processor te koja u svojoj metodi process svaki element 
		a.add("string");												//kojeg primi kao argument sprema u novo polje
		a.add(2.33);													//novo polje na kraju možemo usporediti s poljem expected
		
		class TestProcessor extends Processor{
			int index = 0;
			public void process(Object value) {
				result[index++] = value;
			}
		}
		
		a.forEach(new TestProcessor());
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if size is adjusted when using clear method and if all elements are removed.
	 */
	@Test
	public void testClear() {
		ArrayIndexedCollection a = new ArrayIndexedCollection();
		
		a.add(20);									//brisanje svih elemenata kolekcije testiramo tako što provjerimo je li duljina polja kojeg vrati
		a.add("string");							//metoda toArray nula, ovdje se možemo osloniti na metodu toArray jer je ona već testirana
		a.add(2.33);
		a.clear();
		
		Object[] result = a.toArray();
	
		assertEquals(0, result.length);
	}
	
	/**
	 * Checks if method remove which accepts an object removes element from the beginning correctly.
	 */
	@Test
	public void testRemoveValueBeginning() {
		Object[] expected = new Object[] {"string", 2.33};
		
		ArrayIndexedCollection a = new ArrayIndexedCollection();
		a.add(20);
		a.add("string");
		a.add(2.33);								//provjeravamo metodu remove koja prima objekt je li ispravno briše element sa početka 
		a.remove(Integer.valueOf(20));				//tako da se oslonimo na metodu toArray i usporedimo polje expected i result
		
		Object[] result = a.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method remove which accepts an object removes element from the end correctly.
	 */
	@Test
	public void testRemoveValueEnd() {
		Object[] expected = new Object[] {20, "string"};
		
		ArrayIndexedCollection a = new ArrayIndexedCollection();	//provjeravamo metodu remove koja prima objekt je li ispravno briše element sa kraja 
		a.add(20);													//tako da se oslonimo na metodu toArray i usporedimo polje expected i result
		a.add("string");
		a.add(2.33);
		a.remove(2.33);
		
		Object[] result = a.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method remove which accepts an object removes element from the middle.
	 */
	@Test
	public void testRemoveValueMiddle() {
		Object[] expected = new Object[] {20, 2.33};
		
		ArrayIndexedCollection a = new ArrayIndexedCollection();	//provjeravamo metodu remove koja prima objekt je li ispravno briše element iz sredine
		a.add(20);													//tako da se oslonimo na metodu toArray i usporedimo polje expected i result
		a.add("string");
		a.add(2.33);
		a.remove("string");
		
		Object[] result = a.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method remove which accepts integer index removes element from the beginning.
	 */
	@Test
	public void testRemoveIndexBeginning() {
		Object[] expected = new Object[] {"string", 2.33};
		
		ArrayIndexedCollection a = new ArrayIndexedCollection();	//provjeravamo metodu remove koja prima index je li ispravno briše element sa početka
		a.add(20);													//tako da se oslonimo na metodu toArray i usporedimo polje expected i result
		a.add("string");
		a.add(2.33);
		a.remove(0);
		
		Object[] result = a.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method remove which accepts integer index removes element from the end.
	 */
	@Test
	public void testRemoveIndexEnd() {
		Object[] expected = new Object[] {20, "string"};
		
		ArrayIndexedCollection a = new ArrayIndexedCollection();	//provjeravamo metodu remove koja prima index je li ispravno briše element sa kraja
		a.add(20);													//tako da se oslonimo na metodu toArray i usporedimo polje expected i result
		a.add("string");
		a.add(2.33);
		a.remove(2);
		
		Object[] result = a.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method remove which accepts integer index removes element from the middle.
	 */
	@Test
	public void testRemoveIndexMiddle() {
		Object[] expected = new Object[] {20, 2.33};
		
		ArrayIndexedCollection a = new ArrayIndexedCollection();	//provjeravamo metodu remove koja prima index je li ispravno briše element iz sredine
		a.add(20);													//tako da se oslonimo na metodu toArray i usporedimo polje expected i result
		a.add("string");
		a.add(2.33);
		a.remove(1);
		
		Object[] result = a.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks method insert if it throws NullPointerException when given null value as an argument.
	 */
	@Test
	public void insertNull() {
		assertThrows(NullPointerException.class, () -> {
			ArrayIndexedCollection a = new ArrayIndexedCollection();
			a.insert(null, 0);					//metodu insert testiramo baci li NullPointerException ako probamo ubaciti null vrijednost u kolekciju
		});
	}
	
	/**
	 * Checks method insert if it throws IndexOutOfBoundsException when given wrong index as an argument.
	 */
	@Test
	public void insertAtWrongIndex() {
		assertThrows(IndexOutOfBoundsException.class, () -> {
			ArrayIndexedCollection a = new ArrayIndexedCollection();
			a.add(1);				//testiramo baca li metoda insert IndexOutOfBoundsException ako probamo dodati element na krivu poziciju u kolekciji
			a.insert(23, 2);
		});
	}
	
	/**
	 * Checks if method insert which accepts integer index and object inserts element correctly at the beginning.
	 */
	@Test
	public void testInsertAtBeginning() {
		Object[] expected = new Object[] {20, "string",2.33};
		
		ArrayIndexedCollection a = new ArrayIndexedCollection();	//insert na početak kolekcije testiramo oslanjajući se na metodu toArray te 
		a.add("string");											//uspoređujemo polje expected sa poljem koje vrati metoda toArray
		a.add(2.33);
		a.insert(20, 0);
		
		Object[] result = a.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method insert which accepts integer index and object inserts element correctly at the end.
	 */
	@Test
	public void testInsertAtEnd() {
		Object[] expected = new Object[] {20, "string",2.33};
		
		ArrayIndexedCollection a = new ArrayIndexedCollection();	//insert na kraj kolekcije testiramo oslanjajući se na metodu toArray te 
		a.add(20);													//uspoređujemo polje expected sa poljem koje vrati metoda toArray
		a.add("string");
		a.insert(2.33, 2);
		
		Object[] result = a.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method insert which accepts integer index and object inserts element correctly in the middle.
	 */
	@Test
	public void testInsertAtMiddle() {
		Object[] expected = new Object[] {20, "string",2.33};
		
		ArrayIndexedCollection a = new ArrayIndexedCollection();	//insert u sredinu kolekcije testiramo oslanjajući se na metodu toArray te 
		a.add(20);													//uspoređujemo polje expected sa poljem koje vrati metoda toArray
		a.add(2.33);
		a.insert("string", 1);
		
		Object[] result = a.toArray();
		
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method indexOf returns correct integer index for desired elements in collection.
	 */
	@Test
	public void testIndexOf() {
		int[] expected = new int[] {0,1,1,0};					//metodu indexOf testiramo tako da ju nekoliko puta pozovemo te sve rezultate spremimo
		int[] result = new int[4];								//u polje, a zatim to polje usporedimo sa poljem expected za koje znamo da ima 
		ArrayIndexedCollection a = new ArrayIndexedCollection();//ispravne vrijednosti indexa
		
		a.add(20);
		result[0] = a.indexOf(Integer.valueOf(20));
		
		a.add("string");
		result[1] = a.indexOf("string");
		
		a.insert(2.33, 1);
		result[2] = a.indexOf(2.33);
		
		result[3] = a.indexOf(20);
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if method size works correctly after add, insert, clear and remove.
	 */
	@Test
	public void testSize() {
		int[] expected = new int[] {1,3,2,1,2,0};				//metodu size testiramo u nekoliko slučajeva te rezultate spremamo u polje koje na kraju
		int[] result = new int[6];								//usporedimo sa poljem expected za koje znamo da ima ispravne vrijednosti size
		ArrayIndexedCollection a = new ArrayIndexedCollection();
		a.add(20);
		result[0] = a.size();
		a.add("string");
		a.add(2.33);
		result[1] = a.size();
		a.remove(0);
		result[2] = a.size();
		a.remove("string");
		result[3] = a.size();
		a.insert(30, 1);
		result[4] = a.size();
		a.clear();
		result[5] = a.size();
		assertArrayEquals(expected, result);
	}
	
	/**
	 * Checks if contains method works correctly after add, insert, remove and for some values that not exist in collection.
	 */
	@Test
	public void testContains() {
		boolean[] expected = new boolean[] {true, true, false, false};	//metodu contains testiramo tako da prvo dodamo neke elemente, zatim obrišemo
		boolean[] result = new boolean[4];								//te nakon toga pozovemo contains za neke vrijednosti 
																		//za neke očekujemo da postoje u kolekciji, a neke ne 
		ArrayIndexedCollection a = new ArrayIndexedCollection();		//sve rezultate spremamo u polje result koje na kraju usporedimo sa poljem result
		a.add(20);
		a.add(2.33);
		a.insert("string", 1);
		
		result[0] = a.contains("string");
		result[1] = a.contains(Integer.valueOf(20));
		result[2] = a.contains("not here");
		a.remove(1);
		result[3] = a.contains("string");
	}
	
	/**
	 * Checks if method removes which accepts index throws IndexOutOfBoundsException if given wrong value of index.
	 */
	@Test 
	public void removeWrongIndex() {
		ArrayIndexedCollection a = new ArrayIndexedCollection();	//testiramo baca li metoda remove iznimku IndexOutOfBoundsException
		a.add(20);													//ako predamo krivi index
		a.add(2.33);
		a.insert("string", 1);
		
		assertThrows(IndexOutOfBoundsException.class, () -> {
			a.remove(3);
		});
	}
	
}
	