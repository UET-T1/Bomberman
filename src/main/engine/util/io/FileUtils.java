package main.engine.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
  public static String loadAsString(String path) {
    StringBuilder res = new StringBuilder();
    try {
      // Change class to FileUtils in order to work when we package to a .jar
      BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.class.getResourceAsStream(path)));
      String line = null;
      // Parse string to res
      while ((line = reader.readLine()) != null) {
        res.append(line).append("\n");
      }
    } catch (IOException e) {
      System.err.println("Couldn't read file at: " + path);
      //e.printStackTrace();
    }
    return res.toString();
  }
}