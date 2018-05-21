package pbs.researchmobile.com.mach.users;


public class User {
    public String uid;
    public String email;
    public String username;
    public String firebaseToken;

    public User(){

    }

    public User(String uid, String email, String username){
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.firebaseToken = firebaseToken;
    }
}
