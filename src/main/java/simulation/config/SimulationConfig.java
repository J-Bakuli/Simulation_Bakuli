package simulation.config;

import simulation.entities.EntityType;
import simulation.pathFinding.BreadthFirstSearchPathFinder;
import simulation.pathFinding.PathFinder;
import simulation.rendering.ConsoleEmojiWorldMapRenderer;
import simulation.rendering.InfoRenderer;
import simulation.rendering.WorldMapRenderer;

public class SimulationConfig {
    public static final int DEFAULT_HERBIVORE_SPEED = 1;
    public static final int DEFAULT_HERBIVORE_MAX_HP = 5;

    public static final int DEFAULT_PREDATOR_SPEED = 2;
    public static final int DEFAULT_PREDATOR_MAX_HP = 7;
    public static final int DEFAULT_PREDATOR_ATTACK_POWER = 2;

    public static final int DEFAULT_WORLD_MAP_HEIGHT = 18;
    public static final int DEFAULT_WORLD_MAP_WIDTH = 14;

    public static final int DEFAULT_HERBIVORE_COUNT = 10;
    public static final int DEFAULT_PREDATOR_COUNT = 4;
    public static final int DEFAULT_TREE_COUNT = 15;
    public static final int DEFAULT_ROCK_COUNT = 15;
    public static final int DEFAULT_GRASS_COUNT = 20;

    public static final int DEFAULT_MIN_GRASS_THRESHOLD = 3;
    public static final int DEFAULT_MIN_HERBIVORE_THRESHOLD = 3;

    private final int worldMapHeight;
    private final int worldMapWidth;

    private final HerbivoreConfig herbivoreConfig;
    private final PredatorConfig predatorConfig;
    private final int treeCount;
    private final int rockCount;
    private final int grassCount;

    private final int minGrassThreshold;
    private final int minHerbivoreThreshold;

    private final WorldMapRenderer worldMapRenderer;
    private final InfoRenderer infoRenderer;
    private final PathFinder pathFinder;

    private SimulationConfig(
            int worldMapHeight,
            int worldMapWidth,
            HerbivoreConfig herbivoreConfig,
            PredatorConfig predatorConfig,
            int treeCount,
            int rockCount,
            int grassCount,
            int minGrassThreshold,
            int minHerbivoreThreshold,
            InfoRenderer infoRenderer,
            WorldMapRenderer worldMapRenderer, PathFinder pathFinder) {
        this.worldMapHeight = worldMapHeight;
        this.worldMapWidth = worldMapWidth;
        this.herbivoreConfig = herbivoreConfig;
        this.predatorConfig = predatorConfig;
        this.treeCount = treeCount;
        this.rockCount = rockCount;
        this.grassCount = grassCount;
        this.minGrassThreshold = minGrassThreshold;
        this.minHerbivoreThreshold = minHerbivoreThreshold;
        this.infoRenderer = infoRenderer;
        this.worldMapRenderer = worldMapRenderer;
        this.pathFinder = pathFinder;
    }

    public static SimulationConfig defaultConfig() {
        SimulationConfig simulationConfig = new SimulationConfig(
                DEFAULT_WORLD_MAP_HEIGHT,
                DEFAULT_WORLD_MAP_WIDTH,
                new HerbivoreConfig(
                        DEFAULT_HERBIVORE_SPEED,
                        DEFAULT_HERBIVORE_MAX_HP,
                        DEFAULT_HERBIVORE_MAX_HP,
                        DEFAULT_HERBIVORE_COUNT),
                new PredatorConfig(
                        DEFAULT_PREDATOR_SPEED,
                        DEFAULT_PREDATOR_MAX_HP,
                        DEFAULT_HERBIVORE_MAX_HP,
                        DEFAULT_PREDATOR_ATTACK_POWER,
                        DEFAULT_PREDATOR_COUNT),
                DEFAULT_TREE_COUNT,
                DEFAULT_ROCK_COUNT,
                DEFAULT_GRASS_COUNT,
                DEFAULT_MIN_GRASS_THRESHOLD,
                DEFAULT_MIN_HERBIVORE_THRESHOLD,
                new InfoRenderer(),
                new ConsoleEmojiWorldMapRenderer(),
                new BreadthFirstSearchPathFinder()
        );

        SimulationConfigValidator.validate(simulationConfig);

        return simulationConfig;
    }

    public int getWorldMapHeight() {
        return worldMapHeight;
    }

    public int getWorldMapWidth() {
        return worldMapWidth;
    }

    public HerbivoreConfig getHerbivoreConfig() {
        return herbivoreConfig;
    }

    public PredatorConfig getPredatorConfig() {
        return predatorConfig;
    }

    public int getTreeCount() {
        return treeCount;
    }

    public int getRockCount() {
        return rockCount;
    }

    public int getGrassCount() {
        return grassCount;
    }

    public InfoRenderer getInfoRenderer() {
        return infoRenderer;
    }

    public WorldMapRenderer getWorldMapRenderer() {
        return worldMapRenderer;
    }

    public PathFinder getPathFinder() {
        return pathFinder;
    }

    public int getMinEntityTypeThreshold(EntityType entityType) {
        return switch (entityType) {
            case GRASS -> minGrassThreshold;
            case HERBIVORE -> minHerbivoreThreshold;
            default -> throw new IllegalArgumentException("Unsupported entity type: " + entityType);
        };
    }

    public int getTargetCountForType(EntityType entityType) {
        return switch (entityType) {
            case GRASS -> getGrassCount();
            case HERBIVORE -> getHerbivoreConfig().count();
            default -> throw new IllegalArgumentException("Unsupported entity type: " + entityType);
        };
    }

    public record HerbivoreConfig(int speed, int maxHp, int hp, int count) {
    }

    public record PredatorConfig(int speed, int maxHp, int hp, int attackPower, int count) {
    }
}
