package main.engine;

import org.joml.Vector3f;

public class Util {
    public static Vector3f round100(Vector3f pos) {
        pos.x =  (float) Math.round(pos.x * 100) / 100;
        pos.y =  (float) Math.round(pos.y * 100) / 100;
        pos.z =  (float) Math.round(pos.z * 100) / 100;
        return pos;
    }

    public static Vector3f round1(Vector3f pos) {
        pos.x =  (float) Math.round(pos.x);
        pos.y =  (float) Math.round(pos.y);
        pos.z =  (float) Math.round(pos.z);
        return pos;
    }

    // calulate distant between s and t in square way
    public static float distant(Vector3f s, Vector3f t) {
        float x = Math.abs(s.x - t.x);
        float y = Math.abs(s.y - t.y);
        float ans = x + y;
        return (float) Math.round(ans);
    }
}
