package engine.network;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Client {
  public static DataOutputStream dos;
  public static DataInputStream dis;
  public static Socket s;
  public static serverThread server;
  public static String username;
  public static String connectIP;

  public static JTextPane chatMessages = new JTextPane();
  public static JScrollPane JPchatMessages = new JScrollPane(chatMessages);

  public static String msgHistory = "";

  public static void main(String[] args) throws IOException {
    JFrame frame1 = new JFrame("Bomberman Chatroom");
    JFrame frame2 = new JFrame("LAN");

    frame2.setSize(500, 700);
    frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame2.setLayout(new GridBagLayout());

    JLabel helloUser = new JLabel("Welcome to Bomberman Public Chatroom! ");
    frame2.add(
        helloUser,
        new GridBagConstraints(
            0,
            0,
            1,
            1,
            3,
            1,
            GridBagConstraints.CENTER,
            GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0),
            0,
            0));

    chatMessages.setEditable(false);
    frame2.add(
        JPchatMessages,
        new GridBagConstraints(
            0,
            1,
            3,
            1,
            1.0,
            100.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0),
            0,
            0));

    JTextField message = new JTextField(20);
    frame2.add(
        message,
        new GridBagConstraints(
            0,
            2,
            1,
            1,
            .5,
            1,
            GridBagConstraints.CENTER,
            GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0),
            0,
            0));

    JButton send = new JButton("send");
    frame2.add(
        send,
        new GridBagConstraints(
            1,
            2,
            1,
            1,
            10,
            .25,
            GridBagConstraints.CENTER,
            GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0),
            0,
            0));
    send.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent sendButtonClick) {
            String msg = message.getText();
            message.setText(null);
            try {
              dos.writeUTF(username + " : " + msg);
            } catch (IOException e) {
            }
          }
        });

    frame1.setLayout(new GridBagLayout());

    frame1.setSize(500, 700);
    frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // TO-DO: add logo
    JLabel background = new JLabel(new ImageIcon("resources/logo/logo.png"), JLabel.CENTER);
    frame1.add(
        background,
        new GridBagConstraints(
            0,
            0,
            1,
            1,
            2.0,
            1.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0),
            0,
            0));

    JLabel enter = new JLabel("Enter your nickname: ");
    frame1.add(
        enter,
        new GridBagConstraints(
            0,
            2,
            1,
            1,
            1.0,
            1.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0),
            0,
            0));

    JTextField usernameTextArea = new JTextField(10);
    frame1.add(
        usernameTextArea,
        new GridBagConstraints(
            0,
            3,
            1,
            1,
            1.0,
            1.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0),
            0,
            0));

    JLabel serverIP = new JLabel("Enter server IP: ");
    frame1.add(
        serverIP,
        new GridBagConstraints(
            0,
            4,
            1,
            1,
            1.0,
            1.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0),
            0,
            0));

    JTextField serverIPTextArea = new JTextField(10);
    frame1.add(
        serverIPTextArea,
        new GridBagConstraints(
            0,
            5,
            1,
            1,
            1.0,
            1.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0),
            0,
            0));

    JButton login = new JButton("Join chat");
    frame1.add(
        login,
        new GridBagConstraints(
            0,
            6,
            1,
            1,
            1.0,
            1.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0),
            0,
            0));

    login.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent buttonClick) {
            username = usernameTextArea.getText();
            connectIP = serverIPTextArea.getText();
            helloUser.setText("Hello " + username + ". Welcome !");
            frame1.setVisible(false);

            try {
              s = new Socket(connectIP, 8245);
              dos = new DataOutputStream(s.getOutputStream());
              dis = new DataInputStream(s.getInputStream());

              server = new serverThread(dis);
              Thread t = new Thread(server);
              t.start();

              frame2.setVisible(true);
            } catch (IOException e) {
              System.out.println("Server is currently unavailable. Please try again in a few minutes ...");
              return;
            }
          }
        });

    frame1.setVisible(true);
  }

  public static void updateMessageArea(String msg) {
    msgHistory = msgHistory + "\n";
    msgHistory = msgHistory + msg;
    chatMessages.setText(msgHistory);
  }

  public static void terminate() {
    System.exit(0);
  }

  public static void reconnect() {
    try {
      s.close();
      s = new Socket(connectIP, 8245);
      dos = new DataOutputStream(s.getOutputStream());
      dis = new DataInputStream(s.getInputStream());
      server = new serverThread(dis);
      Thread newConnection = new Thread(server);
      newConnection.start();

    } catch (Exception e) {
      System.out.println("Exception caught in reconnect().");
    }
  }
}

class serverThread extends Thread {
  DataInputStream disServer;
  public boolean isRunning;
  public serverThread(DataInputStream z) {
    disServer = z;
    isRunning = true;
  }

  @Override
  public void run() {
    while (isRunning) {
      try {
        String str = disServer.readUTF();
        Client.updateMessageArea(str);
      } catch (IOException e) {
        System.out.println("Exception in run method. Reconnecting..");
        Client.reconnect();
        break;
      }
    }
  }
}
