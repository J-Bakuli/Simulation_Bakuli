package simulation.rendering;

import simulation.entities.creatures.Creature;
import simulation.entities.creatures.Herbivore;

public class EntityColorizer {
    public static String getAnsiBackgroundColor(Creature creature) {
        if (!(creature instanceof Herbivore herbivore)) {
            return "\u001B[0m";
        }

        double hpPercent = (double) herbivore.getHp() / herbivore.getMaxHp();

        if (hpPercent >= 0.8) {
            return "\u001B[0m";
        }
        if (hpPercent >= 0.5) {
            return "\u001B[43m";
        }
        if (hpPercent >= 0.2) {
            return "\u001B[41m";
        }
        return "\u001B[41m";
    }
}
