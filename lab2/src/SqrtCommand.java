import java.util.logging.Logger;


public class SqrtCommand implements Command {
    private static final Logger logger = Logger.getLogger(SqrtCommand.class.getName());

    @Override
    public void execute(ExecutionContext context, String[] args) {
        if (context.getStack().isEmpty()) {
            logger.severe("SQRT: стек пуст");
            throw new StackException("Stack is empty");
        }
        double v = context.getStack().pop();
        if (v < 0) {
            logger.severe("SQRT: попытка извлечь корень из отрицательного числа: " + v);
            throw new MathCalculatorException("Cannot calculate square root of a negative number: " + v);
        }
        double res = Math.sqrt(v);
        context.getStack().push(res);
        logger.info(String.format("SQRT: корень из %s равен %s", v, res));
    }
}