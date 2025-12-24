package simulation.entities.landscape;

import simulation.entities.Entity;
import simulation.entities.EntityType;

public class Tree extends Entity {
    @Override
    public EntityType getType() {
        return EntityType.TREE;
    }
}
