package simulation.worldmap;

import simulation.entities.Entity;
import simulation.entities.creatures.Creature;
import simulation.exceptions.EntityNotFoundException;
import simulation.exceptions.InvalidCoordinateException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WorldMap {
    protected final int width;
    protected final int height;
    protected final Map<Coordinate, Entity> entities = new HashMap<>();

    public WorldMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Optional<Entity> getEntity(Coordinate coordinate) {
        return Optional.ofNullable(entities.get(coordinate));
    }

    public Map<Coordinate, Entity> getEntities() {
        return new HashMap<>(entities);
    }

    public Optional<Coordinate> getEntityCoordinate(Entity entity) {
        for (Map.Entry<Coordinate, Entity> entry : entities.entrySet()) {
            if (entry.getValue() == entity) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }

    public boolean hasEntity(Coordinate coordinate) {
        return coordinate != null && entities.containsKey(coordinate);
    }

    public boolean hasEntity(Entity entity) {
        return entities.containsValue(entity);
    }

    public void setEntity(Coordinate coordinate, Entity entity) {
        validate(coordinate);
        entities.put(coordinate, entity);
    }

    public void updateEntity(Creature creature) {
        Optional<Coordinate> coord = getEntityCoordinate(creature);
        coord.ifPresent(coordinate -> entities.put(coordinate, creature));
    }

    public boolean addEntity(Coordinate coordinate, Entity entity) {
        if (!isInBounds(coordinate) || entities.containsKey(coordinate)) {
            return false;
        }
        entities.put(coordinate, entity);
        return true;
    }

    public void removeEntity(Coordinate coordinate, Entity entity) {
        if (!isInBounds(coordinate)) {
            throw new InvalidCoordinateException(String.format("Coordinate %s is out of bounds", coordinate));
        }

        Optional<Entity> currentEntityOpt = getEntity(coordinate);

        if (currentEntityOpt.isEmpty()) {
            throw new EntityNotFoundException(String.format("Entity %s not found", entity));
        }

        Entity currentEntity = currentEntityOpt.get();

        if (!(currentEntity == entity)) {
            String message = "Expected entity %s does not match current entity %s at coordinate %s"
                    .formatted(entity, currentEntity, coordinate);
            throw new EntityNotFoundException(message);
        }

        entities.remove(coordinate);
    }

    private void validate(Coordinate coordinate) {
        int x = coordinate.x();
        int y = coordinate.y();

        if (!isInBounds(coordinate)) {
            String message = "Coordinates (%d, %d) are out of bounds (%d×%d)".formatted(x, y, width, height);
            throw new InvalidCoordinateException(message);
        }
    }

    public boolean isInBounds(Coordinate coordinate) {
        int x = coordinate.x();
        int y = coordinate.y();

        return x >= 0 && x < width && y >= 0 && y < height;
    }
}