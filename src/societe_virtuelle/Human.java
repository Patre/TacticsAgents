package societe_virtuelle;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;


public class Human extends Animal {
	
	public Human(ContinuousSpace<Object> space, Grid<Object> grid, int lifetime, int age) {
		super(space, grid, lifetime, age);
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void act() {
		GridPoint pt = grid.getLocation(this);
		boolean moved = false;
		//System.out.print("Je suis "+id+" et je suis en case "+pt+". ");
		if(thirst > 0) {
			//System.out.print("J'ai soif : "+thirst+". ");
			GridCellNgh<Water> nghCreator = new GridCellNgh<Water>(grid, pt, Water.class, 1, 1);
			List<GridCell<Water>> gridCells = nghCreator.getNeighborhood(true);
			SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
			for(GridCell<Water> cell : gridCells) {
				if(cell.size() > 0) {
					moveTowards(cell.getPoint());
					moved = true;
					drink();
					//System.out.println("Je bouge en case "+cell.getPoint()+" et je bois. ");
					break;
				}
			}
			if(!moved) {
				moveTowards(gridCells.get(0).getPoint());
				//System.out.println("Je bouge en case "+gridCells.get(0).getPoint());
			}
		}
		//else
		//	System.out.println("Je n'ai pas soif. ");
	}
}
