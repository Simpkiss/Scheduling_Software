package model;
/** Model for users. */
public class user {
    private int userID;
    private String userName;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public user(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }
}
