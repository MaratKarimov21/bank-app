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


public class Server {

    //set up frame details
    public Server() throws SQLException {
//        super("Server");
//
//        setSize(500, 350);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setResizable(false);
//        setVisible(true);
//
//        panel = new JPanel();
//        add(panel);
//        area = new JTextArea();
//        area.setEditable(false);

        //launch();
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
                    System.out.println("Server ended the connection! \n");
                }
            }
        } catch(IOException ioException) { };
    }

    //Server waits for connection
    private void waitForConnection() throws IOException {
        System.out.println("Waiting for connections... \n");
        connection = server.accept();
        pool.execute(new ServerThread(connection, base));
        System.out.println("Now connected to " + connection.getInetAddress().getHostName() + "\n");
    }


    //send updates
//    private void showMessage(final String message) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                area.append(message);
//            }
//        });
//    }

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


