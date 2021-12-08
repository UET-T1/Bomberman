package main.engine.testGUI;

import java.io.InputStream;
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

    // calulate distant between s and t
    public static float distant(Vector3f s, Vector3f t) {
        float x = Math.abs(s.x - t.x);
        float y = Math.abs(s.y - t.y);
        float ans = x + y;
        return (float) Math.round(ans);
    }

}