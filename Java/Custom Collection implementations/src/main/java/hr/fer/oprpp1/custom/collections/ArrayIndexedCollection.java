package hr.fer.oprpp1.custom.collections;

/**
 * Implementation of Collection class using Object array.
 *@author Ivan Bilobrk
 *@version 1.0
 */

public class ArrayIndexedCollection extends Collection {
	
	/**
	 * Integer which represents how many items are currently in collection.
	 */
	private int size;
	
	/**
	 * Object array which is used to store elements.
	 */
	private Object[] elements;
	
	/**
	 * Constructor which makes Array Indexed Collection with desired initial capacity.
	 * @param initialCapacity - a desired initial size of a collection.
	 * @throws IllegalArgumentException is thrown if initial capacity is smaller than 1.
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		super();
		if(initialCapacity < 1) throw new IllegalArgumentException("Initial capacity cannot be less than 1");
		this.size = 0;
		elements = new Object[initialCapacity];
	}
	
	/**
	 * Default constructor which makes Array Indexed Collection with initial capacity of 16.
	 */
	public ArrayIndexedCollection() {
		this(16);
	}
	
	/**
	 * Constructor which accepts other collection as an argument and creates new Array Indexed Collection with
	 * elements from other collection.
	 * @param other - a collection which elements will be added to the new collection.
	 * @throws NullPointerException is thrown if other is null.
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, other.size());
	}
	
	/**
	 * Constructor which creates new Array Indexed Collection of desired initial capacity and fills
	 * it with elements from another collection.
	 * @param other - a collection which elements will be added to the new collection
	 * @param initialCapacity - desired initial capacity of a collection.
	 * @throws NullPointerException is thrown if other is null.
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		super();
		if(other == null) throw new NullPointerException("Other collection cannot be null");
		if(initialCapacity < other.size()) initialCapacity = other.size();
		elements = new Object[initialCapacity];
		this.addAll(other);
	}
	
	/**
	 * @throws NullPointerException if value is null.
	 */
	
	/*
	 * Metoda dodaje novi Objekt na zadnje mjesto u polju te uvećava broj trenutnih elemenata za jedan.
	 * Prije nego što doda element radi se provjera ukoliko je trenutno polje puno te ako je radi se veće polje duple veličine 
	 * pomoću privatne metode resizeArray().
	 */
	@Override
	public void add(Object value) {
		if(value == null) throw new NullPointerException("Cannot add object which is null");
		if(this.size == elements.length) { //staro polje je puno pa treba stvoriti novo i napuniti ga sa starim elementima
			resizeArray();
		}
		elements[size++] = value;
	}
	
	/**
	 * Method to get an element from Array Indexed Collection at desired index. 
	 * @param index - Integer which says at which index you want to get element at.
	 * @return Element at desired index.
	 * @throws IndexOutOfBoundsException if index is smaller than 0 or bigger than size - 1.
	 */
	
	public Object get(int index) {
		if(index < 0 || index > size-1) {
			throw new IndexOutOfBoundsException("Wrong index");
		} else {
			return elements[index];
		}
	}
	
	@Override
	/*
	 * Mičemo sve elemente iz kolekcije tako što svaki element polja postavimo na null te referencu na polje na kraju 
	 * isto stavimo na null te size vratimo na 0.
	 */
	public void clear() {
		for(int i = 0; i < size; i++) {
			elements[i] = null;
		}
		size = 0;
	}
	
	/**
	 * Private method used when needed to reallocate new array with double storage capacity.
	 * Creates new array which is double the size of old array and copies all elements from old array to the new one and 
	 * replaces reference to the elements array with new one.
	 */
	
	private void resizeArray() {
		Object[] elementsNew = new Object[this.size*2];
		int index = 0;
		for(Object o: elements) {
			elementsNew[index++] = o;
		}
		elements = elementsNew;
	}
	
	/**
	 * Inserts new object at desired position in collection.
	 * @param value - object to insert in collection
	 * @param position - index at which the element is inserted.
	 * @throws IndexOutOfBoundsException if position is smaller than 0 or bigger than current size of collection.
	 * @throws NullPointerException if value is null.
	 */
	
	/*prvo radimo provjeru jesu li parametri ispravno zadani, ako jesu potrebno je provjeriti je li trenutno polje dovoljno veliko 
	 * za još jedan objekt te ako nije to popravimo. Mjesto za novi element radimo tako da sve elemente od te pozicije 
	 * uključivo do kraja polja uključivo pomaknemo za jedno mjesto u desno. Nakon što izađemo iz petlje tako ćemo si osloboditi 
	 * jedno mjesto na željenoj poziciji.
	 */
	public void insert(Object value, int position) {
		if(position < 0 || position > size) {
			throw new IndexOutOfBoundsException("Wrong index");
		} else if(value == null) {
			throw new NullPointerException("Cannot add object which is null");
		} else {
			if(size == elements.length) {
				resizeArray();
			}
				for(int i = size-1; i >= position; i--) {
					int temp = i+1;
					elements[temp] = elements[i];
				}
			elements[position] = value;
		}
		size++;
	}
	
	/**
	 * Gives an index at which one object is stored in collection.
	 * @param value - object whose position you want to know.
	 * @return Integer representing object's position in collection, -1 if object doesn't exist in collection.
	 */
	public int indexOf(Object value) {
		for(int i = 0; i < size; i++) {
			if(elements[i].equals(value)) return i;
		}
		return -1;
	}
	
	/**
	 * Removes an element from collection at desired index.
	 * @param index - position of element you want to remove.
	 */
	
	/*Prvo radimo provjeru je li parametar index ispravno zadan te element mičemo tako da sve članove od pozicije index uključivo do
	 * predzadnjeg člana polja uključivo pomaknemo za jedno mjesto ulijevo. Tako smo element na željenoj poziciji prepisali
	 * drugim elementom te na zadnjem mjestu sada imamo duplikat predzadnjeg mjesta te je samo potrebno na zadnje mjesto upisati
	 * null vrijednost.
	 */
	void remove(int index) {
		if(index < 0 || index > size-1) {
			throw new IndexOutOfBoundsException("Wrong index");
		} else {
			for(int i = index; i < size-1; i++) {
				int temp = i+1;
				elements[i] = elements[temp];
			}
			elements[size-1] = null;
			size -= 1;
		}
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	
	/*
	 * Provjeravamo je li element već u polju tako što pozovemo metodu indexOf koja će vratiti vrijednost različitu od -1
	 * ako je element u polju.
	 */
	public boolean contains(Object value) {
		if(indexOf(value) != -1) return true;
		return false;
	}
	
	@Override
	
	/*
	 * Objekt ćemo maknuti tako da prvo saznamo njegov index u polju te onda koristimo prostojeću metodu remove(int index).
	 */
	public boolean remove(Object value) {
		int valueIndex = indexOf(value);
		if(valueIndex != -1) {
			remove(valueIndex);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	
	/*
	 * Prvo stvaram novo polje koje je veličine trenutnog broja elemenata te potom kopiramo sve elemente kolekcije u polje.
	 */
	public Object[] toArray() {
		Object[] allElements = new Object[size];
		for(int i = 0; i < size; i++) {
			allElements[i] = elements[i];
		}
		return allElements;
	}
	
	@Override
	
	/*
	 * Metodu forEach implementiramo tako da u for petlji prolazim svim elementima kolekcije te 
	 * pozovem metodu processor.process(neki element)
	 */
	public void forEach(Processor processor) {
		for(int i = 0; i < size; i++) {
			processor.process(elements[i]);
		}
	}
}
