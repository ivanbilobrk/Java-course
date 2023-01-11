package hr.fer.zemris.math;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

/**
 * Class used for drawing fractals derived from Newton-Raphson iteration. This class uses multithreading to calculate faster.
 * @author Ivan Bilobrk
 *
 */
public class NewtonParallel {

	public static void main(String[] args) {
		boolean foundW = false;
		boolean foundT = false;
		int len = args.length;
		int workers = 0;
		int tracks = 0;
		
		//parsiranje argumenata programa
		for(int i = 0; i < len; i++) {
			if(args[i].contains("--workers=") && !foundW) {
				try {
					workers = extractNumber1(args[i], 10);
					foundW = true;	
				} catch(Exception e) {
					System.out.println("Wrong argument format for number of workers.");
					System.exit(1);
				}
			} else if(args[i].equals("-w") && !foundW) {
				try {
					workers = Integer.parseInt(args[i+1]);
					i++;
					foundW = true;
				} catch(Exception e) {
					System.out.println("Wrong argument format for number of workers.");
					System.exit(1);
				}
			} else if(args[i].contains("--tracks=") && !foundT) {
				try {
					tracks = extractNumber1(args[i], 9);
					foundT = true;	
				} catch(Exception e) {
					System.out.println("Wrong argument format for number of tracks.");
					System.exit(1);
				}
			} else if(args[i].equals("-t") && !foundT) {
				try {
					tracks = Integer.parseInt(args[i+1]);
					i++;
					foundT = true;
				} catch(Exception e) {
					System.out.println("Wrong argument format for number of tracks.");
					System.exit(1);
				}
			} else {
				System.out.println("Wrong arguments format.");
				System.exit(1);
			}
		}
		
		if(foundT && tracks < 1) {
			System.out.println("Minimal amount of tracks is 1.");
			System.exit(1);
		}
		
		if(!foundW) {
			workers = Runtime.getRuntime().availableProcessors();
		}
		
		
		if(!foundT) {
			tracks = 4*Runtime.getRuntime().availableProcessors();
		}
		
		Complex[] input = Newton.userInput();
		
		System.out.println("Broj threadova je "+workers);
		System.out.println("Broj poslova je "+tracks);
		
		FractalViewer.show(new MojProducer(workers, tracks, input));	

	}
	
	/**
	 * Helper method used for parsing program arguments.
	 * Extracts number from String s which is positioned in String starting from index len
	 * @param s 
	 * @param len
	 * @return - number extracted from String s
	 */
	private static int extractNumber1(String s, int len) {
		return Integer.parseInt(s.substring(len));
	}
	
	/**
	 * Nested class used for communicating with gui after calculation is done.
	 * @author Ivan Bilobrk
	 *
	 */
	public static class MojProducer implements IFractalProducer {
		
		/**
		 * Number of threads for calculating.
		 */
		private int workers;
		
		/**
		 * Number of jobs used in this calculation.
		 */
		private int tracks;
		
		/**
		 * Array of Complex numbers used to create polynomials.
		 */
		private Complex[] roots;
		
		/**
		 * Constructor
		 * @param workers
		 * @param tracks
		 * @param roots
		 */
		public MojProducer(int workers, int tracks, Complex[] roots) {
			this.workers = workers;
			this.tracks = tracks;
			this.roots = roots;
		}
		
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			
			System.out.println("Zapocinjem izracun...");
			
			//stvaranje polinoma potrebnih za izračune
			ComplexRootedPolynomial r1 = new ComplexRootedPolynomial(Complex.ONE, roots);
			ComplexPolynomial polynomial = r1.toComplexPolynom();
			ComplexPolynomial derived = polynomial.derive();
			
			//maksimalni broj iteracija
			int m = 16*16*16;
			
			//polje u koje ćemo spremati rezulate koje se kasnije prosljeđuje gui-ju
			short[] data = new short[width * height];
			
			//ukoliko je potrebno korigiramo broj poslova ako je user unio krivi broj
			if(tracks > height) {
				tracks = height;
			}
			
			//računamo kolika je visina svake trake
			int brojYPoTraci = height / tracks;
			
			//red za poslove dretvi
			final BlockingQueue<PosaoIzracuna> queue = new LinkedBlockingQueue<>();
			
			//stvaranje dretvi
			Thread[] radnici = new Thread[workers];
			for(int i = 0; i < radnici.length; i++) {
				radnici[i] = new Thread(new Runnable() {
					@Override
					public void run() {
						while(true) {
							PosaoIzracuna p = null;
							try {
								p = queue.take();
								if(p==PosaoIzracuna.NO_JOB) break;
							} catch (InterruptedException e) {
								continue;
							}
							p.run();
						}
					}
				});
			}
			
			//pokretanje dretvi
			for(int i = 0; i < radnici.length; i++) {
				radnici[i].start();
			}
			
			//stvaranje poslova za dretve i stavljanje u red 
			for(int i = 0; i < tracks; i++) {
				int yMin = i*brojYPoTraci;
				int yMax = (i+1)*brojYPoTraci-1;
				if(i==tracks-1) {
					yMax = height-1;
				}
				PosaoIzracuna posao = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, cancel, r1, derived, polynomial);
				while(true) {
					try {
						queue.put(posao);
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			
			//stvaranje poslova koji označavaju kraj rada dretvi i stavljanje tih poslova u red
			for(int i = 0; i < radnici.length; i++) {
				while(true) {
					try {
						queue.put(PosaoIzracuna.NO_JOB);
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			
			//čekanje da dretve završe sa radom
			for(int i = 0; i < radnici.length; i++) {
				while(true) {
					try {
						radnici[i].join();
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			
			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			
			//slanje podataka gui-ju
			observer.acceptResult(data, (short)(polynomial.order()+1), requestNo);
		}
	}
	
	/**
	 * Class which represents one job of a thread.
	 * @author Ivan Bilobrk
	 *
	 */
	public static class PosaoIzracuna implements Runnable {
		
		/**
		 * ComplexRootedPolynomial with desired complex coefficients.
		 */
		ComplexRootedPolynomial r1;
		
		/**
		 * ComplexPolynomial created from r1.
		 */
		ComplexPolynomial polynomial;
		
		/**
		 * Derived polynomial version of polynomial.
		 */
		ComplexPolynomial derived;
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		int yMin;
		int yMax;
		int m;
		short[] data;
		AtomicBoolean cancel;
		public static PosaoIzracuna NO_JOB = new PosaoIzracuna();
		
		/**
		 * Default constructor.
		 */
		private PosaoIzracuna() {
		}
		
		/**
		 * Constructor
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
		 * @param r1
		 * @param derived
		 * @param polynomial
		 */
		public PosaoIzracuna(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, 
				int m, short[] data, AtomicBoolean cancel, ComplexRootedPolynomial r1, ComplexPolynomial derived, ComplexPolynomial polynomial) {
			super();
			this.r1 = r1;
			this.derived = derived;
			this.polynomial = polynomial;
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
			this.cancel = cancel;
		}
		
		@Override
		public void run() {
			
			Newton.calculate(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, cancel, r1, derived, polynomial);
			
		}
	}

}
