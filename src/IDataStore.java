import java.util.List;

public interface IDataStore {
    public List<Grammar> getAllGrammars();

    public List<CnfInput> getAllCnfInputs();

    public List<CykResults> getAllCykResultList();

    public void addGrammar(Grammar grammar);

    public void addCnfInput(CnfInput cnfInput);

    public void addCykResult(CykResults cykResult);

    public void clearGrammarList();

    public void clearCnfInputList();

    public void clearCykResultList();

    public void clearAll();
}