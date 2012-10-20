package societe_virtuelle;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;


public class Animal extends Living {
	protected int hunger, thirst;
	protected boolean sexe;
	
	public Animal(ContinuousSpace<Object> space, Grid<Object> grid, int lifetime, int age) {
		super(space, grid, lifetime, age);
		this.hunger = 0;
		this.thirst = 0;
		this.sexe = (RandomHelper.nextIntFromTo(0, 1) > 0.5);
		energeticValue = 2;
	}
	
	public void moveTowards(GridPoint pt) {
		if(!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
			space.moveByVector(this, 2, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
		}
	}
	
	public void eat(Living o) {
		if(hunger >= 0)
			hunger -= o.energeticValue;
	}
	
	public void drink() {
		thirst = 0;
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = ScheduleParameters.LAST_PRIORITY)
	public void digest() {
		hunger++;
		thirst++;
		if(hunger > 7 || thirst > 3)
			die();
	}
	
	public void breed() {
		Context<Object> context = ContextUtils.getContext(this);
		int nbChildren = RandomHelper.nextIntFromTo(1, 2);
		Animal child;
		for(int i = 0 ; i < nbChildren ; i++) {
			child = new Animal(space, grid, RandomHelper.nextIntFromTo(1, 10), 0);
			context.add(child);
		}
	}
}
