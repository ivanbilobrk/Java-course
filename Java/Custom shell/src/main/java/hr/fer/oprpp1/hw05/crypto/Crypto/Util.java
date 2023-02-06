package hr.fer.oprpp1.hw05.crypto.Crypto;

import java.util.Arrays;
import java.util.List;

/**
 * Util class with static methods for converting hex input to bytes or bytes to hex.
 * @author Ivan Bilobrk
 *
 */
public class Util {
	  
	/**
	 * Constant List representing all hexadecimal numbers.
	 */
	private final static List<String> listaHex = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f");

		
		/**
		 * Method which converts given string argument to byte array. 
		 * @param keyText - string you want to convert to byte array
		 * @return byte array representation of string argument.
		 * @throws IllegalArgumentException if string argument length is not even or if string contains symbols which are not hexadecimal numbers.
		 */
		public static byte[] hextobyte(String keyText) {
			
			keyText = keyText.toLowerCase();
			int keylength = keyText.length();
			
			if(keylength % 2 != 0) throw new IllegalArgumentException("Invalid length of keyText parameter.");
			
			int sizeNew = keylength/2;
			
			byte[] byteArray = new byte[sizeNew];
			
			for(int i = 0; i < keylength; i+= 2) {
				if(!listaHex.contains(""+keyText.charAt(i))) {
					throw new IllegalArgumentException("Invalid keyText parameter.");
				}
				
				byteArray[i/2] = (byte) ((byte)((listaHex.indexOf(""+keyText.charAt(i))) << 4 ) + (byte)(listaHex.indexOf(""+keyText.charAt(i+1))));
		
			}
			return byteArray;
		}
		
		/**
		 * Static method which converts given byte array to string.
		 * @param bytearray - byte array you want to convert to string
		 * @return String representation in hexadecimal numbers of given byte array.
		 */
		public static String bytetohex(byte[] bytearray) {
			
			//svaki byte će se pretvorit u dva znaka pa je polje duple veličine ulaznog polja
			char[] hexChars = new char[bytearray.length * 2];  
			
			for (int i = 0; i < bytearray.length; i++) { 
				
				int v = bytearray[i] & 0xFF;  
				
				hexChars[i * 2] = listaHex.get(v >>> 4).charAt(0);  
				hexChars[i * 2 + 1] = listaHex.get(v & 0x0F).charAt(0); 
			}
			
			return new String(hexChars);  
			
		}
		
		
}
