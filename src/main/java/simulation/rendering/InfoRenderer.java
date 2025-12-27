package simulation.rendering;

import simulation.worldmap.WorldMap;
import simulation.worldmap.WorldMapUtils;

public class InfoRenderer implements WorldMapRenderer {
    @Override
    public void render(WorldMap worldMap) {
        int herbivoreCount = WorldMapUtils.getHerbivoreCoordinateList(worldMap).size();
        int predatorCount = WorldMapUtils.getPredatorCoordinateList(worldMap).size();

        System.out.printf("Current count: herbivores — %d, predators — %d.%n%n", herbivoreCount, predatorCount);
    }
}
