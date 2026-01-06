package simulation;

import simulation.action.Action;
import simulation.action.MoveCreaturesAction;
import simulation.action.RegulateEntitiesAction;
import simulation.action.WorldSetupAction;
import simulation.config.SimulationConfig;
import simulation.rendering.InfoRenderer;
import simulation.rendering.WorldMapRenderer;
import simulation.worldmap.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Simulation {
    private static final String PAUSE_SIMULATION_MESSAGE = "Simulation is paused.";
    private static final String CONTINUE_SIMULATION_MESSAGE = "We continue. Simulation is ongoing.";
    private static final String EXIT_SIMULATION_MESSAGE = "Simulation is finished. Thank you for the game!";
    private static final long PAUSE_TIMEOUT_MS = 60_000L;
    private static final long TURN_DURATION_MS = 1500L;
    private final WorldMap worldMap;
    private final InfoRenderer infoRenderer;
    private final WorldMapRenderer worldMapRenderer;
    private final List<Action> initActions = new ArrayList<>();
    private final List<Action> turnActions = new ArrayList<>();
    private volatile boolean isRunning = false;
    private int currentTurn;
    private volatile SimulationState state = SimulationState.STOPPED;

    public Simulation(SimulationConfig cfg) {
        this.worldMap = new WorldMap(cfg.getWorldMapHeight(), cfg.getWorldMapWidth());
        this.infoRenderer = cfg.getInfoRenderer();
        this.worldMapRenderer = cfg.getWorldMapRenderer();
        initActions.add(new WorldSetupAction(cfg));
        turnActions.add(new MoveCreaturesAction(cfg));
        turnActions.add(new RegulateEntitiesAction(cfg));
    }

    public enum SimulationState {
        STOPPED,
        PAUSED,
        RUNNING
    }

    public void nextTurn() {
        if (state != SimulationState.RUNNING) {
            return;
        }
        incrementAndPrintCurrentTurnInfo();
        executeActions(turnActions);
        render();
        printUserControlInfoMenu();
    }

    private void executeActions(List<Action> actions) {
        for (Action action : actions) {
            action.execute(worldMap);
        }
    }

    public synchronized void startSimulation() {
        if (state != SimulationState.STOPPED) {
            return;
        }

        state = SimulationState.RUNNING;
        isRunning = true;

        new Thread(() -> {
            try {
                incrementAndPrintCurrentTurnInfo();
                executeActions(initActions);
                render();
                printUserControlInfoMenu();

                while (state != SimulationState.STOPPED) {
                    if (state == SimulationState.RUNNING) {
                        processRunningState();
                    } else if (state == SimulationState.PAUSED) {
                        processPausedState();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void processRunningState() throws InterruptedException {
        nextTurn();
        Thread.sleep(TURN_DURATION_MS);
    }

    private final CountDownLatch pauseLatch = new CountDownLatch(1);

    private void processPausedState() {
        try {
            if (!pauseLatch.await(PAUSE_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                System.out.println("Timeout pause exceeded. Simulation stopped.");
                state = SimulationState.STOPPED;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void stopSimulation() {
        state = SimulationState.STOPPED;
        isRunning = false;
        System.out.println(EXIT_SIMULATION_MESSAGE);
    }

    public synchronized void pauseSimulation() {
        if (state == SimulationState.RUNNING) {
            state = SimulationState.PAUSED;
            System.out.println(PAUSE_SIMULATION_MESSAGE);
        }
    }

    public synchronized void continueSimulation() {
        if (state == SimulationState.PAUSED) {
            state = SimulationState.RUNNING;
            pauseLatch.countDown();
            System.out.println(CONTINUE_SIMULATION_MESSAGE);
        }
    }

    private synchronized int incrementTurn() {
        return ++currentTurn;
    }

    private void incrementAndPrintCurrentTurnInfo() {
        int newTurn = incrementTurn();
        System.out.printf("Step: %d\n", newTurn);
    }

    private void render() {
        infoRenderer.render(worldMap);
        worldMapRenderer.render(worldMap);
    }

    private void printUserControlInfoMenu() {
        System.out.printf(
                "[MENU] → %d: Pause | → %d: Continue | → %d: Exit \n",
                SimulationRunner.PAUSE_SIMULATION_COMMAND,
                SimulationRunner.CONTINUE_SIMULATION_COMMAND,
                SimulationRunner.EXIT_SIMULATION_COMMAND);
    }
}
