package hr.fer.oprpp1.custom.collections;

/**
 * Custom Dictionary implementation.
 * @author IvanPC
 * @version 1.0
 * @param <K> desired type of key
 * @param <V> desired type of value.
 */
public class Dictionary<K,V> {
	
	/**
	 * Private nested class used for storing one key-value pair.
	 * @author Ivan Bilobrk
	 * @version 1.0
	 * @param <A> - desired type of key.
	 * @param <B> - desired type of value.
	 */
	private class KeyValue<A, B>{
		
		/**
		 * Key of one pair.
		 */
		private A key;
		
		/**
		 * Value of one pair.
		 */
		private B value;
		
		/**
		 * Constructor for one pair.
		 * @param key - unique key of one pair
		 * @param value - value you want to assign to this pair.
		 * @throws NullPointerException if key is null.
		 */
		KeyValue(A key, B value){
			if(key == null) throw new NullPointerException("Key cannot be null.");
			
			this.key = key;
			this.value = value;
		}
	}
	
	/**
	 * Internal collection used for storing key-value pairs.
	 */
	private ArrayIndexedCollection<KeyValue<K, V>> pairs;
	
	/**
	 * Constructor for class Dictionary.
	 */
	public Dictionary() {
		pairs = new ArrayIndexedCollection<>();
	}
	
	/**
	 * Checks if this dictionary is empty.
	 * @return true if dictionary is empty, otherwise false.
	 */
	public boolean isEmpty() {
		return pairs.isEmpty();
	}
	
	/**
	 * Returns number of key-value pairs currently in dictionary.
	 * @return current number of key-value pairs in this dictionary.
	 */
	public int size() {
		return pairs.size();
	}
	
	/**
	 * Removes all elements from dictionary.
	 */
	public void clear() {
		pairs.clear();
	}
	
	/**
	 * Puts new key-value pair in dictionary. If there is already a mapping for given key then the old value
	 * for that key is replaced with the new one, otherwise creates new key-value pair and puts it into dictionary.
	 * @param key which you want to map the value to in dictionary
	 * @param value - desired value which will be mapped to key
	 * @return old value if there was already a mapping for given key in dictionary or null if 
	 * method had to create new key-value pair.
	 * @throws NullPointerException if key is null.
	 */
	public V put(K key, V value) {
		
		if(key == null) throw new NullPointerException("Key cannot be null.");
		ElementsGetter<KeyValue<K,V>> getter = pairs.createElementsGetter();
		
		while(getter.hasNextElement()) {
			
			KeyValue<K,V> pair = getter.getNextElement();
			//iteriramo sve elemente te tražimo postoji li već zapis sa istim ključem
			if(pair.key.equals(key)) {
				V oldValue = pair.value;
				pair.value = value;
				return oldValue;	//vraćamo stari zapis pod tim ključem
			}
		}
		
		//ovdje možemo doći samo ako ne postoji zapis sa istim ključem te dodajemo novi zapis i vraćamo null
		pairs.add(new KeyValue<K, V>(key, value));
		
		return null;
	}
	
	/**
	 * Method to get value which is mapped to given key.
	 * @param key which is used to get mapping from.
	 * @return value mapped to key or null if there is no mapping for key.
	 */
	public V get(Object key) {
		
		if(key == null) return null;
		
		ElementsGetter<KeyValue<K,V>> getter = pairs.createElementsGetter();
		
		while(getter.hasNextElement()) {
			
			KeyValue<K,V> pair = getter.getNextElement();
			//iteriramo kroz sve elemente te tražimo isti ključ čiju vrijednost ćemo vratiti
			if(pair.key.equals(key)) {
				return pair.value;
			}
		}
		
		//nismo pronašli zapis sa zadanim ključem
		return null;
	}
	
	/**
	 * Method to remove key-value pair in dictionary.
	 * @param key - which mapping you want to remove from dictionary.
	 * @return value which value is removed or null if there is no mapping for given key.
	 */
	public V remove(K key) {
		
		if(key == null) return null;
		
		ElementsGetter<KeyValue<K,V>> getter = pairs.createElementsGetter();
		int index = 0;
		
		while(getter.hasNextElement()) {
			
			KeyValue<K,V> pair = getter.getNextElement();
			
			//iteriramo po svim elementima i tražimo zapis pod zadanim ključem koji moramo obrisati
			if(pair.key.equals(key)) {
				pairs.remove(index);
				return pair.value;
			}
			
			++index;
		}
		
		//ne postoji zapis sa zadanim ključem te ništa ne brišemo
		return null;
	}
	
}
