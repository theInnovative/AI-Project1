package view;

public class AStar extends HeuristicAlgorithm {

	@Override
	void fOfNeighbor(Cell cell) {
		cell.f = cell.g + cell.h;
	}

	@Override
	void hOfNeighbor(Cell cell) {
		/*
		 * heuristics not net up yet
		 * 
		 * 
		 */
		cell.h = 0;
	}

}
