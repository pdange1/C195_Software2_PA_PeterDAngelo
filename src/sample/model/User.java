package sample.model;

public class User {

    private int userId;
    private String userName;
//  private String password;

    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
//      this.password = password; no need to add this as it will either be encrypted or you will not want to reference it anyway.
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    @Override //Override refers to the Parent. Study OVERRIDE again. i.e. REVIEW
    public String toString() {
        return "[" + userId + "]" + userName;
    }

}
