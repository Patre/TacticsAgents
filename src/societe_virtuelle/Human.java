package societe_virtuelle;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

import communication.Socket;

public class Human extends Animal
{
	public boolean has_spoken;
	
	

	public Human(ContinuousSpace<Object> space, Grid<Object> grid, int lifetime,
			int age)
	{
		super(space, grid, lifetime, age);
	}

	@ScheduledMethod(start = 10, interval = 100)
	public void speak()
	{
		has_spoken = true;
	}

		
	@Watch(watcheeClassName = "societe_virtuelle.Human",
			watcheeFieldNames = "has_spoken",
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void hear(Human h)
	{
		System.out.println("I (" + id + ") CAN HEAR " + h.id);
		h.has_spoken = false;
	}

}
