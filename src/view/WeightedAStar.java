package view;

/**
 * Class representation of the Weighted A* algorithm,
 * extending the A* class which is also an extension
 * of the Heuristic Algorithm class.
 *
 * @author Eric Cajuste
 * @author Thurgood Kilper
 */

public class WeightedAStar extends AStar {

	/**
	 * Weight factor used in calculating heuristic values.
	 */
	public double weight;

	public WeightedAStar(){
		name = "W-AStar:";
	}

	/**
	 * Calculates the heuristic value using A* implementation
	 * and is factored by specified weight.
	 */
	@Override
	void hOfNeighbor(Cell cell) {
		super.hOfNeighbor(cell);
		cell.h[0] *= weight;
	}

}
