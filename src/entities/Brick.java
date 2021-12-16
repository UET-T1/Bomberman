package entities;


import engine.GameItem;
import engine.Input;
import engine.ObjectManager;
import engine.Utils;
import engine.Window;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;

// class for gameitem can not move
public class Brick extends GameItem {

  protected boolean visible;//is item visible?

  private static Texture brickTexture;
  private static Mesh mesh;

  static {
    try {
      brickTexture = new Texture("resources/textures/brick1.png");
      textCoords = new float[]{
          0.0f, 0.0f,
          1.0f, 0.0f,
          1.0f, 1.0f,
          0.0f, 1.0f};
      mesh = new Mesh(positions, textCoords, indices, brickTexture);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public Brick(Mesh mesh) throws Exception {
    super(mesh);
    visible = true;
  }

  public Brick() throws Exception {
    this(mesh);
  }

  public boolean getVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  @Override
  public void render(Renderer renderer) {
    renderer.render(this);
  }

  @Override
  public void onCollapse() {
    setVisible(false);
    Utils.round1(getPosition());
    int x = (int) getPosition().x;
    int y = (int) getPosition().y;
    ObjectManager.createItem(x, y);
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
