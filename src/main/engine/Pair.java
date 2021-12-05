package main.engine;

import org.joml.Vector3f;

public class Pair {
    public int x;
    public int y;
    public Pair father;
    public Vector3f value = new Vector3f();

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
        father = null;
        value.x = x;
        value.y = y;
    }

    public Pair(int x, int y, Pair father) {
        this.x = x;
        this.y = y;
        this.father = father;
        value.x = x;
        value.y = y;
    }
}