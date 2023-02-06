package hr.fer.oprpp1.custom.collections;

/**
 * Interface with methods for List.
 * @author Ivan Bilobrk
 * @version 1.0
 * @param <E> - Desired type of List.
 */
public interface List<E> extends Collection<E>{
	
	/**
	 * Method to get element from desired position.
	 * @param index - position from where you want to get an element.
	 * @return Element from index position.
	 * @throws IndexOutOfBoundsException if index is smaller than 0 or bigger than size - 1.
	 */
	public E get(int index);
	
	/**
	 * Inserts new object at desired position in collection.
	 * @param value - object to insert in collection
	 * @param position - index at which the element is inserted.
	 * @throws IndexOutOfBoundsException if position is smaller than 0 or bigger than current size of collection.
	 * @throws NullPointerException if value is null.
	 */
	public void insert(E value, int position);
	
	/**
	 * Gives an index at which one object is stored in collection.
	 * @param value - object whose position you want to know.
	 * @return Integer representing object's position in collection, -1 if object doesn't exist in collection.
	 */
	public int indexOf(Object value);
	
	/**
	 * Removes an element from collection at desired index.
	 * @param index - position of element you want to remove.
	 * @throws IndexOutOfBoundsException if index is smaller than 0 or bigger than size-1.
	 */
	public void remove(int index);

}
