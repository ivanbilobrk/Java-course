package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class implementing hash table to store data.
 * @author Ivan Bilobrk
 * @version 1.0
 * @param <K> type of Key
 * @param <V> type of value.
 */
public class SimpleHashtable<K,V> implements Iterable<SimpleHashtable.TableEntry<K, V>>{
	
	/**
	 * Variable representing current amount of entries in hash table.
	 */
	private int size;
	
	/**
	 * Internal array used for storing references to first element in one slot.
	 */
	private TableEntry<K, V>[] table;
	
	/**
	 * Variable holding current number of modifications.
	 */
	private int modificationCount;
	
	/**
	 * Default constructor which creates a new hash table with initial capacity of 16.
	 */
	public SimpleHashtable() {
		this(16);
	}
	
	/**
	 * Constructor which creates new hash table with desired initial capacity.
	 * @param capacity - desired initial number of slots in hash table. This number must be a whole integer representing the power of number
	 * 2 if not than the closest power of two is chosen so that it is bigger than or equal to the given argument.
	 * @throws IllegalArgumentException if capacity is smaller than 1.
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if(capacity < 1) throw new IllegalArgumentException("Initial size of hash table cannot be null.");
		
		int initialCapacity =  (int)Math.pow(2, (int)Math.ceil(Math.log(capacity)/Math.log(2)));
		
		table = (TableEntry<K,V>[]) new TableEntry[initialCapacity];
		this.size = 0;
	}
	
	
	/**
	 * Public nested static class representing one entry in hash table.
	 * @author Ivan Bilobrk
	 * @version 1.0
	 * @param <K> - desired type of key
	 * @param <V> - desired type of value.
	 */
	public static class TableEntry<K,V>{
		
		/**
		 * Key for one entry.
		 */
		private K key;
		
		/**
		 * Value mapped to the key.
		 */
		private V value;
		
		/**
		 * If more elements have the same key hash than overflow occurs and those elements with the same key hash are stored 
		 * like a linked list so this is a reference to the next entry in the same slot.
		 */
		private TableEntry<K, V> next;
		
		/**
		 * Constructor.
		 * @param key is a unique parameter in hash table used to map one value to it and cannot be null
		 * @param value mapped to the given key, can be null
		 * @param next is a reference to the next element in the same slot of hash table if more entries have the same key hash.
		 * @throws NullPointerException if key is null.
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next) {
			if(key == null) throw new NullPointerException("Key cannot be null.");
			
			this.key = key;
			this.value = value;
			this.next = next;
		}
		
		/**
		 * Getter for key.
		 * @return key of this entry in hash table.
		 */
		public K getKey() {
			return key;
		}
		
		/**
		 * Getter for value.
		 * @return value of this entry in hash table.
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Setter for value of this entry.
		 * @param value desired value of this entry.
		 */
		public void setValue(V value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return key+"="+value;
		}
	}
	
	/**
	 * Returns current number of table entries.
	 * @return integer representing number of table entries.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Checks if hash table is empty.
	 * @return true if table is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Method to put new table entry in hash table.
	 * @param key of new table entry.
	 * @param value which is mapped to the given key.
	 * @return if there was already a mapping for this key than this method returns that old value, if there was no mapping for
	 * given key, new table entry is created and null is returned.
	 * @throws NullPointerException if key is null.
	 */
	public V put(K key, V value) {
		if(key == null) throw new NullPointerException("Cannot put element with null key in hash table.");
		
		if(size/table.length >= 0.75) {	//provjera je li trebamo povećati veličinu tablice
			resizeHashTable();
			++modificationCount;
		}
		
		int slotNumber =   Math.abs(key.hashCode())%table.length;
		
		TableEntry<K, V> slotElement = table[slotNumber];
		
		//slučaj kada dodajemo prvi element u neki slot
		if(slotElement == null){
			slotElement = new TableEntry<K, V>(key, value, null);
			table[slotNumber] = slotElement;
			++size;
			++modificationCount;
			return null;
		}
		
		//prolazimo sve elemente slota u slučaju da već postoji zapis sa istim ključem u tablici
		TableEntry<K, V> previousSlotElement = null;
		while(slotElement != null) {
			
			if(slotElement.getKey().equals(key)) {	//već postoji zapis sa istim ključem te samo ažuriramo vrijednost
				V oldValue = slotElement.getValue();
				slotElement.setValue(value);
				return oldValue;
			}
			previousSlotElement = slotElement;
			slotElement = slotElement.next;
		}
		++size;
		++modificationCount;
		//ovdje znamo da nema zapisa sa istim ključem
		
		previousSlotElement.next = new TableEntry<K, V>(key, value, null);
		return null;
	}
	
