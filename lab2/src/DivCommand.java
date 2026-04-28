import java.util.logging.Logger;


public class DivCommand implements Command {
    private static final Logger logger = Logger.getLogger(DivCommand.class.getName());

    @Override
    public void execute(ExecutionContext context, String[] args) {
        if (context.getStack().size() < 2) {
            logger.severe("/: недостаточно элементов");
            throw new StackException("Not enough elements");
        }
        double v1 = context.getStack().pop();
        if (v1 == 0.0) {
            logger.severe("/: попытка деления на ноль");
            throw new MathCalculatorException("Division by zero");
        }
        double v2 = context.getStack().pop();
        double res = v2 / v1;
        context.getStack().push(res);
        logger.info(String.format("/: %s разделено на %s, результат %s", v2, v1, res));
    }
}