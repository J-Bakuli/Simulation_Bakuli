package simulation.pathFinding;

import simulation.entities.EntityType;
import simulation.entities.creatures.Creature;
import simulation.worldMap.Coordinate;
import simulation.worldMap.WorldMap;
import simulation.worldMap.WorldMapUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

public class BreadthFirstSearchPathFinder implements PathFinder {
    private static final int MAX_SEARCH_DEPTH = 10;

    @Override
    public Optional<Coordinate> findNearestMealViaBfs(WorldMap worldMap, Coordinate currentPos, Creature creature) {
        if (!worldMap.isInBounds(currentPos)) {
            return Optional.empty();
        }

        Optional<Coordinate> nearbyMealOpt = findMealInAdjacentCells(worldMap, currentPos, creature);
        if (nearbyMealOpt.isPresent()) {
            return nearbyMealOpt;
        }

        return performBfsSearch(worldMap, currentPos, creature);
    }

    private Optional<Coordinate> performBfsSearch(WorldMap worldMap, Coordinate currentPos, Creature creature) {
        Queue<Coordinate> requireCheck = new LinkedList<>();
        Set<Coordinate> alreadyChecked = new HashSet<>();

        requireCheck.add(currentPos);
        alreadyChecked.add(currentPos);

        int depth = 0;
        while (!requireCheck.isEmpty() && depth <= MAX_SEARCH_DEPTH) {
            Optional<Coordinate> found = processCurrentBfsLevel(worldMap, requireCheck, alreadyChecked, creature);
            if (found.isPresent()) {
                return found;
            }
            depth++;
        }

        return Optional.empty();
    }

    private Optional<Coordinate> processCurrentBfsLevel(
            WorldMap worldMap,
            Queue<Coordinate> requireCheck,
            Set<Coordinate> alreadyChecked,
            Creature creature) {
        int levelSize = requireCheck.size();

        for (int i = 0; i < levelSize; i++) {
            Coordinate current = requireCheck.poll();

            if (isValidMealAtPosition(worldMap, current, creature)) {
                assert current != null;
                return Optional.of(current);
            }

            addUnvisitedNeighbours(worldMap, requireCheck, alreadyChecked, current);
        }
        return Optional.empty();
    }

    private boolean isValidMealAtPosition(WorldMap worldMap, Coordinate position, Creature creature) {
        return worldMap.getEntity(position)
                .filter(entity -> entity.getType() == creature.getMealTypePreference())
                .isPresent();
    }

    private void addUnvisitedNeighbours(WorldMap worldMap,
                                        Queue<Coordinate> requireCheck,
                                        Set<Coordinate> alreadyChecked,
                                        Coordinate current) {
        for (Coordinate neighbour : WorldMapUtils.getNeighbouringCells(current)) {
            if (worldMap.isInBounds(neighbour) && !alreadyChecked.contains(neighbour)) {
                requireCheck.add(neighbour);
                alreadyChecked.add(neighbour);
            }
        }
    }

    @Override
    public Optional<Direction> findNextStepTowardMeal(WorldMap worldMap, Coordinate currentPos, Creature creature) {
        Optional<Coordinate> nearestMealPosOpt = findNearestMealViaBfs(worldMap, currentPos, creature);
        return nearestMealPosOpt.map(nextPos -> calculateDirection(currentPos, nextPos));
    }

    private Direction calculateDirection(Coordinate from, Coordinate to) {
        int dx = Integer.compare(to.x() - from.x(), 0);
        int dy = Integer.compare(to.y() - from.y(), 0);

        for (Direction dir : Direction.values()) {
            if (dir.dx == dx && dir.dy == dy) {
                return dir;
            }
        }

        throw new IllegalStateException("Direction is not found for dx=" + dx + ", dy=" + dy);
    }

    private Optional<Coordinate> findMealInAdjacentCells(WorldMap worldMap, Coordinate currentPos, Creature creature) {
        EntityType mealTypePreference = creature.getMealTypePreference();
        return WorldMapUtils.getNeighbouringCells(currentPos).stream()
                .filter(c -> worldMap.getEntities().get(c) != null)
                .filter(c -> worldMap.getEntities().get(c).getType() == mealTypePreference)
                .findFirst();
    }
}
