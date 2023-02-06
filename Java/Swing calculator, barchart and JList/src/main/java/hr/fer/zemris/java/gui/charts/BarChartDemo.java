package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
		//JLabel file = new JLabel(fileName, SwingConstants.CENTER);
		
		//this.getContentPane().add(file, BorderLayout.PAGE_START);
		
		BarChartComponent chart1 = new BarChartComponent(chart);
		BarChartComponent chart2 = new BarChartComponent(chart);
		BarChartComponent chart3 = new BarChartComponent(chart);
		BarChartComponent chart4 = new BarChartComponent(chart);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(4, 1));
		
		JButton button1 = new JButton("Promjeni");
		button1.addActionListener(getListener(chart1));
		
		JButton button2 = new JButton("Promjeni");
		button2.addActionListener(getListener(chart2));
		
		JButton button3 = new JButton("Promjeni");
		button3.addActionListener(getListener(chart3));
		
		JButton button4 = new JButton("Promjeni");
		button4.addActionListener(getListener(chart4));
		
		JPanel charts = new JPanel();
		charts.setLayout(new GridLayout(2,2));
		
		charts.add(chart1);
		charts.add(chart2);
		charts.add(chart3);
		charts.add(chart4);
		
		buttons.add(button1);
		buttons.add(button2);
		buttons.add(button3);
		buttons.add(button4);
		
		this.getContentPane().add(buttons, BorderLayout.LINE_START);
		this.getContentPane().add(charts, BorderLayout.CENTER);
	}
	
	private ActionListener getListener(BarChartComponent chart) {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				chart.changeChart(randomChart());
				chart.revalidate();
				chart.repaint();

			}
		};
	}
	
	private BarChart randomChart(){

		String xName, yName;
		List<XYValue> old = chart.getList();
		int len = old.size();
		int yMin = chart.getyMin();
		int yMax = chart.getyMax();
		
		xName = chart.getxDesc();
		yName = chart.getYdesc();
		
		Random r = new Random();
		
		
		List<XYValue> newlist = new ArrayList<>();
		
		for(int i = 0; i < len; i++) {
			int x = old.get(i).getX();
			int y = r.nextInt(yMax-yMin) + yMin;
			XYValue value = new XYValue(x, y);
			newlist.add(value);
			
		}
		int space = chart.getSpace();

		BarChart chart = new BarChart(newlist, xName, yName, yMin, yMax, space);
		
		return chart;
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
