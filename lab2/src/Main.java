import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.info("Запуск стекового калькулятора");

        ExecutionContext context = new ExecutionContext(new HashMap<>());
        CommandFactory factory = CommandFactory.getInstance();

        try (Scanner scanner = getScanner(args)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\s+");
                String commandName = parts[0].toUpperCase();

                try {
                    Command command = factory.createCommand(commandName);
                    command.execute(context, parts);
                } catch (CalculatorException e) {
                    logger.warning("Ошибка выполнения команды: " + e.getMessage());
                    System.err.println("Ошибка калькулятора: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Непредвиденная ошибка: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при инициализации ввода", e);
        }
    }

    private static Scanner getScanner(String[] args) throws Exception {
        if (args.length > 0) {
            String fileName = args[0];
            logger.info(String.format("Чтение команд из файла: %s", fileName));
            return new Scanner(Files.newInputStream(Paths.get(fileName)));
        } else {
            logger.info("Чтение команд из стандартного потока ввода");
            return new Scanner(System.in);
        }
    }
}