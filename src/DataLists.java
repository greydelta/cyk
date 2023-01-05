import java.util.*;

public class DataLists implements IDataStore {

    private List<Grammar> grammarList;
    private List<String> cnfInputList;

    public DataLists() {
        grammarList = new ArrayList<>();
        cnfInputList = new ArrayList<>();
    }

    public List<Grammar> getAllGrammars() {
        return this.grammarList;
    }

    public List<String> getAllCnfInputs() {
        return this.cnfInputList;
    }

    public void addGrammar(Grammar grammar) {
        this.grammarList.add(grammar);
    }

    public void addCnfInput(String cnfInput) {
        this.cnfInputList.add(cnfInput);
    }

    public void clearGrammarList() {
        this.grammarList.clear();
    }

    public void clearCnfInputList() {
        this.cnfInputList.clear();
    }

    public void clearAll() {
        this.grammarList.clear();
        this.cnfInputList.clear();
    }
}