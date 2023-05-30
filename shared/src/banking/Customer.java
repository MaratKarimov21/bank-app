package banking;

import java.util.ArrayList;
import java.util.Collections;

public class Customer implements java.io.Serializable {
    private long phone;
    private String name;
    private String password;

    private ArrayList<Account> accounts = new ArrayList<Account>();

    public ArrayList<Account> addAccount(Account acc) {
        accounts.add(acc);
        return accounts;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> accs) {
        accounts = accs;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
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

    public void setPassword(String password) throws Exception {
        this.password = password;
    }

    public void setPasswordHash(String hash){
        this.password = hash;
    }

    public boolean checkPassword(String psw) throws Exception {
        return true;
    }

    @Override
    public String toString(){
        return getPhone()+": "+getName()+" ("+getPassword()+")";
    }
}
