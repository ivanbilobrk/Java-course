package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;


/**
 * Implementation of Collection interface using doubly linked list.
 *@author Ivan Bilobrk
 *@version 1.0
 */

public class LinkedListIndexedCollection implements List{
	
	/**
	 * Private class ListNode which represents one node in linked list. Each node has its value and reference to 
	 * the previous and next element.
	 * @author IvanPC
	 *@version 1.0
	 */
	
	/*
	 * Kako bi ostvarili povezanu listu potrebno je stvoriti privatnu klasu ListNode koja će predstavljati jedan čvor 
	 * u listi sa referencama na prethodni i sljedeći član te varijablom value koja čuva vrijednost koja je pohranjena u taj čvor. 
	 */
	
	private static class ListNode{
		
		/**
		 *Next and previous are references to  the next and previous element.
		 *Value is an Object which holds a value of current node.
		 */
		private ListNode next = null;
		private ListNode previous = null;
		private Object value;
		
		/**
		 * Constructor which creates new node.
		 * @param value - object which is stored in a node.
		 */
		ListNode(Object value) {
			this.value = value;
		}
		
		/**
		 * Getter for next node of a current node in list.
		 * @return Next node in linked list.
		 */
		public ListNode getNext() {
			return next;
		}
		
		/**
		 * Setter for next node of a current node in list.
		 * @param next - node which becomes next node of a current node.
		 */
		public void setNext(ListNode next) {
			this.next = next;
		}
		
		/**
		 * Getter for previous node of a current node in list.
		 * @return Previous node in linked list.
		 */
		public ListNode getPrevious() {
			return previous;
		}
		
		/**
		 * Setter for previous node of a current node in list.
		 * @param next - node which becomes previous node of a current node.
		 */
		public void setPrevious(ListNode previous) {
			this.previous = previous;
		}
		
		/**
		 * Getter for value which is stored in a node.
		 * @return value of current node.
		 */
		public Object getValue() {
			return value;
		}
		
		/**
		 * Setter for value of current node.
		 * @param value - desired value of current node.
		 */
		public void setValue(Object value) {
			this.value = value;
		}
	}
	
	/**
	 * Size is an integer which tells us how many elements are currently in a list. First is a reference to 
	 * the beginning of a list. Last is a reference to the end of a list.
	 */
	private int size;
	private ListNode first;
	private ListNode last;
	
	/**
	 * Represents how many changes have been made to the Collection.
	 */
	private long modificationCount = 0;
	
	/**
	 * Default constructor which creates new empty linked list.
	 */
	public LinkedListIndexedCollection(){
		first = last = null;
		size = 0;
	}
	
	/**
	 * Constructor which accepts other collection as an argument and creates new Linked List Indexed Collection with
	 * elements from other collection.
	 * @param other - a collection which elements will be added to the new collection.
	 * @throws NullPointerException is thrown if other is null.
	 */
	public LinkedListIndexedCollection(Collection other){
		if(other == null) throw new NullPointerException("Other collection cannot be null");
		
		this.addAll(other);
		size = other.size();
	}
	
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * @throws NullPointerException if value is null.
	 */
	@Override
	
	/*
	 * Novi objekt dodajemo tako da prvo stvorimo čvor sa dobivenim objektom value te ukoliko je lista prazna 
	 * dovoljno je samo reference na prvi i posljednji član postavit da pokazuju na taj novi čvor.
	 * Ukoliko lista nije prazna novi čvor dodajemo na kraj tako što prvo trenutnom zadnjem čvoru postavimo 
	 * da je sljedeći čvor upravo novo stvoreni čvor, a novom čvoru postavimo da je prethodni čvor trenutni zadnji.
	 * Nakon toga promjenimo referencu zadnjeg čvora da pokazuje na novi čvor. 
	 */
	
