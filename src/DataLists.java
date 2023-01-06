import java.util.*;

public class DataLists implements IDataStore {

    private List<Grammar> grammarList;
    private List<CnfInput> cnfInputList;
    private List<CykResults> cykResultList;

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

    public List<CykResults> getAllCykResultList() {
        return this.cykResultList;
    }

    public CykResults getCykResultsByStep(int step) {
        CykResults tempCykResults = new CykResults();
        int count = 0;
        for (CykResults cyk : this.cykResultList) {
            count++;
            int stepInt = (int) cyk.getStep();
            if (Integer.compare(stepInt, step) == 0) {
                tempCykResults.setStep(stepInt);
                tempCykResults.setCykResultsList(cyk.getCykResultsList());
                return cyk;
            }
        }
        return new CykResults();
    }

    public void addGrammar(Grammar grammar) {
        this.grammarList.add(grammar);
    }

    public void addCnfInput(CnfInput cnfInput) {
        this.cnfInputList.add(cnfInput);
    }

    public void addCykResult(CykResults cykResult) {
        this.cykResultList.add(cykResult);
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