package simulation.worldmap;

import simulation.entities.EntityType;

import java.util.Map;

public class WorldMapValidator {
    public static void validateEntityPlacementCapacity(
            Map<EntityType, Integer> entityQuantities,
            int totalAvailableCoordinates
    ) {
        int totalEntities = entityQuantities.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        if (totalEntities > totalAvailableCoordinates) {
            throw new IllegalArgumentException("There are not enough coordinates to place the entities: " +
                    totalEntities + " > " + totalAvailableCoordinates
            );
        }
    }

    public static void validateCoordinateIsEmpty(WorldMap worldMap, Coordinate coordinate) {
        if (worldMap.hasEntity(coordinate)) {
            throw new IllegalArgumentException(String.format("The coordinate (%d, %d) is already occupied",
                    coordinate.x(), coordinate.y())
            );
        }
    }
}
