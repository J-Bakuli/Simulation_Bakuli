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
    private static final SimulationConfig simulationConfig = SimulationConfigFactory.createDefaultSimulationConfig();

    public static void execute() {
        InitMessages.print();

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
        final String prompt = String.format("Start simulation? [%d: Yes | %d: No]",
                START_SIMULATION_INIT_COMMAND, STOP_SIMULATION_INIT_COMMAND);
        final String errorInput = String.format("Input error: please enter a number (%d–%d).",
                START_SIMULATION_INIT_COMMAND, STOP_SIMULATION_INIT_COMMAND);
        final String errorInvalid = String.format("Invalid input. Please enter %d, %d.",
                START_SIMULATION_INIT_COMMAND, STOP_SIMULATION_INIT_COMMAND);
        final String leaveMessage = "Exiting the simulation.";

        while (true) {
            System.out.println(prompt);

            try {
                int choice = scanner.nextInt();

                if (choice == START_SIMULATION_INIT_COMMAND) {
                    return true;
                } else if (choice == STOP_SIMULATION_INIT_COMMAND) {
                    System.out.println(leaveMessage);
                    return false;
                }
                System.out.println(errorInvalid);
            } catch (InputMismatchException e) {
                System.err.println(errorInput);
                scanner.next();
            }
        }
    }

    private static void runUserControlCommands(Simulation simulation, Scanner scanner) {
        final String errorInput = String.format("Input error: please enter a number (%d–%d).",
                PAUSE_SIMULATION_COMMAND, EXIT_SIMULATION_COMMAND);
        final String errorInvalid = String.format("Invalid input. Please enter %d, %d, or %d.",
                PAUSE_SIMULATION_COMMAND, CONTINUE_SIMULATION_COMMAND, EXIT_SIMULATION_COMMAND);

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
                    default -> System.out.println(errorInvalid);
                }
            } catch (InputMismatchException e) {
                System.err.println(errorInput);
                scanner.next();
            }
        }
    }
}
