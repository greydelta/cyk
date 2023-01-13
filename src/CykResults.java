import java.util.*;

public class CykResults {
    private int step;
    private List<String> cykResultsList;

    public CykResults() {
        this.step = 0;
        this.cykResultsList = null;
    }

    public CykResults(int step, List<String> cykResultsList) {
        this.step = step;
        this.cykResultsList = cykResultsList;
    }

    public int getStep() {
        return this.step;
    }

    public List<String> getCykResultsList() {
        return this.cykResultsList;
    }

    public String getCykResultsInFullForm() {
        String msg = "";
        int count = 0;
        for (String cyk : this.cykResultsList) {
            count++;
            msg += cyk.toString();
            if (count != this.getCykResultsList().size())
                msg += "|";
        }
        return msg;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setCykResultsList(List<String> cykResultsList) {
        this.cykResultsList = cykResultsList;
    }

    public void add(String result) {
        this.cykResultsList.add(result);
    }
}
