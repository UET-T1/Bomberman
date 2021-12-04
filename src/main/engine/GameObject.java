package main.engine;

import org.joml.Vector3f;

import main.engine.testGUI.Mesh;
import main.engine.testGUI.Texture;

// same as Object in game 
public abstract class GameObject implements Observable, Collidable {

    protected Mesh mesh;

    protected Texture texture;
    
    private final Vector3f position;

    private final Vector3f rotation;
    
    private float scale;

    protected boolean isTargetMoved;// true if target moved
    protected float speed;// speed of item
    protected boolean chaseStat;// true if item not go full one square
    protected String chaseDiection = "";// chase direction  
    protected int dem = 0;// the number of step to go full one square
    protected Vector3f nextPosition;

    public GameObject(Mesh mesh) {
        position = new Vector3f();
        scale = 1;
        rotation = new Vector3f();
        this.mesh = mesh;
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

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
    
    public Mesh getMesh() {
        return mesh;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    // move funtion
    public abstract void handleEvent(Window window, Input input) throws Exception;

}