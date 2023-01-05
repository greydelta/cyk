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
