package banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBHelper {
    private Connection conn;
    public DBHelper(
            String host,
            int port,
            String user,
            String password
    ) throws SQLException {
        var connStr = "jdbc:postgresql://" + host + ":" + port + "/demo";
        conn = DriverManager.getConnection(connStr, user, password);
    }

    public void addCustomer(Customer c) throws SQLException {
        var sql = "INSERT INTO customers (customer_phone, customer_name, customer_password) " +
                "VALUES (?, ?, ?)";
        var st = conn.prepareStatement(sql);
        st.setLong(1, c.getPhone());
        st.setString(2, c.getName());
        st.setString(3, c.getPassword());
        st.executeUpdate();
    }

    public void addAccount(Account acc) throws SQLException {
        var sql = "INSERT INTO accounts (customer_id, account_count) " +
                "VALUES (?, ?)";
        var st = conn.prepareStatement(sql);
        st.setLong(1, acc.getCustomerId());
        st.setLong(2, acc.getCount());
        st.executeUpdate();
    }

    public ArrayList<Customer> getCustomers() throws SQLException {
        var customers = new ArrayList<Customer>();
        var sql = "SELECT * FROM customers";
        var st = conn.createStatement();
        var cursor = st.executeQuery(sql);
        while (cursor.next()){
            Customer c = new Customer();
            c.setPhone(cursor.getLong("phone"));
            c.setName(cursor.getString("name"));
            c.setPasswordHash(cursor.getString("password"));
            customers.add(c);
        }
        return customers;
    }

    public ArrayList<Account> getAccounts(Customer c) throws SQLException {
        var accounts = new ArrayList<Account>();
        var sql = "SELECT * FROM accounts WHERE customer_id=?";
        var st = conn.prepareStatement(sql);
        st.setLong(1, c.getPhone());
        var cursor = st.executeQuery();
        while (cursor.next()){
            Account acc = new Account();
            acc.setId(cursor.getLong("account_id"));
            acc.setCustomerId(cursor.getLong("customer_id"));
            acc.setCount(cursor.getLong("account_count"));

            accounts.add(acc);
        }
        return accounts;
    }

    public Customer getCustomerByPhone(long phone) throws SQLException {
        Customer c = new Customer();
        var sql = "SELECT * FROM customers WHERE customer_phone=?";
        var st = conn.prepareStatement(sql);
        st.setLong(1, phone);
        var cursor = st.executeQuery();
        if (cursor.next()){
            c.setPhone(cursor.getLong("customer_phone"));
            c.setName(cursor.getString("customer_name"));
            c.setPasswordHash(cursor.getString("customer_password"));
            return c;
        } else return null;
    }

    public Account getAccountById(long id) throws SQLException {
        Account c = new Account();
        var sql = "SELECT * FROM accounts WHERE account_id=?";
        var st = conn.prepareStatement(sql);
        st.setLong(1, id);
        var cursor = st.executeQuery();
        if (cursor.next()){
            c.setId(cursor.getLong("account_id"));
            c.setCustomerId(cursor.getLong("customer_id"));
            c.setCount(cursor.getLong("account_count"));

            return c;
        } else return null;
    }

    public void updateCount(long newCount, long id) throws SQLException {
        var sql = "UPDATE accounts SET account_count=? WHERE account_id=?";
        var st = conn.prepareStatement(sql);
        st.setLong(1, newCount);
        st.setLong(2, id);
        st.executeUpdate();
    }

    public Customer doTransaction(Account from, Account to, long amount) throws SQLException {
        try {
            conn.setAutoCommit(false);
            Customer sender = getCustomerByPhone(from.getCustomerId());

            if (from.getCount() >= amount) {
                updateCount(from.getCount() - amount, from.getId());
                updateCount(to.getCount() + amount, to.getId());
            }

            conn.commit();
            System.out.println("Transaction is commited successfully.");

            return sender;
        } catch (SQLException e) {
            conn.rollback();
            return null;
        }
    }

    public void clearAll() throws SQLException {
        conn.createStatement().executeUpdate("DELETE FROM `customers`");
    }


}