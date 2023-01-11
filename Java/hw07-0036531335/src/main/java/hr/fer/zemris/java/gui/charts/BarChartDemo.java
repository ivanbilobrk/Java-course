package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Demo class used to show chart component.
 * @author Ivan Bilobrk
 *
 */
public class BarChartDemo extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Path of a file where data for chart is stored.
	 */
	private String fileName;
	
	/**
	 * Chart component.
	 */
	private BarChart chart;
	
	/**
	 * Constructor.
	 * @param fileName
	 * @param chart
	 */
	public BarChartDemo(String fileName, BarChart chart) {
		this.fileName = fileName;
		this.chart = chart;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		initGUI();
	}
	
	/**
	 * Method to initialize GUI.
	 */
	private void initGUI() {
		//koristimo BorderLayout koji na vrh stavlja putanju do datoteke, a u sredinu graf
		this.getContentPane().setLayout(new BorderLayout());
		JLabel file = new JLabel(fileName, SwingConstants.CENTER);
		
		this.getContentPane().add(file, BorderLayout.PAGE_START);
		this.getContentPane().add(new BarChartComponent(chart), BorderLayout.CENTER);
	}
	
	public static void main(String[] args) throws IOException {
		
		//čitanje ulaznih parametara
		BufferedReader reader = Files.newBufferedReader(Path.of(args[0]));
		String fileName = args[0];
		String xName, yName;
		List<XYValue> list = new ArrayList<>();
		int yMin, yMax, space;
		
		xName = reader.readLine();
		yName = reader.readLine();
		
		String values = reader.readLine();
		String[] valueArray = values.split(" ");
		
		for(var x: valueArray) {
			String[] tempArray = x.split(",");
			XYValue value = new XYValue(Integer.parseInt(tempArray[0]), Integer.parseInt(tempArray[1]));
			list.add(value);
		}
		
		yMin = Integer.parseInt(reader.readLine());
		yMax = Integer.parseInt(reader.readLine());
		space = Integer.parseInt(reader.readLine());
		BarChart chart = new BarChart(list, xName, yName, yMin, yMax, space);
		
		SwingUtilities.invokeLater(()->{
			new BarChartDemo(fileName, chart).setVisible(true);
		});
		reader.close();
	}
}
