package simulation.action;

import simulation.config.SimulationConfig;
import simulation.entities.Entity;
import simulation.entities.creatures.Creature;
import simulation.worldmap.WorldMap;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCreaturesAction implements Action {
    private final SimulationConfig simulationConfig;

    public MoveCreaturesAction(SimulationConfig simulationConfig) {
        this.simulationConfig = simulationConfig;
    }

    @Override
    public void execute(WorldMap worldMap) {
        synchronized (worldMap.getEntities()) {
            Collection<Entity> entityCollection = new ArrayList<>(worldMap.getEntities().values());

            for (Entity entity : entityCollection) {
                if (entity instanceof Creature creature) {
                    if (worldMap.hasEntity(creature)) {
                        creature.makeMove(worldMap, simulationConfig.getPathFinder());
                    }
                }
            }
        }
    }
}
