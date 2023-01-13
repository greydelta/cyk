import java.util.*;

public class Controller {

    private IDataStore dataLists;

    public Controller(IDataStore dataLists) {
        this.dataLists = dataLists;
    }

    public List<Grammar> getAllGrammars() {
        return this.dataLists.getAllGrammars();
    }

    public List<CnfInput> getAllCnfInputs() {
        return this.dataLists.getAllCnfInputs();
    }

    public List<CykResults> getAllCykResultList() {
        return this.dataLists.getAllCykResultList();
    }

    public String getCykResultByStep(int step) {
        return this.dataLists.getCykResultByStep(step);
    }

    public void addGrammar(Grammar grammar) {
        this.dataLists.addGrammar(grammar);
    }

    public void addCnfInput(String cnfInputString, List<String> cnfInputStringList) {
        CnfInput cnfInputToAdd = new CnfInput(cnfInputString, cnfInputStringList);
        this.dataLists.addCnfInput(cnfInputToAdd);
    }

    public void addCykResult(int step, List<String> result) {
        CykResults cykResultToAdd = new CykResults(step, result);
        this.dataLists.addCykResult(cykResultToAdd);
    }

    public void clearGrammarList() {
        this.dataLists.clearGrammarList();
    }

    public void clearCnfInputList() {
        this.dataLists.clearCnfInputList();
    }

    public void clearCykResultList() {
        this.dataLists.clearCykResultList();
    }

    public void clearAll() {
        this.dataLists.clearAll();
    }
}
