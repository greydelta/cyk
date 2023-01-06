import java.util.List;

public interface IDataStore {
    public List<Grammar> getAllGrammars();

    public List<CnfInput> getAllCnfInputs();

    public List<String[]> getAllCykResultList();

    public void addGrammar(Grammar grammar);

    public void addCnfInput(CnfInput cnfInput);

    public void clearGrammarList();

    public void clearCnfInputList();

    public void clearCykResultList();

    public void clearAll();
}