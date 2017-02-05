package view;

/**
 * Class representation of the A* algorithm,
 * extending the Heuristic Algorithm class.
 * 
 * @author Eric Cajuste
 * @author Thurgood Kipler
 */

import java.util.Arrays;

public class AStar extends HeuristicAlgorithm {

	@Override
	void fOfNeighbor(Cell cell) {
		cell.f=cell.g+cell.h[0];
	}

	@Override
	void hOfNeighbor(Cell cell) {
		cell.h[1] = manhattanDistance(cell);
		cell.h[2] = euclideanDistance(cell);
		cell.h[3] = beelineDistance(cell);
		
		// more heuristic algorithms can be calculated
		
		//cell.h=(dx+dy) + (Math.sqrt(2)-2) * Math.min(dx, dy);
		
		//selects the largest heuristic calculation
		cell.h[0] = Arrays.stream(cell.h).max().getAsDouble();
	}
	
	/**
	 * Calculates the Manhattan distance from cell to the target cell
	 * @param cell	starting location
	 * @return Manhattan distance from cell to goal
	 */
	protected double manhattanDistance(Cell cell){
		double dx = Math.abs(cell.self.x-goalpoint.x);
		double dy = Math.abs(cell.self.y-goalpoint.y);
		return dx + dy;
	}
	
	/**
	 * Calculates the euclidean (ordinary) distance from cell to the target cell
	 * @param cell	starting location
	 * @return euclidean distance from cell to goal
	 */
	protected double euclideanDistance(Cell cell){
		double dx = cell.self.x-goalpoint.x;
		double dy = cell.self.y-goalpoint.y;
		return Math.sqrt(dx*dx + dy*dy);
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
		return Math.sqrt(2*dy*dy) + Math.abs(dx-dy);
	}
}
