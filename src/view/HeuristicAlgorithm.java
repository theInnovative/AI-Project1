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
	Stats stats;

	public HeuristicAlgorithm(){
		closed = new ArrayList<Cell>();
		fringe = new PriorityQueue<Cell>(11,
				(a,b) -> {if(a.f == b.f)
							return 0;
						else if(a.f < b.f)
							return -1;
						return 1;});
		stats = new Stats();
	}
	
	public class Stats{
		
		
		public String toString(){
			return null;
		}
	}

	abstract void fOfNeighbor(Cell cell);
	abstract void hOfNeighbor(Cell cell);

	protected double findPath(Point start, Point goal, Cell[][] gV, SimGUI grid) {
		Cell tmp = gV[start.x][start.y], tmp2;
		List<Cell> n = null;
		goalpoint=goal;

		tmp.g = 0;
		tmp.parent = tmp;

		fringe.clear();
		fringe.add(tmp);
		closed.clear();

		while(!fringe.isEmpty()){
			tmp = fringe.poll();
			if(tmp.self.equals(goal))
				return tmp.f;
			closed.add(tmp);
			grid.setCell(tmp.self.y, tmp.self.x, Color.PINK);
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

		return Double.POSITIVE_INFINITY;
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
		int diff = Math.abs(a.self.x - b.self.x) + Math.abs(b.self.y - b.self.y);
		double cost;

		if(diff == 1){
			cost = (a.type + b.type) / 2;
			if(a.path && b.path)
				cost /= 4;
		}else
			cost = (a.type + b.type) * Math.sqrt(2) / 2;

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
					Color c = grid.getColor(tmp.self.y, tmp.self.x);
					if(c != Color.PINK && c != Color.MAGENTA)
						grid.setCell(tmp.self.y, tmp.self.x, Color.MAGENTA);
				}
			}
		}

		return neighbors;
	}

}
