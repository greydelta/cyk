import java.util.*;

public class Grammar {
    private List<String> variable;
    private String startVariable;
    private boolean canGenerateVariable;

    public Grammar() {
        this.variable = null;
        this.startVariable = "";
        this.canGenerateVariable = false;
    }

    public Grammar(List<String> variable, String startVariable, boolean canGenerateVariable) {
        this.variable = variable;
        this.startVariable = startVariable;
        this.canGenerateVariable = canGenerateVariable;
    }

    public List<String> getVariable() {
        return this.variable;
    }

    public String getStartVariable() {
        return this.startVariable;
    }

    public boolean getCanGenerateVariable() {
        return this.canGenerateVariable;
    }

    public void setVariable(List<String> variable) {
        this.variable = variable;
    }

    public void setStartVariable(String startVariable) {
        this.startVariable = startVariable;
    }

    public void setCanGenerateVariable(boolean canGenerateVariable) {
        this.canGenerateVariable = canGenerateVariable;
    }

    public void addVariable(String variable) {
        this.variable.add(variable);
    }
}
