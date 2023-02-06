package hr.fer.zemris.math;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class representing one polynomial of complex numbers. Polynomial is of type zn*z^n+zn-1*z^(n-1)+...+z2*z^2+z1*z+z0.
 * @author Ivan Bilobrk
 *
 */
public class ComplexPolynomial {
	
	/**
	 * Array representing coefficients z0...zn of this polynomial.
	 */
	private Complex[] factors;
	
	/**
	 * Constructor which creates one ComplexPolynomial with given coefficients.
	 * @param factors
	 */
	public ComplexPolynomial(Complex ...factors) {
		this.factors = factors;
	}
	
	/**
	 * @return - order of this polynomial; eg. For (7+2i)z^3+2z^2+5z+1 returns 3.
	 */
	public short order() {
		return (short)(factors.length - 1);
	}
	
	/**
	 * Method used for multiplication of ComplexPolynomials.
	 * @param p ComplexPolynomial you want to multiply with this ComplexPolynomial.
	 * @return ComplexPolynomial which is the result of multiplication of two ComplexPolynomials.
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		
		Map<Integer, Complex> map = new TreeMap<>();
		int len1 = this.factors.length;
		int len2 = p.factors.length;
		
		for(int i = 0; i < len1; i++) {
			for(int j = 0; j < len2; j++) {
				map.merge(i+j, this.factors[i].multiply(p.factors[j]), (oldV, newV) -> oldV.add(newV));
			}
		}
		
		Complex[] array = new Complex[1];
		
		return new ComplexPolynomial(map.values().toArray(array));
	}
	
	/**
	 * Method which computes first derivative of this polynomial; for example, for
	 * (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
	 * @return - ComplexPolynomial representing first derivative of this ComplexPolynomial.
	 */
	public ComplexPolynomial derive() {
		int len = this.factors.length;
		Complex[] array = new Complex[len-1];
		
		for(int i = 1; i < len; i++) {
			array[i-1] = factors[i].multiply(new Complex(i, 0));
		}
		
		return new ComplexPolynomial(array);
	}
	
	/**
	 * Method which computes polynomial value at given point z.
	 * @param z
	 * @return - complex number which is the polynomial value at given point z.
	 */
	public Complex apply(Complex z) {
		int len = factors.length;
		
		if(len > 0) {
			Complex result = factors[0];
			
			for(int i = 1; i < len; i++) {
				result = result.add(factors[i].multiply(z.power(i)));
			}
			return result;
		} else {
			return new Complex(0, 0);
		}
	}
	
	@Override
	public String toString() {
		String result = "f(z) = ";
		
		int len = factors.length;
		
		for(int i = len-1; i >= 0; i--) {
			if(i == 0) {
				result += "+("+factors[i].toString()+")";
			} else if(i == len-1){
				result += "("+factors[i].toString() + ")*z^"+i;
			} else {
				result += "+("+factors[i].toString() + ")*z^"+i;
			}
		}
		
		return result;
		
	}
}
