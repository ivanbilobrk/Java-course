package hr.fer.zemris.java.gui.prim;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Class which implements ListModel which stores prime numbers and generates them.
 * @author Ivan Bilobrk
 *
 */
public class PrimListModel implements ListModel<Integer> {
	
	/**
	 * List holding all prime numbers of this model.
	 */
	private List<Integer> elementi = new ArrayList<>();
	
	/**
	 * All listeners for this model.
	 */
	private List<ListDataListener> promatraci = new ArrayList<>();
	
	@Override
	public void addListDataListener(ListDataListener l) {
		promatraci.add(l);
	}
	@Override
	public void removeListDataListener(ListDataListener l) {
		promatraci.remove(l);
	}
	
	@Override
	public int getSize() {
		return elementi.size();
	}
	@Override
	public Integer getElementAt(int index) {
		
		if(index >= getSize()) {
			throw new IllegalArgumentException("Illegal index.");
		}
		return elementi.get(index);
	}
	
	/**
	 * Method to check if number is prime.
	 * @param num - number which you want to check if it is prime.
	 * @return true if number is prime, false otherwise.
	 */
	private boolean isPrime(int num) {
        if(num<=1)
        {
            return false;
        }
       for(int i=2;i<=num/2;i++)
       {
           if((num%i)==0)
               return  false;
       }
       return true;
    }
	
	/**
	 * Method which generates next prime number of this model and puts it at the end of a list.
	 */
	public void next() {
		int len = elementi.size();
		
		if(len == 0) {
			elementi.add(1);
		} else {
			int current = elementi.get(len-1);
			
			while(true) {
				current += 1;
				if(isPrime(current)) {
					elementi.add(current);
					break;
				}
			}
	}
		
		int pos = len;
		
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, pos, pos);
		
		for(ListDataListener l : promatraci) {
			l.intervalAdded(event);
		}
		
	}

}