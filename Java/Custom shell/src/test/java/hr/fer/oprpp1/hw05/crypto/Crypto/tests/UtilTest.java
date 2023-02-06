package hr.fer.oprpp1.hw05.crypto.Crypto.tests;
import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.hw05.crypto.Crypto.Util;

import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {
	@Test
	public void testHexToByte() {
		assertArrayEquals(new byte[] {1, -82, 34}, Util.hextobyte("01aE22"));
		assertArrayEquals(new byte[] {-128, 127}, Util.hextobyte("807f"));
		assertArrayEquals(new byte[] {}, Util.hextobyte(""));
		assertThrows(IllegalArgumentException.class, ()->Util.hextobyte("807"));
	}
	
	@Test
	public void testByteToHex() {
		assertEquals("01ae22", Util.bytetohex(new byte[] {1, -82, 34}));
		assertEquals("807f", Util.bytetohex(new byte[] {-128, 127}));
		assertEquals("", Util.bytetohex(new byte[] {}));
	}
}
