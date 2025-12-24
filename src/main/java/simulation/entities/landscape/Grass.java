package simulation.entities.landscape;

import simulation.entities.Entity;
import simulation.entities.EntityType;

public class Grass extends Entity {
    @Override
    public EntityType getType() {
        return EntityType.GRASS;
    }
}
