import java.util.*;

public class CnfInput {
    private String cnfInputInString;
    private List<String> cnfInputInList;

    public CnfInput() {
        this.cnfInputInString = "";
        this.cnfInputInList = null;
    }

    public CnfInput(String cnfInputInString, List<String> cnfInputInList) {
        this.cnfInputInString = cnfInputInString;
        this.cnfInputInList = cnfInputInList;
    }

    public String getCnfInputInString() {
        return this.cnfInputInString;
    }

    public List<String> getCnfInputInList() {
        return this.cnfInputInList;
    }

    public void setCnfInputInString(String cnfInputInString) {
        this.cnfInputInString = cnfInputInString;
    }

    public void setCnfInputInList(List<String> cnfInputInList) {
        this.cnfInputInList = cnfInputInList;
    }
}
