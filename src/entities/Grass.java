package entities;


import engine.GameItem;
import engine.Input;
import engine.Window;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;

public class Grass extends GameItem {

  private static Texture grassTexture;
  private static Mesh mesh;
  static {
    try {
      grassTexture = new Texture("resources/textures/grass1.png");
      textCoords = new float[]{
          0.0f, 0.5f,
          0.5f, 0.5f,
          0.5f, 1.0f,
          0.0f, 1.0f};
      mesh = new Mesh(positions, textCoords, indices, grassTexture);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public Grass() throws Exception {
    this(mesh);
  }

  public Grass(Mesh mesh) throws Exception {
    super(mesh);
  }


  @Override
  public void render(Renderer renderer) {
    renderer.render(this);
  }

  @Override
  public void onCollapse() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onCollision() {
    // TODO Auto-generated method stub

  }

  @Override
  public void handleCollision() {
    // TODO Auto-generated method stub

  }

  @Override
  public void handleEvent(Window window, Input input) {
    // TODO Auto-generated method stub

  }

}
