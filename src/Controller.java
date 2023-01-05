import java.util.*;

public class Controller {

    private IDataStore dataLists;

    public Controller(IDataStore dataLists) {
        this.dataLists = dataLists;
    }

    public List<Grammar> getAllGrammars() {
        return dataLists.getAllGrammars();
    }

    public List<String> getAllCnfInputs() {
        return dataLists.getAllCnfInputs();
    }

    public void addGrammar(Grammar grammar) {
        dataLists.addGrammar(grammar);
    }

    public void addCnfInput(String cnfInput) {
        dataLists.addCnfInput(cnfInput);
    }

    public void clearGrammarList() {
        dataLists.clearGrammarList();
    }

    public void clearCnfInputList() {
        dataLists.clearCnfInputList();
    }

    public void clearAll() {
        dataLists.clearAll();
    }
}
