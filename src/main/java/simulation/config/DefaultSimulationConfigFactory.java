package simulation.config;

import simulation.pathfinding.BreadthFirstSearchPathFinder;
import simulation.rendering.ConsoleEmojiWorldMapRenderer;
import simulation.rendering.InfoRenderer;

public class DefaultSimulationConfigFactory implements SimulationConfigFactory {
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

    @Override
    public SimulationConfig create() {
        SimulationConfig simulationConfig = new SimulationConfig(
                DEFAULT_WORLD_MAP_HEIGHT,
                DEFAULT_WORLD_MAP_WIDTH,
                new SimulationConfig.HerbivoreConfig(
                        DEFAULT_HERBIVORE_SPEED,
                        DEFAULT_HERBIVORE_MAX_HP,
                        DEFAULT_HERBIVORE_MAX_HP,
                        DEFAULT_HERBIVORE_COUNT),
                new SimulationConfig.PredatorConfig(
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
}
