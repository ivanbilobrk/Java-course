package hr.fer.oprpp1.custom.collections.demo;

import java.util.Arrays;
import hr.fer.oprpp1.custom.collections.*;

/**
 * Test class with main method used for testing Collection implementations.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class Test {
	public static void main(String[] args) {
		
		List col1 = new ArrayIndexedCollection();
		List col2 = new LinkedListIndexedCollection();
		col1.add("Ivana");
		col2.add("Jasna");
		Collection col3 = col1;
		Collection col4 = col2;
		col1.get(0);
		col2.get(0);
		//col3.get(0); // neće se prevesti! Razumijete li zašto?
		//col4.get(0); // neće se prevesti! Razumijete li zašto?
		col1.forEach(System.out::println); // Ivana
		col2.forEach(System.out::println); // Jasna
		col3.forEach(System.out::println); // Ivana
		col4.forEach(System.out::println); // Jasna
		
		ArrayIndexedCollection arr = new ArrayIndexedCollection();
		arr.add("1");
		arr.add(23);
		arr.add(333);
		arr.forEach(System.out::println);

	}
}
