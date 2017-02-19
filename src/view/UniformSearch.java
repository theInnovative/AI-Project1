package view;

/**
 * Class representation of the Uniform Search algorithm,
 * an extension of the Heuristic algorithm class.
 *
 * @author Eric Cajuste
 * @author Thurgood Kilper
 */

public class UniformSearch extends HeuristicAlgorithm {

	public UniformSearch(){
		name = "Uniform:";
	}
	/**
	 * For Uniform search f(cell) = g(cell).
	 */
	@Override
	void fOfNeighbor(Cell cell) {
		cell.f = cell.g;
	}

	/**
	 * For Uniform search heuristics are not needed.
	 */
	@Override
	void hOfNeighbor(Cell cell) {
		return;
	}
}
