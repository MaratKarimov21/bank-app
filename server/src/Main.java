import banking.Account;
import banking.Customer;
import banking.DBHelper;
import banking.Server;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws Exception {

        Server server = new Server();

        server.startRunning();


//        DBHelper db = new DBHelper("localhost", 5433, "maratkarimov", "");
//
//        Account acc1 = db.getAccountById(1);
//        Account acc2 = db.getAccountById(2);
//
//        System.out.println(acc1.getCount());
//        System.out.println(acc2.getCount());
//
//
//        //db.updateCount(acc1.getCount() + 20, acc1.getId());
//
//        db.doTransaction(acc1, acc2, 20);
//
//

//        Customer c1 = new Customer();
//        c1.setPhone(89872218378L);
//        c1.setName("dodo");
//        c1.setPassword("toto");
//
//        Account acc = new Account();
//        acc.setCount(100L);
//        acc.setCustomerId(c1.getPhone());
//
//
//
//        c1.addAccount(acc);
//
//
//        db.addCustomer(c1);
//
//        db.addAccount(acc);
//
//        System.out.println(db.getAccounts(c1));


    }
}