package model;

/** Report model that allows reports of a list of strings and integers associated with them.
 */
public class report {
    private String stringValue;
    private int intValue;

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public report(String stringValue, int intValue){
        this.stringValue = stringValue;
        this.intValue = intValue;
    }
}
