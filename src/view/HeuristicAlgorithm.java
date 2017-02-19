package view;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public abstract class HeuristicAlgorithm {
	double avg = 0;
	List<Cell> closed;
	PriorityQueue<Cell> fringe;
	Point goalpoint;
	String name = "";
	GridPrinter.GPStruct gps;

	public HeuristicAlgorithm(){
		closed = new ArrayList<Cell>();
		fringe = new PriorityQueue<Cell>(11,
				(a,b) -> {if(a.f == b.f)
							return 0;
						else if(a.f < b.f)
							return -1;
						return 1;});
	}

	public class Stats{
		double totalCost = 0;
		double runtime = 0;
		double expanded = 0;
		double cellsTraveled = 0;
		double fringe = 0;
		
		public void addStats(Stats s){
			totalCost 		+= s.totalCost;
			runtime 		+= s.runtime;
			expanded		+= s.expanded;
			cellsTraveled	+= s.cellsTraveled;
			fringe			+= s.fringe;
		}
		
		public void average(int divisor){
			totalCost 		/= divisor;
			runtime 		/= divisor;
			expanded		/= divisor;
			cellsTraveled	/= divisor;
			fringe			/= divisor;
		}
		
		public String toString(){
			return "\t" + name
					+ "\tTotal Cost: " + totalCost
					+ "\tRuntime: " + runtime + "sec"
					+ "\tCells Traveled: " + cellsTraveled
					+ "\tCells Expanded: " + expanded
					+ "\tFringe Size: " + fringe;
		}
	}

	abstract void fOfNeighbor(Cell cell);
	abstract void hOfNeighbor(Cell cell);
	
	public Stats getNewStats(){
		return new Stats();
	}

	protected Stats findPath(Point start, Point goal, Cell[][] gV, SimGUI grid, 
			GridPrinter.GPStruct gps) {
		this.gps = gps;
		Cell tmp = gV[start.x][start.y], tmp2;
		List<Cell> n = null;
		goalpoint=goal;
		Stats s = new Stats();
		long startTime = System.currentTimeMillis();

		tmp.g = 0;
		tmp.parent = tmp;

		fringe.clear();
		fringe.add(tmp);
		closed.clear();

		while(!fringe.isEmpty()){
			tmp = fringe.poll();
			if(tmp.self.equals(goal)){
				s.totalCost = tmp.f;
				s.runtime = (System.currentTimeMillis() - startTime)/1000.0;
				return s;
			}
			closed.add(tmp);
			tmp.c = Color.PINK;
			gps.add(tmp.self, Color.PINK);
			
			s.expanded++;
			n = getNeighbors(tmp, gV, grid);
			for(int i = 0; i < n.size(); i++){
				tmp2 = n.get(i);
				if(!closed.contains(tmp2)){
					if(!fringe.contains(tmp2)){
						tmp2.g = Double.POSITIVE_INFINITY;
						tmp2.parent = null;
					}
					updateVertex(tmp, tmp2);
				}
			}
		}

		s.totalCost = Double.POSITIVE_INFINITY;
		s.runtime = System.currentTimeMillis() - startTime;
		return s;
	}

	protected void updateVertex(Cell home, Cell neighbor) {
		double c = home.g + cost(home, neighbor);
		if(c < neighbor.g){
			neighbor.g = c;
			neighbor.parent = home;
			hOfNeighbor(neighbor);
			fOfNeighbor(neighbor);
			fringe.remove(neighbor);
			fringe.add(neighbor);
		}
	}

	protected double cost(Cell a, Cell b){
		int diff = Math.abs(a.self.x - b.self.x) + Math.abs(a.self.y - b.self.y);
		double cost;

		//unblocked move cost
		if(diff == 1){
			cost = 1;
			//reduced highway cost
			if(a.path && b.path)
				cost /= 4;
		}else
			cost = Math.sqrt(2);

		//moving from hard to hard
		if(a.type>1 && b.type>1){
			cost *= 2;
		//moving between unblocked and hard
		}else if(a.type>1 || b.type>1)
			cost *= 1.5;

		return cost;
	}

	protected List<Cell> getNeighbors(Cell cell, Cell[][] gV, SimGUI grid){
		List<Cell> neighbors = new ArrayList<Cell>();
		Cell tmp = null;
		int x, y;

		if(cell == null)
			return null;

		for(int i = -1; i < 2; i++){
			for(int j = -1; j < 2; j++){
				x = cell.self.x + i;
				y = cell.self.y + j;

				if(x < 0 || y < 0 || x > 159 || y > 119)
					continue;
				tmp = gV[x][y];
				if(cell == tmp)
					continue;
				if(tmp.type != 0){
					neighbors.add(tmp);
					Color c = tmp.c;
					if(c != Color.PINK && c != Color.MAGENTA){
						tmp.c = Color.MAGENTA;
						gps.add(tmp.self, Color.MAGENTA);
					}
				}
			}
		}

		return neighbors;
	}

}
