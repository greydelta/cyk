import java.util.List;

public interface IDataStore {
    public List<Grammar> getAllGrammars();

    public List<String> getAllCnfInputs();

    public void addGrammar(Grammar grammar);

    public void addCnfInput(String cnfInput);

    public void clearGrammarList();

    public void clearCnfInputList();

    public void clearAll();
}