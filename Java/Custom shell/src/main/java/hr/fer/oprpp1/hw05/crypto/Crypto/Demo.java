package hr.fer.oprpp1.hw05.crypto.Crypto;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Demo class with main method used for checking digests of files or encrypting and decrypting files.
 * @author Ivan Bilobrk
 *
 */
public class Demo {
	
public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		
		Scanner sc = new Scanner(System.in);
		
		//korisnik želi izračunati sažetak datoteke
		if(args[0].equals("checksha")) {
			System.out.println("Please provide expected sha-256 digest for "+ args[1] +":");
			String digest1;
			
			while(true) {
				digest1 = sc.nextLine();
				byte[] digest1Byte = Util.hextobyte(digest1);
				
				if(digest1Byte.length*8 != 256) {
					System.out.println("Digest always has 256 bits while using SHA-256 algorithm.");
				} else {
					break;
				}
			}
			
			byte[] byteArray = null;
			
			try {
				byteArray = checkSha(Paths.get(args[1]));
			} catch(Exception e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
			
			
			if(Arrays.equals(Util.hextobyte(digest1), byteArray)) {
				System.out.println("Digesting completed. Digest of "+ args[1] +" matches expected digest.");
			} else {
				System.out.println("Digesting completed. Digest of "+ args[1] +" does not match the expected digest. Digest was: "+
					Util.bytetohex(byteArray));
			}
		} else {
			//korisnik želi kriptirati ili dekriptirati datoteku
			boolean encrypt = false;;
			if(args[0].equals("encrypt")) {
				encrypt = true;
			} else if(args[0].equals("decrypt")) {
				
			} else {
				sc.close();
				throw new IllegalArgumentException("First argument can only be encrypt decrypt or checksha.");
			}
			
			System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
			
			String keyText = sc.nextLine();
			
			System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
			
			String ivText = sc.nextLine();
			sc.close();
			
			SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
			
			if(args[1].length() <= 0 || args[2].length() <= 0) {
				throw new IllegalArgumentException("Illegal path input and output string.");
			}
			
			try {
				encryptDecrypt(cipher, Paths.get(args[1]), Paths.get(args[2]));
				if(encrypt) {
					System.out.println("Encryption completed. Generated file "+ args[2] +" based on file "+args[1]+".");
				} else {
					System.out.println("Decription completed. Generated file "+ args[2] +" based on file "+args[1]+".");
				}
			} catch(Exception e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
			
			
		}
		
		
	}
	
	/**
	 * Static method for encrypting or decrypting files.
	 * @param cipher - cipher object used for encrypting or decrypting
	 * @param p1 - file which you want to encrypt or decrypt
	 * @param p2 - file where encrypted or decrypted Path p1 will be stored.
	 * @throws Exception if error occurs while encryption or decryption.
	 * @throws IllegalArgumentException if trying to encrypt or decrypt a directory.
	 */
	public static void encryptDecrypt(Cipher cipher, Path p1, Path p2) throws Exception {
		byte[] byteArray = new byte[4096];
		
		if(Files.isDirectory(p1) || Files.isDirectory(p2)) {
			throw new IllegalArgumentException("Cannot encrypt or decrypt a directory.");
		}
		
		try(InputStream is = Files.newInputStream(p1); OutputStream os = Files.newOutputStream(p2)) {
			
			int read;
			byte[] array = new byte[4096];
			int encrypted;
			//čitanje datoteke u dijelovima
			while((read = is.read(byteArray, 0, 4096)) != -1) {
				encrypted = cipher.update(byteArray, 0, read, array, 0);
				os.write(array, 0, encrypted);
			}
			os.write(cipher.doFinal());
			
			
		} catch(Exception e) {
			System.out.println("Error occured while using cipher.");
			throw new Exception(e);
		}
		
	}

	/**
	 * Static method for calculating digest of a file.
	 * @param p - file for which you want to calculate a digest.
	 * @return byte array representing digest of a file.
	 * @throws Exception if error occurs while reading from a file or calculating digest.
	 * @throws IllegalArgumentException if path argument is a directory.
	 */
	public static byte[] checkSha(Path p) throws Exception {
		byte[] byteArray = new byte[4096];
		
		if(Files.isDirectory(p)) {
			throw new IllegalArgumentException("Cannot calculate digest of a directory.");
		}
		
		try (InputStream is = Files.newInputStream(p)){
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			
			int read;
			//čitanje datoteke u dijelovima
			while((read = is.read(byteArray, 0, 4096)) != -1) {
				sha.update(byteArray, 0, read);
			}
			
			return sha.digest();
			
		} catch(Exception e) {
			System.out.println("Error occured while calculating digest.");
			throw new Exception(e);
		}
	}

	

}
