package hr.fer.oprpp1.custom.collections;

/** Class Collection is used for storing elements.
 * It also has a lot of methods which enable you to manipulate with data.
 * There are two implementations, one is using linked list and the other an array for storage.
 * 
 *@author Ivan Bilobrk 
 *@version 1.0
 */

public class Collection {
	
	/**
	 * Default protected constructor.
	 */
	protected Collection() {
		super();
	}
	
	/**
	 * Checks if collection is empty.
	 * @return True if collection is empty, false otherwise.
	 */
	public boolean isEmpty() {
		if(size() == 0)
			return true;
		else 
			return false;
	}
	
	/**
	 * 
	 * @return Integer size of collection.
	 */
	public int size() {
		return 0;
	}
	
	/**
	 * Adds given object in a collection.
	 * @param value - object which is added to collection.
	 */
	public void add(Object value) {
		
	}
	
	/**
	 * Checks if collection contains a given object.
	 * @param value - object which you test if it is in collection.
	 * @return Boolean which is true if object is in collection, false otherwise.
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * Removes an object from collection.
	 * @param value - object to remove from collection.
	 * @return Boolean which is true if object is removed, false otherwise.
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Puts all collection elements in an array.
	 * @return Object array filled with collection elements.
	 * @throws UnsupportedOperationException if action is not implemented.
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();	
	}
	
	/**
	 * Goes through whole collection and calls processor.process(element) for every element.
	 * @param processor - processor class instance with appropriate process method.
	 */
	
	public void forEach(Processor processor) {
		
	}
	
	/**
	 * Adds all elements from other collection to the current collection.
	 * @param other - a collection which elements will be added to the current collection.
	 */
	
	/*
	 * Metodu addAll implementiramo tako da stvorimo novu lokalnu klasu ProcessorNew koja nasljeđuje klasu Processor. 
	 * Metodu process implementiramo tako da za svaki objekt value kojeg primi kao argument proslijedi metodi add
	 * koja će navedeni value dodati u kolekciju.
	 * Sada kad imamo odgovarajući Processor možemo nad kolekcijom other, koju primamo kao argument, pozvati metodu 
	 * forEach koja će proći kroz sve elemente druge kolekcije te svaki element proslijediti metodi process koja će pozvati add
	 * te dodati element u kolekciju.  
	 */
	public void addAll(Collection other) {
		class ProcessorNew extends Processor{
			
			public void process(Object value) {
				add(value);
			}
		}
		
		other.forEach(new ProcessorNew());
	}
	
	/**
	 * Removes all elements from collection.
	 */
	public void clear() {
		
	}
}
