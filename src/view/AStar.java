package view;

public class AStar extends HeuristicAlgorithm {

	@Override
	void fOfNeighbor(Cell cell) {
		cell.f=cell.g+cell.h;
	}

	@Override
	void hOfNeighbor(Cell cell) {
		double dx = Math.abs(cell.self.x-goalpoint.x);
		double dy = Math.abs(cell.self.y-goalpoint.y);
		//manhattan distance
		//cell.h=(dx+dy) + (Math.sqrt(2)-2) * Math.min(dx, dy);
		//euclidean distance
		cell.h = Math.sqrt(dx*dx + dy*dy);
	}




}
