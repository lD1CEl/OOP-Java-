import java.util.logging.Logger;


public class SubCommand implements Command {
    private static final Logger logger = Logger.getLogger(SubCommand.class.getName());

    @Override
    public void execute(ExecutionContext context, String[] args) {
        if (context.getStack().size() < 2) {
            logger.severe("-: недостаточно элементов в стеке");
            throw new StackException("Not enough elements in stack");
        }
        double v1 = context.getStack().pop();
        double v2 = context.getStack().pop();
        double res = v2 - v1;
        context.getStack().push(res);
        logger.info(String.format("-: вычтено %s из %s, результат %s", v1, v2, res));
    }
}