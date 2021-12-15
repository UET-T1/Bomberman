package entities;


import engine.GameItem;
import engine.Input;
import engine.Window;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;

public class Wall extends GameItem {

  private static Texture wallTexture;
  private static Mesh mesh;

  static {
    try {
      wallTexture = new Texture("resources/textures/square.jpg");
      textCoords = new float[]{
          0.0f, 0.0f,
          5.0f, 0.0f,
          0.5f, 0.5f,
          0.0f, 0.5f};
      mesh = new Mesh(positions, textCoords, indices, wallTexture);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public Wall() throws Exception {
    this(mesh);
  }

  public Wall(Mesh mesh) throws Exception {
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
