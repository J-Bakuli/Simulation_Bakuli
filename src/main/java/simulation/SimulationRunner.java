package simulation;

import simulation.config.SimulationConfig;
import simulation.config.SimulationConfigFactory;
import simulation.messages.InitMessages;

import java.util.InputMismatchException;
import java.util.Scanner;

public class SimulationRunner {
    public static final int START_SIMULATION_INIT_COMMAND = 0;
    public static final int STOP_SIMULATION_INIT_COMMAND = 1;
    public static final int PAUSE_SIMULATION_COMMAND = 1;
    public static final int CONTINUE_SIMULATION_COMMAND = 2;
    public static final int EXIT_SIMULATION_COMMAND = 3;
    private static final SimulationConfig simulationConfig;

    static {
        simulationConfig = SimulationConfigFactory.createDefaultSimulationConfig();
    }

    public static void run() {
        InitMessages.printMessages();

        Scanner scanner = new Scanner(System.in);

        if (!waitForStartCommand(scanner)) {
            return;
        }

        Simulation simulation = new Simulation(simulationConfig);
        simulation.startSimulation();

        runUserControlCommands(simulation, scanner);

        scanner.close();
    }

    private static boolean waitForStartCommand(Scanner scanner) {
        final String PROMPT = "Start simulation? [0: Yes | 1: No]";
        final String ERROR_INPUT = "Input error: please enter a number (0–1).";
        final String ERROR_INVALID = "Invalid input. Please enter 0, 1.";
        final String LEAVE_MESSAGE = "Exiting the simulation.";

        while (true) {
            System.out.println(PROMPT);

            try {
                int choice = scanner.nextInt();

                if (choice == START_SIMULATION_INIT_COMMAND) {
                    return true;
                } else if (choice == STOP_SIMULATION_INIT_COMMAND) {
                    System.out.println(LEAVE_MESSAGE);
                    return false;
                } else {
                    System.out.println(ERROR_INVALID);
                }
            } catch (InputMismatchException e) {
                System.err.println(ERROR_INPUT);
                scanner.next();
            }
        }
    }

    public static void runUserControlCommands(Simulation simulation, Scanner scanner) {
        final String ERROR_INPUT = "Input error: please enter a number (1–3).";
        final String ERROR_INVALID = "Invalid input. Please enter 1, 2, or 3.";

        while (true) {
            try {
                int choice = scanner.nextInt();

                switch (choice) {
                    case PAUSE_SIMULATION_COMMAND -> simulation.pauseSimulation();
                    case CONTINUE_SIMULATION_COMMAND -> simulation.continueSimulation();
                    case EXIT_SIMULATION_COMMAND -> {
                        simulation.stopSimulation();
                        return;
                    }
                    default -> System.out.println(ERROR_INVALID);
                }
            } catch (InputMismatchException e) {
                System.err.println(ERROR_INPUT);
                scanner.next();
            }
        }
    }
}
