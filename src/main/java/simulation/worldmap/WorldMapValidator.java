package simulation.worldmap;

import simulation.entities.EntityType;

import java.util.Map;

public class WorldMapValidator {
    public static void validateEntityPlacementCapacity(
            Map<EntityType, Integer> entityQuantities,
            int totalAvailCoords
    ) {
        int totalEntities = entityQuantities.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        if (totalEntities > totalAvailCoords) {
            String message =
                    "There are not enough coordinates to place the entities: " + totalEntities + " > " + totalAvailCoords;
            throw new IllegalArgumentException(message);
        }
    }

    public static void validateCoordinateIsEmpty(WorldMap worldMap, Coordinate coordinate) {
        if (worldMap.hasEntity(coordinate)) {
            String message = "The coordinate (%d, %d) is already occupied".formatted(coordinate.x(), coordinate.y());
            throw new IllegalArgumentException(message);
        }
    }
}
