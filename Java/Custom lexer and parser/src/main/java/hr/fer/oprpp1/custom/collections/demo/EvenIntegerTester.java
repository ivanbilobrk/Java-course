package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.Tester;

/**
 * Implementation of Tester interface
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class EvenIntegerTester implements Tester {
	
	/**
	 * Method which tests if Object obj is an even number
	 * @return true - if number is even, otherwise false.
	 */
	 public boolean test(Object obj) {
		 
		 if(!(obj instanceof Integer)) return false;
	 
		 Integer i = (Integer)obj;
		 return i % 2 == 0;
	 
	 }
	 
}