package societe_virtuelle;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

import communication.Message;
import communication.SocketManager;

public class Human extends Animal
{
	//! ATTRIBUTES

	private SocketManager socketManager = new SocketManager(this);
	

	public Human(ContinuousSpace<Object> space, Grid<Object> grid, int lifetime,
			int age)
	{
		super(space, grid, lifetime, age);
	}

	@ScheduledMethod(start = 10, interval = 100)
	public void speak()
	{
		socketManager.newConnexion(new Message(id, Message.Type.EXPRESSIVE), id+1);
	}

}
