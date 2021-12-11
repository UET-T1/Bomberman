package engine.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Server {
  public static final int maxUsers = 10;
  public static user[] usersList = new user[maxUsers];
  public static int totalClientsOnline = 0;
  static ServerSocket ss;
  public static File file = new File("resources/leaderboard/leaderboard.txt");
  public static Scanner sc;
  public static int bestTime;
  public static String bestPlayer;
  static {
    try {
      sc = new Scanner(file);
      String temp = sc.nextLine();
      bestTime = Integer.parseInt(temp.split("-")[1]);
      bestPlayer = temp.split("-")[0];
      sc.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

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
  public static String secretKey = new String("1237164");
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
        if (message.startsWith(secretKey)) {
          String name = message.split("-")[1];
          int time = Integer.parseInt(message.split("-")[2]);
          if (time < Server.bestTime) {
            PrintWriter pw = new PrintWriter("resources/leaderboard/leaderboard.txt");
            pw.print(name + "-" + time);
            tirth.sendMessageToAll("NEW RECORD!!! --- " + name + " ----- " + time + " seconds!!!");
            continue;
          }
        }
        else if (message.endsWith("leaderboard./.")){
          tirth.sendMessageToAll("BEST PLAYER: " + Server.bestPlayer + " : " + Server.bestTime);
          continue;
        }
        tirth.sendMessageToAll(message);
      } catch (Exception e) {
        e.printStackTrace();
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
