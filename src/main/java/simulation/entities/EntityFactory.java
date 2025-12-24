package simulation.entities;

import simulation.config.SimulationConfig;
import simulation.entities.creatures.Herbivore;
import simulation.entities.creatures.Predator;
import simulation.entities.landscape.Grass;
import simulation.entities.landscape.Rock;
import simulation.entities.landscape.Tree;

public class EntityFactory {
    private final SimulationConfig simulationConfig;

    public EntityFactory(SimulationConfig simulationConfig) {
        this.simulationConfig = simulationConfig;
    }

    public Entity createEntity(EntityType entityType) {
        return switch (entityType) {
            case HERBIVORE -> new Herbivore(
                    simulationConfig.getHerbivoreConfig().speed(),
                    simulationConfig.getHerbivoreConfig().maxHp(),
                    simulationConfig.getHerbivoreConfig().hp());
            case PREDATOR -> new Predator(
                    simulationConfig.getPredatorConfig().speed(),
                    simulationConfig.getPredatorConfig().maxHp(),
                    simulationConfig.getPredatorConfig().hp(),
                    simulationConfig.getPredatorConfig().attackPower()
            );
            case GRASS -> new Grass();
            case ROCK -> new Rock();
            case TREE -> new Tree();
        };
    }
}
