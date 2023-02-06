package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class used for handling students database.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class StudentDataBase {
	
	/**
	 * Internal list used for storing student data which is used for sequential reading.
	 */
	private List<StudentRecord> records;
	
	/**
	 * Internal map used for getting student data if we know a jmbag in O(1) time.
	 */
	private HashMap<String, StudentRecord> index;
	
	/**
	 * Constructor
	 * @param rows - list consisting of rows where each row represents data for one student.
	 * @throws IllegalStateException if there are duplicate students or if grade is not between 1 and 5.
	 */
	public StudentDataBase(List<String> rows) {
		records = new ArrayList<>();
		index = new HashMap<>();
		init(rows);
	}
	
	/**
	 * Private method used for initializing internal list and map of student records.
	 * @param rows - list consisting of rows where each row represents data for one student.
	 * @throws IllegalStateException if there are duplicate students or if grade is not between 1 and 5.
	 */
	private void init(List<String> rows) {
		
		records = rows.stream().map((row)->{
			String[] data = row.split("\\t");
			int ocjena = Integer.parseInt(data[3]);
			if(!(ocjena >= 1 && ocjena <= 5)) {
				throw new IllegalStateException("Ocjena mora biti između 1 i 5.");
			}
			
			StudentRecord record = new StudentRecord(data[0], data[1], data[2], ocjena);
			
			if(index.put(data[0], record) != null) {
				throw new IllegalStateException("Dupli student.");
			}
			
			return record;
			
		}).collect(Collectors.toList());
		
	}
	
	/**
	 * Method used for getting student record if we know jmbag. This method gets student data from map in O(1) time.
	 * @param jmbag of desired student.
	 * @return StudentRecord linked to jmbag.
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return index.get(jmbag);
	}
	
	/**
	 * Method used for sequential reading of student records from internal list and filtering the data.
	 * @param filter which tells this method which student records are allowed to be returned and which aren't.
	 * @return list of student records which satisfy filter conditions.
	 */
	public List<StudentRecord> filter(IFilter filter){
		ArrayList<StudentRecord> filteredRecords = new ArrayList<>();
		
		records.stream().forEach((record)->{
			if(filter.accepts(record)) filteredRecords.add(record);
		});
		
		return filteredRecords;
	}

	
}
