package simulation.config;

public class SimulationConfigValidator {
    private static final String ERR_NEGATIVE_COUNT = "The amount of %s cannot be negative.";

    public static void validate(SimulationConfig simulationConfig) throws IllegalArgumentException {
        validateWorldMap(simulationConfig);
        validateHerbivoreConfig(simulationConfig.getHerbivoreConfig());
        validatePredatorConfig(simulationConfig.getPredatorConfig());
        validateNonNegativeEntityCounts(simulationConfig);
        validateConfiguredMapCapacity(simulationConfig);
    }

    private static void validateWorldMap(SimulationConfig simulationConfig) {
        if (simulationConfig.getWorldMapHeight() <= 0) {
            throw new IllegalArgumentException("The map height must be positive.");
        }
        if (simulationConfig.getWorldMapWidth() <= 0) {
            throw new IllegalArgumentException("The map width must be positive.");
        }
    }

    private static void validateHerbivoreConfig(SimulationConfig.HerbivoreConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("The herbivore configuration cannot be null.");
        }

        if (config.speed() < 1) {
            throw new IllegalArgumentException("The herbivore speed must be positive.");
        }
        if (config.maxHp() < 0) {
            throw new IllegalArgumentException("The herbivore HP must be positive.");
        }
        if (config.count() < 0) {
            throw new IllegalArgumentException("The number of herbivores must be positive.");
        }
    }

    private static void validatePredatorConfig(SimulationConfig.PredatorConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("The predator configuration cannot be null.");
        }

        if (config.speed() < 1) {
            throw new IllegalArgumentException("The predator speed must be positive.");
        }
        if (config.maxHp() < 0) {
            throw new IllegalArgumentException("The predator HP must be positive.");
        }
        if (config.attackPower() <= 0) {
            throw new IllegalArgumentException("The attack power of predator must be positive.");
        }
        if (config.count() < 0) {
            throw new IllegalArgumentException("The number of predators must be positive.");
        }
    }

    private static void validateNonNegativeEntityCounts(SimulationConfig simulationConfig) {
        int[] counts = {simulationConfig.getGrassCount(), simulationConfig.getRockCount(), simulationConfig.getTreeCount()};

        String[] names = {"grass", "rocks", "trees"};

        for (int i = 0; i < counts.length; i++) {
            if (counts[i] < 0) {
                String msg = String.format(ERR_NEGATIVE_COUNT, names[i]);
                throw new IllegalArgumentException(msg);
            }
        }

        if (simulationConfig.getPredatorConfig().count() > simulationConfig.getHerbivoreConfig().count()) {
            throw new IllegalArgumentException("The number of predators must not exceed the number of herbivores.");
        }

        if (simulationConfig.getHerbivoreConfig().count() == 0 && simulationConfig.getPredatorConfig().count() > 0) {
            throw new IllegalArgumentException("You cannot create predators without herbivores.");
        }
    }

    private static void validateConfiguredMapCapacity(SimulationConfig simulationConfig) {
        int totalEntities = simulationConfig.getHerbivoreConfig().count() +
                simulationConfig.getPredatorConfig().count() +
                simulationConfig.getGrassCount() +
                simulationConfig.getRockCount() +
                simulationConfig.getTreeCount();

        if (totalEntities > (simulationConfig.getWorldMapHeight() * simulationConfig.getWorldMapWidth())) {
            throw new IllegalArgumentException("The total number of objects exceeds the map size.");
        }
    }
}
