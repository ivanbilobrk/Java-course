package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;

/**
 * Custom layout manager implementation.
 * @author Ivan Bilobrk
 *
 */
public class CalcLayout implements LayoutManager2{
	
	/**
	 * Desired horizontal and vertical gap between components.
	 */
	private int gap;
	
	/**
	 * Map which contains all RCPositions of components.
	 */
	HashMap<RCPosition, Component> map = new HashMap<>();
	
	/**
	 * Default constructor which creates layout with 0 gap.
	 */
	public CalcLayout() {
		this(0);
	}
	
	/**
	 * Constructor which creates layout with desired gap.
	 * @param gap
	 */
	public CalcLayout(int gap) {
		this.gap = gap;
	}
	
	/**
	 * This method is not supported by this layout manager.
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException("Operacija nije podržana.");
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		
		//tražimo ključ za kojeg je vezana komponenta comp te ako nađemo ključ brišemo nađeni par ključ - vrijednost iz interne mape
		RCPosition key = containsComponent(comp);
		
		if(key != null) {
			map.remove(key);
		}
		
	}
	
	/**
	 * Method which calculates Dimensions of given Container. This method can calculate preferred, minimum and maximum size
	 * depending on a given code.
	 * @param parent - Container which uses this layout manager
	 * @param code - tells this method whether you want to get preferred size (code 0), minimum size (code 1) or maximum size (code 2) of parent Component.
	 * @return Dimension of Container parent.
	 */
    private Dimension getSize(Container parent, int code) {
    	Dimension size = new Dimension(0,0);
    	
    	//prolazimo sve komponente koje se nalaze u internoj mapi
    	for (var x : map.keySet()) {
    		Component c = map.get(x);
    		
    		//posebna pravila vrijede ako je komponenta na poziciji (1,1)
    		boolean isFirst = x.getColumn() == 1 && x.getRow() == 1 ? true : false;
    		
    		if (c != null) {
    			Dimension componentSize;
    			//ovisno o dobivenom kodu kroz argument računamo prikladnu veličinu
    			if (code == 0) {
    				componentSize = c.getPreferredSize();
    			} else if(code == 1) {
    				componentSize = c.getMinimumSize();
    			} else {
    				componentSize = c.getMaximumSize();
    			}
    			
    			//širina bilo koje komponente koja nije na poziciji (1,1)
    			int normalWidth = componentSize.width*7+6*this.gap;
    			//visina svake komponente
    			int normalHeight = componentSize.height*5+4*this.gap;
    			//širina komponente na poziciji (1,1)
    			int widthFirst = ((componentSize.width-4*this.gap)/5)*7+6*this.gap;
    			
    			//ako tražimo preferiranu veličinu ili minimalnu onda moramo gledati maksimume
    			if(code == 0 || code == 1) {
    				
    				if(!isFirst) {
        				size.width = Math.max(size.width, normalWidth);
    				} else {
    					size.width = Math.max(size.width, widthFirst);
    				}
    				size.height = Math.max(size.height, normalHeight);
    			} else {
    				//ako tražimo maksimalnu velučinu moramo gledati minimume jer ne želimo da nijedna komponenta dobije veličinu 
    				//koja je veća od njezine maksimalne
    				if(!isFirst) {
    					size.width = Math.min(size.width, normalWidth);
    				} else {
    					size.width = Math.min(size.width, widthFirst);
    				}
    				size.height = Math.min(size.height, normalHeight);
    			}

    		}
    	}
    	return size;
    }
    
    
    /**
     * Private method used to add insets of parent component when calculating minimum, 
     * maximum or preferred size of parent Component
     * @param parent - Container for which you want to calculate desired size (depending on code) with insets
     * @param code - tells this method whether you want to get preferred size (code 0), minimum size (code 1) or maximum size (code 2) of parent Component.
     * @return Dimension of parent Container with insets.
     */
    private Dimension sizeWithInsets(Container parent, int code) {
    	Dimension size = getSize(parent, code);
    	
    	Insets insets = parent.getInsets();
    	
    	size.width = size.width + insets.right + insets.left;
    	size.height = size.height + insets.top + insets.bottom;
    	
    	return size;
    }

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		//samo pozivamo metodu sizeWithInsets s kodom 0 jer želimo preferred layout size
		return sizeWithInsets(parent, 0);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		//samo pozivamo metodu sizeWithInsets s kodom 1 jer želimo minimum layout size
		return sizeWithInsets(parent, 1);
	}

	@Override
	public void layoutContainer(Container parent) {
		
        Dimension size = parent.getSize();
        Insets insets = parent.getInsets();
        
        //mičemo bilo kakve unutarnje bordere koje komponenta roditelja može imati
        int totalW = size.width - (insets.left + insets.right);
        int totalH = size.height - (insets.top + insets.bottom);
        
        //visina i širina jedne ćelije
        double totalCellW = (totalW - 6*gap)/ 7;
        double totalCellH = (totalH - 4*gap)/ 5;
		
        //prolazimo sve komponente te za svaku određujemo poziciju
		for(var k: map.keySet()) {
			Component comp = map.get(k);
			RCPosition rcp = k;
			
			//poseban slučaj ako je komponenta na poziciji (1,1)
			if(rcp.getColumn() == 1 && rcp.getRow() == 1) {
				double w = totalCellW*5 + 4*gap;
				comp.setBounds(insets.left, insets.top, (int)w, (int)totalCellH);
			} else {
				//sve ostale komponente imaju zajedničku vrijednost x koordinate
				double x = insets.left + (rcp.getColumn()-1)*totalCellW + (rcp.getColumn()-1)*gap;
				
				//slučaj za prvi red
				 if(rcp.getRow() == 1) {
						comp.setBounds((int)x, insets.top, (int)totalCellW, (int)totalCellH);
					} else {
						//ostali redovi imaju jedino y koordinatu drugačiju
						double y = insets.top + (rcp.getRow()-1)*totalCellH + (rcp.getRow()-1)*gap;
						comp.setBounds((int)x, (int)y, (int)totalCellW, (int)totalCellH);
					}
			}
		}
	}
	
	/**
	 * Private method used to check if there is a given Component comp in this layout.
	 * @param comp - Component you want to search if it is in this layout.
	 * @return RCPositon of given Component if it is in layout, otherwise null.
	 */
	private RCPosition containsComponent(Component comp) {
		
		RCPosition rcp = null;
		
		for(var x: map.keySet()) {
			if(map.get(x).equals(comp))
				rcp = x;
		}
		return rcp;
	}
	
	/**
	 * Method which adds desired Component comp to this layout.
	 * @throws IllegalArgumentException if given argument constraints is not a String or RCPosition object
	 * @throws CalcLayoutException if there is currently 31 component in this layout
	 * @throws CalcLayoutException if you want to add component to position with row < 1 or row > 5 or column < 1 or column > 7
	 * @throws CalcLayoutException if you want to add component to position with row 1 and column > 1 && column < 6
	 * @throws CalcLayoutException if you try to add Component with same constraint that is already present in this layout or 
	 * if you try to add same component which already exists in this layout.
	 */
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		
		if(!(constraints instanceof String || constraints instanceof RCPosition)) {
			throw new IllegalArgumentException("Constraints može biti samo string ili RCPosition objekt");
		}
		
		if(map.size() == 31) {
			throw new CalcLayoutException("Ne mogu dodati više od 31 element.");
		}
		
		RCPosition rcp = null;
		
		if(constraints instanceof String) {
			rcp = RCPosition.parse((String) constraints);
		} else if(constraints instanceof RCPosition){
			rcp = (RCPosition) constraints;
		}
		
		int column = rcp.getColumn();
		int row = rcp.getRow();
			
		if(row < 1 || row > 5 || column < 1 || column > 7) {
			throw new CalcLayoutException("Krivi parametri za poziciju.");
		}
			
		if(row == 1 && (column > 1 && column < 6)) {
			throw new CalcLayoutException("Krivi parametri za poziciju.");
		}
			
		if(map.get(rcp) != null) {
			throw new CalcLayoutException("Krivi parametri za poziciju, pozicija je već dodana.");
		}
		
		if(containsComponent(comp) != null) {
			throw new CalcLayoutException("Komponenta je već dodana.");
		}
		
		//nakon provjere svih argumenata možemo dodati novu komponentu
		map.put(rcp, comp);
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		//samo pozivamo metodu sizeWithInsets s kodom 2 jer želimo maximum layout size
		return sizeWithInsets(target, 2);
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
		// TODO Auto-generated method stub
		
	}

}
