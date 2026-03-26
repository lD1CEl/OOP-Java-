import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private ExecutionContext context;

    @BeforeEach
    void setUp() {
        context = new ExecutionContext(new HashMap<>());
    }

    @Test
    void testDefineAndPush() {
        Command define = new DefineCommand();
        define.execute(context, new String[]{"DEFINE", "a", "4"});
        assertEquals(4.0, context.getDefines().get("a"));

        Command push = new PushCommand();
        push.execute(context, new String[]{"PUSH", "a"});

        assertEquals(1, context.getStack().size());
        assertEquals(4.0, context.getStack().peek());
    }

    @Test
    void testPushDirectValue() {
        Command push = new PushCommand();
        push.execute(context, new String[]{"PUSH", "7.5"});
        assertEquals(1, context.getStack().size());
        assertEquals(7.5, context.getStack().peek());
    }

    @Test
    void testAdd() {
        context.getStack().push(2.0);
        context.getStack().push(3.0);
        Command add = new AddCommand();
        add.execute(context, new String[]{"+"});
        assertEquals(1, context.getStack().size());
        assertEquals(5.0, context.getStack().peek());
    }

    @Test
    void testSub() {
        context.getStack().push(5.0);
        context.getStack().push(10.0);
        Command sub = new SubCommand();
        sub.execute(context, new String[]{"-"});
        assertEquals(-5.0, context.getStack().peek());
    }

    @Test
    void testMul() {
        context.getStack().push(4.0);
        context.getStack().push(3.0);
        Command mul = new MulCommand();
        mul.execute(context, new String[]{"*"});
        assertEquals(12.0, context.getStack().peek());
    }

    @Test
    void testDiv() {
        context.getStack().push(10.0);
        context.getStack().push(2.0);
        Command div = new DivCommand();
        div.execute(context, new String[]{"/"});
        assertEquals(5.0, context.getStack().peek());
    }

    @Test
    void testDivByZero() {
        context.getStack().push(5.0);
        context.getStack().push(0.0);
        Command div = new DivCommand();
        assertThrows(MathCalculatorException.class, () -> div.execute(context, new String[]{"/"}));
    }

    @Test
    void testSqrt() {
        context.getStack().push(16.0);
        Command sqrt = new SqrtCommand();
        sqrt.execute(context, new String[]{"SQRT"});
        assertEquals(4.0, context.getStack().peek());
    }

    @Test
    void testSqrtNegative() {
        context.getStack().push(-16.0);
        Command sqrt = new SqrtCommand();
        assertThrows(MathCalculatorException.class, () -> sqrt.execute(context, new String[]{"SQRT"}));
    }

    @Test
    void testPop() {
        context.getStack().push(42.0);
        Command pop = new PopCommand();
        pop.execute(context, new String[]{"POP"});
        assertTrue(context.getStack().isEmpty());
    }

    @Test
    void testPrint() {
        context.getStack().push(42.0);
        Command print = new PrintCommand();
        assertDoesNotThrow(() -> print.execute(context, new String[]{"PRINT"}));
        assertEquals(1, context.getStack().size());
    }

    @Test
    void testStackExceptionOnEmpty() {
        Command pop = new PopCommand();
        assertThrows(StackException.class, () -> pop.execute(context, new String[]{"POP"}));

        Command print = new PrintCommand();
        assertThrows(StackException.class, () -> print.execute(context, new String[]{"PRINT"}));

        Command sqrt = new SqrtCommand();
        assertThrows(StackException.class, () -> sqrt.execute(context, new String[]{"SQRT"}));

        Command add = new AddCommand();
        assertThrows(StackException.class, () -> add.execute(context, new String[]{"+"}));

        Command sub = new SubCommand();
        assertThrows(StackException.class, () -> sub.execute(context, new String[]{"-"}));
        
        Command div = new DivCommand();
        assertThrows(StackException.class, () -> div.execute(context, new String[]{"/"}));
        
        Command mul = new MulCommand();
        assertThrows(StackException.class, () -> mul.execute(context, new String[]{"*"}));
    }

    @Test
    void testInvalidArgumentsException() {
        Command define = new DefineCommand();
        assertThrows(InvalidArgumentsException.class, () -> define.execute(context, new String[]{"DEFINE"}));
        assertThrows(InvalidArgumentsException.class, () -> define.execute(context, new String[]{"DEFINE", "var"}));
        assertThrows(InvalidArgumentsException.class, () -> define.execute(context, new String[]{"DEFINE", "var", "нечисло"}));

        Command push = new PushCommand();
        assertThrows(InvalidArgumentsException.class, () -> push.execute(context, new String[]{"PUSH"}));
        assertThrows(InvalidArgumentsException.class, () -> push.execute(context, new String[]{"PUSH", "неизвестная_переменная"}));
    }

    @Test
    void testFactory() {
        CommandFactory factory = CommandFactory.getInstance();
        assertTrue(factory.createCommand("PUSH") instanceof PushCommand);
        assertTrue(factory.createCommand("+") instanceof AddCommand);
        assertThrows(CommandNotFoundException.class, () -> factory.createCommand("UNKNOWN_CMD"));
    }
}
