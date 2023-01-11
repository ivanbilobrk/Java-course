package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing complex numbers and providing methods useful for calculation.
 * @author Ivan Bilobrk
 *
 */
public class Complex {
	
	/**
	 * Double representing real part of complex number.
	 */
	private double re;
	
	/**
	 * Double representing imaginary part of complex number.
	 */
	private double im;
	
	public static final Complex ZERO = new Complex(0,0);
	public static final Complex ONE = new Complex(1,0);
	public static final Complex ONE_NEG = new Complex(-1,0);
	public static final Complex IM = new Complex(0,1);
	public static final Complex IM_NEG = new Complex(0,-1);
	
	/**
	 * Default constructor.
	 */
	public Complex() {
		
	}
	
	/**
	 * Constructor which creates a new complex number with desired real and imaginary part.
	 * @param re - real part of complex number
	 * @param im - imaginary part of complex number.
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}
	
	/**
	 * Method which calculates a module of complex number.
	 * @return module of complex number.
	 */
	public double module() {
		return Math.sqrt(Math.pow(this.re, 2) + Math.pow(this.im, 2));
	}
	
	/**
	 * Method used for multiplication of complex numbers.
	 * @param c - complex number you want to multiply with this complex number. 
	 * @return this*c.
	 */
	public Complex multiply(Complex c) {
		double newReal = this.re*c.re - this.im*c.im;
		double newComplex = this.im*c.re + this.re*c.im;
		return new Complex(newReal, newComplex);
	}
	
	/**
	 * Method used for division of complex numbers.
	 * @param c - complex number you want to use for division with this complex number.
	 * @return this/c.
	 */
	public Complex divide(Complex c) {
		double newReal = (this.re*c.re + this.im*c.im)/(Math.pow(c.re, 2) + Math.pow(c.im, 2));
		double newComplex = (this.im*c.re - this.re*c.im)/(Math.pow(c.re, 2) + Math.pow(c.im, 2));
		return new Complex(newReal, newComplex);
		
	}
	
	/**
	 * Method used for addition of complex numbers.
	 * @param c - complex number you want to use for addition.
	 * @return this+c.
	 */
	public Complex add(Complex c) {
		return new Complex(this.re + c.re, this.im + c.im);
	}
	
	/**
	 * Method used for subtraction of complex numbers.
	 * @param c - complex number you want to use for subtraction.
	 * @return this-c.
	 */
	public Complex sub(Complex c) {
		return new Complex(this.re - c.re, this.im - c.im);
	}
	
	/**
	 * Method used for negation of complex numbers.
	 * @return -this.
	 */
	public Complex negate() {
		return new Complex(-this.re, -this.im);
	}
	
	/**
	 * Method used to raise a complex number to a desired power.
	 * @param n - power you want to raise a complex number to. This argument must be positive.
	 * @return - this^n.
	 * @throws IllegalArgumentException if power is negative.
	 */
	public Complex power(int n) {
		
		if(n < 0) {
			throw new IllegalArgumentException("Power can't be negative.");
		}
		
		double theta = n*Math.atan2(this.im, this.re);
		double newModule = Math.pow(this.module(), n);
		return new Complex(newModule*Math.cos(theta), newModule*Math.sin(theta));
	}
	
	/**
	 * Method used to get complex number desired root.
	 * @param n - desired root you want to get for a complex number. This argument must be positive.
	 * @return - n-th root of this complex number.
	 * @throws IllegalArgumentException if root is negative.
	 */
	public List<Complex> root(int n) {
		
		if(n < 0) {
			throw new IllegalArgumentException("Power can't be negative.");
		}
		
		double newModule = Math.pow(this.module(), 1/n);
		double theta = Math.atan2(this.im, this.re);
		List<Complex> lista = new ArrayList<>();
		
		for(int i = 0; i < n; i++) {
			lista.add(new Complex(newModule*Math.cos((theta + 2*Math.PI*i)/n), newModule*Math.sin((theta + 2*Math.PI*i)/n)));
		}
		return lista;
	}
	
	@Override
	public String toString() {
		if(this.im >= 0) {
			return this.re + "+i"+this.im;
		} else {
			return this.re + "-i"+Math.abs(this.im);
		}
	}
}