	public void add(Object value) {
		if(value == null) throw new NullPointerException("Cannot add object which is null");
		
		ListNode node = new ListNode(value);
		++modificationCount;
		++size;
		
		if(first == null) {
			first = last = node;
			return;
		} 
		
		node.setPrevious(last);
		last.setNext(node);
		last = node;
		
		return;
	}
	
	/*
	 * Metodu equals ostvarujemo tako da prođemo cijelom listom te kada naiđemo na istu vrijednost vratimo true, a 
	 * ukoliko prođemo cijelu listu te referenca node postane null znači da nismo našli istu vrijednost nigdje u listi te
	 * možemo vratiti false
	 */
	
	@Override
	public boolean contains(Object value) {
		ListNode node = first;
		
		while(node != null) {
			if(node.getValue().equals(value)) return true;
			node = node.getNext();
		}
		
		return false;
	}
	
	/*
	 * Brisanje objekta iz liste radimo tako da prvo provjerimo je li se on nalazi u listi te ako ne odmah vratimo false.
	 * U suprotnom prolazimo cijelom listo dok ne naiđemo na traženi objekt.
	 * Postoje četiri slučajeva: 
	 * 								a)lista je imala samo jedan element
	 * 								b)moramo obrisati prvi element
	 * 								c)moramo obrisati zadnji element
	 * 								d)element je negdje u sredini
	 */
	
	@Override
	public boolean remove(Object value) {
		if(contains(value)) {
			++modificationCount;
			ListNode node = first;
			while(node != null) {
				if(node.getValue().equals(value)) {
					if(size == 1) {
						first = last = null;				/* element je jedini u listi pa ga brišemo tako da reference na prvi
															* i zadnji element postavimo na null
															*/
						
					} else if(node.getPrevious() == null) { /* element je prvi u listi pa ga brišemo tako da referencu na početak liste
															* pomaknemo na sljedeći element
															*/
						first = first.getNext();
						first.setPrevious(null);
					} else if(node.getNext() == null) {		/* element je zadnji u listi pa ga brišemo tako da referencu na zadnji 
															* element liste pomaknemo za jedno mjesto u nazad
															*/
						last = last.getPrevious();
						last.setNext(null);
					} else {										/* element je negdje u sredini pa ga brišemo tako da njegovom 
																	* prethodniku postavimo da je sljedeći čvor njegov sljedbenik, a
																	* njegovom sljedbeniku stavimo da je prethodni čvor njegov prethodnik
																	*/
						node.getPrevious().setNext(node.getNext());
						node.getNext().setPrevious(node.getPrevious());
					}
					--size;
					
					return true;
				}
				node = node.getNext();
			}
		}
		
		return false;
	}
	
	@Override
	/* Metodu toArray ostvarujemo tako da napravimo vlastitu klasu linkedListProcessor koja nasljeđuje klasu Processor te u svojoj 
	 * metodi process element koji dobije kao argument dodaje u postojeće polje.
	 */
	
	public Object[] toArray() {
		Object[] array = new Object[size];
	
		this.forEach(value -> {				//lambda izraz kojim implementiramo sučelje Processor na način da za svaki element koji primi 
			int index = 0;					//kao argument metode process spremi u polje array i poveća index kako bi ga pripremila za sljedeći element
			array[index++] = value;
		});
		
		return array;
	}
	
	
	@Override
	/*
	 * Listu brišemo na način da prolazimo cijelom listom te u svakoj iteraciji petlje spremimo u varijablu temp referencu na 
	 * sljedeći čvor trenutnog čvora te postavimo da previous i next od trenutnog čvora su null, a zatim i sami čvor stavimo da je null.
	 * Nakon toga u varijablu koja nam služi za iteriranje po listi postavimo vrijednost od varijable temp.
	 */
	public void clear() {
		while(first != null) {
			ListNode temp = first.getNext();
			first.setPrevious(null);
			first.setNext(null);
			first = null;
			first = temp;
		}
		
		last = null;
		size = 0;
		++modificationCount;
	}
	
