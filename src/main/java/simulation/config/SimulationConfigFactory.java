package simulation.config;

public class SimulationConfigFactory {
    private SimulationConfigFactory() {}

    public static SimulationConfig createDefaultSimulationConfig() {
        return SimulationConfig.defaultConfig();
    }
}
