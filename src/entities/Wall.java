package entities;


import engine.GameItem;
import engine.Input;
import engine.Window;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;

public class Wall extends GameItem {

  private Texture wallTexture = new Texture("src/main/engine/testGUI/square.jpg");

  {
    textCoords = new float[]{
        0.0f, 0.0f,
        5.0f, 0.0f,
        0.5f, 0.5f,
        0.0f, 0.5f};
    mesh = new Mesh(positions, textCoords, indices, wallTexture);
  }

  public Wall() throws Exception {
    super();
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
