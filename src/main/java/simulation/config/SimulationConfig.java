package simulation.config;

import simulation.entities.EntityType;
import simulation.pathfinding.PathFinder;
import simulation.rendering.InfoRenderer;
import simulation.rendering.WorldMapRenderer;

public class SimulationConfig {
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

    SimulationConfig(
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
