package simulation.action;

import simulation.config.SimulationConfig;
import simulation.entities.Entity;
import simulation.entities.EntityFactory;
import simulation.entities.EntityType;
import simulation.worldMap.Coordinate;
import simulation.worldMap.WorldMap;
import simulation.worldMap.WorldMapValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WorldSetupAction implements Action {
    private final SimulationConfig simulationConfig;
    private final EntityFactory entityFactory;
    private final Random random = new Random();

    public WorldSetupAction(SimulationConfig simulationConfig) {
        this.simulationConfig = simulationConfig;
        this.entityFactory = new EntityFactory(simulationConfig);
    }

    @Override
    public void execute(WorldMap worldMap) {
        Map<EntityType, Integer> entityCounts = new EnumMap<>(EntityType.class);
        List<Coordinate> allCoordinatesShuffled = generateAllCoordinatesShuffled(worldMap);

        entityCounts.put(EntityType.HERBIVORE, simulationConfig.getHerbivoreConfig().count());
        entityCounts.put(EntityType.PREDATOR, simulationConfig.getPredatorConfig().count());
        entityCounts.put(EntityType.GRASS, simulationConfig.getGrassCount());
        entityCounts.put(EntityType.ROCK, simulationConfig.getRockCount());
        entityCounts.put(EntityType.TREE, simulationConfig.getTreeCount());

        placeEntitiesRandomly(worldMap, entityCounts, allCoordinatesShuffled);
    }

    private List<Coordinate> generateAllCoordinatesShuffled(WorldMap worldMap) {
        int width = worldMap.getWidth();
        int height = worldMap.getHeight();
        List<Coordinate> freeCoordinates = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Coordinate coordinate = new Coordinate(x, y);
                freeCoordinates.add(coordinate);
            }
        }

        Collections.shuffle(freeCoordinates, random);
        return freeCoordinates;
    }

    protected void placeEntitiesRandomly(WorldMap worldMap, Map<EntityType, Integer> entityQuantities,
                                         List<Coordinate> coordinates) {
        int totalEntities = entityQuantities.values().stream().mapToInt(Integer::intValue).sum();

        if (totalEntities > coordinates.size()) {
            throw new IllegalArgumentException("Not enough coordinates to place entities: " + totalEntities + " > " +
                    coordinates.size());
        }
        WorldMapValidator.validateEntityPlacementCapacity(entityQuantities, coordinates.size());

        int index = 0;
        for (EntityType type : entityQuantities.keySet()) {
            int count = entityQuantities.getOrDefault(type, 0);
            for (int i = 0; i < count; i++) {
                Coordinate coordinate = coordinates.get(index++);
                WorldMapValidator.validateCoordinateIsEmpty(worldMap, coordinate);
                Entity entity = entityFactory.createEntity(type);
                worldMap.setEntity(coordinates.get(index++), entity);
            }
        }
    }
}
