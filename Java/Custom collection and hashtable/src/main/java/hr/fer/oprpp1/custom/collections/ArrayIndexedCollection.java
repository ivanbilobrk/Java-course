package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Implementation of Collection interface using Object array.
 * @author Ivan Bilobrk
 * @version 1.0
 * @param <E> - Desired type of ArrayIndexedCollection.
 */

public class ArrayIndexedCollection<E> implements List<E> {
	
	/**
	 * Integer which represents how many items are currently in collection.
	 */
	private int size;
	
	/**
	 * Object array which is used to store elements.
	 */
	private Object[] elements;
	
	/**
	 * Represents how many changes have been made to the Collection.
	 */
	private long modificationCount = 0;
	
	/**
	 * Constructor which makes Array Indexed Collection with desired initial capacity.
	 * @param initialCapacity - a desired initial size of a collection.
	 * @throws IllegalArgumentException is thrown if initial capacity is smaller than 1.
	 */
	public ArrayIndexedCollection(int initialCapacity) {
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
	public ArrayIndexedCollection(Collection<? extends E> other) {
		this(other, other.size());
	}
	
	/**
	 * Constructor which creates new Array Indexed Collection of desired initial capacity and fills
	 * it with elements from another collection.
	 * @param other - a collection which elements will be added to the new collection
	 * @param initialCapacity - desired initial capacity of a collection.
	 * @throws NullPointerException is thrown if other is null.
	 */
	public ArrayIndexedCollection(Collection<? extends E> other, int initialCapacity) {
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
	public void add(E value) {
		if(value == null) throw new NullPointerException("Cannot add object which is null");
		
		if(this.size == elements.length) { //staro polje je puno pa treba stvoriti novo i napuniti ga sa starim elementima
			resizeArray();
		}
		
		elements[size++] = value;
		++modificationCount;
	}
	
	/**
	 * Method to get an element from Array Indexed Collection at desired index. 
	 * @param index - Integer which says at which index you want to get element at.
	 * @return Element at desired index.
	 * @throws IndexOutOfBoundsException if index is smaller than 0 or bigger than size - 1.
	 */
	
	@SuppressWarnings("unchecked")
	public E get(int index) {
		if(index < 0 || index > size-1) {
			throw new IndexOutOfBoundsException("Wrong index");
		} else {
			return (E)elements[index];
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
		++modificationCount;
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
		++modificationCount;
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
	public void insert(E value, int position) {
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
		++modificationCount;
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
	 * @throws IndexOutOfBoundsException if index is smaller than 0 or bigger than size-1.
	 */
	
	/*Prvo radimo provjeru je li parametar index ispravno zadan te element mičemo tako da sve članove od pozicije index uključivo do
	 * predzadnjeg člana polja uključivo pomaknemo za jedno mjesto ulijevo. Tako smo element na željenoj poziciji prepisali
	 * drugim elementom te na zadnjem mjestu sada imamo duplikat predzadnjeg mjesta te je samo potrebno na zadnje mjesto upisati
	 * null vrijednost.
	 */
	public void remove(int index) {
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
		++modificationCount;
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
			++modificationCount;
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
	
	
	/**
	 * Private class which implements ElementsGetter interface for ArrayIndexedCollection class.
	 * @author Ivan Bilobrk
	 * @version 1.0
	 * @param <A> - Desired type of ArrayElementsGetter.
	 */
	
	/*
	 *  ArrayElementsGetter je privatna statička klasa koja implementira ElementsGetter sučelje. U konstruktoru prima referencu na primjerak 
	 *  razreda ArrayIndexedCollection kako bi imala pristup elementima kolekcije. Varijabla index nam služi kako bi pamtili trenutnu poziciju
	 *  ElementsGetter-a u polju.
	 */
	private static class ArrayElementsGetter<A> implements ElementsGetter<A>{
		
		/**
		 * Reference to ArrayIndexedCollection class instance.
		 */
		ArrayIndexedCollection<A> arrayCollection;
		/**
		 * Integer which remembers current position of ElementsGetter in ArrayIndexedCollection.
		 */
		int index;
		
		private long savedModificationCount;
		
		/**
		 * Constructor which creates an instance of ArrayElementsGetter.
		 * @param arrayCollection - instance of ArrayIndexedCollection
		 * @throws NullPointerException if arrayCollection is null.
		 */
		ArrayElementsGetter(ArrayIndexedCollection<A> arrayCollection){
			if(arrayCollection == null) throw new NullPointerException("Cannot create ArrayElementsGetter with null ArrayIndexedCollection.");				
																									//kako bi imali pristup nestatičkim metodama razreda ArrayIndexedCollection
			this.arrayCollection = arrayCollection;													//potrebna nam je referenca na jedan primjerak tog razreda
			index = 0;																				//index stavljamo na nulu jer elemente želimo dohvaćati od početka
			this.savedModificationCount = arrayCollection.modificationCount;
		}
		
		@Override
		public boolean hasNextElement() {
			if(savedModificationCount != arrayCollection.modificationCount) 	//provjera je li bilo modifikacije kolekcije
				throw new ConcurrentModificationException("Collection has been changed while using ElementsGetter.");
			
			if(index >= arrayCollection.size()) return false;	//provjera jesmo li prošli sve elemente kolekcije
			
			return true;
		}

		@SuppressWarnings("unchecked")
		@Override
		public A getNextElement() {
			if(savedModificationCount != arrayCollection.modificationCount) 	//provjera je li bilo modifikacije kolekcije
				throw new ConcurrentModificationException("Collection has been changed while using ElementsGetter.");
			
			if(!hasNextElement()) throw new NoSuchElementException("There are no more elements in collection to return."); //provjera ima li još elemenata za vratiti
			return (A)arrayCollection.elements[index++];
		}
	}
	
	/**
	 * Method which creates an ElementsGetter for this collection.
	 * @return new ElementsGetter for ArrayIndexedCollection.
	 */
	public ElementsGetter<E> createElementsGetter() {
		return new ArrayElementsGetter<E>(this);
	}
	
	
	@SuppressWarnings("unchecked")
	public E[] toArray(E[] other) {
		if(other == null) throw new NullPointerException("Other array cannot be null.");
		
		if (other.length < size)
            return (E[]) Arrays.copyOf(elements, size, other.getClass());
		
        System.arraycopy(elements, 0, other, 0, size);
        
        if (other.length > size)
            other[size] = null;
        
        return other;
	}
	
}
