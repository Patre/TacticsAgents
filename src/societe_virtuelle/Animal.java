package societe_virtuelle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import math.V2;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

public class Animal extends Living
{
	protected int hunger, thirst, nbPossibleActions;
	protected boolean sexe;
	protected Map<String, Boolean> states;

	public Animal(ContinuousSpace<Object> space, Grid<Object> grid,
			int lifetime, int age)
	{
		super(space, grid, lifetime, age);
		this.hunger = 0;
		this.thirst = 0;
		this.sexe = (RandomHelper.nextIntFromTo(0, 1) > 0.5);
		energeticValue = 2;
		states = new HashMap<String, Boolean>();
		states.put("inactive", true);
		states.put("fleeing", false);
		states.put("thirst_quenching", false);
		states.put("hunger_satisfying", false);
		states.put("seeking_mate", false);
		nbPossibleActions = 5;
	}

	public void moveTowards(GridPoint pt)
	{
		if (!pt.equals(grid.getLocation(this)))
		{
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint,
					otherPoint);
			space.moveByVector(this, 2, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY());
		}
	}

	public void eat(Living o)
	{
		if (hunger >= 0)
			hunger -= o.energeticValue;
	}

	public void drink()
	{
		thirst = 0;
	}

	@ScheduledMethod(start = 1, interval = 1, priority = ScheduleParameters.LAST_PRIORITY)
	public void digest()
	{
		hunger++;
		thirst++;
		if (hunger > 7 || thirst > 3)
			die();
	}

	public void breed()
	{
		Context<Object> context = ContextUtils.getContext(this);
		int nbChildren = RandomHelper.nextIntFromTo(1, 2);
		Animal child;
		for (int i = 0; i < nbChildren; i++)
		{
			child = new Animal(space, grid, RandomHelper.nextIntFromTo(1, 10),
					0);
			context.add(child);
		}
	}

	@ScheduledMethod(start = 1, interval = 1, priority = ScheduleParameters.FIRST_PRIORITY)
	public void decision()
	{
		int action = 1;
		while (action <= nbPossibleActions)
		{
			/* get neighbourhood */
			GridPoint pt = grid.getLocation(this);
			GridCellNgh<Human> humanNghCreator = new GridCellNgh<Human>(grid,
					pt, Human.class, 10, 10);
			List<GridCell<Human>> humanGridCells = humanNghCreator
					.getNeighborhood(true);
			GridCellNgh<Human> waterNghCreator = new GridCellNgh<Human>(grid,
					pt, Human.class, 10, 10);
			List<GridCell<Human>> waterGridCells = humanNghCreator
					.getNeighborhood(true);
			GridCellNgh<Human> animalNghCreator = new GridCellNgh<Human>(grid,
					pt, Human.class, 10, 10);
			List<GridCell<Human>> animalGridCells = humanNghCreator
					.getNeighborhood(true);
			GridCellNgh<Human> plantNghCreator = new GridCellNgh<Human>(grid,
					pt, Human.class, 10, 10);
			List<GridCell<Human>> plantGridCells = humanNghCreator
					.getNeighborhood(true);
			
			/* fleeing humans */
			ArrayList<V2> directions = new ArrayList<V2>();
			V2 myPoint = new V2(pt.getX(), pt.getY()), dest, vect;
			for (GridCell<Human> cell : humanGridCells)
			{
				if (cell.size() > 0)
				{
					dest = new V2(cell.getPoint().getX(), cell.getPoint().getY());
					vect = new V2(myPoint, dest);
					directions.add(vect);
				}
			}
			if(!directions.isEmpty())
			{
				states.put("inactive", false);
				states.put("fleeing", true);
				states.put("thirst_quenching", false);
				states.put("hunger_satisfying", false);
				states.put("seeking_mate", false);
				
				V2 vecSum = directions.get(0);
				for(int i = 1 ; i < directions.size() ; i++)
				{
					vecSum.add(directions.get(i));
				}
				vecSum.scale(-1.0f);
				vecSum.add(pt.getX(), pt.getY());
				moveTowards(new GridPoint((int)vecSum.x(), (int)vecSum.y()));
			}
			
			else
			{
				
			}
			
			if (states.get("inactive") == true)
			{
				
			} 
			else if (states.get("fleeing") == true)
			{

			} else if (states.get("thirst_quenching") == true)
			{

			} else if (states.get("hunger_satisfying") == true)
			{

			} else if (states.get("seeking_mate") == true)
			{

			}
			action++;
		}
	}
}
