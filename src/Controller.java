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

    public List<String[]> getAllCykResultList() {
        return this.dataLists.getAllCykResultList();
    }

    public void addGrammar(Grammar grammar) {
        this.dataLists.addGrammar(grammar);
    }

    public void addCnfInput(CnfInput cnfInput) {
        this.dataLists.addCnfInput(cnfInput);
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
