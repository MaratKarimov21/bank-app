package banking;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;


public class Server extends JFrame {

    //set up frame details
    public Server() throws SQLException {
        super("Server");

        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        panel = new JPanel();
        add(panel);
        area = new JTextArea();
        area.setEditable(false);

        //launch();
    }

    //init frame with components
    public void launch() {

//        panel.removeAll();
//
//        panel.setLayout(new BorderLayout());
//
//        base.sumUp();
//
//        final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//        tabbedPane.setBounds(6, 6, 438, 266);
//
//        JPanel tab1 = new JPanel();
//        tab1.setLayout(new FlowLayout());
//        tab1.add(new JLabel("Total: "));
//        final JLabel total = new JLabel(base.getTotal() + "");
//        tab1.add(total);
//
//
//        JPanel tab2 = new JPanel();
//        tab2.setLayout(new FlowLayout());
//        tab2.add(new JLabel("Number of Accounts"));
//        final JLabel allAcc = new JLabel();
//        allAcc.setText(base.getList().size() + "");
//        tab2.add(allAcc);
//
//
//        JPanel tab3 = new JPanel();
//        tab3.setLayout(new FlowLayout());
//        final JList list = new JList(base.getList().toArray());
//        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//        list.setLayoutOrientation(JList.VERTICAL);
//        list.setVisibleRowCount(-1);
//        JScrollPane scroll = new JScrollPane(list);
//        scroll.setPreferredSize(new Dimension(100, 200));
//        tab3.add(scroll);
//        JButton find = new JButton("Find!");
//        find.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent ae) {
//                Person temp = base.find((String) list.getSelectedValue());
//                JOptionPane.showMessageDialog(null, temp.getInfo());
//            }
//        });
//        tab3.add(find);
//        tabbedPane.addTab("All", tab3);
//
//        JButton tab0 = new JButton();
//        tab0.setLayout(new GridLayout(1,1));
//        tab0.add(new JButton(new AbstractAction("Refresh") {
//            public void actionPerformed(ActionEvent ae) {
//
//                total.setText(base.getTotal() + "");
//                allAcc.setText(base.getSize() + "");
//
//                final JPanel tab3 = new JPanel();
//                tab3.setLayout(new FlowLayout());
//                final JList list = new JList(base.getList().toArray());
//                list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//                list.setLayoutOrientation(JList.VERTICAL);
//                list.setVisibleRowCount(-1);
//                JScrollPane scroll = new JScrollPane(list);
//                scroll.setPreferredSize(new Dimension(100, 200));
//                tab3.add(scroll);
//                JButton find = new JButton("Info!");
//                find.addActionListener(new ActionListener() {
//                    public void actionPerformed(ActionEvent ae) {
//                        Person temp = base.find((String) list.getSelectedValue());
//                        JOptionPane.showMessageDialog(null, temp.getInfo());
//                    }
//                });
//                tabbedPane.remove(3);
//                tab3.add(find);
//                tabbedPane.addTab("All", tab3);
//
//                JOptionPane.showMessageDialog(null, "All server tabs have been refreshed!");
//            }
//        }));
//
//        tabbedPane.add(("Refresh"), tab0);
//        tabbedPane.addTab("Data", tab1);
//        tabbedPane.addTab("Stats", tab2);
//        tabbedPane.addTab("All", tab3);
//
//        panel.add(tabbedPane, BorderLayout.NORTH);
//
//        panel.add(new JScrollPane(area), BorderLayout.CENTER);
//
//        panel.updateUI();
    }

    //Server actions
    public void startRunning() {

        pool = Executors.newCachedThreadPool();

        try {
            server = new ServerSocket(1337, 100);
            while(true) {
                try {
                    waitForConnection();
                } catch(EOFException eofException) {
                    showMessage("Server ended the connection! \n");
                }
            }
        } catch(IOException ioException) { };
    }

    //Server waits for connection
    private void waitForConnection() throws IOException {
        showMessage("Waiting for connections... \n");
        connection = server.accept();
        pool.execute(new ServerThread(connection, base));
        showMessage("Now connected to " + connection.getInetAddress().getHostName() + "\n");
    }


    //send updates
    private void showMessage(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                area.append(message);
            }
        });
    }

    //close connections
    private void closeDown() {
        try {
            output.close();
            input.close();
            connection.close();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }

    //MultiThread Variables
    private ExecutorService pool;

    //Frame Variables
    private JPanel panel;
    private DBHelper base = new DBHelper("localhost", 5433, "demo", "");
    private JTextArea area;

    //Server Variables
    private ServerSocket server;
    private Socket connection;
    private ObjectOutputStream output;
    private ObjectInputStream input;

}


