package simulation.entities.creatures;

import simulation.entities.Entity;
import simulation.entities.EntityType;
import simulation.worldmap.Coordinate;
import simulation.worldmap.WorldMap;

import java.util.Optional;

public class Herbivore extends Creature {
    private static final int HP_RESTORED_PER_GRASS_EATEN = 1;

    public Herbivore(int speed, int maxHp, int hp) {
        super(speed, maxHp, hp);
    }

    @Override
    public EntityType getType() {
        return EntityType.HERBIVORE;
    }

    @Override
    public EntityType getMealTypePreference() {
        return EntityType.GRASS;
    }

    @Override
    public void interactWithTarget(WorldMap worldMap, Coordinate targetPos, Creature creature) {
        if (!this.isAlive()) {
            return;
        }

        Optional<Entity> targetEntityOpt = worldMap.getEntity(targetPos);
        if (targetEntityOpt.isEmpty()) {
            return;
        }

        Entity targetEntity = targetEntityOpt.get();

        if (targetEntity.getType().equals(EntityType.PREDATOR)) {
            this.handlePredatorAttack(worldMap, ((Predator) targetEntity).getAttackPower());
            this.wander(worldMap, targetPos);
            return;
        }

        if (targetEntity.getType().equals(EntityType.GRASS)) {
            worldMap.removeEntity(targetPos, targetEntity);
            this.heal(HP_RESTORED_PER_GRASS_EATEN);
            worldMap.updateEntity(this);
        }
    }

    public void handlePredatorAttack(WorldMap worldMap, int attackPower) {
        this.takeDamage(attackPower);
        this.updateHealthStatus(worldMap);
    }

    public void takeDamage(int attackPower) {
        int newHp = Math.max(0, this.getHp() - attackPower);
        this.setHp(newHp);
    }
}
