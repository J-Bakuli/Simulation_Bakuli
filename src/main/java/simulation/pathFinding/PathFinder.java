package simulation.pathFinding;

import simulation.entities.creatures.Creature;
import simulation.worldMap.Coordinate;
import simulation.worldMap.WorldMap;

import java.util.Optional;

public interface PathFinder {
    Optional<Coordinate> findNearestMealViaBfs(WorldMap worldMap, Coordinate currentCoordinate, Creature creature);

    Optional<Direction> findNextStepTowardMeal(WorldMap worldMap, Coordinate currentPos, Creature creature);
}
