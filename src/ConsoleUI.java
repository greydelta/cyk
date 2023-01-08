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
                System.out.print(">>Choice: ");
                try {
                    mainMenuChoice = intInputValidation(1, 3);
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
                    promptCNFInput();
                    generateCYK();
                    break;
                case 2:
                    inputManually();
                    // promptCNFInput();
                    // generateCYK();
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

            if (tempGrammar.getCanGenerateVariable())
                msg += " (can generate)";
            System.out.println("Grammar: " + msg);
        }
    }

    public void promptFileToLoad() {
        try {
            System.out.println("Select the corresponding number to select file");
            int count = 0, status = -1, fileChoice = -1;
            File[] filesList = getAllTextFilesInDirectory();
            System.out.println("No. \tFile Name");
            System.out.println("------------------------");
            for (File file : filesList) {
                String filePath = file.getCanonicalPath();
                String fileName = extractFileName(filePath);
                System.out.println(++count + ". \t" + fileName);
            }

            // ! TODO: if no files

            do {
                System.out.print(">>Choice: ");
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
            System.out.print("Error: " + e);
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

            // ^ canGenerateVariable
            boolean canGenerateVariableFlag = false;
            for (String variable : grammarToAdd.getVariable()) {
                // @ debug
                if (variable.length() <= 1) {
                    System.out
                            .println("variable: " + variable + "\t\tlength: " + variable.length() + " variable ASCII: "
                                    + (int) variable.charAt(0));
                    canGenerateVariableFlag = true;
                } else
                    System.out.println("variable: " + variable + "\t\tlength: " + variable.length());
                // if (variable)
            }
            grammarToAdd.setCanGenerateVariable(canGenerateVariableFlag);

            // ^ Add to DataLists
            control.addGrammar(grammarToAdd);
        }
        System.out.println("=== (END) Loaded data from " + fileName + "===");
    }

    public void promptCNFInput() {
        List<CnfInput> cnfInputList = control.getAllCnfInputs();
        String cnfInput = null;
        int doWhile = -1, innerDoWhile = -1, choice = -1;
        System.out.println("\n================");
        System.out.println("<<CNF Input>>");
        do {
            try {
                System.out.print("Input CNF: ");
                cnfInput = stringInputValidation();
                if (!checkIfCnfInputExists(cnfInput)) {
                    // boolean flag = checkIfCnfInputIsValid(cnfInput);

                    control.addCnfInput(cnfInput, splitCnfInput(cnfInput));
                } else {
                    System.out.println("\nCNF Input entered already exists!");
                }
                do { // innerDoWhile
                    System.out.println("\n<<CNF Input Summary>>");
                    System.out.println("<<CNF Input in current run: " + cnfInputList.size() + ">>");
                    System.out.println("1. Add more CNF Input");
                    System.out.println("2. Proceed to generate");
                    System.out.print(">>Choice: ");
                    try {
                        choice = intInputValidation(1, 2);
                        innerDoWhile = 1;
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getMessage());
                        bufferFor5Miliseconds();
                        innerDoWhile = 0;
                    }

                    if (choice == 1)
                        doWhile = 0;
                    else if (choice == 2)
                        doWhile = 1;
                } while (innerDoWhile != 1);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
                bufferFor5Miliseconds();
            }
        } while (doWhile != 1);
    }

    public boolean checkIfCnfInputExists(String cnfInputEntered) {
        List<CnfInput> cnfInputList = control.getAllCnfInputs();
        boolean flag = false;
        for (CnfInput cnfInput : cnfInputList) {
            // @ debug
            // System.out.println(
            // "Compare " + cnfInput.toString() + " & " + cnfInputEntered + " = "
            // + (new String(cnfInput).equals(cnfInputEntered)));
            if (new String(cnfInput.getCnfInputInString()).equals(cnfInputEntered))
                flag = true;
        }
        return flag;
    }

    public List<String> splitCnfInput(String cnfInput) {
        List<String> listString = new ArrayList<>();
        String[] split = cnfInput.split("");
        for (int i = 0; i < split.length; i++) {
            listString.add(split[i]);
        }
        return listString;
    }

    public void inputManually() {
        // handle @ frontend
    }

    public void generateCYK() {
        System.err.println("\n<<Generate CYK>>");
        List<CnfInput> cnfInputList = control.getAllCnfInputs();

        for (CnfInput cnfInput : cnfInputList) { // ! TODO: refactor - separate CNF List
            System.out.println("## Generating for CNF Input: " + cnfInput.getCnfInputInString());
            System.out.println("Received num of inputs: " + cnfInput.getCnfInputInList().size());
        }

        initiateIteration();
    }

    public void initiateIteration() {
        System.out.println("\ninitiateIteration()");
        List<Grammar> grammarList = control.getAllGrammars();
        List<CnfInput> cnfInputList = control.getAllCnfInputs();

        // ^ set cnf input to generate / test against grammar - only 1 for now but can
        // ^ refactor to accomodate selection
        CnfInput selectedCnfInput = new CnfInput();
        for (CnfInput cnfInput : cnfInputList) {
            selectedCnfInput.setCnfInputInList(cnfInput.getCnfInputInList());
            selectedCnfInput.setCnfInputInString(cnfInput.getCnfInputInString());
        }

        int step = 0;
        List<String> result = new ArrayList<>();

        for (int i = 0; i < selectedCnfInput.getCnfInputInList().size(); i++) {
            ++step;
            int substep = 0;
            for (String cnf : selectedCnfInput.getCnfInputInList()) {
                System.out.println("step " + step + " substep " + ++substep);
                for (Grammar grammar : grammarList) {
                    for (String gra : grammar.getVariable()) {
                        // ^ Step 1
                        if (step == 1) {
                            System.out.print(
                                    "Compare: " + cnf + " against grammar: " + grammar.getStartVariable() + " var: "
                                            + gra
                                            + " Action > ");
                            if (cnf.equals(gra)) {
                                System.out.print(
                                        "Add " + grammar.getStartVariable() + " to Substep " + substep + " array");
                                result.add(grammar.getStartVariable());
                            }
                        } else if (step == 2) {
                            if (substep == 1) {
                                control.addCykResult(step - 1, result); // $ add results from step 1

                                List<CykResults> tempcyk = control.getAllCykResultList();
                                for (CykResults cyk : tempcyk) {
                                    for (String s : cyk.getCykResultsList()) {
                                        System.out
                                                .println("cyk (step): " + cyk.getStep() + " (result): "
                                                        + cyk.getCykResultsInFullForm());
                                    }
                                }

                                result.clear(); // $ resets result array
                            }
                        }
                        System.out.print("\n");
                    }
                }
            }
        }
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
