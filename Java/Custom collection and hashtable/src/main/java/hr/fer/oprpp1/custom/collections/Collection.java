package hr.fer.oprpp1.custom.collections;


/** Interface Collection specifies methods which all collections have to implement if they implement this interface.
 * It specifies a lot of methods which enable you to manipulate with data.
 * @author Ivan Bilobrk 
 * @version 1.0
 * @param <E> - Desired type of Collection.
 */

public interface Collection <E> {
	
	/**
	 * Checks if collection is empty.
	 * @return True if collection is empty, false otherwise.
	 */
	public default boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * 
	 * @return Integer size of collection.
	 */
	public int size();
	
	/**
	 * Adds given object in a collection.
	 * @param e - object which is added to collection.
	 * @throws NullPointerException if value is null.
	 */
	public void add(E e);
	
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
	 * Returns an array of the same type as collection type.
	 * @param e an array in which the elements will be stored if the size is big enough or if the size is too small it will be 
	 * 			used to create a new array of the same type.
	 * @return array of the same type as collection.
	 * @throws NullPointerException if e is null.
	 */
	public E[] toArray(E[] other);
	
	/**
	 * Adds all elements from other collection to the current collection.
	 * @param other - a collection which elements will be added to the current collection.
	 * @throws NullPointerException if other is null.
	 */
	
	/*
	 * Metodu addAll implementiramo tako da stvorimo novu lokalnu klasu ProcessorNew koja nasljeđuje klasu Processor. 
	 * Metodu process implementiramo tako da za svaki objekt value kojeg primi kao argument proslijedi metodi add
	 * koja će navedeni value dodati u kolekciju.
	 * Sada kad imamo odgovarajući Processor možemo nad kolekcijom other, koju primamo kao argument, pozvati metodu 
	 * forEach koja će proći kroz sve elemente druge kolekcije te svaki element proslijediti metodi process koja će pozvati add
	 * te dodati element u kolekciju.  
	 */
	public default void addAll(Collection<? extends E> other) {
		if(other == null) throw new NullPointerException("Other collection cannot be null.");
		
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
	public abstract ElementsGetter<E> createElementsGetter();
	
	/**
	 * Default method which adds all elements from Collection col which satisfy tester.test method in current collection.
	 * @param col - Collection which elements you want to test and add in current Collection
	 * @param tester - implementation of interface Tester with method test.
	 * @throws NullPointerException if col or tester is null.
	 */
	public default void addAllSatisfying(Collection<? extends E> col, Tester<? super E> tester) {
		
		if(col == null || tester == null) throw new NullPointerException("Cannot have null arguments.");
			
		ElementsGetter<? extends E> getter = col.createElementsGetter();
		
		while(getter.hasNextElement()) {			//prolazimo kroz sve elemente kolekcije te za svaki elemnt koji zadovoljava uvjet dodamo ga u kolekciju
			E element = getter.getNextElement();
			
			if(tester.test(element)) this.add(element);
		}
		
	}
	
	/**
	 * Goes through whole collection and calls processor.process(element) for every element.
	 * @param processor - processor class instance with appropriate process method.
	 * @throws NullPointerException if processor is null.
	 */
	public default void forEach(Processor<? super E> processor) {
		if(processor == null) throw new NullPointerException("Processor cannot be null.");
		
		ElementsGetter<E> getter = createElementsGetter();
		
		while(getter.hasNextElement()) {				//dokle god imamo elemenata u kolekciji pozivamo process metodu za svaki
			processor.process(getter.getNextElement());
		}
		
	}
}
