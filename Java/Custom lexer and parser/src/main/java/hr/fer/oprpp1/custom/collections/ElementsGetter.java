package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;

/**
 * Interface ElementsGetter is used to specify two methods: hasNextElement() and getNextElement(). These methods are used in classes which implement
 * ElementsGetter for certain collections.  
 * @author Ivan Bilobrk
 * @version 1.0
 */

/*
 * Sučelje ElementsGetter nam je potrebno kako bi deklarirali metode koje su nam potrebne za dohvat elemenata u nekoj kolekciji te 
 * provjeru ima li još elemenata za dohvatiti. Konkretna implementacija ovisi o tipu kolekcije.
 */
public interface ElementsGetter {
	
	/**
	 * Used to check whether current ElementsGetter of Collection has gone through all elements of Collection or if it has more elements to return.
	 * @return true if there are more elements, otherwise false.
	 * @throws ConcurrentModificationException if Collection has been changed while using ElementsGetter
	 * @throws ConcurrentModificationException if Collection has been changed while using ElementsGetter
	 */
	public boolean hasNextElement();
	
	/**
	 * Method to get element at the current position of ElementsGetter
	 * @return Object at the current position of ElementsGetter.
	 * @throws NoSuchElementException if ElementsGetter has gone through all elements of Collection and there is nothing to be returned anymore.
	 * @throws ConcurrentModificationException if Collection has been changed while using ElementsGetter
	 */
	public Object getNextElement();
	
	/**
	 * Default method which goes through whole collection from the current position of ElementsGetter 
	 * and calls Processor method process for each element. 
	 * @param p
	 */
	public default void processRemaining(Processor p) {
		
		while(this.hasNextElement()) {
			p.process(this.getNextElement());
		}
		
	}
	
}
