package banking;

import javax.swing.*;
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
               if(o instanceof LoginForm) {
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


    public void recieve(LoginForm login) throws IOException, ClassNotFoundException, SQLException {
        Customer c = base.getCustomerByPhone(login.getPhone());

        if (c != null && c.getPassword().equals(login.getPassword())) {
            c.setAccounts(base.getAccounts(c));
            var accountsList = base.getAllAccounts();
            String[] stringArray = accountsList.toArray(new String[0]);
            c.setPossibleAccountIds(stringArray);
            sendCustomer(c);
            customer = c;
        } else {
            System.out.println("not found");
            ErrorMessage err = new ErrorMessage("Customer not found!");
            sendErrorMessage(err);
        }
    }

    public void recieve(RegistrationForm form) throws Exception {
        if (form.validateAll()) {
            Customer c = new Customer();
            c.setPhone(form.getPhone());
            c.setName(form.getName());
            c.setPassword(form.getPassword());

            Account acc = new Account();

            acc.setCustomerId(c.getPhone());
            acc.setCount(100L); // by default
            //c.addAccount(acc);

            System.out.println("saving");

            base.addCustomer(c);
            base.addAccount(acc);

            c.setAccounts(base.getAccounts(c));

            var accountsList = base.getAllAccounts();
            String[] stringArray = accountsList.toArray(new String[0]);
            c.setPossibleAccountIds(stringArray);
            sendCustomer(c);

            sendCustomer(c);
            customer = c;
        } else {
            ErrorMessage err = new ErrorMessage("Data is invalid!");
            sendErrorMessage(err);
        }
    }

    public void recieve(TransactionForm form) throws IOException, ClassNotFoundException, SQLException {
        Account from = base.getAccountById(form.getFromId());
        Account to = base.getAccountById(form.getToId());

        if (customer.getPhone() == from.getCustomerId()) {
            Customer c = base.doTransaction(from, to, form.getAmount());
            c.setAccounts(base.getAccounts(c));

            sendCustomer(c);
        } else {
            ErrorMessage err = new ErrorMessage("not your account!");
            sendErrorMessage(err);
        }
    }

    private void sendCustomer(Customer c) {
        try {
            output.writeObject(c);
            output.flush();
        } catch(IOException ioException) { };
    }

    private void sendErrorMessage(ErrorMessage err) {
        try {
            output.writeObject(err);
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

    private DBHelper base;
    private Customer customer;
}
