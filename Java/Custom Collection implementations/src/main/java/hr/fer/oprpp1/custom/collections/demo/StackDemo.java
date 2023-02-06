package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;

/**
 * Class StackDemo is used for evaluating expressions in postfix representation.
 * It has a main method which accepts a single string as an argument which represents an expression to be evaluated.
 * All elements of expression should be separated by one space.
 * If expression is evaluated successfully it prints the result, else writes an error message.
 * @author IvanPC
 *	@version 1.0
 */
public class StackDemo {
	
	public static void main(String[] args) {
		
		/*
		 * Dobivanje izraza te stvaranje novog stoga.
		 */
		
		String expression = args[0];
		String[] elements = expression.split(" ");  //pojedine elemente izraza dobivamo koristeći metodu split
		ObjectStack stack = new ObjectStack();
		
		/*
		 * Prolazimo kroz sve elemente izraza te razilujemo brojeve od operatora
		 */
		
		
		for(String element: elements) {
			boolean isNumber = false;
			 final int len = element.length();
			 for (int i = 0; i < len; i++) {
				 if (!Character.isDigit(element.charAt(i)) && i == 0) {
					 continue;
				 } else if(Character.isDigit(element.charAt(i))) {
					 isNumber = true;
				 } else {
					 isNumber = false;
				 }
			 }
			 
			 if(isNumber) {
				 stack.push(Integer.valueOf(element));	//ako je riječ o broju samo ga stavimo na vrh stoga
			 } else {
				 int n1 = (int) stack.pop();			//ako je riječ o operatoru moramo napraviti određenu akciju tako da uzmemo dva 
				 int n2 = (int) stack.pop();			//elementa sa vrha stoga i rezultat stavimo na vrh stoga
				 
				 if(element.equals("+")) {
					 stack.push(n1+n2);
				 } else if(element.equals("-")) {
					 stack.push(n2-n1);
				 } else if(element.equals("*")) {
					 stack.push(n1*n2);
				 } else if(element.equals("/")) {
					 if(n1 == 0) throw new ArithmeticException("Division by zero"); 	//provjera ispravnosti izraza
					 stack.push(n2/n1);
				 } else if(element.equals("%")) {
					 if(n1 == 0) throw new ArithmeticException("Division by zero");		//provjera ispravnosti izraza
					 stack.push((int)(n2%n1));
				 }
			 }
		}
		
		//ispis rezultata te eventualne pogreške
		if(stack.size() != 1) {
			System.err.println("Wrong expression");
		} else {
			System.out.println(stack.pop());
		}
	}
}
