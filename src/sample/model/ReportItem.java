package sample.model;

public class ReportItem {

    private String valueString1;
    private String valueString2;
    private int valueInt;

    public ReportItem(String valueString1, String valueString2, int valueInt) {
        this.valueString1 = valueString1;
        this.valueString2 = valueString2;
        this.valueInt = valueInt;
    }

    public String getValueString1() {
        return valueString1;
    }

    public void setValueString1(String valueString1) {
        this.valueString1 = valueString1;
    }

    public String getValueString2() {
        return valueString2;
    }

    public void setValueString2(String valueString2) {
        this.valueString2 = valueString2;
    }

    public int getValueInt() {
        return valueInt;
    }

    public void setValueInt(int valueInt) {
        this.valueInt = valueInt;
    }

}
