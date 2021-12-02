package main.engine;

import org.joml.Vector3f;

import main.engine.testGUI.Mesh;

// same as Object in game 
public class GameItem {

    private final Mesh mesh;
    
    private final Vector3f position;
    
    private float scale;

    private final Vector3f rotation;

    public GameItem(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f();
        scale = 1;
        rotation = new Vector3f();
    }

    public Vector3f round100(Vector3f pos) {
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

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }
    
    public Mesh getMesh() {
        return mesh;
    }
}