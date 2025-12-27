package simulation.entities.creatures;

import simulation.entities.Entity;
import simulation.entities.EntityType;
import simulation.worldmap.Coordinate;
import simulation.worldmap.WorldMap;

import java.util.Optional;

public class Predator extends Creature {
    private final int attackPower;
    private static final int HP_RESTORED_PER_HERBIVORE_EATEN = 1;

    public Predator(int speed, int maxHp, int hp, int attackPower) {
        super(speed, maxHp, hp);
        this.attackPower = attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }

    @Override
    public boolean isAlive() {
        int hp = getHp();
        return hp > 0;
    }

    @Override
    public EntityType getType() {
        return EntityType.PREDATOR;
    }

    public EntityType getMealTypePreference() {
        return EntityType.HERBIVORE;
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

        Optional<Coordinate> selfCoordOpt = worldMap.getEntityCoordinate(this);
        if (selfCoordOpt.isPresent() && selfCoordOpt.get().equals(targetPos)) {
            return;
        }

        if (targetEntity.getType().equals(getMealTypePreference())
                && targetEntity instanceof Herbivore herbivore) {
            herbivore.handlePredatorAttack(worldMap, this.getAttackPower());

            worldMap.updateEntity(this);

            if (!herbivore.isAlive()) {
                this.heal(HP_RESTORED_PER_HERBIVORE_EATEN);
                worldMap.updateEntity(this);
            }
        }
    }

    @Override
    public void updateHealthStatus(WorldMap worldMap) {
        System.out.printf("[PREDATOR_CHECK] %s HP: %d (ignored)%n", this, this.getHp());
    }
}
