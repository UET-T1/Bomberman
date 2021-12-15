package engine;

import engine.graphics.Mesh;
import engine.graphics.Renderer;
import org.joml.Vector3f;

public abstract class GameItem implements Observable, Collidable {

  protected final Vector3f position;
  protected Mesh mesh;
  protected float scale;

  protected Vector3f rotation;

  protected float speed;

  protected Vector3f nextPosition;

  protected boolean chaseStat;// true if item not go full one square

  protected int[] indices = new int[]{
      0, 1, 3, 3, 1, 2};
  protected float[] positions = new float[]{
      // V0
      1.0f, -1.0f, 1.0f,
      // V1
      1.0f, 0.0f, 1.0f,
      // V2
      0.0f, 0.0f, 1.0f,
      // V3
      0.0f, -1.0f, 1.0f};

  protected float[] textCoords;

  public GameItem(Mesh mesh) {
    this.mesh = mesh;
    position = new Vector3f();
    scale = 1;
    rotation = new Vector3f();
  }

  public GameItem() {
    position = new Vector3f();
    scale = 1;
    rotation = new Vector3f();
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

  public void setMesh(Mesh mesh) {
    this.mesh = mesh;
  }

  @Override
  public void onCollision() {

  }

  @Override
  public void handleCollision() {

  }

  @Override
  public void render(Renderer renderer) {

  }

  @Override
  public void onCollapse() {

  }

  public void handleEvent(Window window, Input input) throws Exception {

  }
}