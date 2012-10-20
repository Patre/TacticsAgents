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
	protected int hunger, thirst, movementSpeed, percept;
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
		states.put("fleeing", false);
		states.put("thirst_quenching", false);
		states.put("hunger_satisfying", false);
		states.put("seeking_mate", true);
		movementSpeed = 8;
		percept = 20;
	}

	public void moveTowards(GridPoint pt)
	{
		if (!pt.equals(grid.getLocation(this)))
		{
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint,
					otherPoint);
			space.moveByVector(this, Math.min(movementSpeed, 
											(new V2(new V2((int)myPoint.getX(), (int)myPoint.getY()), 
													new V2((int)otherPoint.getX(), (int)otherPoint.getY())).norm())), 
											angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY());
		}
	}

	public void eat(Living o)
	{
		hunger -= o.energeticValue;
		o.die();
	}

	public void drink()
	{
		thirst = 0;
	}

	@ScheduledMethod(start = 60 * 60 * 24 / 6, interval = 60 * 60 * 24 / 6, priority = ScheduleParameters.LAST_PRIORITY)
	public void digest()
	{
		hunger++;
		thirst++;
		if (hunger > 7 || thirst > 3)
		{
			System.out.println("Je suis l'animal num "+id+" et je meurs d'inanition.");
			die();
		}
	}

	public void breed(Animal mate)
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
		/* get neighbourhood */
		GridPoint pt = grid.getLocation(this);
		GridCellNgh<Human> humanNghCreator = new GridCellNgh<Human>(grid, pt,
				Human.class, percept, percept);
		List<GridCell<Human>> humanGridCells = humanNghCreator
				.getNeighborhood(true);

		/* watch humans */
		ArrayList<V2> directions = new ArrayList<V2>();
		V2 myPoint = new V2(pt.getX(), pt.getY()), dest, vect, vecSum = null;
		for (GridCell<Human> cell : humanGridCells)
		{
			if (cell.size() > 0)
			{
				dest = new V2(cell.getPoint().getX(), cell.getPoint().getY());
				vect = new V2(myPoint, dest);
				directions.add(vect);
			}
		}
		if (!directions.isEmpty())
		{
			vecSum = directions.get(0);
			for (int i = 1; i < directions.size(); i++)
			{
				vecSum.add(directions.get(i));
			}
			vecSum.scale(-1.0f);
			vecSum.add(pt.getX(), pt.getY());
		}

		/* change state */
		if (states.get("fleeing") == true)
		{
			if (directions.isEmpty())
			{
				states.put("fleeing", false);

				if (thirst > 1 && (hunger <= 4 || thirst == 3))
				{
					states.put("thirst_quenching", true);
				} else if (hunger > 1)
				{
					states.put("hunger_satisfying", true);
				} else
				{
					states.put("seeking_mate", true);
				}
			}
		} else if (states.get("thirst_quenching") == true)
		{
			if (!directions.isEmpty())
			{
				states.put("thirst_quenching", false);
				states.put("fleeing", true);
			} else if (thirst < 3 && hunger > 4)
			{
				states.put("thirst_quenching", false);
				states.put("hunger_satisfying", true);
			} else if (thirst <= 1)
			{
				states.put("thirst_quenching", false);
				states.put("seeking_mate", true);
			}
		} else if (states.get("hunger_satisfying") == true)
		{
			if (!directions.isEmpty())
			{
				states.put("hunger_satisfying", false);
				states.put("fleeing", true);
			} else if (thirst == 3 || (hunger <= 1 && thirst > 1))
			{
				states.put("hunger_satisfying", false);
				states.put("thirst_quenching", true);
			} else if (hunger <= 1)
			{
				states.put("hunger_satisfying", false);
				states.put("seeking_mate", true);
			}
		} else if (states.get("seeking_mate") == true)
		{
			if (!directions.isEmpty())
			{
				states.put("seeking_mate", false);
				states.put("fleeing", true);
			} else if (thirst > 1 && (hunger <= 4))
			{
				states.put("seeking_mate", false);
				states.put("thirst_quenching", true);
			} else if (hunger > 1)
			{
				states.put("seeking_mate", false);
				states.put("hunger_satisfying", true);
			}
		}

		act(vecSum);
	}

	public void act(V2 vecSum)
	{
		GridPoint pt = grid.getLocation(this);
		System.out.print("Je suis l'animal num "+id+" ");

		if (states.get("fleeing") == true)
		{
			System.out.println("et je fuis.");
			moveTowards(new GridPoint((int) vecSum.x(), (int) vecSum.y()));
		} else if (states.get("thirst_quenching") == true)
		{
			System.out.println("et je veux boire.");
			GridCellNgh<Water> waterNghCreator = new GridCellNgh<Water>(grid,
					pt, Water.class, percept, percept);
			List<GridCell<Water>> waterGridCells = waterNghCreator
					.getNeighborhood(true);
			SimUtilities.shuffle(waterGridCells, RandomHelper.getUniform());
			GridPoint nearestPoint = null, farestPoint = null;
			float minDistance = Float.MAX_VALUE, maxDistance = -1;
			for (GridCell<Water> cell : waterGridCells)
			{
				V2 v = new V2(new V2(pt.getX(), pt.getY()), new V2(cell
						.getPoint().getX(), cell.getPoint().getY()));
				if (cell.size() > 0)
				{
					if (v.norm() < minDistance)
					{
						minDistance = v.norm();
						nearestPoint = cell.getPoint();
					}
				}
				if (v.norm() > maxDistance)
				{
					maxDistance = v.norm();
					farestPoint = cell.getPoint();
				}
			}
			if(nearestPoint == null)
				moveTowards(farestPoint);
			else
			{
				moveTowards(nearestPoint);
				if (pt.equals(nearestPoint))
				{
					drink();
				}
			}
		} else if (states.get("hunger_satisfying") == true)
		{
			System.out.println("et je manger.");
			GridCellNgh<Plant> plantNghCreator = new GridCellNgh<Plant>(grid,
					pt, Plant.class, percept, percept);
			List<GridCell<Plant>> plantGridCells = plantNghCreator
					.getNeighborhood(true);
			SimUtilities.shuffle(plantGridCells, RandomHelper.getUniform());
			GridPoint nearestPoint = null, farestPoint = null;
			float minDistance = Float.MAX_VALUE, maxDistance = -1;
			GridCell<Plant> plantCell = null;
			for (GridCell<Plant> cell : plantGridCells)
			{
				V2 v = new V2(new V2(pt.getX(), pt.getY()), new V2(cell
						.getPoint().getX(), cell.getPoint().getY()));
				if (cell.size() > 0)
				{
					if (v.norm() < minDistance)
					{
						minDistance = v.norm();
						nearestPoint = cell.getPoint();
						plantCell = cell;
					}
				}
				if (v.norm() > maxDistance)
				{
					maxDistance = v.norm();
					farestPoint = cell.getPoint();
				}
			}
			if(nearestPoint == null)
				moveTowards(farestPoint);
			else
			{
				moveTowards(nearestPoint);
				if (pt.equals(nearestPoint))
				{
					for(Plant plant : plantCell.items())
					{
						eat(plant);
						break;
					}
				}
			}
		} else if (states.get("seeking_mate") == true)
		{
			System.out.println("et je veux me reproduire.");
			GridCellNgh<Animal> animalNghCreator = new GridCellNgh<Animal>(
					grid, pt, Animal.class, percept, percept);
			List<GridCell<Animal>> animalGridCells = animalNghCreator
					.getNeighborhood(false);
			SimUtilities.shuffle(animalGridCells, RandomHelper.getUniform());
			GridPoint nearestPoint = null, farestPoint = null;
			float minDistance = Float.MAX_VALUE, maxDistance = -1;
			GridCell<Animal> mateCell = null;
			for (GridCell<Animal> cell : animalGridCells)
			{
				V2 v = new V2(new V2(pt.getX(), pt.getY()), new V2(cell
						.getPoint().getX(), cell.getPoint().getY()));
				if (cell.size() > 0)
				{
					if (v.norm() < minDistance)
					{
						minDistance = v.norm();
						nearestPoint = cell.getPoint();
						mateCell = cell;
					}
				}
				if (v.norm() > maxDistance)
				{
					maxDistance = v.norm();
					farestPoint = cell.getPoint();
				}
			}
			if(nearestPoint == null)
				moveTowards(farestPoint);
			else
			{
				moveTowards(nearestPoint);
				if (pt.equals(nearestPoint))
				{
					for(Animal mate : mateCell.items())
					{
						eat(mate);
						break;
					}
				}
			}
		}
	}
}
