package hr.fer.oprpp1.hw04.db;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Tests for StudentDataBase class.
 * @author Ivan Bilobrk
 * @version 1.0
 *
 */
public class StudentDataBaseTest {
	

	@Test
	public void testForJmbag1() throws IOException {
		List<String> lines = Files.readAllLines(
				 Paths.get("src/main/java/hr/fer/oprpp1/hw04/db/database.txt"),
				 StandardCharsets.UTF_8
				);
		StudentDataBase base = new StudentDataBase(lines);

		assertEquals(new StudentRecord("0000000009", "Dean", "Nataša", 2), base.forJMBAG("0000000009"));
		assertEquals(new StudentRecord("0000000017", "Grđan", "Goran", 2), base.forJMBAG("0000000017"));
		assertEquals(new StudentRecord("0000000001", "Akšamović", "Marin", 2), base.forJMBAG("0000000001"));
		assertEquals(new StudentRecord("0000000063", "Žabčić", "Željko", 4), base.forJMBAG("0000000063"));
		assertEquals(new StudentRecord("0000000049", "Saratlija", "Branimir", 2), base.forJMBAG("0000000049"));
	}
	
	IFilter filterAll = (record) -> record.getFinalGrade() < 10;
	IFilter filterNone = (record) -> record.getFinalGrade() > 5;
	
	@Test
	public void testForJmbag2() throws IOException {
		List<String> lines = Files.readAllLines(
				 Paths.get("src/main/java/hr/fer/oprpp1/hw04/db/database.txt"),
				 StandardCharsets.UTF_8
				);
		StudentDataBase base = new StudentDataBase(lines);
		
		List<StudentRecord> listAll = base.filter(filterAll);
		List<StudentRecord> listNone = base.filter(filterNone);
		
		assertEquals(63, listAll.size());
		assertEquals(0, listNone.size());
	}
	
	
	
}
