package banking;

public class RegistrationForm implements java.io.Serializable {

    public boolean validatePhone() {
        return String.valueOf(phone).length() == 11;
    }
    public boolean validatePassword() {
        return password.length() >= 6;
    }
    public boolean validateName() {
        return name.length() >= 4;
    }
    public boolean validateAll() {
        return validatePhone() && validateName() && validatePassword();
    }
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
