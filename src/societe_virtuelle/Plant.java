package societe_virtuelle;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class Plant extends Living {
	public Plant(ContinuousSpace<Object> space, Grid<Object> grid) {
		super(space, grid, 1, 0);
		energeticValue = 1;
	}
	
	@ScheduledMethod(start = 10*24*60*60/6, interval = 150*24*60*60/6, duration = 10*24*60*60/6)
	public void breed() {
		Context<Object> context = ContextUtils.getContext(this);
		int nbChildren = RandomHelper.nextIntFromTo(1, 20);
		Plant child;
		for(int i = 0 ; i < nbChildren ; i++) {
			child = new Plant(space, grid);
			context.add(child);
		}
	}
}
