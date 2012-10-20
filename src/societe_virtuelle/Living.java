package societe_virtuelle;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class Living {
	protected ContinuousSpace<Object> space;
	protected Grid<Object> grid;
	protected int lifetime, age, energeticValue;
	protected static int id_tmp = 0;
	protected int id;

	public Living(ContinuousSpace<Object> space, Grid<Object> grid, int lifetime, int age) {
		this.space = space;
		this.grid = grid;
		this.lifetime = lifetime;
		this.age = age;
		id = id_tmp++;
	}

	@ScheduledMethod(start = 1, interval = 365)
	public void growOld() {
		age++;
		if(age > lifetime) {
			die();
		}
	}
	
	public void die() {
		System.out.println("Je suis "+id+" et je meurs. ");
		Object obj = this;
		Context<Object> context = ContextUtils.getContext(obj);
		context.remove(obj);
	}
}
