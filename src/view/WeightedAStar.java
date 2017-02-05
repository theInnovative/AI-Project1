package view;

import java.awt.Point;

public class WeightedAStar extends HeuristicAlgorithm {

	private double weight = 2.5;
	
	@Override
	void fOfNeighbor(Cell cell) {
		cell.f=cell.g+cell.h;
	}

	@Override
	void hOfNeighbor(Cell cell) {
		double dx = Math.abs(cell.self.x-goalpoint.x);
		double dy = Math.abs(cell.self.y-goalpoint.y);
		//manhattan distance
		//cell.h=(dx+dy) + (Math.sqrt(2)-2) * Math.min(dx, dy) * weight;
		//euclidean distance
		cell.h = Math.sqrt(dx*dx + dy*dy) * weight;
	}

}
