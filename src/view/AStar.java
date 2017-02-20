package view;

/**
 * Class representation of the A* algorithm,
 * extending the Heuristic Algorithm class.
 *
 * @author Eric Cajuste
 * @author Thurgood Kilper
 */


public class AStar extends HeuristicAlgorithm {

	int heuristic = 0;

	public AStar(int h){
		this.heuristic = h;
		name = "AStar("+h+"):";
	}

	@Override
	void fOfNeighbor(Cell cell) {
		cell.f=cell.g+cell.h;
	}

	@Override
	void hOfNeighbor(Cell cell) {

		switch(heuristic){
		case 0:	cell.h = fourWayManhattanDistance(cell); return;
		case 1:	cell.h = euclideanDistance(cell); return;
		case 2:	cell.h = eightWayManhattanDistance(cell); return;
		case 3:	cell.h = beelineDistance(cell); return;
		default:
		}

		//tie breaker
		//cell.h*=(1.0+0.25/300);
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
		return 0.25 *((dx+dy) + (Math.sqrt(2)-2) * Math.min(dx, dy));
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
	 * @return beeline distance from cell to goal
	 */
	protected double beelineDistance(Cell cell){
		double dx = Math.abs(cell.self.x-goalpoint.x);
		double dy = Math.abs(cell.self.y-goalpoint.y);
		if(dx > dy)
			return (Math.sqrt(2*dx*dx) + Math.abs(dx-dy)) *0.25;
		return (Math.sqrt(2*dy*dy) + Math.abs(dx-dy)) *0.25;
	}

	/**
	 * Favors nodes going horizontal since the graph is wider than it is vertical
	 * @param cell starting location
	 * @return
	 */
	protected double widescaleDistance(Cell cell){
		double dx = Math.abs(cell.self.x-goalpoint.x);
		double dy = Math.abs(cell.self.y-goalpoint.y);
		return 0.25 *((dx*1.25+dy) + (Math.sqrt(2)-2) * Math.min(dx, dy));
	}


}
