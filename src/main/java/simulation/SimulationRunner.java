package simulation;

import simulation.config.SimulationConfig;
import simulation.config.SimulationConfigFactory;
import simulation.messages.InitMessages;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class SimulationRunner {
    public static final int PAUSE_SIMULATION_COMMAND = 1;
    public static final int CONTINUE_SIMULATION_COMMAND = 2;
    public static final int EXIT_SIMULATION_COMMAND = 3;
    private static final SimulationConfig simulationConfig;

    static {
        simulationConfig = SimulationConfigFactory.createDefaultSimulationConfig();
    }

    public static void run() {
        InitMessages.printMessages();
        Simulation simulation = new Simulation(simulationConfig);
        simulation.startSimulation();
        runUserControlCommands(simulation);
    }

    public static void runUserControlCommands(Simulation simulation) {
        final String ERROR_INPUT = "Input error: please enter a number (1–3).";
        final String ERROR_INVALID = "Invalid input. Please try again.";

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                if (!scanner.hasNextInt()) {
                    System.err.println(ERROR_INPUT);
                    scanner.next();
                    continue;
                }

                int choice = scanner.nextInt();
                switch (choice) {
                    case PAUSE_SIMULATION_COMMAND:
                        simulation.pauseSimulation();
                        break;
                    case CONTINUE_SIMULATION_COMMAND:
                        simulation.continueSimulation();
                        break;
                    case EXIT_SIMULATION_COMMAND:
                        simulation.stopSimulation();
                        return;
                    default:
                        System.out.println(ERROR_INVALID);
                }
            }
        } catch (NoSuchElementException e) {
            System.err.println("Input stream closed. Exiting...");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}
