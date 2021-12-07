package entities;


import engine.GameItem;
import engine.Input;
import engine.Window;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;

public class Gate extends GameItem {

  private static Texture gateTexture;
  private static Mesh mesh;

  static {
    try {
      gateTexture = new Texture("resources/textures/gate.jpg");
      textCoords = new float[]{
          0.0f, 0.0f,
          1.0f, 0.0f,
          1.0f, 1.0f,
          0.0f, 1.0f};
      mesh = new Mesh(positions, textCoords, indices, gateTexture);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public Gate(Mesh mesh) throws Exception {
    super(mesh);
    //TODO Auto-generated constructor stub
  }

  public Gate() throws Exception {
    this(mesh);
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
