package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComparisonOperatorsTest {
	
	@Test
	public void testOperatorLess() {
		assertEquals(true, ComparisonOperators.LESS.satisfied("aabbzz", "abz"));
		assertEquals(true, ComparisonOperators.LESS.satisfied("Baiii", "Djh"));
		assertEquals(true, ComparisonOperators.LESS.satisfied("Čurić", "Željko"));
		assertEquals(true, ComparisonOperators.LESS.satisfied("Mišura", "Šamija"));
		
		assertEquals(false, ComparisonOperators.LESS.satisfied("abz", "aabbzz"));
		assertEquals(false, ComparisonOperators.LESS.satisfied("Djh", "Baiii"));
		assertEquals(false, ComparisonOperators.LESS.satisfied("Željko", "Čurić"));
		assertEquals(false, ComparisonOperators.LESS.satisfied("Šamija", "Mišura"));
	}
	
	@Test
	public void testOperatorGreater() {
		assertEquals(true, ComparisonOperators.GREATER.satisfied("abz", "aabbzz"));
		assertEquals(true, ComparisonOperators.GREATER.satisfied("Djh", "Baiii"));
		assertEquals(true, ComparisonOperators.GREATER.satisfied("Željko", "Čurić"));
		assertEquals(true, ComparisonOperators.GREATER.satisfied("Šamija", "Mišura"));
		
		assertEquals(false, ComparisonOperators.GREATER.satisfied("aabbzz", "abz"));
		assertEquals(false, ComparisonOperators.GREATER.satisfied("Baiii", "Djh"));
		assertEquals(false, ComparisonOperators.GREATER.satisfied("Čurić", "Željko"));
		assertEquals(false, ComparisonOperators.GREATER.satisfied("Mišura", "Šamija"));
	}
	
	@Test
	public void testOperatorLessOrEquals() {
		assertEquals(true, ComparisonOperators.LESS_OR_EQUALS.satisfied("aabbzz", "abz"));
		assertEquals(true, ComparisonOperators.LESS_OR_EQUALS.satisfied("Baiii", "Djh"));
		assertEquals(true, ComparisonOperators.LESS_OR_EQUALS.satisfied("Čurić", "Željko"));
		assertEquals(true, ComparisonOperators.LESS_OR_EQUALS.satisfied("Mišura", "Šamija"));
		
		assertEquals(false, ComparisonOperators.LESS_OR_EQUALS.satisfied("abz", "aabbzz"));
		assertEquals(false, ComparisonOperators.LESS_OR_EQUALS.satisfied("Djh", "Baiii"));
		assertEquals(false, ComparisonOperators.LESS_OR_EQUALS.satisfied("Željko", "Čurić"));
		assertEquals(false, ComparisonOperators.LESS_OR_EQUALS.satisfied("Šamija", "Mišura"));
		
		assertEquals(true, ComparisonOperators.LESS_OR_EQUALS.satisfied("aabbzz", "aabbzz"));
		assertEquals(true, ComparisonOperators.LESS_OR_EQUALS.satisfied("Baiii", "Baiii"));
	}
	
	@Test
	public void testOperatorGreaterOrEquals() {
		assertEquals(true, ComparisonOperators.GREATER_OR_EQUALS.satisfied("abz", "aabbzz"));
		assertEquals(true, ComparisonOperators.GREATER_OR_EQUALS.satisfied("Djh", "Baiii"));
		assertEquals(true, ComparisonOperators.GREATER_OR_EQUALS.satisfied("Željko", "Čurić"));
		assertEquals(true, ComparisonOperators.GREATER_OR_EQUALS.satisfied("Šamija", "Mišura"));
		
		assertEquals(false, ComparisonOperators.GREATER_OR_EQUALS.satisfied("aabbzz", "abz"));
		assertEquals(false, ComparisonOperators.GREATER_OR_EQUALS.satisfied("Baiii", "Djh"));
		assertEquals(false, ComparisonOperators.GREATER_OR_EQUALS.satisfied("Čurić", "Željko"));
		assertEquals(false, ComparisonOperators.GREATER_OR_EQUALS.satisfied("Mišura", "Šamija"));
		
		assertEquals(true, ComparisonOperators.GREATER_OR_EQUALS.satisfied("Željko", "Željko"));
		assertEquals(true, ComparisonOperators.GREATER_OR_EQUALS.satisfied("Šamija", "Šamija"));
	}
	
	@Test
	public void testOperatorEquals() {
		assertEquals(true, ComparisonOperators.EQUALS.satisfied("aabbzz", "aabbzz"));
		assertEquals(true, ComparisonOperators.EQUALS.satisfied("Baiii", "Baiii"));
		
		assertEquals(false, ComparisonOperators.EQUALS.satisfied("Željko", "Šamija"));
		assertEquals(false, ComparisonOperators.EQUALS.satisfied("Šamija", "dhdsu"));
	}
	
	@Test
	public void testOperatorNotEquals() {
		assertEquals(false, ComparisonOperators.NOT_EQUALS.satisfied("aabbzz", "aabbzz"));
		assertEquals(false, ComparisonOperators.NOT_EQUALS.satisfied("Baiii", "Baiii"));
		
		assertEquals(true, ComparisonOperators.NOT_EQUALS.satisfied("Željko", "Šamija"));
		assertEquals(true, ComparisonOperators.NOT_EQUALS.satisfied("Šamija", "dhdsu"));
	}
	
	@Test
	public void testOperatorLike() {
		assertEquals(true, ComparisonOperators.LIKE.satisfied("Bakamović", "B*ć"));
		assertEquals(true, ComparisonOperators.LIKE.satisfied("AAAA", "AA*AA"));
		
		assertEquals(false, ComparisonOperators.LIKE.satisfied("Zagreb", "Aba*"));
		assertEquals(false, ComparisonOperators.LIKE.satisfied("AAA", "AA*AA"));
		
		assertEquals(false, ComparisonOperators.LIKE.satisfied("AAA", "*izegzfe"));
		assertEquals(true, ComparisonOperators.LIKE.satisfied("AAA", "*"));
		assertEquals(true, ComparisonOperators.LIKE.satisfied("AAAbdjwhw", "AAA*"));
		
	}
	
}
