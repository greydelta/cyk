import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class ConsoleUI {

    private Scanner scan;
    private Controller control;
    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";

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
                            .println("variable: " + variable + "\t\tlength: " + variable.length() + " ASCII: "
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
                    control.addCnfInput(cnfInput, splitStringRegex(cnfInput, "", false));
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

        step1();
    }

    public void step1() {
        System.out.println(YELLOW_BACKGROUND + "========== Step 1 ==========" + RESET);
        List<Grammar> grammarList = control.getAllGrammars();
        List<CnfInput> cnfInputList = control.getAllCnfInputs();
        int step = 0;

        // ^ set cnf input to generate / test against grammar - only 1 for now but can
        // ^ refactor to accomodate selection
        int cnfInputSize = 0;
        CnfInput selectedCnfInput = new CnfInput();
        for (CnfInput cnfInput : cnfInputList) {
            selectedCnfInput.setCnfInputInList(cnfInput.getCnfInputInList());
            selectedCnfInput.setCnfInputInString(cnfInput.getCnfInputInString());
            cnfInputSize = cnfInput.getCnfInputInList().size();
        }
        System.out.println("input size: " + cnfInputSize);

        String cnfResultsArray[] = new String[cnfInputSize];
        List<String> result = new ArrayList<>();

        for (int i = 0; i < selectedCnfInput.getCnfInputInList().size(); i++) {
            ++step;
            int substep = 0;
            for (String cnf : selectedCnfInput.getCnfInputInList()) {
                if(step == 1) System.out.println(YELLOW + "step " + step + " substep " + ++substep + RESET);
                for (Grammar grammar : grammarList) {
                    for (String gra : grammar.getVariable()) {
                        // ^ Step 1
                        if (step == 1) { // ^ TODO : consider double variable scenario
                            System.out.print(
                                    "Compare: " + cnf + " against grammar: " + grammar.getStartVariable() + " var: "
                                            + gra
                                            + " Action > ");
                            if (cnf.equals(gra)) {
                                if (cnfResultsArray[substep - 1] != null) {
                                    String tempVariable = cnfResultsArray[substep - 1];
                                    tempVariable += grammar.getStartVariable();
                                    cnfResultsArray[substep - 1] = tempVariable;
                                    System.out.println("add " + tempVariable + " to array[" + (substep - 1) + "]");
                                }
                                else {
                                    System.out.println("add " + grammar.getStartVariable() + " to array[" + (substep - 1) + "]");
                                    cnfResultsArray[substep - 1] = grammar.getStartVariable();
                                }
                            } else {
                                System.out.print("\n");
                            }
                        }
                    }
                }
            }
        }

        for (int count = 0; count < cnfResultsArray.length; count++) {
            System.out.println(GREEN + "adding " + cnfResultsArray[count] + " to cnfResultsArray");
            result.add(cnfResultsArray[count]); 
        }

        System.out.println("Add CykResults (Step 1) to DataLists" + RESET);
        control.addCykResult(1, new ArrayList<>(result));
        result.clear();

        step2();
    }

    public void step2() {
        System.out.println(YELLOW_BACKGROUND + "========== Step 2 ==========" + RESET);
        String resultFromStep1 = new String("");
        resultFromStep1 = control.getCykResultByStep(1);
        System.out.println(BLUE + "resultFromStep1: " + resultFromStep1 + RESET);

        List<String> splitResult = splitStringRegex(resultFromStep1, "|");
        int count = 1;
        String variableToCompare = new String("");
        List<String> listOfVariablesToCompare = new ArrayList<>();

        for (String s : splitResult) {
            // @ debug
            // System.out.println("count: " + count);
            if (count == 1) {
                variableToCompare = s; // ^ initial variable
                // @ debug
                // System.out.println("initial var: " + variableToCompare);
            } else if (count != 1) {
                // ^ if length more than 2 than split
                if (variableToCompare.length() >= 2) {
                    variableToCompare = variableToCompare.substring(1);
                    // @ debug
                    // System.out.println("after substring: " + variableToCompare);
                }

                // ^ combine with previous variable
                if (variableToCompare.length() < 2) {
                    variableToCompare = variableToCompare.concat(s);
                    // @ debug
                    // System.out.println("after concat: " + variableToCompare);
                }

                // ^ add to list
                if (variableToCompare.length() > 1)
                    listOfVariablesToCompare.add(variableToCompare);
            }
            count++;
        }

        List<Grammar> grammarList = control.getAllGrammars();
        List<String> result = new ArrayList<>();
        boolean flag = false;
        for (String variable : listOfVariablesToCompare) {
            flag = false;
            for (Grammar grammar : grammarList) {
                for (String gra : grammar.getVariable()) {
                    System.out.print(
                            "Compare: " + variable + " against grammar: "
                                    + grammar.getStartVariable()
                                    + " var: "
                                    + gra + " Action > ");

                    if (variable.equals(gra)) {
                        System.out.println(
                                "Add " + grammar.getStartVariable() + " to CykResults Step: " + 2);
                        result.add(grammar.getStartVariable());
                        flag = true;
                    } else {
                        System.out.print("\n");
                    }
                }
            }
            System.out.print(BLUE + "End action > ");
            if (flag == false) { // ^ insert "#" for comparisons that cannot generate
                System.out.print("adding #");
                result.add("#");
            } else 
            System.out.print("no action");
            System.out.println(RESET);
        }

        System.out.println(GREEN + "Add CykResults (Step 2) to DataLists" + RESET);
        control.addCykResult(2, new ArrayList<>(result));
        result.clear(); 

        step3_n();
    }

    public int[] generateInitialCoordinates(int z1, int z2) {
        int[] tempArray = {0, z2, (z1 - 1), (z2 + 1)};
        return tempArray;
    }

    public void displayCoordinates(int x1, int x2, int y1, int y2, boolean showInitial) {
        if(showInitial) System.out.print("(Initial) " + RED + " x1: " + BLUE + x1 + RED + " x2: " + BLUE + x2 + RED + " y1: " + BLUE + y1 + RED + " y2: " + BLUE + y2 + RESET);
        else System.out.print(RED + " x1: " + YELLOW + x1 + RED + " x2: " + YELLOW + x2 + RED + " y1: " + YELLOW + y1 + RED + " y2: " + YELLOW + y2 + RESET);
    }

    public void step3_n() {
        System.out.println(YELLOW_BACKGROUND + "========== Step 3_n ==========" + RESET);
        int step = 3;
        List<CnfInput> cnfInputList = control.getAllCnfInputs();
        int cnfInputSize = 0;
        for (CnfInput cnfInput : cnfInputList) { // ! TODO: refactor - separate CNF List
            cnfInputSize = cnfInput.getCnfInputInList().size();
        }
        System.out.println("input size: " + cnfInputSize);

        String resultFromStep1 = new String("");
        resultFromStep1 = control.getCykResultByStep(1);
        List<String> splitResult1 = splitStringRegex(resultFromStep1, "|", true);
        System.out.println(BLUE + "resultFromStep1: " + resultFromStep1 + RESET);

        String resultFromStep2 = new String("");
        resultFromStep2 = control.getCykResultByStep(2);
        List<String> splitResult2 = splitStringRegex(resultFromStep2, "|", true);
        System.out.println(BLUE + "resultFromStep2: " + resultFromStep2 + RESET);

        String cnfResultsArray[][] = new String[cnfInputSize][cnfInputSize];
        // ^ initialize step 1
        int count = 0;
        for (String s : splitResult1) {
            cnfResultsArray[0][count] = s;
            count++;
        }

        // ^ initialize step 2
        count = 0;
        for (String s : splitResult2) {
            cnfResultsArray[1][count] = s;
            count++;
        }

        int inner = 3;
        for (int i = 0; i < 2; i++) {
            System.out.print("ROW " + (i + 1) + ": ");
            inner--;
            for (int j = 0; j <= inner; j++) {
                System.out.print("(" + i + "," + j + ") " + cnfResultsArray[i][j]);
                if (j != inner)
                    System.out.print(" | ");
            }
            System.out.println();
        }

        // ^ STEP 3 START
        System.out.println(BLUE + "Step 3 actual START" + RESET);
        List<Grammar> grammarList = control.getAllGrammars();
        List<String> result = new ArrayList<>();
        List<String> tempResult = new ArrayList<>();
        boolean flag = false;

        int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
        for (int i = 0; i < cnfInputSize; i++) { 
            List<String> tempResultCol = new ArrayList<>();
            // System.out.print("Initial state > ");
            // displayCoordinates(x1, x2, y1, y2, true);

            if (i > 1) { // skip steps 1 & 2
                System.out.print(WHITE_BACKGROUND + "ROW " + (i + 1) + " >> i: " + i + " " + RESET);
                displayCoordinates(x1, x2, y1, y2, true);
                System.out.println();
                for (int j = 0; j < (cnfInputSize - i); j++) { 
                    // $ ======================
                    int[] tempArray = generateInitialCoordinates(i, j);
                    String msgg = ""; int iter = 0;
                    for(int coordinate : tempArray) {
                        msgg += coordinate;
                        msgg += " ";
                        iter++;
                        switch (iter) {
                            case 1: x1 = coordinate; break;
                            case 2: x2 = coordinate; break;
                            case 3: y1 = coordinate; break;
                            case 4: y2 = coordinate; break;
                        }
                    }
                    // System.out.println(GREEN + " here: " + msgg + RESET);
                    // $ ======================
                    System.out.print(BLUE_BACKGROUND + "COL " + j + " >> j: " + j + " " + RESET);
                    // displayCoordinates(x1, x2, y1, y2, false);
                    System.out.println();

                    for(int k = 0; k < i; k++) { // k should execute (row num) times
                        if(k != 0) {
                            x1++; // x1 increment 1
                            // x2 remain
                            y1--; // y1 decrement 1
                            y2++; // y2 increment 1
                        }
                        System.out.println();
                        System.out.print(RED_BACKGROUND + "ROCO " + k + " >> k: " + k + " " + RESET);
                        displayCoordinates(x1, x2, y1, y2, false);
                        System.out.println();
                        
                        String tempFirst = getVariableFromResultsArray(cnfResultsArray, cnfInputSize, x1, x2);
                        // System.out.print("Compare (" + x1 + "," + x2 + ") " + tempFirst + " and ");
                        String tempSecond = getVariableFromResultsArray(cnfResultsArray, cnfInputSize, y1, y2);
                        // System.out.print("Compare (" + y1 + "," + y2 + ") " + tempSecond);
                        String variable = "";
                        variable += tempFirst;
                        variable += tempSecond;
                        
                        flag = false;
                        for (Grammar grammar : grammarList) {
                            for (String gra : grammar.getVariable()) {
                                System.out.print("Compare: " + variable + " against grammar: " + grammar.getStartVariable() + " var: " + gra + " Action > ");
                                                
                                if (variable.equals(gra)) {
                                    System.out.println("Add " + grammar.getStartVariable() + " to CykResults Step: " + 3);
                                    tempResult.add(grammar.getStartVariable());
                                    cnfResultsArray[i][j] = grammar.getStartVariable();
                                    flag = true;
                                } else {
                                    System.out.print("\n");
                                }
                            }
                        }
                        System.out.print(PURPLE + "End action > ");
                        if (flag == false) { // ^ insert "#" for comparisons that cannot generate
                            System.out.println("adding #");
                            tempResult.add("#");
                            
                        } else {
                            System.out.println("action taken");
                            System.out.print("action for cnfResultsArray");
                        }
                    }
                    x1++; // x1 increment 1
                    // x2 remain
                    y1--; // y1 decrement 1
                    y2++; // y2 increment 1
                    
                    System.out.println("" + RESET);
 
                    String msg = "";
                    for(String s : tempResult) {
                        msg += s;
                        msg += "|";
                        if(!s.equals("#")) result.add(s);
                    }
                    System.out.println(BLUE + "check COL " + j + ": " + msg + RESET);

                    System.out.println(BLUE + "Add " + getVariableFromTempResults(msg) + " to tempResultCol" + RESET);
                    tempResultCol.add(getVariableFromTempResults(msg));
                    
                    System.out.println(GREEN + "adding " + getVariableFromTempResults(msg) + " to cnfResultsArray["+i+"]["+j+"]" + RESET);
                    cnfResultsArray[i][j] = getVariableFromTempResults(msg);

                    tempResult.clear();
                }
            }
            String msg = ""; int county = 0;
            for(String s : tempResultCol) {
                county++;
                msg += s;
                if(county != tempResultCol.size()) {
                    msg += "|";
                }
                if(!s.equals("#")) result.add(s);
            }

            if(i > 1) {
                System.out.println("check ROW " + (i + 1) + ": " + msg);
                // for (String s : tempResultCol) {
                //     System.out.println(GREEN_BACKGROUND + "Check results: " + s + RESET);
                // }
                System.out.println(GREEN + "Add CykResults (Step " + step +") to DataLists" + RESET);
                control.addCykResult(step++, new ArrayList<>(tempResultCol));
                tempResult.clear();
                tempResultCol.clear();
                result.clear();
            } 
        }
        checkResults();
    }

    public String getVariableFromTempResults(String tempResults){
        List<String> splitResult  = splitStringRegex(tempResults, "|", false);
        String msg = "";
        int count = -1;
        for(String s : splitResult) { 
            count++;
            if(!s.equals("#")) msg += s;
            if(count == (splitResult.size()-1) && msg.length() == 0) msg += '#';
            // System.out.println(">>> !s.equals(#): " + !s.equals("#"));
            // System.out.println(">>> " + count + " == " + (splitResult.size()-1) + " :: " + (count == (splitResult.size()-1)));
        }
        return msg;
    }

    public String getVariableFromResultsArray(String[][] cnfResultsArray, int cnfInputSize, int x1, int x2) {
        String variable = "";
        for (int i = 0; i < cnfInputSize; i++) {
            for (int j = 0; j < cnfInputSize; j++) {
                if (x1 == i && x2 == j) {
                    variable = cnfResultsArray[i][j];
                    System.out.println("Coordinate: (" + x1 + "," + x2 + "): " + variable);
                }
            }
        }
        return variable;
    }

    public void checkResults() {
        System.out.println(YELLOW_BACKGROUND + "========== CHECK Results ==========" + RESET);
        List<CnfInput> cnfInputList = control.getAllCnfInputs();
        int cnfInputSize = 0, step = 1;
        String cnfInputFromUser = "";
        for (CnfInput cnfInput : cnfInputList) { // ! TODO: refactor - separate CNF List
            cnfInputSize = cnfInput.getCnfInputInList().size();
            cnfInputFromUser = cnfInput.getCnfInputInString();
        }
        System.out.println("input size: " + cnfInputSize);

        System.out.println("==================================================");
        for(int count = 0; count < cnfInputSize; count++) {   
            String resultFromStep = new String("");
            resultFromStep = control.getCykResultByStep(step);
            System.out.println(BLUE + "resultFromStep" + (step++) + ": " + resultFromStep + RESET);
            if (count == (cnfInputSize - 1)) {
                if (resultFromStep.equals("S")) System.out.println(GREEN + "Conclusion: " + cnfInputFromUser + " IS PART of the language" + RESET);
                else System.out.println(RED + "Conclusion: " + cnfInputFromUser + " IS NOT PART of the language" + RESET);
            }
        }
        System.out.println("==================================================");
    }

    /*
     * =======================================
     * ====== SECTION : Utility Methods ======
     * =======================================
     */

    public List<String> splitStringRegex(String stringToSplit, String regexParams, boolean includeSeparator) {
        List<String> listString = new ArrayList<>();
        String[] split = stringToSplit.split(regexParams);
        for (int i = 0; i < split.length; i++) {
            if (includeSeparator) 
                listString.add(split[i]);
            else 
                if (!split[i].equals(regexParams))
                    listString.add(split[i]);
        }
        return listString;
    }

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
