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

	boolean path = false, route = false;
	double f, g, h;
	Point self = null;
	Cell parent;

	public Cell(int x, int y){
		self = new Point(x,y);
	}

	@Override
	public boolean equals(Object o){
		if(!(o instanceof Cell))
			return false;
		Cell c = (Cell) o;
		return c.self.equals(self);
	}
}
