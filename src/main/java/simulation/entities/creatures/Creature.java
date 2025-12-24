package simulation.entities.creatures;

import simulation.entities.Entity;
import simulation.entities.EntityType;
import simulation.exceptions.InvalidWorldMapException;
import simulation.pathFinding.Direction;
import simulation.pathFinding.PathFinder;
import simulation.worldMap.Coordinate;
import simulation.worldMap.WorldMap;
import simulation.worldMap.WorldMapUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public abstract class Creature extends Entity {
    private static final Logger logger = Logger.getLogger(Creature.class.getName());
    private final int speed;
    private final int maxHp;
    private int hp;
    private boolean isAlive = true;

    public Creature(int speed, int maxHp, int hp) {
        this.speed = speed;
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public boolean isAlive() {
        return this.getHp() > 0;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getHp() {
        return hp;
    }

    protected void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, this.maxHp));
    }

    public abstract EntityType getMealTypePreference();

    protected abstract void interactWithTarget(WorldMap worldMap, Coordinate targetPos, Creature creature);

    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
        if (!this.isAlive()) {
            return;
        }

        Optional<Coordinate> currentPosOpt = worldMap.getEntityCoordinate(this);

        if (currentPosOpt.isEmpty()) {
            this.isAlive = false;
            return;
        }

        Coordinate currentPos = currentPosOpt.get();

        Optional<Direction> nextDirectionOpt = pathFinder.findNextStepTowardMeal(worldMap, currentPos, this);

        if (nextDirectionOpt.isPresent()) {
            this.moveToward(worldMap, currentPos, nextDirectionOpt.get(), this);
        } else {
            this.wander(worldMap, currentPos);
        }
    }

    private void moveToward(WorldMap worldMap, Coordinate currentPos, Direction direction, Creature creature) {
        Coordinate targetPos = new Coordinate(
                currentPos.x() + direction.dx,
                currentPos.y() + direction.dy
        );
        performMovement(worldMap, currentPos, targetPos, creature);
    }

    private void moveToward(WorldMap worldMap, Coordinate currentPos, Coordinate targetPos, Creature creature) {
        performMovement(worldMap, currentPos, targetPos, creature);
    }

    private void performMovement(WorldMap worldMap, Coordinate currentPos, Coordinate targetPos, Creature creature) {
        interactWithTarget(worldMap, targetPos, creature);

        if (!this.isAlive()
                || worldMap.getEntity(targetPos).isPresent()
                && !worldMap.getEntity(targetPos).get().equals(this)) {
            return;
        }

        if (this.isAlive() && !worldMap.getEntity(targetPos).map(e -> e.getType().equals(EntityType.GRASS)).orElse(false)) {
            if (WorldMapUtils.allowsMovementTo(worldMap, targetPos, this)) {
                moveTowardTarget(worldMap, currentPos, targetPos, creature);
            } else {
                Optional<Coordinate> alternativePos = findAlternativeMove(worldMap, currentPos);
                alternativePos.ifPresent(alt -> moveTowardTarget(worldMap, currentPos, alt, creature));
            }
        }
    }

    protected void wander(WorldMap worldMap, Coordinate currentPos) {
        List<Coordinate> possibleMoves = WorldMapUtils.getNeighbouringCells(currentPos).stream().toList();

        List<Coordinate> validMoves = possibleMoves.stream()
                .filter(coord -> WorldMapUtils.allowsMovementTo(worldMap, coord, this))
                .filter(coord -> !worldMap.getEntityCoordinate(this).orElse(currentPos).equals(coord))
                .toList();

        if (validMoves.isEmpty()) {
            logger.warning("No valid moves are available for this creature.");
        }
        Coordinate randomPos = validMoves.get(new Random().nextInt(validMoves.size()));
        moveToward(worldMap, currentPos, randomPos, this);
    }

    private synchronized void moveTowardTarget(WorldMap worldMap, Coordinate from, Coordinate to, Creature creature) {
        if (!WorldMapUtils.allowsMovementTo(worldMap, to, creature)) {
            return;
        }

        Optional<Entity> currentEntity = worldMap.getEntity(from);
        if (currentEntity.isEmpty() || !currentEntity.get().equals(creature)) {
            return;
        }

        worldMap.removeEntity(from, creature);

        boolean added = worldMap.addEntity(to, creature);

        if (!added) {
            worldMap.addEntity(from, creature);
        }
    }

    private Optional<Coordinate> findAlternativeMove(WorldMap worldMap, Coordinate currentPos) {
        Set<Coordinate> neighbours = WorldMapUtils.getNeighbouringCells(currentPos);

        return neighbours.stream()
                .filter(worldMap::isInBounds)
                .filter(pos -> WorldMapUtils.allowsMovementTo(worldMap, pos, this))
                .min(Comparator.comparing(pos -> Math.abs(pos.x() - currentPos.x()) + Math.abs(pos.y() - currentPos.y())));
    }

    public void heal(int amount) {
        if (amount <= 0 || !this.isAlive()) {
            return;
        }

        int newHp = Math.min(this.getHp() + amount, this.getMaxHp());

        if (newHp > this.getHp()) {
            this.setHp(newHp);
        }
    }

    public void checkHealth(WorldMap worldMap) {
        if (worldMap == null) {
            throw new InvalidWorldMapException("WorldMap cannot be null.");
        }

        if (!this.isAlive() && !(this instanceof Predator)) {
            worldMap.getEntityCoordinate(this).ifPresent(coord -> {
                worldMap.removeEntity(coord, this);
            });
        }
    }
}
