package simulation.messages;

public class InitMessages {
    private static final String INIT_MESSAGES = "Добро пожаловать в игру Симуляция. \n" +
            "Вы увидете пошаговую симуляцию 2D мира, населённого травоядными и хищниками. \n" +
            "Кроме существ, мир содержит ресурсы (траву), которыми питаются травоядные, и статичные объекты, " +
            "камни  и деревья. %n%n";

    public static void printMessages() {
        System.out.printf(INIT_MESSAGES);
    }
}
