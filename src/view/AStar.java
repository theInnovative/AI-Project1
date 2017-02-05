package view;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class AStar extends HeuristicAlgorithm {

	@Override
	double findPath(Point start, Point goal, Cell[][] gV, SimGUI grid)
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

	        if (current.parent == goal) {
	            break;
	        }

	        open.remove(current);
	        closed.add(current);
	        getNeighbors(gV, 160, 120, current);
	        for (Cell neighbor : current.neighbors) {
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
	                neighbor.h = getManhattanDist(neighbor.pos, goal);
	                neighbor.f = neighbor.g + neighbor.h;
	                neighbor.parent = current.pos;
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
			tracer=gV[tracer.parent.x][tracer.parent.y];
		}

		return totalCost;
	}

	//return manhattan distance between two points
	public int getManhattanDist(Point c, Point goal) {
	    return Math.abs(c.x - goal.x) + Math.abs(c.y - goal.y);
	}

	//populate neighbor list for a cell in gV at coordinates x,y
	public void getNeighbors(Cell[][] gV, int width, int height, Cell curr) {
		for (int i=-1 ; i<2 ; i++)
			for (int j=-1 ; j<2 ; j++)
			  if (i !=0 && j != 0){
			     int rx = curr.pos.x + i;
			     int ry = curr.pos.y + j;
			     if (rx >= 0 && ry >= 0 && rx < width && ry < height){
			    	 if(gV[rx][ry].type>0){
			    		 gV[rx][ry].g=1;
			    		 curr.neighbors.add(gV[rx][ry]);
			    	 }
			     }
			  }
        return;
    }
}
