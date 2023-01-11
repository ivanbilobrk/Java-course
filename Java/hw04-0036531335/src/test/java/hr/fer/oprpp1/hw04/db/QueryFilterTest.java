package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QueryFilterTest {
	
	@Test
	public void testQueryFilter() {
		QueryFilter filter = new QueryFilter(new QueryParser("firstName>\"A\" and firstName<\"C\" "
				+ "and lastName LIKE \"B*ć\" and jmbag>\"0000000002\"").getQuery());
		
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Ana", 4);
		
		assertEquals(true, filter.accepts(record));
	}
	
	@Test
	public void testQueryFilter2() {
		QueryFilter filter = new QueryFilter(new QueryParser("firstName>\"A\" and jmbag>\"0000000002\"").getQuery());
		
		StudentRecord record = new StudentRecord("0000000001", "Brezović", "Željko", 4);
		
		assertEquals(false, filter.accepts(record));
	}
	
	@Test
	public void testQueryFilter3() {
		QueryFilter filter = new QueryFilter(new QueryParser("firstName>\"A\" and jmbag>\"0000000002\" and lastName 	LIKE 	\"Be*\"").getQuery());
		
		StudentRecord record = new StudentRecord("0000000004", "Begović", "Željko", 4);
		
		assertEquals(true, filter.accepts(record));
	}
	
	
	
}
