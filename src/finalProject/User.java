package finalProject;

public class User {

    private String emailAddress;
    private String pswrd;
    private String fullName;

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }

    public void setPswrd(String pswrd){
        this.pswrd = pswrd;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public String getPswrd(){
        return pswrd;
    }

    public String getFullName(){
        return fullName;
    }

}
