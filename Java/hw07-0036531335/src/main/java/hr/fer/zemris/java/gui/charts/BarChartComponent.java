package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/**
 * GUI component for showing chart. 
 * @author Ivan Bilobrk
 *
 */
public class BarChartComponent extends JComponent{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Chart object holding all values which are shown on chart.
	 */
	private BarChart chart;

	/**
	 * Constructor.
	 * @param chart
	 */
	public BarChartComponent(BarChart chart) {
		super();
		this.chart = chart;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		Insets ins = getInsets();
		Dimension dim = getSize();
		Rectangle r = new Rectangle(
				ins.left, 
				ins.top, 
				dim.width-ins.left-ins.right,
				dim.height-ins.top-ins.bottom);
		
		//crtanje pozadine po potrebi
		if(isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(r.x, r.y, r.width, r.height);
		}
		
		//dohvat metrike za font
		FontMetrics metrics = g2d.getFontMetrics();
		
		//pohrana y vrijednosti grafa u listu
		List<Integer> yValues = new ArrayList<>();
		
		yValues.add(chart.getyMin());
		int current = chart.getyMin();
		
		while(true) {
			current += chart.getSpace();
			if(current >= chart.getyMax()) {
				yValues.add(chart.getyMax());
				break;
			} else {
				yValues.add(current);
			}
		}
		
		//traženje najšire y vrijednosti koja se ispisuje sa strane grafa
		int maxWidth = 0;
		for(var x: yValues) {
			int tempWidth = metrics.stringWidth(""+x);
			if(tempWidth > maxWidth) {
				maxWidth = tempWidth;
			}
		}
		
		//početna y koordinata x strelice 
		int startXarrowY = r.height-2*metrics.getHeight()-15+r.y;
		//početna x koordinata x strelice 
		int startXarrowX =  r.x+maxWidth+10+metrics.getHeight();
		//duljina x strelice
		int arrowXLen = (int)(0.85*r.width);
		//crtanje x strelice
		g2d.drawLine(startXarrowX, startXarrowY, startXarrowX+arrowXLen, startXarrowY);
		
		//crtanje trokuta na vrhu x strelice
		int[] xPoints1 = {startXarrowX+arrowXLen, startXarrowX+arrowXLen, startXarrowX+arrowXLen+6};
		int[] yPoints1 = {startXarrowY-5, startXarrowY+5, startXarrowY};
		Polygon p = new Polygon(xPoints1, yPoints1, 3);
		g2d.fillPolygon(p);
		
		//početna x koordinata y strelice 
		int startYarrowX = r.x+maxWidth+15+metrics.getHeight();
		//početna y koordinata y strelice 
		int startYarrowY = startXarrowY + 5 +r.y;
		//duljina y strelice
		double arrowYLen = (int)(0.85*r.height);
		//crtanje x strelice
		g2d.drawLine(startYarrowX, startYarrowY, startYarrowX, (int)(startYarrowY-arrowYLen));
		
		//crtanje trokuta na vrhu y strelice
		int[] xPoints2 = {startYarrowX-5, startYarrowX+5, startYarrowX};
		int[] yPoints2 = {(int)(startYarrowY-arrowYLen), (int)(startYarrowY-arrowYLen), (int)(startYarrowY-arrowYLen-6)};
		g2d.fillPolygon(xPoints2, yPoints2, 3);
		
		//crtanje opisa x osi
		int xNameX = startYarrowX + (arrowXLen-15)/2 - (metrics.stringWidth(chart.getxDesc().strip()))/2;
		int xNameY = r.height-5;
		g2d.drawString(chart.getxDesc(), xNameX, xNameY);
		
		//crtanje opisa y osi
		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI / 2);
		AffineTransform old = g2d.getTransform();
		g2d.setTransform(at);
		double yNameX = -(startXarrowY+15 - ((arrowYLen-15)/2 - 0) + (metrics.stringWidth(chart.getYdesc().strip())/2));
		double yNameY = r.x+13;
		g2d.drawString(chart.getYdesc(), (int)yNameX, (int)yNameY);
		
		//vraćanje transformacije na staru vrijednost kako bi mogli nastaviti normalno crtati
		g2d.setTransform(old);
		
