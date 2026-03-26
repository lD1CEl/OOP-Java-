import java.util.Map;
import java.util.Stack;

public class ExecutionContext {
    private final Stack<Double> stack = new Stack<>();
    private final Map<String, Double> defines;

    public ExecutionContext(Map<String, Double> defines) {
        this.defines = defines;
    }

    public Stack<Double> getStack() {
        return stack;
    }

    public Map<String, Double> getDefines() {
        return defines;
    }
}
