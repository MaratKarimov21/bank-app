package banking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;


public class ServerThread implements Runnable {

    public ServerThread(Socket connection, DBHelper base) {
        try {
            this.connection = connection;
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());
            this.base = base;
        } catch(Exception e) {
            e.printStackTrace();
        };
    }

    public void run() {
        Boolean flag = true;
        Object o;
        while(flag) {
            try {
                o = input.readObject();
                System.out.println(o.toString());
                if(o instanceof String[]) {
                    recieve((String[]) o);
                }
                else if(o instanceof Integer) {
                    recieve((Integer) o);
                }
                else if(o instanceof String) {
                    recieve((String) o);
                }
                else if(o instanceof LoginForm) {
                    recieve((LoginForm) o);
                }
                else if(o instanceof RegistrationForm) {
                    recieve((RegistrationForm) o);
                }
                else if(o instanceof TransactionForm) {
                    recieve((TransactionForm) o);
                }
                else if(o instanceof Boolean) {
                    input.close();
                    output.close();
                    connection.close();
                    flag = false;
                }
            } catch(Exception e) { };
        }
    }

    public void recieve(String[] info) throws IOException, ClassNotFoundException {
//        Person p = new Person(info[0], info[1], base.getNum(), info[2], info[3], info[4]);
//        base.addPerson(p);
    }

    public void recieve(LoginForm login) throws IOException, ClassNotFoundException, SQLException {

        Customer c = base.getCustomerByPhone(login.getPhone());

        System.out.println(c);
        if (c != null && c.getPassword().equals(login.getPassword())) {
            System.out.println("get accs");
            c.setAccounts(base.getAccounts(c));
            sendCustomer(c);
            customer = c;
        } else {
            System.out.println("not found");
            // обработка ненайденного юзера
        }
    }

    public void recieve(RegistrationForm form) throws Exception {
        System.out.println("recieve");

        Customer c = new Customer();
        c.setPhone(form.getPhone());
        c.setName(form.getName());
        c.setPassword(form.getPassword());

        Account acc = new Account();

        acc.setCustomerId(c.getPhone());
        acc.setCount(100L); // by default
        c.addAccount(acc);

        System.out.println("saving");

        base.addCustomer(c);
        base.addAccount(acc);



        System.out.println(c);

        sendCustomer(c);
        customer = c;
    }

    public void recieve(TransactionForm form) throws IOException, ClassNotFoundException, SQLException {
        Account from = base.getAccountById(form.getFromId());
        Account to = base.getAccountById(form.getToId());
        Customer c = base.doTransaction(from, to, form.getAmount());
        c.setAccounts(base.getAccounts(c));

        System.out.println(c.getAccounts());

        sendCustomer(c);
        customer = c;
    }

    public void recieve(int n) throws IOException {
//        if(n > 0) {
//            person.getAccount().deposit(n);
//            sendPerson(person);
//        }
//        else if(n < 0) {
//            person.getAccount().withdraw(n*-1);
//            sendPerson(person);
//        }
//        else { }

    }

    public void recieve(String s) throws IOException, ClassNotFoundException {
//        if(base.hasPerson(s)) {
//            sendPerson(base.getPerson(s));
//            person = base.getPerson(s);
//        }
    }

    private void sendPerson(Person p) {
        try {
            output.writeObject(p);
            output.flush();
        } catch(IOException ioException) { };
    }

    private void sendCustomer(Customer c) {
        try {
            output.writeObject(c);
            output.flush();
        } catch(IOException ioException) { };
    }
    private void closeDown() {
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Socket connection;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private ArrayList<Person> newAccounts;
    private DBHelper base;
    private Customer customer;
}
