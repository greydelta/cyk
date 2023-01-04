import java.util.*;

public class Grammar {
    private List<String> variable;
    private String startVariable;

    public Grammar() {
        this.variable.clear();
        this.startVariable = "";
    }

    public Grammar(List<String> variable, String startVariable) {
        this.variable = variable;
        this.startVariable = startVariable;
    }

    public List<String> getVariable() {
        return variable;
    }

    public String getStartVariable() {
        return startVariable;
    }

    public void setVariable(List<String> variable) {
        this.variable = variable;
    }

    public void setStartVariable(String startVariable) {
        this.startVariable = startVariable;
    }

    public void addVariable(String variable) {
        this.variable.add(variable);
    }
}
