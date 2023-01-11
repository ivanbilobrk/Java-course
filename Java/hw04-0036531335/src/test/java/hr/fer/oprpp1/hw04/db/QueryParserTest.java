package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTest {
	
	@Test
	public void testQueryParser1() {
		QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");
		
		assertEquals(true, qp1.isDirectQuery());
		assertEquals("0123456789", qp1.getQueriedJMBAG());
		assertEquals(1, qp1.getQuery().size());
		
		ConditionalExpression expr = qp1.getQuery().get(0);
		
		assertEquals("0123456789", expr.getStringLiteral());
		assertEquals(FieldValueGetters.JMBAG, expr.getFieldGetter());
		assertEquals(ComparisonOperators.EQUALS, expr.getComparisonOperator());
	}
	
	@Test
	public void testQueryParser2() {
		QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
		
		assertEquals(false, qp2.isDirectQuery());
		assertEquals(2, qp2.getQuery().size());
		assertThrows(IllegalStateException.class, ()->{qp2.getQueriedJMBAG();});
		
		ConditionalExpression expr1 = qp2.getQuery().get(0);
		ConditionalExpression expr2 = qp2.getQuery().get(1);
		
		assertEquals("0123456789", expr1.getStringLiteral());
		assertEquals(FieldValueGetters.JMBAG, expr1.getFieldGetter());
		assertEquals(ComparisonOperators.EQUALS, expr1.getComparisonOperator());
		
		assertEquals("J", expr2.getStringLiteral());
		assertEquals(FieldValueGetters.LAST_NAME, expr2.getFieldGetter());
		assertEquals(ComparisonOperators.GREATER, expr2.getComparisonOperator());
	}
	
	@Test
	public void testQueryParser3() {
		QueryParser qp2 = new QueryParser("firstName>\"A\" an firstName<\"C\" and lastName LIKE \"B*ć\" and jmbag>\"0000000002\"");
		assertThrows(IllegalStateException.class, ()->{qp2.getQuery();});
		
		QueryParser qp3 = new QueryParser("firstName>\"A\" and firstName<\"C\" and lastame LIKE \"B*ć\" and jmbag>\"0000000002\" and finalGrade=3");
		assertThrows(IllegalStateException.class, ()->{qp3.getQuery();});
	}
	
}