		//udlajenost među stupcima
		int spaceBetWeenColumns = 1;
		
		//duljina područja y strelice bez trokuta na vrhu te smanjeno za 8 kako ne bi prikazivali graf skroz do početka trokuta na vrhu
		double yAreaLen = (arrowYLen - 8);
		List<XYValue> listValue = chart.getList();
		int lenValues = listValue.size();
		
		//visina jedne ćelije u grafu tj. udaljenost između dvije susjedne y vrijednosti
		double rowHeight = (yAreaLen)/(yValues.size()-1);
		
		//prolazimo kroz sve y vrijednosti i ispisujemo string sa strane grafa
		int yvaluesLen = yValues.size();
		for(int i = yvaluesLen-1; i >= 0; i--) {
			int value = yValues.get(i);
			
			//x koordinata baseline-a
			int currentX = startXarrowX - 5 -  metrics.stringWidth(""+value);
			//y koordinata baseline-a, ali nije centrirana
			double currentYWithoutFont = (int) (r.height - rowHeight*(i) -  (r.height - startXarrowY));
			//y koordinata baseline-a, centrirana
			double currentYWithFont = currentYWithoutFont + metrics.getAscent()/2;
			
			//crtanje stringa y vrijednosti
			g2d.drawString(""+value, currentX, (int)currentYWithFont);
			//crtanje male crtice na grafu uz y vrijednost
			g2d.drawLine(startXarrowX, (int)currentYWithoutFont, startXarrowX+5, (int)currentYWithoutFont);
			
			//ako nismo na zadnjoj y vrijednosti crtamo i jednu horizontalnu liniju mreže
			if(i != 0) {
				g2d.setColor(new Color(240,225,196,255));
				g2d.drawLine(startXarrowX+5, (int)currentYWithoutFont, startXarrowX+arrowXLen, (int)currentYWithoutFont);
				g2d.setColor(Color.BLACK);
			}
		}
		
		//duljina područja x strelice bez trokuta na vrhu te smanjeno za 5 kako ne bi prikazivali graf skroz do početka trokuta na vrhu
		//i smanjeno za veličinu razmaka između stupaca
		int xAreaLen = (arrowXLen-spaceBetWeenColumns*(lenValues-1) - 8);
		//širina jednog stupca
		double columnWidth = xAreaLen/listValue.size();
		
		//početna x koordinata za stupce grafa
		int xColumn = (int) (spaceBetWeenColumns+startYarrowX);
		
		//prolazimo sve vrijednosti i crtamo stupce grafa, x vrijednosti uz donji rub te male crtice uz x vrijednosti
		for(int i = 0; i < lenValues; i++) {
			XYValue tempV = listValue.get(i);
			int number = tempV.getX();
			
			//crtanje brojeva na x osi
			int xCord = (int)(i*columnWidth+ columnWidth/2 + startYarrowX);
			int yCord = r.height-25;
			g2d.drawString(""+number, xCord, yCord);
			
			//računanje y koordinate stupca grafa
			double temp = (1.0*(tempV.getY()-chart.getyMin()))/(1.0*chart.getSpace());
	
			double yColumn = startXarrowY - (rowHeight*(temp));
			
			//visina stupca grafa
			double columnHeihgt = startXarrowY - yColumn;
			
			//crtanje stupca grafa
			g2d.setColor(new Color(244,119,72,255));
			g2d.fillRect(xColumn, (int)(yColumn-1), (int)columnWidth, (int)columnHeihgt);
			g2d.setColor(Color.BLACK);
			
			//računanje nove x koordinate za sljedeću iteraciju petlje
			xColumn = (int)(xColumn + columnWidth + spaceBetWeenColumns);
	
			//crtanje male crtice uz x vrijednost
			g2d.drawLine(xColumn-1, startXarrowY, xColumn-1, startXarrowY+5);
			
			//crtanje vertikalne linije mreže prikladne za trenutnu iteraciju
			g2d.setColor(new Color(240,225,196,255));
			g2d.drawLine(xColumn-1, startXarrowY, xColumn-1, (int)(startXarrowY-yAreaLen));
			g2d.setColor(Color.BLACK);
		}
		
	}
}
