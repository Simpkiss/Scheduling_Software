package model;


import java.time.LocalDateTime;
/** Appointment model. */
public class appointment {
    private int apptID;
    private String apptTitle;
    private String apptDesc;
    private String apptLocation;
    private String apptType;
    private String dateNice;
    private String startTimeNice;
    private String endTimeNice;
    private LocalDateTime apptStart;
    private LocalDateTime apptEnd;
    private LocalDateTime apptCreated;
    private String apptBookedBy;
    private LocalDateTime apptUpdated;
    private String apptUpdatedBy;
    private int customerID;
    private int userID;
    private int contactID;

    public String getStartTimeNice() {
        return startTimeNice;
    }

    public String getEndTimeNice() {
        return endTimeNice;
    }

    public String getDateNice() {
        return dateNice;
    }

    public int getApptID() {
        return apptID;
    }

    public String getApptTitle() {
        return apptTitle;
    }

    public String getApptDesc() {
        return apptDesc;
    }

    public String getApptLocation() {
        return apptLocation;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getUserID() {
        return userID;
    }

    public int getContactID() {
        return contactID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public appointment (int apptID, String apptTitle, String apptDesc, String apptLocation, String apptType, String dateNice, String startTimeNice, String endTimeNice,
                        LocalDateTime apptStart, LocalDateTime apptEnd, LocalDateTime apptCreated, String apptBookedBy,
                        LocalDateTime apptUpdated, String apptUpdatedBy, int customerID, int userID, int contactID){
        this.apptID = apptID;
        this.apptTitle = apptTitle;
        this.apptDesc = apptDesc;
        this.apptLocation = apptLocation;
        this.apptType = apptType;
        this.dateNice = dateNice;
        this.startTimeNice = startTimeNice;
        this.endTimeNice = endTimeNice;
        this.apptStart = apptStart;
        this.apptEnd = apptEnd;
        this.apptCreated = apptCreated;
        this.apptBookedBy = apptBookedBy;
        this.apptUpdated = apptUpdated;
        this.apptUpdatedBy = apptUpdatedBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    public void setApptID(int apptID) {
        this.apptID = apptID;
    }

    public void setApptTitle(String apptTitle) {
        this.apptTitle = apptTitle;
    }

    public void setApptDesc(String apptDesc) {
        this.apptDesc = apptDesc;
    }

    public void setApptLocation(String apptLocation) {
        this.apptLocation = apptLocation;
    }

    public String getApptType() {
        return apptType;
    }

    public void setApptType(String apptType) {
        this.apptType = apptType;
    }

    public void setDateNice(String dateNice) {
        this.dateNice = dateNice;
    }

    public void setStartTimeNice(String startTimeNice) {
        this.startTimeNice = startTimeNice;
    }

    public void setEndTimeNice(String endTimeNice) {
        this.endTimeNice = endTimeNice;
    }

    public LocalDateTime getApptStart() {
        return apptStart;
    }

    public void setApptStart(LocalDateTime apptStart) {
        this.apptStart = apptStart;
    }

    public LocalDateTime getApptEnd() {
        return apptEnd;
    }

    public void setApptEnd(LocalDateTime apptEnd) {
        this.apptEnd = apptEnd;
    }

    public LocalDateTime getApptCreated() {
        return apptCreated;
    }

    public void setApptCreated(LocalDateTime apptCreated) {
        this.apptCreated = apptCreated;
    }

    public String getApptBookedBy() {
        return apptBookedBy;
    }

    public void setApptBookedBy(String apptBookedBy) {
        this.apptBookedBy = apptBookedBy;
    }

    public LocalDateTime getApptUpdated() {
        return apptUpdated;
    }

    public void setApptUpdated(LocalDateTime apptUpdated) {
        this.apptUpdated = apptUpdated;
    }

    public String getApptUpdatedBy() {
        return apptUpdatedBy;
    }

    public void setApptUpdatedBy(String apptUpdatedBy) {
        this.apptUpdatedBy = apptUpdatedBy;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
}
