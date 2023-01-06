import java.util.*;

public class DataLists implements IDataStore {

    private List<Grammar> grammarList;
    private List<CnfInput> cnfInputList;
    private List<String[]> cykResultList;

    public DataLists() {
        grammarList = new ArrayList<>();
        cnfInputList = new ArrayList<>();
        cykResultList = new ArrayList<>();
    }

    public List<Grammar> getAllGrammars() {
        return this.grammarList;
    }

    public List<CnfInput> getAllCnfInputs() {
        return this.cnfInputList;
    }

    public List<String[]> getAllCykResultList() {
        return this.cykResultList;
    }

    public void addGrammar(Grammar grammar) {
        this.grammarList.add(grammar);
    }

    public void addCnfInput(CnfInput cnfInput) {
        this.cnfInputList.add(cnfInput);
    }

    public void clearGrammarList() {
        this.grammarList.clear();
    }

    public void clearCnfInputList() {
        this.cnfInputList.clear();
    }

    public void clearCykResultList() {
        this.cykResultList.clear();
    }

    public void clearAll() {
        this.grammarList.clear();
        this.cnfInputList.clear();
        this.cykResultList.clear();
    }
}