	/**
	 * Method to get an element from Linked List Indexed Collection at desired index. 
	 * @param index - Integer which says at which index you want to get element at.
	 * @return Element at desired index.
	 * @throws IndexOutOfBoundsException if index is smaller than 0 or bigger than size - 1.
	 */
	
	/*
	 * Metodu get koja vraća element na nekon indexu radimo tako da nakon provjere indexa prolazimo kroz cijelu listu te povećavamo neki
	 * temp index te kada se vrijednosti tih indexa izjednače mozemo se zaustaviti i vratiti vrijednost.  
	 */
	
	public Object get(int index) {
		if(index < 0 || index > size-1) {
			throw new IndexOutOfBoundsException("Wrong index");
		} else {
			int tempIndex = 0;
			ListNode node = first;
			
			while(tempIndex != index) {
				node = node.getNext();
				++tempIndex;
			}
			
			return node.getValue();
		}
	}
	
	/**
	 * Inserts new object at desired position in collection.
	 * @param value - object to insert in collection
	 * @param position - index at which the element is inserted.
	 * @throws IndexOutOfBoundsException if position is smaller than 0 or bigger than current size of collection.
	 * @throws NullPointerException if value is null.
	 */
	
	/*
	 *Metodu insert radimo tako da prvo provjerimo ulazne argumente, a zatim razdvajamo sljedeće slučajeve:
	 *				a) novi čvor dodajemo na početak 
	 *				b) novi čvor dodajemo na kraj
	 *				c) novi čvor dodajemo negdje u sredinu 
	 */
	public void insert(Object value, int position) {
		if(position < 0 || position > size) {
			throw new IndexOutOfBoundsException("Wrong index");
		} else if(value == null) {
			throw new NullPointerException("Cannot add object which is null");
		} else {
			ListNode node = new ListNode(value);
			if(position == 0) {				//ako treba čvor dodati na početak moramo postaviti da njegov next pokazuje na trenutni prvi
				node.setNext(first);		//čvor, a previous od prvog postaje novi čvor te prvi čvor postaje novi čvor
				first.setPrevious(node);	
				first = node;
			} else if(position == size) {	//ako dodajemo na kraj liste onda next od trenutnog zadnjeg mora pokazivati na novi čvor
				node.setPrevious(last);		//te novi čvor mora imati previous koji je trenutni zadnji, a nakon toga zadnji čvor
				last.setNext(node);			//postaje novi čvor
				last = node;
			} else {			
				int index = 0;
				ListNode temp = first;		  //ako dodajemo negdje u sredini liste novi čvor radimo sličan postupak brisanju čvora 
											  //iz sredine tako da dođemo do željene pozicije umanjene za jedan
				while(index != position -1) { 
					temp = temp.getNext();
					++index;
				}
				
				node.setPrevious(temp);			     //nakon što smo došli do pozicije -1 postavljamo da je novom čvoru previous 
				node.setNext(temp.getNext());	     //čvor upravo na pozicija -1, next mu je next od čvora na pozicija -1
				temp.setNext(node);					 //također moramo popraviti čvor na pozicija -1, tj. njegov next postaje novi čvor, a
				node.getNext().setPrevious(node);	 //previous njegovog nexta postaje novi čvor
													 //na ovaj način smo napravili prespajanje čvorova
			}
		}
		
		++size;
		++modificationCount;
	}
	
	/**
	 * Gives an index at which one object is stored in collection.
	 * @param value - object whose position you want to know.
	 * @return Integer representing object's position in collection, -1 if object doesn't exist in collection.
	 */
	
	/*
	 * Metodu indexOf radimo tako da prolazimo po listi te uvećavamo index sve dok ne naiđemo na traženu vrijednost. Kada nađemo traženu
	 * vrijednost dobili smo prvi takav element u listi te njegovu poziciju.
	 */
	public int indexOf(Object value) {
		ListNode node = first;
		int index = 0;
		
		while(node != null) {
			if(node.getValue().equals(value)) return index;
			node = node.getNext();
			++index;
		}
		
		return -1;
	}
	
