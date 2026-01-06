package simulation.worldmap;

import simulation.entities.Entity;
import simulation.entities.EntityType;
import simulation.entities.creatures.Creature;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class WorldMapUtils {
    public static List<Coordinate> getHerbivoreCoordinateList(WorldMap worldMap) {
        Objects.requireNonNull(worldMap, "WorldMap cannot be null.");

        return worldMap.getEntities().entrySet().stream()
                .filter(entry -> entry.getValue().getType().equals(EntityType.HERBIVORE))
                .map(Map.Entry::getKey)
                .toList();
    }

    public static List<Coordinate> getPredatorCoordinateList(WorldMap worldMap) {
        Objects.requireNonNull(worldMap, "WorldMap cannot be null.");

        return worldMap.getEntities().entrySet().stream()
                .filter(entry -> entry.getValue().getType().equals(EntityType.PREDATOR))
                .map(Map.Entry::getKey)
                .toList();
    }

    public static Set<Coordinate> getNeighbouringCells(WorldMap worldMap, Coordinate pos) {
        int mapHeight = worldMap.getHeight();
        int mapWidth = worldMap.getWidth();

        int x = pos.x();
        int y = pos.y();

        if (x < 0 || y < 0) {
            throw new IllegalArgumentException(
                    "Coordinates must be non-negative: x=" + x + ", y=" + y);
        }

        Set<Coordinate> neighbours = new HashSet<>(8);

        if (y + 1 < mapHeight) {
            neighbours.add(coord(x, y + 1));
        }

        if (y - 1 >= 0) {
            neighbours.add(coord(x, y - 1));
        }

        if (x + 1 < mapWidth) {
            neighbours.add(coord(x + 1, y));
        }

        if (x - 1 >= 0) {
            neighbours.add(coord(x - 1, y));
        }

        if (x + 1 < mapWidth && y + 1 < mapHeight) {
            neighbours.add(coord(x + 1, y + 1));
        }

        if (x - 1 >= 0 && y + 1 < mapHeight) {
            neighbours.add(coord(x - 1, y + 1));
        }

        if (x + 1 < mapWidth && y - 1 >= 0) {
            neighbours.add(coord(x + 1, y - 1));
        }

        if (x - 1 >= 0 && y - 1 >= 0) {
            neighbours.add(coord(x - 1, y - 1));
        }

        return Collections.unmodifiableSet(neighbours);
    }

    private static Coordinate coord(int x, int y) {
        return new Coordinate(x, y);
    }

    public static int countEntitiesByType(WorldMap worldMap, EntityType type) {
        return (int) worldMap.getEntities().entrySet().stream()
                .filter(entity -> entity.getValue().getType() == type)
                .count();
    }

    public static boolean allowsMovementTo(WorldMap worldMap, Coordinate newCoordinate, Creature movingCreature) {
        Objects.requireNonNull(worldMap, "WorldMap cannot be null");
        Objects.requireNonNull(newCoordinate, "Coordinate cannot be null");
        Objects.requireNonNull(movingCreature, "Moving creature cannot be null");

        if (!worldMap.isInBounds(newCoordinate)) {
            return false;
        }

        Optional<Entity> targetEntityOpt = worldMap.getEntity(newCoordinate);

        if (targetEntityOpt.isEmpty() || targetEntityOpt.get().equals(movingCreature)) {
            return true;
        }

        if (movingCreature.getType() == EntityType.HERBIVORE) {
            return targetEntityOpt.get().getType() == EntityType.GRASS;
        }

        if (movingCreature.getType() == EntityType.PREDATOR) {
            return targetEntityOpt.get().getType() == EntityType.HERBIVORE;
        }

        return false;
    }
}
