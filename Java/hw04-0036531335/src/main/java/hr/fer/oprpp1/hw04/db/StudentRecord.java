package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * Class representing one student record in database.
 * @author Ivan Bilobrk
 * @version 1.0
 */
public class StudentRecord {
	
	/**
	 * Jmbag of student.
	 */
	private String jmbag;
	
	/**
	 * Last name of student.
	 */
	private String lastName;
	
	/**
	 * First name of student.
	 */
	private String firstName;
	
	/**
	 * Final grade of student.
	 */
	private int finalGrade;
	
	/**
	 * Constructor
	 * @param jmbag
	 * @param lastName
	 * @param firstName
	 * @param finalGrade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		super();
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}
	
	/**
	 * Getter for jmbag.
	 * @return student's jmbag.
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Getter for last name.
	 * @return student's last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Getter for first name.
	 * @return student's first name.
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Getter for final grade.
	 * @return student's final grade.
	 */
	public int getFinalGrade() {
		return finalGrade;
	}


	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentRecord other = (StudentRecord) obj;
		return Objects.equals(jmbag, other.jmbag);
	}
	
	@Override
	public String toString() {
		return jmbag+" "+lastName+" "+firstName+" "+finalGrade;
	}
	
}