	/**
	 * Method to get value mapped to one key.
	 * @param key whose value you want to get.
	 * @return value mapped to given key or null if key is null or if there is no mapping for given key.
	 */
	public V get(Object key) {
		if(key == null) return null;	//ako je key null možemo odmah vratiti null jer ne pohranjujemo null ključeve
		
		int slotNumber = Math.abs(key.hashCode())%table.length;
		
		TableEntry<K, V> element = table[slotNumber];
		
		while(element != null) {	//prolazimo sve elemente u slotu te tražimo onog sa istom vrijednosti ključa
			
			if(element.getKey().equals(key))
				return element.getValue();
			
			element = element.next;
		}
		
		return null;	//nismo pronašli entry sa istim ključem te vraćamo null
	}
	
	/**
	 * Method to check if there is a given key in hash table.
	 * @param key which you want to check if it exists in this hash table.
	 * @return false if given key argument is null or if there is no mapping with this key in hash table and true if there is a 
	 * mapping for this key in hash table.
	 */
	public boolean containsKey(Object key) {	//ne možemo koristiti metodu get(key) jer ona u nekim slučajevima vraća null i kada je to vrijednost zapisana 
												//pod nekin ključem, pa ne bi mogli razlikovati tada je li nema zapisa ili je vrijednost null ali zapis postoji
		if(key == null) return false;
		
		int slotNumber = Math.abs(key.hashCode())%table.length;
		
		TableEntry<K, V> element = table[slotNumber];
		
		while(element != null) {
			if(element.key.equals(key)) return true;
			
			element = element.next;
		}
		
		return false;
	}
	
	/**
	 * Method to check if there is a mapping with given value in this hash table.
	 * @param value which you want to check if it exists in this hash table.
	 * @return true if there is a mapping with this value, false otherwise.
	 */
	public boolean containsValue(Object value) {
		
		TableEntry<K, V> element;
		int tableCapacity = table.length;
		
		for(int i = 0; i < tableCapacity; i++) {	//iteriramo po svim slotovima tablice
			if(table[i] != null) {
				
				element = table[i];
				
				while(element != null) {	//iteriramo po svim elementima unutar slota
					
					if(value == null && element.value == null) //provjera je li riječ o pohranjenoj null vrijednosti
						return true;
					 else if(value.equals(element.value)) return true;	//nije riječ o null vrijednosti pa uspoređujemo sa equals
					
					element = element.next;
				}
				
			}
		}
		
		return false;
	}
	
