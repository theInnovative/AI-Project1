package view;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public abstract class HeuristicAlgorithm {
	HashSet<Cell> closed;
	PriorityQueue<Cell> fringe;
	Point goalpoint;
	String name = "";

	public HeuristicAlgorithm(){
		closed = new HashSet<Cell>();
		fringe = new PriorityQueue<Cell>(11,
				(a,b) -> {if(a.f == b.f)
							return 0;
						else if(a.f < b.f)
							return -1;
						return 1;});
	}

	public class Stats{
		double totalCost;
		long runtime;
		double expanded;
		int cellsTraveled;
		long memUsed;

		public String toString(){
			return "\t" + name
					+ "\tTotal Cost: " + totalCost
					+ "\tRuntime: " + runtime
					+ "\tCells Traveled: " + cellsTraveled
					+ "\tExpanded: " + expanded
					+ "\tMemory Used: " + memUsed;
		}
	}

	abstract void fOfNeighbor(Cell cell);
	abstract void hOfNeighbor(Cell cell);

	protected Stats findPath(Point start, Point goal, Cell[][] gV, SimGUI grid) {
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
				s.runtime = System.currentTimeMillis() - startTime;
				s.memUsed=(closed.size() + fringe.size())*56;
				return s;
			}
			closed.add(tmp);
			//grid.setCell(tmp.self.y, tmp.self.x, Color.PINK);
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
					//Color c = grid.getColor(tmp.self.y, tmp.self.x);
					//if(c != Color.PINK && c != Color.MAGENTA)
					//	grid.setCell(tmp.self.y, tmp.self.x, Color.MAGENTA);
				}
			}
		}

		return neighbors;
	}

}
