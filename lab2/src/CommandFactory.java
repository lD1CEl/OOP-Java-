import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CommandFactory {
    private static final Logger logger = Logger.getLogger(CommandFactory.class.getName());
    private static volatile CommandFactory instance;
    private final Map<String, Class<?>> commandClasses = new HashMap<>();

    private CommandFactory() {
        loadCommands();
    }

    public static CommandFactory getInstance() {
        if (instance == null) {
            synchronized (CommandFactory.class.getName()) {
                if (instance == null) {
                    instance = new CommandFactory();
                }
            }
        }
        return instance;
    }

    private void loadCommands() {
        try (InputStream is = getClass().getResourceAsStream("/commands.properties")) {
            if (is == null) {
                logger.severe("Файл конфигурации команд commands.properties не найден.");
                throw new RuntimeException("commands.properties not found");
            }
            Properties props = new Properties();
            props.load(is);

            for (String key : props.stringPropertyNames()) {
                String className = props.getProperty(key);
                try {
                    Class<?> clazz = Class.forName(className);
                    commandClasses.put(key, clazz);
                    logger.info(String.format("Загружена команда: %s -> %s", key, className));
                } catch (ClassNotFoundException e) {
                    logger.log(Level.SEVERE, String.format("Класс для команды %s не найден: %s", key, className, e));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при загрузке конфигурации команд", e);
            throw new RuntimeException(e);
        }
    }

    public Command createCommand(String commandName) {
        Class<?> clazz = commandClasses.get(commandName);
        if (clazz == null) {
            logger.log(Level.SEVERE, String.format("Неизвестная команда: %s", commandName));
            throw new CommandNotFoundException("Unknown command: " + commandName);
        }
        try {
            return (Command) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("Ошибка при создании объекта команды %s", commandName, e));
            throw new RuntimeException("Could not instantiate command: " + commandName, e);
        }
    }
}
