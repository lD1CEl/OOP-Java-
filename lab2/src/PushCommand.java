import java.util.logging.Logger;
import java.util.logging.Level;

public class PushCommand implements Command {
    private static final Logger logger = Logger.getLogger(PushCommand.class.getName());

    @Override
    public void execute(ExecutionContext context, String[] args) {
        if (args.length < 2) {
            logger.severe("команда PUSH требует 1 аргумент");
            throw new InvalidArgumentsException("PUSH requires 1 argument");
        }
        String arg = args[1];
        double value;

        if (context.getDefines().containsKey(arg)) {
            value = context.getDefines().get(arg);
            logger.info(String.format("PUSH: параметр %s заменен на %s", arg, value));
        } else {
            try {
                value = Double.parseDouble(arg);
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE, String.format("PUSH: неверный формат числа или неизвестный параметр: %s", arg));
                throw new InvalidArgumentsException("Invalid number format or undefined parameter: " + arg);
            }
        }

        context.getStack().push(value);
        logger.info(String.format("В стек добавлено значение: %s", value));
    }
}
