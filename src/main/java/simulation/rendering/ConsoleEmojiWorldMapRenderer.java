package simulation.rendering;

import simulation.entities.Entity;
import simulation.entities.EntityType;
import simulation.entities.creatures.Creature;
import simulation.entities.creatures.Herbivore;
import simulation.worldmap.Coordinate;
import simulation.worldmap.WorldMap;

import java.util.Objects;
import java.util.Optional;

public class ConsoleEmojiWorldMapRenderer implements WorldMapRenderer {
    @Override
    public void render(WorldMap worldMap) {
        if (worldMap == null) {
            System.err.println("Error: worldMap is null");
            return;
        }

        int width = worldMap.getWidth();
        int height = worldMap.getHeight();

        for (int y = 0; y < height; y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < width; x++) {
                Coordinate coordinate = new Coordinate(x, y);
                Optional<Entity> entityOpt = worldMap.getEntity(coordinate);
                String cellContent = getCellContent(entityOpt);
                row.append(cellContent);
            }
            System.out.println(row);
        }
        System.out.println();
    }

    private enum SpriteType {
        EMPTY("⬛"),
        PREDATOR("\uD83D\uDC3A"),
        HERBIVORE("\uD83D\uDC04"),
        GRASS("\uD83C\uDF3E"),
        ROCK("⛰"),
        TREE("\uD83C\uDF33");

        private final String sprite;

        SpriteType(String sprite) {
            this.sprite = sprite;
        }

        public String getSprite() {
            return sprite;
        }
    }

    public static String getSprite(EntityType entityType) {
        if (entityType == null) {
            return SpriteType.EMPTY.getSprite();
        }
        return switch (entityType) {
            case HERBIVORE -> SpriteType.HERBIVORE.getSprite();
            case PREDATOR -> SpriteType.PREDATOR.getSprite();
            case GRASS -> SpriteType.GRASS.getSprite();
            case ROCK -> SpriteType.ROCK.getSprite();
            case TREE -> SpriteType.TREE.getSprite();
            default -> SpriteType.EMPTY.getSprite();
        };
    }

    private String getCellContent(Optional<Entity> entityOpt) {
        if (entityOpt.isEmpty()) {
            return SpriteType.EMPTY.getSprite();
        }

        Entity entity = entityOpt.get();
        EntityType type = entity.getType();

        if (Objects.requireNonNull(type) == EntityType.HERBIVORE) {
            return getColorizedSprite((Herbivore) entity);
        }
        return getSprite(type);
    }

    private String getColorizedSprite(Creature creature) {
        String ansiColor = EntityColorizer.getAnsiBackgroundColor(creature);
        String resetColor = "\u001B[0m";
        EntityType type = creature.getType();
        String sprite = getSprite(type);

        return ansiColor + sprite + resetColor;
    }
}
