package engine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.joml.Vector3f;

public class Utils {

  public static String loadResource(String fileName) throws Exception {
    String result;
    try (InputStream in = Utils.class.getResourceAsStream(fileName);
        Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
      result = scanner.useDelimiter("\\A").next();
    }
    return result;
  }

  public static List<String> readAllLines(String fileName) throws Exception {
    List<String> list = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(
        Class.forName(Utils.class.getName()).getResourceAsStream(fileName)))) {
      String line;
      while ((line = br.readLine()) != null) {
        list.add(line);
      }
    }
    return list;
  }

  public static float[] listToArray(List<Float> list) {
    int size = list != null ? list.size() : 0;
    float[] floatArr = new float[size];
    for (int i = 0; i < size; i++) {
      floatArr[i] = list.get(i);
    }
    return floatArr;
  }

  public static Vector3f round100(Vector3f pos) {
    pos.x = (float) Math.round(pos.x * 100) / 100;
    pos.y = (float) Math.round(pos.y * 100) / 100;
    pos.z = (float) Math.round(pos.z * 100) / 100;
    return pos;
  }

  public static Vector3f round1(Vector3f pos) {
    pos.x = (float) Math.round(pos.x);
    pos.y = (float) Math.round(pos.y);
    pos.z = (float) Math.round(pos.z);
    return pos;
  }

  // calulate distant between s and t in square way
  public static float distance(Vector3f s, Vector3f t) {
    float x = Math.abs(s.x - t.x);
    float y = Math.abs(s.y - t.y);
    float ans = x + y;
    return (float) Math.round(ans);
  }

}