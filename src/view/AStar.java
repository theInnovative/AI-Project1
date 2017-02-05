package view;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class AStar extends HeuristicAlgorithm {

	@Override
	protected double findPath(Point start, Point goal, Cell[][] gV, SimGUI grid)
	{
		Set<Cell> open = new HashSet<Cell>();
	    Set<Cell> closed = new HashSet<Cell>();

		Cell strt = gV[start.x][start.y];
		strt.g=0;
		strt.h = getManhattanDist(start, goal);
		strt.f = strt.h;

		open.add(strt);

		while(true){
			Cell current=null;
			if (open.size() == 0) {
	            throw new RuntimeException("no route found");
	        }
			for (Cell cell : open) {
	            if (current == null || cell.f < current.f) {
	                current = cell;
	            }
	        }

	        if (current.parent.equals(gV[goal.x][goal.y])) {
	            break;
	        }

	        open.remove(current);
	        closed.add(current);

	        for (Cell neighbor : getNeighbors(current, gV)) {
	            if (neighbor == null) {
	                continue;
	            }

	            double nextG = current.g + neighbor.type;

	            if (nextG < neighbor.g) {
	                open.remove(neighbor);
	                closed.remove(neighbor);
	            }

	            if (!open.contains(neighbor) && !closed.contains(neighbor)) {
	                neighbor.g = nextG;
	                neighbor.h = getManhattanDist(neighbor.self, goal);
	                neighbor.f = neighbor.g + neighbor.h;
	                neighbor.parent = current;
	                open.add(neighbor);
	            }
	        }
			break;
		}

		//retrace and mark the path
		Cell tracer = gV[goal.x][goal.y];
		double totalCost = 0;
		while(tracer.parent != null){
			tracer.route=true;
			totalCost+=tracer.f;
			tracer=tracer.parent;
		}

		return totalCost;
	}

	//return manhattan distance between two points
	public int getManhattanDist(Point c, Point goal) {
	    return Math.abs(c.x - goal.x) + Math.abs(c.y - goal.y);

	}

	@Override
	void fOfNeighbor(Cell cell) {
		// TODO Auto-generated method stub

	}

	@Override
	void hOfNeighbor(Cell cell) {
		// TODO Auto-generated method stub

	}

}
