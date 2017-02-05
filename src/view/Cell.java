package view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

//blocked cell = type 0
//open cell = type 1
//hard cell = type 2

//f = g + h
//g = cost to move to neighbor cell
//h = estimated distance from cell to goal

public class Cell {
	short type= 1;
	boolean path = false;
	boolean route = false;
	double f, g = Double.POSITIVE_INFINITY, h;
	Point parent = null;

	Point pos = null;
	List<Cell> neighbors= new ArrayList<Cell>();

	public Cell(){}
	public Cell(int x, int y){
		this.pos=new Point(x, y);
	}
}
