import java.util.logging.Logger;


public class PrintCommand implements Command {
    private static final Logger logger = Logger.getLogger(PrintCommand.class.getName());

    @Override
    public void execute(ExecutionContext context, String[] args) {
        if (context.getStack().isEmpty()) {
            logger.severe("PRINT: стек пуст");
            throw new StackException("Stack is empty");
        }
        double value = context.getStack().peek();
        System.out.println(value);
        logger.info(String.format("PRINT: выведено %s", value));
    }
}
