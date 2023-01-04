import java.util.List;

public interface IDataStore {

    public List<Grammar> getAllGrammars();

    public void addGrammar(Grammar grammar);
}