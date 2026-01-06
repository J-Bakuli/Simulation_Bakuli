package simulation;

import simulation.exceptions.SimulationException;

public class Main {
    public static void main(String[] args) {
        try {
            SimulationRunner.execute();
        } catch (SimulationException e) {
            System.err.println("CRITICAL ERROR: The simulation could not be completed.");
            System.err.println("Reason: " + e.getClass().getSimpleName() + " — " + e.getMessage());
            System.err.println("The program will now terminate.");
            System.exit(1);        }
    }
}