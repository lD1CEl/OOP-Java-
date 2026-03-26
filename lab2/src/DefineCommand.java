import java.util.logging.Logger;
import java.util.logging.Level;

public class DefineCommand implements Command {
    private static final Logger logger = Logger.getLogger(DefineCommand.class.getName());

    @Override
    public void execute(ExecutionContext context, String[] args) {
        if (args.length < 3) {
            logger.severe("DEFINE требует 2 аргумента");
            throw new InvalidArgumentsException("DEFINE requires 2 args");
        }
        String name = args[1];
        try {
            double val = Double.parseDouble(args[2]);
            context.getDefines().put(name, val);
            logger.info(String.format("DEFINE: задано %s = %s", name, val));
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, String.format("DEFINE: неверный формат значения %s", args[2]));
            throw new InvalidArgumentsException("Invalid value for define: " + args[2]);
        }
    }
}