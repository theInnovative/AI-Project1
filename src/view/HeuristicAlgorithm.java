package view;

import java.awt.Point;
import java.util.List;

public abstract class HeuristicAlgorithm {
	List<Cell> closed;
	
	abstract double findPath(Point start, Point goal, Cell[][] gV, SimGUI grid);

}
