package simulation;

import simulation.exceptions.SimulationException;

public class Main {
    public static void main(String[] args) {
        try {
            SimulationRunner.run();
        } catch (SimulationException e) {
            System.out.println(e.getMessage());
        }
    }
}