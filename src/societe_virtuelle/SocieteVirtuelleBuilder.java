/**
 * 
 */
package societe_virtuelle;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

/**
 * @author Clo
 * 
 */
public class SocieteVirtuelleBuilder implements ContextBuilder<Object>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.dataLoader.ContextBuilder#build(repast.simphony.context
	 * .Context)
	 */
	@Override
	public Context build(Context<Object> context)
	{
		context.setId("societe_virtuelle");

		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder
				.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace(
				"space", context, new RandomCartesianAdder<Object>(),
				new repast.simphony.space.continuous.WrapAroundBorders(), 100,
				100);

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
						new SimpleGridAdder<Object>(), true, 10, 10));

		Parameters params = RunEnvironment.getInstance().getParameters();
		int humanCount = (Integer) params.getValue("human_count");
		for (int i = 0; i < humanCount; i++)
		{
			int lifetime = RandomHelper.nextIntFromTo(1, 365 * 100);
			context.add(new Human(space, grid, lifetime, 0));
		}

		int waterSquareNb = 10;
		for (int i = 0; i < waterSquareNb; i++)
		{
			context.add(new Water(space, grid));
		}

		int plantCount = 20;
		for (int i = 0; i < plantCount; i++)
		{
			context.add(new Plant(space, grid));
		}
		
		int animalCount = 20;
		for (int i = 0; i < animalCount; i++)
		{
			context.add(new Animal(space, grid, 10, 0));
		}

		for (Object obj : context)
		{
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) pt.getX(), (int) pt.getY());
		}

		return context;
	}

}
