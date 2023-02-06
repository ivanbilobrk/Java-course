package hr.fer.oprpp1.custom.collections;

/**
 * Class implementing stack using Array Indexed Collection.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class ObjectStack{
	
	/**
	 * Array Indexed Collection instance used for implementing functions of a stack.
	 */
	private ArrayIndexedCollection stack;
	
	/**
	 * Default constructor which creates empty stack instance.
	 */
	public ObjectStack() {
		this.stack = new ArrayIndexedCollection();
	}
	
	/**
	 * Constructor which creates new stack with desired size.
	 * @param size - Desired size of stack.
	 */
	public ObjectStack(int size) {
		this.stack = new ArrayIndexedCollection(size);
	}
	
	/**
	 * Constructor which creates new stack of desired initial capacity and fills
	 * it with elements from another collection.
	 * @param other - a collection which elements will be added to the new stack 
	 * @param initialCapacity - desired initial capacity of a stack
	 * @throws NullPointerException is thrown if other is null.
	 */
	public ObjectStack(Collection other, int size) {
		this.stack = new ArrayIndexedCollection(other, size);
	}
	
	/**
	 * Constructor which accepts other collection as an argument and creates new stack with
	 * elements from other collection.
	 * @param other - a collection which elements will be added to the stack.
	 */
	public ObjectStack(Collection other) {
		this.stack = new ArrayIndexedCollection(other);
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
	public void push(Object value) {
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
		
		Object o = stack.get(stack.size()-1);		//moramo dobiti zadnji element u polju jer tako radi stog
		stack.remove(stack.size()-1);				//metoda pop nalaže da se traženi element na kraju i obriše sa vrha stoga
		return o;
	}
	
	/**
	 * Returns element on top of the stack, but doesn't remove it like pop method.
	 * @return Object on top of the stack.
	 * @throws EmptyStackException if there aren't any objects on stack.
	 */
	
	/*
	 * Isto kao i metoda pop, samo što ne briše element sa vrha stoga.
	 */
	public Object peek() {
		if(stack.size() == 0) throw new EmptyStackException("No ojbects on stack");
		Object o = stack.get(stack.size()-1);
		return o;
	}
	
	/**
	 * Removes all elements from stack.
	 */
	public void clear() {
		stack.clear();
	}
}
