package hr.fer.oprpp1.custom.collections;


/** Interface Collection specifies methods which all collections have to implement if they implement this interface.
 * It specifies a lot of methods which enable you to manipulate with data.
 *@author Ivan Bilobrk 
 *@version 1.0
 */

public interface Collection {
	
	/**
	 * Checks if collection is empty.
	 * @return True if collection is empty, false otherwise.
	 */
	public default boolean isEmpty() {
		if(size() == 0)
			return true;
		else 
			return false;
	}
	
	/**
	 * 
	 * @return Integer size of collection.
	 */
	public int size();
	
	/**
	 * Adds given object in a collection.
	 * @param value - object which is added to collection.
	 */
	public void add(Object value);
	
	/**
	 * Checks if collection contains a given object.
	 * @param value - object which you test if it is in collection.
	 * @return Boolean which is true if object is in collection, false otherwise.
	 */
	public boolean contains(Object value);
	
	/**
	 * Removes an object from collection.
	 * @param value - object to remove from collection.
	 * @return Boolean which is true if object is removed, false otherwise.
	 */
	public boolean remove(Object value);
	
	/**
	 * Puts all collection elements in an array.
	 * @return Object array filled with collection elements.
	 * @throws UnsupportedOperationException if action is not implemented.
	 */
	public Object[] toArray();
	
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
	public default void addAll(Collection other) {
		other.forEach(value -> add(value));		//lambda izraz s kojim implementiramo sučelje Processor da za svaki argument value kojeg primi u metodi process
												//pozove metodu add(value)
	}
	
	/**
	 * Removes all elements from collection.
	 */
	public void clear();
	
	/**
	 * Abstract method to create ElementsGetter for desired Collection
	 * @return ElementsGetter class instance.
	 */
	public abstract ElementsGetter createElementsGetter();
	
	/**
	 * Default method which adds all elements from Collection col which satisfy tester.test method in current collection.
	 * @param col - Collection which elements you want to test and add in current Collection
	 * @param tester - implementation of interface Tester with method test.
	 */
	public default void addAllSatisfying(Collection col, Tester tester) {
		
		ElementsGetter getter = col.createElementsGetter();
		
		while(getter.hasNextElement()) {			//prolazimo kroz sve elemente kolekcije te za svaki elemnt koji zadovoljava uvjet dodamo ga u kolekciju
			Object element = getter.getNextElement();
			
			if(tester.test(element)) this.add(element);
		}
		
	}
	
	/**
	 * Goes through whole collection and calls processor.process(element) for every element.
	 * @param processor - processor class instance with appropriate process method.
	 */
	public default void forEach(Processor processor) {
		ElementsGetter getter = createElementsGetter();
		
		while(getter.hasNextElement()) {				//dokle god imamo elemenata u kolekciji pozivamo process metodu za svaki
			processor.process(getter.getNextElement());
		}
		
	}
}
