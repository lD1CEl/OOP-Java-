import java.util.logging.Logger;


public class AddCommand implements Command {
    private static final Logger logger = Logger.getLogger(AddCommand.class.getName());

    @Override
    public void execute(ExecutionContext context, String[] args) {
        if (context.getStack().size() < 2) {
            logger.severe("+: недостаточно элементов в стеке");
            throw new StackException("Not enough elements in stack for +");
        }
        double v1 = context.getStack().pop();
        double v2 = context.getStack().pop();
        double res = v1 + v2;
        context.getStack().push(res);
        logger.info(String.format("+: сложены %s и %s, результат %s помещен в стек", v1, v2, res));
    }
}
