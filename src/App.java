public class App {
    public static void main(String[] args) throws Exception {
        IDataStore dataLists = new DataLists();
        Controller controller = new Controller(dataLists);
        ConsoleUI userInterface = new ConsoleUI();
        userInterface.setController(controller);
        System.out.println("version 1");
        userInterface.launch();
    }
}