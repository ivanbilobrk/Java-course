package hr.fer.zemris.math;

/**
 * Class representing one polynomial of complex numbers. Polynomial is of type z0*(z-z1)*(z-z2)*...*(z-zn).
 * @author Ivan Bilobrk
 *
 */
public class ComplexRootedPolynomial {
	
	/**
	 * Complex number which represents z0 in polynomial.
	 */
	private Complex constant;
	
	/**
	 * Array of complex numbers which represent zeroes of a polynomial (z1...zn).
	 */
	private Complex[] roots;
	
	/**
	 * Constructor which creates one ComplexRootedPolynomial with given constant and roots.
	 * @param constant 
	 * @param roots
	 */
	public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
		this.constant = constant;
		this.roots = roots;
	}
	
	/**
	 * Method which computes polynomial value at given point z.
	 * @param z
	 * @return - complex number which is the polynomial value at given point z.
	 */
	public Complex apply(Complex z) {
		
		Complex result = constant;
		
		for(var x: roots) {
			constant = constant.multiply(z.sub(x));
		}
		
		return result;
	}
	
	/**
	 * Method which finds index of closest root for given complex number z that is within
	 * threshold; if there is no such root, returns -1.
	 * First root has index 0, second index 1, etc.
	 * @param z
	 * @param treshold
	 * @return - index of closest root for given complex number z.
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		boolean found = false;
		int currentIndex = 0;
		double currentMin = Double.MAX_VALUE;
		for(int i = 0; i < roots.length; i++) {
			double distance = calculateDistance(roots[i], z);
			
			if(i == 0 && distance <= treshold) {
				currentMin = distance;
				found = true;
			} else if(distance <= treshold && distance < currentMin){
				currentMin = distance;
				currentIndex = i;
				found = true;
			}
		} 
		
		return found ? currentIndex : -1;
	}
	
	/**
	 * Method which calculates distance between two complex numbers.
	 * @param z1
	 * @param z2
	 * @return - double representing distance between two complex numbers.
	 */
	private double calculateDistance(Complex z1, Complex z2) {
		Complex difference = z1.sub(z2);
		return difference.module();
	}
	
	/**
	 * Method which transforms this polynomial to ComplexPolynomial type.
	 * @return - ComplexPolynomial representation of this polynomial.
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial result = new ComplexPolynomial(constant);
		
		for(var x: roots) {
			result = result.multiply(new ComplexPolynomial(x.negate(), new Complex(1, 0)));
		}
		return result;
	}
	
	@Override
	public String toString() {
		String result = "f(z) = ("+constant.toString()+")";
		
		for(var x: roots) {
			result += "*(z-("+x.toString()+"))";
		}
		
		return result;
	}
	
}
