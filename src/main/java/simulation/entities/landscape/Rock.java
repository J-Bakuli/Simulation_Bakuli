package simulation.entities.landscape;

import simulation.entities.Entity;
import simulation.entities.EntityType;

public class Rock extends Entity {
    @Override
    public EntityType getType() {
        return EntityType.ROCK;
    }
}
