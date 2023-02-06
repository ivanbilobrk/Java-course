package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentDB {
	
	public static void main(String[] args) throws IOException {
		
		List<String> lines = Files.readAllLines(
				 Paths.get("src/main/java/hr/fer/oprpp1/hw04/db/database.txt"),
				 StandardCharsets.UTF_8
				);
		
		StudentDataBase base = new StudentDataBase(lines);
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			try {
				String query = sc.nextLine();
				query = query.strip();
			
				if(query.equals("exit")) {
					System.out.println("Goodbye!");
					sc.close();
					System.exit(0);
				}
				
				//provjera je li se na prvom mjestu nalazi string query
				if((query.indexOf("query")) != 0) {
					throw new IllegalStateException("Krivi upit.");
				}
			
				//na 5 mjestu mora biti ili space ili tab
				if(query.length() > 5 && query.charAt(5) != ' ' && query.charAt(5) != '\t') {
					throw new IllegalStateException("Krivi upit.");
				}
				
				if(query.length() > 5) {
					query = query.substring(5);
				} else {
					query="";
				}
				
			
				QueryParser qp = new QueryParser(query);
			
				List<ConditionalExpression> conditions = qp.getQuery();
			
				List<StudentRecord> result = getRecords(conditions, base);

				System.out.println(formatOutput(result));
			} catch(Exception e) {
				System.out.println(e);
			}
			
			
		}
	}
	
	public static List<StudentRecord> getRecords(List<ConditionalExpression> conditions, StudentDataBase base){
		List<StudentRecord> result = null;
		QueryFilter filterCustom = new QueryFilter(conditions);
		ConditionalExpression expr = null;
		
		if(conditions.size() == 1) {
			expr = conditions.get(0);
		}
		
		if(conditions.size() == 1 && expr!= null && expr.getFieldGetter() == FieldValueGetters.JMBAG && 
				expr.getComparisonOperator() == ComparisonOperators.EQUALS) {
			result = new ArrayList<>();
			result.add(base.forJMBAG(conditions.get(0).getStringLiteral()));
			System.out.println("Using index for record retrieval.");
		} else {
			result = base.filter(filterCustom); //nije direktan upit te podatke tražimo sekvencijski
		}
		
		return result;
	}
	
	public static String formatOutput(List<StudentRecord> result) {
		if(result.size() == 0) {
			return "Records selected: 0";
		} else {
			int longestJmbag = result.stream().map((record)-> record.getJmbag().length()).mapToInt(Integer::intValue).max().getAsInt();
			int longestLastName = result.stream().map((record)->record.getLastName().length()).mapToInt(Integer::intValue).max().getAsInt();
			int longestFirstName = result.stream().map((record)->record.getFirstName().length()).mapToInt(Integer::intValue).max().getAsInt();
			//ocjena je fiksne veličine uvijek
		
			StringBuilder sb = new StringBuilder();
		
			sb.append("+");
			for(int i = 0; i < longestJmbag+2; i++) {
				sb.append("=");
			}
		
			sb.append("+");
		
			for(int i = 0; i < longestLastName+2; i++) {
				sb.append("=");
			}
		
			sb.append("+");
		
			//+====+=====+=========+
		
			for(int i = 0; i < longestFirstName+2; i++) {
				sb.append("=");
			}
			
			sb.append("+");
		
			sb.append("===+");
			sb.append("\n");
		
			String dekor = sb.toString();
		
			StringBuilder sb2 = new StringBuilder();
		
			//| jmbag | lastname | firstname | grade |
			for(var r: result) {
				sb2.append("| "+r.getJmbag());
				int spaces = longestJmbag - r.getJmbag().length() + 1;
				for(int i = 0; i < spaces; i++) {
					sb2.append(" ");
				}
				sb2.append("| "+r.getLastName());
			
				spaces = longestLastName - r.getLastName().length() + 1;
			
				for(int i = 0; i < spaces; i++) {
					sb2.append(" ");
				}
				sb2.append("| "+r.getFirstName());
			
				spaces = longestFirstName - r.getFirstName().length() + 1;
			
				for(int i = 0; i < spaces; i++) {
					sb2.append(" ");
				}
			
				sb2.append("| "+r.getFinalGrade()+" |");
				sb2.append("\n");
			
			}
			
			return dekor+sb2.toString()+dekor;
		}
	}
}
