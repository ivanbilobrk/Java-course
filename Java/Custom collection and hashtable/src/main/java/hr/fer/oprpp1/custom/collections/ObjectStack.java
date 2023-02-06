package hr.fer.oprpp1.custom.collections;

/**
 * Class implementing stack using Array Indexed Collection.
 * @author Ivan Bilobrk
 * @version 1.0
 * @param <E> - Desired type of ObjectStack.
 */
public class ObjectStack<E>{
	
	/**
	 * Array Indexed Collection instance used for implementing functions of a stack.
	 */
	private ArrayIndexedCollection<E> stack;
	
	/**
	 * Default constructor which creates empty stack instance.
	 */
	public ObjectStack() {
		this.stack = new ArrayIndexedCollection<E>();
	}
	
	/**
	 * Constructor which creates new stack with desired size.
	 * @param size - Desired size of stack.
	 */
	public ObjectStack(int size) {
		this.stack = new ArrayIndexedCollection<E>(size);
	}
	
	/**
	 * Constructor which creates new stack of desired initial capacity and fills
	 * it with elements from another collection.
	 * @param other - a collection which elements will be added to the new stack 
	 * @param size - desired initial capacity of a stack
	 * @throws NullPointerException is thrown if other is null.
	 */
	public ObjectStack(Collection<? extends E> other, int size) {
		if(other == null) throw new NullPointerException("Cannot crate stack from null collection.");
		this.stack = new ArrayIndexedCollection<E>(other, size);
	}
	
	/**
	 * Constructor which accepts other collection as an argument and creates new stack with
	 * elements from other collection.
	 * @param other - a collection which elements will be added to the stack.
	 * @throws NullPointerException if other is null.
	 */
	public ObjectStack(Collection<? extends E> other) {
		if(other == null) throw new NullPointerException("Cannot create stack from null collection");
		
		this.stack = new ArrayIndexedCollection<E>(other);
	}
	
	/**
	 * Checks if stack is empty.
	 * @return True if stack is empty, false otherwise.
	 */
	public boolean isEmpty() {		//koristimo metodu iz ArrayIndexedCollection
		return stack.isEmpty();
	}
	
	/**
	 * 
	 * @return Integer size of stack.
	 */
	public int size() {				//koristimo metodu iz ArrayIndexedCollection
		return stack.size();
	}
	
	/**
	 * Pushes an element on top of the stack.
	 * @param value - element to be added on top of the stack.
	 * @throws NullPointerException if value is null.
	 */
	public void push(E value) {
		if(value == null) throw new NullPointerException("Cannot add null object to stack");
		
		stack.add(value);			//elemente dodajemo na stog pozivajući metodu add iz ArrayIndexedCollection
	}
	
	/**
	 * Removes element from top of the stack and returns it.
	 * @return Object which was on top of the stack.
	 * @throws EmptyStackException if there aren't any objects on stack.
	 */
	public Object pop() {
		if(stack.size() == 0) throw new EmptyStackException("No ojbects on stack");	//provjera parametara
		
		E element = stack.get(stack.size()-1);		//moramo dobiti zadnji element u polju jer tako radi stog
		stack.remove(stack.size()-1);				//metoda pop nalaže da se traženi element na kraju i obriše sa vrha stoga
		return element;
	}
	
	/**
	 * Returns element on top of the stack, but doesn't remove it like pop method.
	 * @return Object on top of the stack.
	 * @throws EmptyStackException if there aren't any objects on stack.
	 */
	
	/*
	 * Isto kao i metoda pop, samo što ne briše element sa vrha stoga.
	 */
	public E peek() {
		if(stack.size() == 0) throw new EmptyStackException("No ojbects on stack");
		
		E element = stack.get(stack.size()-1);
		return element;
	}
	
	/**
	 * Removes all elements from stack.
	 */
	public void clear() {
		stack.clear();
	}
}
