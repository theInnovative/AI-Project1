package view;

import java.awt.Point;

public class Cell {
	short type= 1;
	boolean path = false;
	boolean route = false;
	double f, g = Double.POSITIVE_INFINITY, h;
	Point parent = null;
	
	public Cell(){}
}