	/**
	 * Method to remove a mapping with given key in this hash table.
	 * @param key of mapping which you want to remove from hash table.
	 * @return null if key is null or if the value of entry which you want to remove is null, otherwise returns a value of entry which is removed.
	 */
	public V remove(Object key) {
		if(key == null) return null; //ako je key null možemo odmah vratiti null jer ne pohranjujemo null vrijednosti
		
		int slotNumber = Math.abs(key.hashCode())%table.length;
		
		TableEntry<K, V> previous = null;	          //kako je riječ o jednostrukoj povezanoj listi moramo imati referencu
		TableEntry<K, V> element = table[slotNumber]; //na prethodni i trenutni element u slotu
		
		while(element != null) {
			
			if(element.getKey().equals(key)) {
				--size;
				++modificationCount;
				if(previous == null) {		//slučaj ako je element prvi u slotu
					V oldValue = element.getValue();
					table[slotNumber] = element.next;
					return oldValue;
				} else {
					previous.next = element.next;
					return element.value;
				}
			}
			
			previous = element;
			element = element.next;
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		int slotNum = table.length;
		for(int i = 0; i < slotNum; i++) {
			
			TableEntry<K, V> element = table[i];
			sb.append("[");
			
			while(element != null) {
	
				if(element.next == null) 
					sb.append(element);
				 else 
					sb.append(element+", ");
				
				element = element.next;
			}
			
			sb.append("]\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * Method to get all entries from hash table as an array. 
	 * @return array of table entries from this hash table. Entries are sorted in the same order as in hash table structure.
	 */
	@SuppressWarnings("unchecked")
	public TableEntry<K,V>[] toArray(){
		
		TableEntry<K, V>[] array = (TableEntry<K,V>[])new TableEntry[size];
		
		int slotNum = table.length;
		int index = 0;
		
		for(int i = 0; i < slotNum; i++) {
			
			if(table[i] != null) {			//iteracija kroz sve slotove i sve elemente unutar slota te pohrana u polje
				TableEntry<K, V> element = table[i];
				
				while(element != null) {
					array[index] = element;
					++index;
					element = element.next;
				}
			}
		}
		
		return array;
	}
	
	/**
	 * Private method to resize current hash table. The new table is twice as big as the old one and all elements from the old
	 * table are moved into the new table. After all elements from the old table are in new one reference to the current table is
	 * switched to the new one.
	 */
	@SuppressWarnings("unchecked")
	private void resizeHashTable() {
		
		TableEntry<K, V>[] newTable = (TableEntry<K,V>[])new TableEntry[table.length*2];
		TableEntry<K, V>[] oldTable = toArray();
		
		clear();		//brišemo sve elemente iz stare tablice
		
		this.table = newTable;
		
		for(TableEntry<K, V> entry: oldTable) { //iteriramo kroz elemente stare tablice te stavljamo u novu
			put(entry.key, entry.value);
		}	
	}
	
	/**
	 * Method to remove all entries in hash table.
	 */
	public void clear() {
		
		int slotNum = table.length;
		for(int i = 0; i < slotNum; i++) {
			table[i] = null;		//brišemo sve pokazivače na prve elemente u tablici
		}
		++modificationCount;
		size = 0;
	}

	@Override
	public Iterator<SimpleHashtable.TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}
	
	/**
	 * Private class implementing iterator for hash table.
	 * @author Ivan Bilobrk
	 * @version 1.0
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K,V>> {
		
		/**
		 * Internal number of modifications managed by iterator class. When new instance of iterator is 
		 * created this modification count is the same as the modification count of enclosing class; SimpleHashTable.
		 * It changes only when iterator removes elements and is used to check if hash table has been changed
		 * illegally during iteration.
		 */
		private int modificationCountIt;
		
		/**
		 * Variable holding the next entry which will be returned by iterator.
		 */
		private TableEntry<K, V> next;
		
		/**
		 * Variable holding the last entry returned by iterator.
		 */
		private TableEntry<K, V> current;
		
		/**
		 * Variable representing current slot position in hash table. Used when having to skip some slots in table
		 * because the first element in slot is null.
		 */
		private int slotIndex;
		
		/**
		 * Constructor for new iterator of hash table.
		 */
		IteratorImpl(){
			this.modificationCountIt = modificationCount;
			slotIndex = 0;
			next = null;
			current = null;
		}
		
		/**
		 * Method which checks if there is a next element in hash table which iterator can remove.
		 * @throws ConcurrentModificationException if hash table structure was changed externally and 
		 *         not correctly with iterator method remove.
		 */
		@Override
		public boolean hasNext() {
			
			//provjera je li bilo nepravilnih modifikacija tijekom iteriranja
			if(modificationCountIt != modificationCount) throw new ConcurrentModificationException("Cannot use this function since original hash table was modified.");
			
			//pamtimo stara stanja varijabli koje metoda next mijenja
			int modificationCountIt2 = modificationCountIt;
			TableEntry<K, V> next2 = next;
			TableEntry<K, V> current2 = current;
			int slotIndex2 = slotIndex;
			
			//probamo pozvati metodu next te hvatamo eventualne greške
			try {
				next();
			} catch(Exception e) { //ako se dogodila greška znamo da nema sljedeceg elementa
				return false;
			}
			
			//vraćamo varijable promijenjene metodom next na stare vrijednosti
			modificationCountIt = modificationCountIt2;
			next = next2;
			current = current2;
			slotIndex = slotIndex2;
			return true;
		}
		
		/**
		 * Method to return next iterator element.
		 * @throws ConcurrentModificationException if hash table structure was changed externally and 
		 *         not correctly with iterator method remove
		 * @throws NoSuchElementException if there are no more elements to be returned by this iterator.
		 */
		@Override
		public TableEntry<K, V> next() {
			
			//provjera je li bilo nepravilnih modifikacija tijekom iteriranja
			if(modificationCountIt != modificationCount) throw new ConcurrentModificationException("Cannot use this function since original hash table was modified.");
			
			//ili smo došli na kraj jednog slota ili smo skroz na početku pa je trenutni element i dalje null
			//moramo tražiti novi slot koji ima barem jedan element
			if(next == null) {
				
				next = getNextSlot();
				
				if(next == null)	//ako metoda getNextSlot vrati null znamo da više nema elemenata za iteriranje
					throw new NoSuchElementException("No more elements for this iterator.");
			}
			
			//postoji još elemenata za iteriranje te ažuriramo current i next tako da pokazuju na ispravne nove
			//elemente
			current = next;
			next = next.next;
			return current;
			
		}
		
		/**
		 * Private method used internally by iterator to skip hash table positions starting with null.
		 * @return next hash table entry if there is a table position starting with non null value, otherwise null.
		 */
		private TableEntry<K, V> getNextSlot() {
			
			int tableLength = table.length;
			
			//tražimo sljedeci slot kojem prvi element nije null
			while(next == null && slotIndex < tableLength) {
				next = table[slotIndex++];
			}
			
			//provjera jesmo li prošli cijelu tablicu bez da smo našli ne null element
			if(slotIndex == tableLength && next == null) {
				return null;
			}
			
			return next;
		}
		
		/**
		 * Method to remove last element returned by iterator.
		 * @throws IllegalStateException if remove method was called more than once in the row
		 * @throws ConcurrentModificationException if hash table structure was changed externally and 
		 *         not correctly with iterator method remove.
		 */
		public void remove() {
			
			//provjera da element namijenjen za brisanje nije null
			if(current == null) throw new IllegalStateException("Cannot call remove on iterator more than once in a row."); 
			
			//provjera je li bilo nepravilnih modifikacija strukture tablice tijekom iteriranja
			if(modificationCountIt != modificationCount) throw new ConcurrentModificationException("Cannot use this function since original hash table was modified.");
			
			//koristimo metodu remove od razreda SimpleHashTable
			SimpleHashtable.this.remove(current.key);
			current = null;
			++modificationCountIt;
		}
		
	}
	
}
