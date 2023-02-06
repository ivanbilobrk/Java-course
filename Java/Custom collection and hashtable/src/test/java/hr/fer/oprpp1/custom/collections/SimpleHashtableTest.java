package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.collections.SimpleHashtable.TableEntry;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * Tests for Dictionary class.
 * @author Ivan Bilobrk
 */
public class SimpleHashtableTest {
	
	@Test
	public void testConstructor() {
		
		assertThrows(IllegalArgumentException.class, ()->{
			SimpleHashtable<Integer, String> map = new SimpleHashtable<>(0);
		});
	}
	
	@Test
	public void testPutGet1() {
		//metoda isto tako provjerava radi li se ispravno uvećavanje internog polja
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		
		assertEquals(5, examMarks.get("Ivana"));
		assertEquals(2, examMarks.get("Ante"));
		assertEquals(2, examMarks.get("Jasna"));
		assertEquals(5, examMarks.get("Kristina"));
		assertEquals(4, examMarks.size());
	}
	
	@Test
	public void testPutGet2() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		
		assertThrows(NullPointerException.class, ()->{
			examMarks.put(null, 3);
		});
	}
	
	@Test
	public void testPutGet3() {
		//metoda isto tako provjerava radi li se ispravno uvećavanje internog polja
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", null); // overwrites old grade for Ivana
		examMarks.put("Mate", null);
				
		assertEquals(null, examMarks.get("Ivana"));
		assertEquals(2, examMarks.get("Ante"));
		assertEquals(2, examMarks.get("Jasna"));
		assertEquals(5, examMarks.get("Kristina"));
		assertEquals(null, examMarks.get("Mate"));
		assertEquals(5, examMarks.size());
	}
	
	@Test
	public void testContainsKey() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", null); // overwrites old grade for Ivana
		examMarks.put("Mate", null);
		
		assertEquals(true, examMarks.containsKey("Ivana"));
		assertEquals(false, examMarks.containsKey("ne postoji"));
		assertEquals(false, examMarks.containsKey(null));
		assertEquals(5, examMarks.size());
	}
	
	@Test
	public void testContainsValue() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", null); // overwrites old grade for Ivana
		examMarks.put("Mate", null);
		
		assertEquals(true, examMarks.containsValue(5));
		assertEquals(false, examMarks.containsValue("ne postoji"));
		assertEquals(true, examMarks.containsValue(null));
		assertEquals(5, examMarks.size());
	}
	
	@Test
	public void testRemove() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", null); // overwrites old grade for Ivana
		
		examMarks.remove("Ante");
		TableEntry<String, Integer>[] mapArray = examMarks.toArray();
		
		assertEquals(false, examMarks.containsKey("Ante"));
		assertEquals(3, mapArray.length);
		assertEquals(false, examMarks.containsKey("Ante"));
		assertEquals(3, examMarks.size());
	}
	
	@Test
	public void testRemove2() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", null); // overwrites old grade for Ivana
		
		examMarks.remove("Ivana");
		examMarks.remove("Jasna");
		examMarks.remove("Kristina");
		
		TableEntry<String, Integer>[] mapArray = examMarks.toArray();
		
		assertEquals(1, mapArray.length);
		assertEquals("Ante", mapArray[0].getKey());
		assertEquals(2, mapArray[0].getValue());
		assertEquals(false, examMarks.containsKey("Ivana"));
		assertEquals(false, examMarks.containsKey("Jasna"));
		assertEquals(false, examMarks.containsKey("Kristina"));
		assertEquals(1, examMarks.size());
	}
	
	@Test
	public void testClear() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", null); // overwrites old grade for Ivana
		
		examMarks.clear();
		
		TableEntry<String, Integer>[] mapArray = examMarks.toArray();
		
		assertEquals(0, mapArray.length);
		assertEquals(0, examMarks.size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testIterator1() {
		
		SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		
		TableEntry<String, Integer>[] mapArray = (TableEntry<String, Integer>[]) new TableEntry[4];
		
		int index = 0;
		
		for(SimpleHashtable.TableEntry<String,Integer> pair : examMarks) {
			mapArray[index++] = pair;
		}
		
		assertEquals(4, index);
		
		assertEquals("Ante", mapArray[0].getKey());
		assertEquals("Ivana", mapArray[1].getKey());
		assertEquals("Jasna", mapArray[2].getKey());
		assertEquals("Kristina", mapArray[3].getKey());
		
		assertEquals(2, mapArray[0].getValue());
		assertEquals(5, mapArray[1].getValue());
		assertEquals(2, mapArray[2].getValue());
		assertEquals(5, mapArray[3].getValue());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testIterator2() {
		
		SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		
		TableEntry<String, Integer>[] mapArray = (TableEntry<String, Integer>[]) new TableEntry[4];
		
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
		
		while(iter.hasNext()) {
			SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
			if(pair.getKey().equals("Ivana")) {
				iter.remove(); // sam iterator kontrolirano uklanja trenutni element
			}
		}
		
		int index = 0;
		
		for(SimpleHashtable.TableEntry<String,Integer> pair : examMarks) {
			mapArray[index++] = pair;
		}
		
		assertEquals(3, index);
		
		assertEquals("Ante", mapArray[0].getKey());
		assertEquals("Jasna", mapArray[1].getKey());
		assertEquals("Kristina", mapArray[2].getKey());
		
		assertEquals(2, mapArray[0].getValue());
		assertEquals(2, mapArray[1].getValue());
		assertEquals(5, mapArray[2].getValue());
		
		assertEquals(3, examMarks.size());
	}
	
	@Test
	public void testIterator3() {
		
		SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
		
		assertThrows(IllegalStateException.class, ()->{
			
			while(iter.hasNext()) {
				
				SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
				
				if(pair.getKey().equals("Ivana")) {
					iter.remove();
					iter.remove();	//pogrešna upotreba iteratorove metode remove
				}
			}
		});
	}
	
	@Test
	public void testIterator4() {
		
		SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
		
		assertThrows(ConcurrentModificationException.class, ()->{
			
			while(iter.hasNext()) {
				
				SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
				
				if(pair.getKey().equals("Ivana")) {
					examMarks.remove("Ivana");	//mijenjamo strukturu mape izvana
				}
			}
		});
	}
	
	@Test
	public void testIterator5() {
		
		SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
		
		while(iter.hasNext()) {
			iter.next();
			iter.remove();
		}
		
		assertEquals(0, examMarks.size());
		assertEquals(0, examMarks.toArray().length);
	}
	
}
