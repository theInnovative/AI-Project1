package view;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class SequentialAStar extends AStar{

	
	public SequentialAStar(int h) {
		super(h);
		
	}
	
	void key(Cell s, int i){
		switch(i){
			case 0:	s.key = eightWayManhattanDistance(s);
					break;
			case 1:	s.key = euclideanDistance(s);
					break;
			case 2: s.key = beelineDistance(s);
					break;
			case 3:
			case 4:
			case 5:
			default:s.key = eightWayManhattanDistance(s);
					break;
		}
		s.key += s.g;
		return;
	}

	protected void expandState(Cell s, int i, Cell[][] gV, SimGUI grid){
		openS[i].remove(s);
		List<Cell> n = null;
		Cell tmp;
		n = getNeighbors(s, gV, grid);
		for(int j = 0; j < n.size(); j++){
			tmp = n.get(j);
			double cost = s.g + cost(s, tmp);
			if(!closedS[i].contains(tmp)){
				tmp.g=Double.POSITIVE_INFINITY;
			}
			if(tmp.g > cost){
				tmp.g=cost;
				tmp.parent=s;
				if(!closedS[i].contains(tmp)){
					key(tmp, i);
					openS[i].add(tmp);
				}
			}
		}
	}

	//phase 2 sequential heuristic algorithm
	@Override
	protected Stats findPath(Point start, Point goal, Cell[][] gV, SimGUI grid, 
			GridPrinter.GPStruct gps){
		Stats s = new Stats();
		long startTime = System.currentTimeMillis();
		
		goalpoint=goal;
		Cell tmp = gV[start.x][start.y];
		gV[goal.x][goal.y].g=Double.POSITIVE_INFINITY;
		
		for(int i=0; i<5; i++){
			openS[i] = new PriorityQueue<Cell>(11,
					(a,b) -> {if(a.key == b.f)
						return 0;
					else if(a.key < b.key)
						return -1;
					return 1;});
			openS[i].clear();
			closedS[i] =  new HashSet<Cell>();
			closedS[i].clear();
			
			tmp.g = 0;
			tmp.parent = tmp;
			openS[i].add(tmp);
		}
		
		while(openS[0].poll().key<Double.POSITIVE_INFINITY){
			for(int i=1; i<5; i++){
				if(openS[i].poll().key<=openS[0].poll().key){
					if(gV[goal.x][goal.y].g<openS[i].poll().key){
						if(gV[goal.x][goal.y].g<Double.POSITIVE_INFINITY){
							s.runtime = (System.currentTimeMillis() - startTime)/1000.0;
							return s;
						}
					}else{
						tmp = openS[i].poll();
						expandState(tmp, i, gV, grid);
						closedS[i].add(tmp);
					}
				}else{
					if(gV[goal.x][goal.y].g<openS[0].poll().key){
						if(gV[goal.x][goal.y].g<Double.POSITIVE_INFINITY){
							s.runtime = (System.currentTimeMillis() - startTime)/1000.0;
							return s;
						}
					}else{
						tmp = openS[0].poll();
						expandState(tmp, 0, gV, grid);
						closedS[0].add(tmp);
					}
				}
			}
		}
		
		return null;
	}
	
}
