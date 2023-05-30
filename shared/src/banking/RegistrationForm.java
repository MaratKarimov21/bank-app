package banking;

public class RegistrationForm implements java.io.Serializable {
    public long getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = Long.parseLong(phone);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private long phone;
    private String name;
    private String password;

}
