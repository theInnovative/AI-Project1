package view;

/**
 * Class representation of the A* algorithm,
 * extending the Heuristic Algorithm class.
 *
 * @author Eric Cajuste
 * @author Thurgood Kilper
 */

import java.util.Arrays;

public class AStar extends HeuristicAlgorithm {
	private static final int HEURISTICS = 3;

	@Override
	void fOfNeighbor(Cell cell) {
		cell.f=cell.g+cell.h[0];
	}

	@Override
	void hOfNeighbor(Cell cell) {
		if(cell.h != null)
			return;
		cell.h = new double[HEURISTICS];
		//cell.h[1] = fourWayManhattanDistance(cell);
		cell.h[2] = euclideanDistance(cell);
		//cell.h[3] = beelineDistance(cell);

		// more heuristic algorithms can be calculated

		//selects the largest heuristic calculation
		cell.h[0] = Arrays.stream(cell.h).max().getAsDouble();
	}

	/**
	 * Calculates the Manhattan distance from cell to the target cell
	 * @param cell	starting location
	 * @return Manhattan distance from cell to goal
	 */
	protected double fourWayManhattanDistance(Cell cell){
		double dx = Math.abs(cell.self.x-goalpoint.x);
		double dy = Math.abs(cell.self.y-goalpoint.y);
		return (dx + dy) * 0.25;
	}

	/**
	 * Calculates the Manhattan distance from cell to the target cell
	 * @param cell	starting location
	 * @return Manhattan distance from cell to goal
	 */
	protected double eightWayManhattanDistance(Cell cell){
		double dx = Math.abs(cell.self.x-goalpoint.x);
		double dy = Math.abs(cell.self.y-goalpoint.y);
		return 0.25 *(dx+dy) + (Math.sqrt(2)-2) * Math.min(dx, dy);
	}

	/**
	 * Calculates the euclidean (ordinary) distance from cell to the target cell
	 * @param cell	starting location
	 * @return euclidean distance from cell to goal
	 */
	protected double euclideanDistance(Cell cell){
		double dx = cell.self.x-goalpoint.x;
		double dy = cell.self.y-goalpoint.y;
		return Math.sqrt(dx*dx + dy*dy) *0.25;
	}

	/**
	 * Calculates the distance of path traveling across the least cells from
	 *  cell to the target cell, without taking into account cell types.
	 * @param cell	starting location
	 * @return Manhattan distance from cell to goal
	 */
	protected double beelineDistance(Cell cell){
		double dx = Math.abs(cell.self.x-goalpoint.x);
		double dy = Math.abs(cell.self.y-goalpoint.y);
		if(dx > dy)
			return Math.sqrt(2*dx*dx) + Math.abs(dx-dy);
		return Math.sqrt(2*dy*dy) + Math.abs(dx-dy) *0.25;
	}
}
