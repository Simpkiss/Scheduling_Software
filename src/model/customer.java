package model;

/** Customer model. */
public class customer {
    private int customerID;
    private String customerName;
    private String customerAddress;
    private String customerPostCode;
    private String customerPhone;
    private String customerCreatedBy;
    private int divisionID;

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    private String divisionName;

    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerPostCode() {
        return customerPostCode;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public customer (int customerID, String customerName, String customerAddress, String customerPostCode, String customerPhone, String customerCreatedBy, int divisionID, String divisionName){
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostCode = customerPostCode;
        this.customerPhone = customerPhone;
        this.customerCreatedBy = customerCreatedBy;
        this.divisionID = divisionID;
        this.divisionName = divisionName;
    }
}
