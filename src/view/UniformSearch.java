package view;

public class UniformSearch extends HeuristicAlgorithm {

	@Override
	void fOfNeighbor(Cell cell) {
		cell.f = cell.g;
	}

	@Override
	void hOfNeighbor(Cell cell) {
		return;
	}	
}
