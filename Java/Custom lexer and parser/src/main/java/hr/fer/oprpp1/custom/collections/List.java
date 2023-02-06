package hr.fer.oprpp1.custom.collections;

/**
 * Interface with methods for List.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public interface List extends Collection{
	
	public Object get(int index);
	
	public void insert(Object value, int position);
	
	public int indexOf(Object value);
	
	public void remove(int index);

}
