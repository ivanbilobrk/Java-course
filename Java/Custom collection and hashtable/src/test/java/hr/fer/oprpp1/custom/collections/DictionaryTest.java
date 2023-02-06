package hr.fer.oprpp1.custom.collections;

/**
 * Tests for Dictionary class.
 * @author Ivan Bilobrk
 */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DictionaryTest {
	
	@Test
	public void testPutWithNull() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		
		assertThrows(NullPointerException.class, ()->{
			dict.put(null, "string");
		});
	}
	
	@Test
	public void testPutGet1() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		
		dict.put(1, "prvi");
		
		assertEquals("prvi", dict.get(1));
		assertEquals(1, dict.size());
		assertEquals(false, dict.isEmpty());
	}
	
	@Test
	public void testPutGet2() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		
		dict.put(1, "prvi");
		dict.put(2, "drugi");
		dict.put(3, "treci");
		dict.put(4, "cetvrti");
		
		//stavljamo vrijednost peti na već postojeći ključ
		dict.put(1, "peti");
		
		assertEquals("peti", dict.get(1));
		assertEquals("treci", dict.get(3));
		assertEquals("cetvrti", dict.get(4));
		assertEquals("drugi", dict.get(2));
		assertEquals(4, dict.size());
		assertEquals(false, dict.isEmpty());
	}
	
	@Test
	public void testPutGet3() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		
		dict.put(1, "prvi");
		dict.put(2, "drugi");
		dict.put(3, "treci");
		dict.put(4, "cetvrti");
		dict.put(1, "peti");
		
		assertEquals(null, dict.get("nepostojeci"));
		assertEquals(null, dict.get(null));
		assertEquals(null, dict.get(22));
		assertEquals(false, dict.isEmpty());
		assertEquals(4, dict.size());
	}
	
	@Test
	public void testRemove() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		
		dict.put(1, "prvi");
		dict.put(2, "drugi");
		dict.put(3, "treci");
		dict.put(4, "cetvrti");
		dict.put(1, "peti");
		
		dict.remove(1);
		assertEquals(null, dict.get(1));
		assertEquals(3, dict.size());
		assertEquals(false, dict.isEmpty());
	}
	
	@Test
	public void testClear() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		
		dict.put(1, "prvi");
		dict.put(2, "drugi");
		dict.put(3, "treci");
		dict.put(4, "cetvrti");
		dict.put(1, "peti");
		
		dict.clear();
		assertEquals(0, dict.size());
		assertEquals(null, dict.get(1));
		assertEquals(null, dict.get(2));
		assertEquals(null, dict.get(3));
		assertEquals(null, dict.get(4));
		assertEquals(true, dict.isEmpty());
	}
	
	@Test
	public void testRemove2() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		
		dict.put(1, "prvi");
		dict.put(2, "drugi");
		dict.put(3, "treci");
		dict.put(4, "cetvrti");
		dict.put(1, "peti");
		
		dict.remove(1);
		dict.remove(2);
		dict.remove(3);
		dict.remove(4);
		
		assertEquals(0, dict.size());
		assertEquals(null, dict.get(1));
		assertEquals(null, dict.get(2));
		assertEquals(null, dict.get(3));
		assertEquals(null, dict.get(4));
		assertEquals(true, dict.isEmpty());
	}
}
