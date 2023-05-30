package banking;

public class LoginForm implements java.io.Serializable {

    private long phone;
    private String password;
    public void setPhone(String phone) {
        this.phone = Long.parseLong(phone);
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public long getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

}