	/**
	 * Removes an element from collection at desired index.
	 * @param index - position of element you want to remove.
	 */
	
	/*
	 * Nakon provjere ulaznih parametara razlikujemo sljedeće slučajeve:
	 * 					a) moramo obrisati jedini čvor u listi
	 * 					b) moramo obrisati zadnji čvor u listi
	 * 					c) moramo obrisati prvi čvor u listi
	 * 					d) moramo obrisati čvor negdje u sredini
	 * Postupak brisanja je isti kao i kod metode remove(Object value)
	 */
	public void remove(int index) {
		if(index < 0 || index > size-1) throw new IndexOutOfBoundsException("Wrong index");
		
		int tempIndex = 0;
		ListNode node = first;
		++modificationCount;
		
		if(size == 1 && index == 0) {			
			first = last = null;
			size = 0;
			return;
		}
		
		if(index == size - 1) {					
			last = last.getPrevious();
			last.setNext(null);
			--size;
			return;
		}
		
		--size;
		
		if(index == 0) {
			first = first.getNext();
			first.setPrevious(null);
			return;
		}
		
		while(true) {
			if(index == tempIndex) break;
			++tempIndex;
			node = node.getNext();
		}
		
		node.getPrevious().setNext(node.getNext());
		node.getNext().setPrevious(node.getPrevious());
		node = null;
	}
	
	/**
	 * Private class which implements ElementsGetter interface for LinkedListIndexedCollection class.
	 * @author IvanPC
	 * @version 1.0
	 */
	
	/*
	 * ListElementsGetter je privatna statička klasa koja implementira ElementsGetter sučelje. U konstruktoru prima referencu na primjerak 
	 *  razreda LinkedListIndexedCollection kako bi imala pristup elementima kolekcije.
	 */
	private static class ListElementsGetter implements ElementsGetter {
		
		/**
		 * Reference to LinkedListIndexedCollection class instance.
		 */
		LinkedListIndexedCollection listCollection;
		/**
		 * Reference to the current node in LinkedListIndexedCollection which ListElementsGetter points to.
		 */
		ListNode node;
		
		private long savedModificationCount;
		
		/**
		 * Constructor which creates an instance of ArrayElementsGetter.
		 * @param listCollection - reference to LinkedListIndexedCollection class instance.
		 */
		ListElementsGetter(LinkedListIndexedCollection listCollection){
			this.listCollection = listCollection;
			this.savedModificationCount = listCollection.modificationCount;
			node = listCollection.first;		//postavljamo referencu node da pokazuje na početak liste
		}
		
		@Override
		public boolean hasNextElement() {
			if(savedModificationCount != listCollection.modificationCount) 	//provjera je li bilo modifikacije kolekcije
				throw new ConcurrentModificationException("Collection has been changed while using ElementsGetter.");
			
			if(node == null) return false;	//provjera je li referenca node pokazuje na ikakav element liste ili je došla do kraja
			
			return true;
		}

		@Override
		public Object getNextElement() {
			if(savedModificationCount != listCollection.modificationCount) 	//provjera je li bilo modifikacije kolekcije
				throw new ConcurrentModificationException("Collection has been changed while using ElementsGetter.");
			
			if(!hasNextElement()) throw new NoSuchElementException("There are no more elements in collection to return.");	//provjera ima li više elemenata za vratiti
			
			Object element = node.getValue();	
			node = node.getNext();			//priprema reference node za sljedeći poziv ove metode
			
			return element;
		}
	}
	
	/**
	 * Method which creates an ElementsGetter for this collection.
	 * @return new ElementsGetter for LinkedListIndexedCollection.
	 */
	public ElementsGetter createElementsGetter() {
		return new ListElementsGetter(this);
	}

}
