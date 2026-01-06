package simulation.action;

import simulation.entities.Entity;
import simulation.entities.creatures.Creature;
import simulation.pathfinding.PathFinder;
import simulation.worldmap.WorldMap;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCreaturesAction implements Action {
    private final PathFinder pathFinder;

    public MoveCreaturesAction(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    @Override
    public void execute(WorldMap worldMap) {
        synchronized (worldMap.getEntities()) {
            Collection<Entity> entityCollection = new ArrayList<>(worldMap.getEntities().values());

            for (Entity entity : entityCollection) {
                if (entity instanceof Creature creature) {
                    if (worldMap.hasEntity(creature)) {
                        creature.makeMove(worldMap, pathFinder);
                    }
                }
            }
        }
    }
}
