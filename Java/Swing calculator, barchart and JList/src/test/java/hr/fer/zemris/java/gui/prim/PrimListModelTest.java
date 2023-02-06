package hr.fer.zemris.java.gui.prim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrimListModelTest {
	
	@Test
	public void testAdd() {
		PrimListModel model = new PrimListModel();
		
		assertEquals(0, model.getSize());
		assertThrows(IllegalArgumentException.class, ()->{
			model.getElementAt(0);
		});
		
		model.next();
		assertEquals(1, model.getSize());
		assertEquals(1, model.getElementAt(0));
		assertThrows(IllegalArgumentException.class, ()->{
			model.getElementAt(1);
		});
		
		model.next();
		assertEquals(2, model.getSize());
		assertEquals(1, model.getElementAt(0));
		assertEquals(2, model.getElementAt(1));
		assertThrows(IllegalArgumentException.class, ()->{
			model.getElementAt(2);
		});
		
		model.next();
		model.next();
		model.next();
		model.next();
		assertEquals(6, model.getSize());
		assertEquals(7, model.getElementAt(4));
		assertEquals(11, model.getElementAt(5));
		assertThrows(IllegalArgumentException.class, ()->{
			model.getElementAt(6);
		});
	}
	
}
