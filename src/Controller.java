import java.util.*;

public class Controller {

    private IDataStore dataLists;

    public Controller(IDataStore dataLists) {
        this.dataLists = dataLists;
    }

    public List<Grammar> getAllGrammars() {
        return dataLists.getAllGrammars();
    }

    public void addGrammar(Grammar grammar) {
        dataLists.addGrammar(grammar);
    }

    public void clearAll() {
        dataLists.clearAll();
    }
}
