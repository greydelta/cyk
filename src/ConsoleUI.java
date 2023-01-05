import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class ConsoleUI {

    private Scanner scan;
    private Controller control;

    public ConsoleUI() {
        scan = null;
        control = null;
    }

    public void setScanner(Scanner sc) {
        this.scan = sc;
    }

    public void setController(Controller cont) {
        this.control = cont;
    }

    public Controller getController() {
        return control;
    }

    public void launch() {
        mainMenu();
    }

    public void mainMenu() {
        int mainMenuChoice = -1, status = -1;
        do {
            System.out.println("CYK Algorithm");
            System.out.println("================");
            System.out.println("1. Load From File");
            System.out.println("2. Input Manually");
            System.out.println("3. Exit");
            do {
                System.out.print("Choice: ");
                try {
                    mainMenuChoice = intInputValidation(1, 4);
                    System.out.print("\n");
                    status = 1;
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                    bufferFor5Miliseconds();
                    status = 0;
                }
            } while (status != 1);

            switch (mainMenuChoice) {
                case 1:
                    loadFromFile();
                    break;
                case 2:
                    inputManually();
                    break;
                case 3:
                    System.out.println("\nThank you & have a nice day!");
                    System.exit(0);
                    break;
            }
        } while (status != 1);
    }

    public void loadFromFile() {
        promptFileToLoad();
        List<Grammar> grammar = control.getAllGrammars();

        System.out.println("\n================");
        System.out.println("<<Received Grammar>>");
        for (Grammar tempGrammar : grammar) {
            String msg = "";
            msg += tempGrammar.getStartVariable() + " -> ";

            List<String> list = tempGrammar.getVariable();
            int count = 0;
            for (String ls : list) {
                count++;
                msg += ls;
                if (list.size() > 1 && count != list.size())
                    msg += " | ";
            }
            System.out.println("Grammar: " + msg);
        }
    }

    public void promptFileToLoad() {
        try {
            System.out.println("Select the corresponding number to select file");
            int count = 0, status = -1, fileChoice = -1;
            File[] filesList = getAllTextFilesInDirectory();
            for (File file : filesList) {
                String filePath = file.getCanonicalPath();
                String fileName = extractFileName(filePath);
                System.out.println(++count + ": " + fileName);
            }

            // ! TODO: if no files

            do {
                System.out.print("Choice: ");
                try {
                    fileChoice = intInputValidation(1, filesList.length);
                    System.out.print("\n");
                    status = 1;
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                    bufferFor5Miliseconds();
                    status = 0;
                }
            } while (status != 1);

            File fileToLoad = filesList[fileChoice - 1];
            loadDataFromFile(extractFileName(fileToLoad.getCanonicalPath()));
        } catch (Exception e) {
            System.out.print("IOException");
        }
    }

    public File[] getAllTextFilesInDirectory() throws IOException {
        File f = new File(System.getProperty("user.dir")); // get directory
        FilenameFilter textFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        };
        File[] files = f.listFiles(textFilter);
        return files;
    }

    public String extractFileName(String pathname) {
        String[] split = pathname.split("\\\\");
        return split[split.length - 1];
    }

    public void loadDataFromFile(String fileName) {
        System.out.println("=== (START) Loading data from " + fileName + "===");
        File fileData = new File(fileName);
        try {
            setScanner(new Scanner(fileData));
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }

        while (scan.hasNextLine()) {
            String dataFromFile = scan.nextLine();
            Grammar grammarToAdd = new Grammar();

            // ^ Starting Variable
            String[] firstSplit = dataFromFile.split("->");
            grammarToAdd.setStartVariable(firstSplit[0].trim());
            // @ debug
            System.out.println("startVariable: " + grammarToAdd.getStartVariable() + "\tlength: "
                    + grammarToAdd.getStartVariable().length());

            // ^ Variables
            String[] secondSplit = firstSplit[1].split("\\|");
            List<String> variables = new ArrayList<>();
            for (int i = 0; i < secondSplit.length; i++) {
                variables.add(secondSplit[i].trim()); // trim to remove whitespace
            }
            grammarToAdd.setVariable(variables);
            for (String variable : grammarToAdd.getVariable()) {
                // @ debug
                System.out.println("variable: " + variable + "\t\tlength: " + variable.length());
            }
            control.addGrammar(grammarToAdd);
        }
        System.out.println("=== (END) Loaded data from " + fileName + "===");
    }

    public void inputManually() {
        // handle @ frontend
    }


    /*
     * =======================================
     * ====== SECTION : Utility Methods ======
     * =======================================
     */
    public int intInputValidation(int lower, int upper) throws IllegalArgumentException {
        setScanner(new Scanner(System.in));
        int userInput;

        if (scan.hasNextLine()) {
            String tempString = scan.nextLine();

            // Check if blank / only whitespace
            if (tempString.isEmpty() == true)
                throw new IllegalArgumentException("\nNot allowed to enter blank values!");
            else

                // Check if is valid integer
                try {
                    // Convert to integer
                    userInput = Integer.parseInt(tempString);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("\nEnter integers Only!");
                }

            // Check if range is same, then can only enter that value
            if (lower == upper)
                if (userInput != lower)
                    throw new IllegalArgumentException("\nEnter " + lower + " only!");
                else
                    return userInput;

            // Check if it is within range
            if (userInput < lower || userInput > upper)
                throw new IllegalArgumentException("\nEnter values between " + lower + " and " + upper + " only!");
            else
                return userInput;
        } else
            throw new IllegalArgumentException("\nEnter integers Only!");
    }

    public String stringInputValidation() throws IllegalArgumentException {
        setScanner(new Scanner(System.in));
        String userInput;
        if (scan.hasNextLine()) {
            userInput = scan.nextLine();
            if (userInput.isEmpty() == true || userInput.trim().length() < 1 == true)
                throw new IllegalArgumentException("Not allowed to enter blank values!");
            else
                return userInput;
        } else
            throw new IllegalArgumentException("Scanner is closed");
    }

    public void bufferFor5Miliseconds() {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e1) {
            System.err.print(e1.getMessage());
        }
    }
}
