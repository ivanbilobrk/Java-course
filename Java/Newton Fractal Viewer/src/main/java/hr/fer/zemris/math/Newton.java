package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

/**
 * Class used for drawing fractals derived from Newton-Raphson iteration.
 * @author Ivan Bilobrk
 *
 */
public class Newton {

	public static void main(String[] args) {
		
		Complex[] input = userInput();

		
		FractalViewer.show(new MojProducer(input));
		

	}
	
	/**
	 * Method which parses user input to create complex numbers for gui.
	 * @return - array of complex numbers which user has entered.
	 */
	public static Complex[] userInput() {
		
		List<Complex> lista = new ArrayList<>();
		Scanner sc = new Scanner(System.in);
		String line = "";
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
		
		do {
			line = sc.nextLine();
			line = line.strip();
			
			//provjere uvjeta 
			if(line.equals("done") && lista.size() < 2) {
				System.out.println("You need to enter at least two roots.");
			} else if(line.length() == 0) {
				System.out.println("Empty string is not legal complex number.");
			} else if(!line.equals("done")){
				String[] parts = line.split(" ");
				int real = 0;
				int imaginary = 0;
				int len = parts.length;
				
				if(len != 3 && len != 1) {
					System.out.println("Wrong format of complex number.");
				} else {
					for(int i = 0; i < len; i++) {
						//ako je trenutna vrijednost u polju predznak onda odmah citam vrijednost inputa za jednu vrijednost unaprijed 
						//te provjeravam predznake
						if(parts[i].equals("+") || parts[i].equals("-")) {
							String predznak = "";
							
							if(parts[i].charAt(0) == '-') {
								predznak += "-";
							}
							
							//provjera je li na sljedećem mjestu u inputu znak 'i'
							if(parts[i+1].charAt(0) == 'i') {
								//provjera je li user naveo samo 'i' ili je napisao i neki broj uz 'i'
								if(parts[i+1].length() == 1) {
									imaginary = Integer.parseInt(predznak+"1");
								} else {
									//imamo broj uz 'i' pa ga dodajemo zajedno s predznakom
									imaginary = Integer.parseInt(predznak + parts[i+1].substring(1));
								}
							}
							
							//kako smo gledali za jednu vrijednost unaprijed moramo uvećati 'i'
							i++;
						
							//slučaj ako je u liniji samo 'i' s nekim brojem ili bez broja te bez predznaka
						} else if(parts[i].charAt(0) == 'i') {
							if(parts[i].length() == 1) {
								imaginary = 1;
							} else {
								imaginary = Integer.parseInt(parts[i].substring(1));
							}
							
							//slučaj ako je u liniji jedna realna ili imaginarna vrijednost s predznakom
						} else if(parts[i].charAt(0) == '+' || parts[i].charAt(0) == '-') {
								String predznak = "";
								
								if(parts[i].charAt(0) == '-') {
									predznak += "-";
								}
								
								parts[i] = parts[i].substring(1);
								
								if(parts[i].charAt(0) == 'i') {
									if(parts[i].length() == 1) {
										imaginary = Integer.parseInt(predznak+"1");
									} else {
										imaginary = Integer.parseInt(predznak + parts[i].substring(1));
									}
								} else {
									real = Integer.parseInt(predznak + parts[i]);
								}
								
							//slučaj ako je u liniji samo realni broj
						} else {
							real = Integer.parseInt(parts[i]);
						}
					}
				}
				
				lista.add(new Complex(real, imaginary));
			}
			
			
		}while(!line.equals("done"));
		
		//stvaramo array na osnovu liste i to vraćamo
		Complex[] tempArray = new Complex[1];
		sc.close();
		return lista.toArray(tempArray);
	}
	
	/**
	 * Method which is used for calculation of fractals derived from Newton-Raphson iteration.
	 * @param reMin
	 * @param reMax
	 * @param imMin
	 * @param imMax
	 * @param width
	 * @param height
	 * @param yMin
	 * @param yMax
	 * @param m
	 * @param data
	 * @param cancel
	 * @param r1 - ComplexRootedPolynomial with desired complex coefficients
	 * @param derived - derived polynomial version of argument polynomial
	 * @param polynomial - ComplexPolynomial created from r1
	 */
	public static void calculate (double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, 
				int m, short[] data, AtomicBoolean cancel, ComplexRootedPolynomial r1, ComplexPolynomial derived, ComplexPolynomial polynomial) {
		
		int offset = yMin*width;
		
		for(int y = yMin; y <= yMax; y++) {
			if(cancel.get()) break;
			for(int x = 0; x < width; x++) {
				double cre = x / (width-1.0) * (reMax - reMin) + reMin;
				double cim = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;
				Complex c = new Complex(cre, cim);
				Complex zn = c;
				Complex znold = zn;
				double module = 0;
				int iters = 0;
				do {
					Complex numerator = polynomial.apply(zn);
					Complex denominator = derived.apply(zn);
					znold = zn;
					Complex fraction = numerator.divide(denominator);
					zn = zn.sub(fraction);
					module = zn.sub(znold).module();
					iters++;
				} while(iters < m && module > 0.001);
				int index = r1.indexOfClosestRootFor(zn, 0.002);
				data[offset++] = (short)(index+1);
			}
		}
	}
	
	/**
	 * Nested class used for communicating with gui after calculation is done.
	 * @author Ivan Bilobrk
	 *
	 */
	public static class MojProducer implements IFractalProducer{
		
		/**
		 * Array of Complex numbers used to create polynomials.
		 */
		private Complex[] roots;
		
		/**
		 * Constructor
		 * @param roots
		 */
		public MojProducer(Complex[] roots) {
			this.roots = roots;
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long requestNo, 
				IFractalResultObserver observer, AtomicBoolean cancel) {
			
			//stvaranje polinoma potrebnih za izračune
			ComplexRootedPolynomial r1 = new ComplexRootedPolynomial(Complex.ONE, roots);
			ComplexPolynomial polynomial = r1.toComplexPolynom();
			ComplexPolynomial derived = polynomial.derive();
			
			System.out.println("Zapocinjem izracun...");
			
			//maksimalni broj iteracija
			int m = 16*16*16;
			
			//polje u koje ćemo spremati rezulate koje se kasnije prosljeđuje gui-ju
			short[] data = new short[width * height];
			calculate(reMin, reMax, imMin, imMax, width, height, 0, height-1, m, data, cancel, r1, derived, polynomial);
			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data, (short)(polynomial.order()+1), requestNo);
			
		}
		
	}

}
