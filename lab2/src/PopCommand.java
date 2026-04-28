import java.util.logging.Logger;


public class PopCommand implements Command {
    private static final Logger logger = Logger.getLogger(PopCommand.class.getName());

    @Override
    public void execute(ExecutionContext context, String[] args) {
        if (context.getStack().isEmpty()) {
            logger.severe("POP: стек пуст");
            throw new StackException("Stack is empty");
        }
        double value = context.getStack().pop();
        logger.info(String.format("POP: удалено значение %s", value));
    }
}