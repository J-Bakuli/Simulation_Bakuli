package simulation.messages;

public class InitMessages {
    private static final String WELCOME_MESSAGE = """
            Добро пожаловать в игру «Симуляция»!

            Вы увидите пошаговую симуляцию 2D‑мира, населённого:
            - травоядными;
            - хищниками.

            Мир также содержит:
            - ресурсы (траву) — пища для травоядных;
            - статичные объекты (камни, деревья).
                
                    
            """;

    public static void print() {
        System.out.printf(WELCOME_MESSAGE);
    }
}
