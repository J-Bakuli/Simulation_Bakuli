package simulation.action;

import simulation.config.SimulationConfig;
import simulation.entities.EntityFactory;
import simulation.entities.EntityType;
import simulation.worldMap.Coordinate;
import simulation.worldMap.WorldMap;
import simulation.worldMap.WorldMapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RegulateEntitiesAction implements Action {
    private final SimulationConfig simulationConfig;

    public RegulateEntitiesAction(SimulationConfig simulationConfig) {
        this.simulationConfig = simulationConfig;
    }

    @Override
    public void execute(WorldMap worldMap) {
        maintainEntityCounts(worldMap, EntityType.GRASS);
        maintainEntityCounts(worldMap, EntityType.HERBIVORE);
    }

    private void maintainEntityCounts(WorldMap worldMap, EntityType entityType) {
        int currentCount = WorldMapUtils.countEntitiesByType(worldMap, entityType);
        int threshold = simulationConfig.getMinEntityTypeThreshold(entityType);
        int targetCount = simulationConfig.getTargetCountForType(entityType);

        if (currentCount <= threshold) {
            int toAdd = targetCount - currentCount;
            if (toAdd > 0) {
                addEntityRandomly(worldMap, entityType, toAdd);
            }
        }
    }

    private void addEntityRandomly(WorldMap worldMap, EntityType entityType, int count) {
        EntityFactory entityFactory = new EntityFactory(simulationConfig);
        List<Coordinate> freeCells = findFreeCells(worldMap);

        for (int i = 0; i < count && i < freeCells.size(); i++) {
            Coordinate coord = freeCells.get(i);
            worldMap.setEntity(coord, entityFactory.createEntity(entityType));
        }
    }

    private List<Coordinate> findFreeCells(WorldMap worldMap) {
        List<Coordinate> freeCells = new ArrayList<>();
        int width = worldMap.getWidth();
        int height = worldMap.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Coordinate coord = new Coordinate(x, y);
                if (!worldMap.hasEntity(coord)) {
                    freeCells.add(coord);
                }
            }
        }

        Collections.shuffle(freeCells, new Random());
        return freeCells;
    }
}
