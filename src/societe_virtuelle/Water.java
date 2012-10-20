package societe_virtuelle;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Water {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	public Water(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}
}


