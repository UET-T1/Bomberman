package engine.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  public static final int maxUsers = 10;
  public static user[] usersList = new user[maxUsers];
  public static int totalClientsOnline = 0;
  static ServerSocket ss;

  public static void main(String[] args) throws Exception {
    try {
      ss = new ServerSocket(8245);

      for (int i = 0; i < 10; i++) {
        usersList[i] = new user(i + 1, ss.accept());
        totalClientsOnline++;
      }

    } catch (Exception e) {
      System.out.println("Connection loss!");
    }
  }

  public void sendMessageToAll(String msg) {
    for (int c = 0; c < totalClientsOnline; c++) {
      try {
        usersList[c].sendMessage(msg);
      } catch (Exception e) {
        System.out.println("Sending message failed!");
      }
    }
  }
}

class user extends Thread {
  public Socket userSocket;
  public DataInputStream userDIS;
  public DataOutputStream userDOS;
  public Thread t;
  Server tirth = new Server();
  int userID;
  OutputStream os;

  public user(int id, Socket a) {
    try {
      userID = id;
      userSocket = a;
      userDIS = new DataInputStream(userSocket.getInputStream());
      userDOS = new DataOutputStream(userSocket.getOutputStream());
      System.out.println(userID + " client connected.");

      t = new Thread(this);
      t.start();
    } catch (Exception e) {
      System.out.println("Can't create new user!");
    }
  }

  public void run() {
    String message;
    while (true) {
      try {
        message = userDIS.readUTF();
        tirth.sendMessageToAll(message);
      } catch (Exception e) {

      }
    }
  }

  public void sendMessage(String s) {
    try {
      userDOS.writeUTF(s);
    } catch (Exception e) {
      System.out.println("Send failed!");
    }
  }
}
