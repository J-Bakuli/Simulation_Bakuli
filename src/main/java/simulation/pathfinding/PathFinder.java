package simulation.pathfinding;

import simulation.entities.creatures.Creature;
import simulation.worldmap.Coordinate;
import simulation.worldmap.WorldMap;

import java.util.Optional;

public interface PathFinder {
    Optional<Coordinate> findNearestMealViaBfs(WorldMap worldMap, Coordinate currentCoordinate, Creature creature);

    Optional<Direction> findNextStepTowardMeal(WorldMap worldMap, Coordinate currentPos, Creature creature);
}
