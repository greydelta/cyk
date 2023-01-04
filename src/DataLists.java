import java.util.*;

public class DataLists implements IDataStore {

    private List<Grammar> grammarList;

    public DataLists() {
        grammarList = new ArrayList<>();
    }

    public List<Grammar> getAllGrammars() {
        return grammarList;
    }

    public void addGrammar(Grammar grammar) {
        grammarList.add(grammar);
    }
}