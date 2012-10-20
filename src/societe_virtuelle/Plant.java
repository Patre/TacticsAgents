package societe_virtuelle;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class Plant extends Living {
	public Plant(ContinuousSpace<Object> space, Grid<Object> grid, int lifetime, int age) {
		super(space, grid, lifetime, age);
		energeticValue = 1;
	}
	
	@ScheduledMethod(start = 10, interval = 150, duration = 10)
	public void breed() {
		Context<Object> context = ContextUtils.getContext(this);
		int nbChildren = RandomHelper.nextIntFromTo(1, 20);
		Plant child;
		for(int i = 0 ; i < nbChildren ; i++) {
			child = new Plant(space, grid, 1, 0);
			context.add(child);
		}
	}
}
